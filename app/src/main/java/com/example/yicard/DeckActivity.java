package com.example.yicard;

import static com.example.yicard.db.DbHelper.COLUMN_CONTENT;
import static com.example.yicard.db.DbHelper.COLUMN_FAVORITE;
import static com.example.yicard.db.DbHelper.COLUMN_ID;
import static com.example.yicard.db.DbHelper.COLUMN_PHOTO;
import static com.example.yicard.db.DbHelper.COLUMN_TAG;
import static com.example.yicard.db.DbHelper.COLUMN_TITLE;
import static com.example.yicard.db.DbHelper.TABLE_NAME_CARD;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yicard.db.DbHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DeckActivity extends Fragment {

    // 定义数据库帮助类的实例
    private DbHelper dbHelper;

    // 定义卡片列表和适配器
    private List<Card> cardList;
    private CardAdapter cardAdapter;

    private String username;

    // 定义一个空的构造方法
    public DeckActivity() {
        // Required empty public constructor
    }

    public static DeckActivity newInstance(String username) {
        DeckActivity fragment = new DeckActivity();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    // 重写onCreateView()方法，返回布局视图
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_deck, container, false);
    }

    // 重写onViewCreated()方法，获取控件并添加逻辑
    @SuppressLint("Range")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取控件
        // 定义布局中的控件
        Spinner spinner = view.findViewById(R.id.spinner);
        RecyclerView cardsRecyclerView = view.findViewById(R.id.cardsRecyclerView);


        // 创建数据库帮助类的实例
        dbHelper = new DbHelper(getActivity());

        // 初始化卡片列表和适配器
        cardList = new ArrayList<>();
        cardAdapter = new CardAdapter(cardList);

        // 设置RecyclerView的布局管理器和适配器
        cardsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        cardsRecyclerView.setAdapter(cardAdapter);

        // 为新添加的Spinner组件设置适配器，从数据库中查询所有的标签，并显示在下拉框中
        ArrayList<String> tagList = new ArrayList<>();
        tagList.add("全部");
        tagList.add("收藏");
        Cursor cursor = dbHelper.queryAllTagsWithUser(username);
        while (cursor.moveToNext()) {
            tagList.add(cursor.getString(cursor.getColumnIndex("tag")));
        }
        cursor.close();
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tagList);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(tagAdapter);

        // 为Spinner添加选择事件监听器
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 获取用户选择的标签
                String tag = parent.getItemAtPosition(position).toString();
                // 根据标签查询卡片数据
                queryCardsByTag(tag);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 什么也不做
            }
        });

        cardAdapter.setOnItemLongClickListener(new CardAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                // 获取被长按的卡片
                Card card = cardList.get(position);
                // 删除卡片数据
                deleteCard(card);
            }
        });
        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("11111111111111", String.valueOf(position));
                Card card = cardList.get(position);
                Log.d("11111111111111", card.getId());
                // 更新MainActivity中底部导航栏的选中项
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    BottomNavigationView bottomNavigationView = mainActivity.findViewById(R.id.bottom_navigation_view);
                    bottomNavigationView.setSelectedItemId(R.id.navigation_review);
                }
                // 创建ReviewFragment的实例并传递卡片数据
                ReviewActivity reviewFragment = ReviewActivity.newInstance(
                        username,
                        card.getId(),
                        card.getTitle(),
                        card.getContent()
                );

                // 使用FragmentManager来替换当前的Fragment为ReviewFragment
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                if (fragmentManager.findFragmentByTag("0") != null)
                    fragmentTransaction.remove(Objects.requireNonNull(fragmentManager.findFragmentByTag("0")));
                fragmentTransaction.replace(R.id.fragment_container, reviewFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    // 定义一个方法，根据标签查询卡片数据
    private void queryCardsByTag(String tag) {
        // 清空卡片列表
        cardList.clear();
        Cursor cursor;

        if (tag.equals("全部")){
            cursor = dbHelper.queryAllCards(username);
        }
        else if (tag.equals("收藏")){
            cursor = dbHelper.queryByFav(username);
        }
        else{
            cursor = dbHelper.queryByTag(username, tag);
        }

        // 遍历结果集，将每条数据转换为卡片对象，并添加到卡片列表中
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
            String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(COLUMN_PHOTO));
            int favorite = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FAVORITE));
            Card card = new Card(id, title, content, tag, photo, favorite);
            cardList.add(card);
        }

        // 关闭结果集和数据库
        cursor.close();

        // 通知适配器数据已更新
        cardAdapter.notifyDataSetChanged();
    }

    // 定义一个方法，删除卡片数据
    private void deleteCard(Card card) {
        // 获取一个可写的数据库对象
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // 定义删除的条件
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(card.getId())};

        // 执行删除，并获取影响的行数
        int deletedRows = db.delete(TABLE_NAME_CARD, selection, selectionArgs);

        // 关闭数据库
        db.close();

        // 如果删除成功，从卡片列表中移除卡片，并提示用户
        if (deletedRows > 0) {
            cardList.remove(card);
            cardAdapter.notifyDataSetChanged();
            Toast.makeText(getActivity(), "删除成功，卡片ID为" + card.getId(), Toast.LENGTH_SHORT).show();
        } else {
            // 如果删除失败，提示用户
            Toast.makeText(getActivity(), "删除失败，卡片ID为" + card.getId(), Toast.LENGTH_SHORT).show();
        }
    }
}

