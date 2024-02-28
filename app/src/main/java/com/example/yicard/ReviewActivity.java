package com.example.yicard;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yicard.db.DbHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class ReviewActivity extends Fragment {
    private static final String ARG_CARD_ID = "card_id";
    private static final String ARG_CARD_TITLE = "card_title";
    private static final String ARG_CARD_CONTENT = "card_content";
    private static final String ARG_CARD_PHOTO = "card_photo";

    private String cardId;
    private String cardTitle;
    private String cardContent;
    private String cardPhoto;
    private String username;

    // 定义UI组件
    private TextView tvTitle;
    private TextView tvContent;
    private ImageView ivPhoto;
    private TextView tvLastReviewTime;
    private TextView tvNextReviewTime;
    private ImageView ivFavorite;

    // 定义数据库帮助类
    private DbHelper dbHelper;

    // 定义卡片列表和索引
    private ArrayList<Card> cardList;
    private int index;

    //创建一个SimpleDateFormat对象，用来格式化日期
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    //当前日期
    private String date;
    private String lastDate;//上次复习时间

    // 定义当天的认识，模糊，忘记的卡片数量
    private int know;
    private int blur;
    private int forget;


    private Spinner tagSpinner;


    public ReviewActivity() {
        // Required empty public constructor
    }

    public static ReviewActivity newInstance(String username, String cardId, String cardTitle, String cardContent) {
        ReviewActivity fragment = new ReviewActivity();
        Bundle args = new Bundle();
        args.putString("username", username);
        args.putString(ARG_CARD_ID, cardId);
        args.putString(ARG_CARD_TITLE, cardTitle);
        args.putString(ARG_CARD_CONTENT, cardContent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cardId = getArguments().getString(ARG_CARD_ID);
            cardTitle = getArguments().getString(ARG_CARD_TITLE);
            cardContent = getArguments().getString(ARG_CARD_CONTENT);
            cardPhoto = getArguments().getString(ARG_CARD_PHOTO);
            username = getArguments().getString("username");
        }
    }

    @SuppressLint("Range")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_review, container, false);

        //当前日期
        date = dateFormat.format(new Date());

        // 初始化UI组件
        tvTitle = view.findViewById(R.id.tv_title);
        tvContent = view.findViewById(R.id.tv_content);
        ivPhoto = view.findViewById(R.id.iv_photo);
        tvLastReviewTime = view.findViewById(R.id.tv_last_review_time);
        tvNextReviewTime = view.findViewById(R.id.tv_next_review_time);
        ivFavorite = view.findViewById(R.id.iv_favorite);
        Button btnKnow = view.findViewById(R.id.btn_know);
        Button btnBlur = view.findViewById(R.id.btn_blur);
        Button btnForget = view.findViewById(R.id.btn_forget);

        // 初始化数据库帮助类
        dbHelper = new DbHelper(getActivity());

        // 初始化新添加的Spinner组件
        tagSpinner = view.findViewById(R.id.tag_spinner);

        // 为新添加的Spinner组件设置适配器，从数据库中查询所有的标签，并显示在下拉框中
        ArrayList<String> tagList = new ArrayList<>();
        Cursor cursor = dbHelper.queryAllTagsWithUser(username);
        while (cursor.moveToNext()) {
            tagList.add(cursor.getString(cursor.getColumnIndex("tag")));
        }
        cursor.close();
        ArrayAdapter<String> tagAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tagList);
        tagAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tagSpinner.setAdapter(tagAdapter);

        // 为新添加的Spinner组件设置监听器，当用户选择不同的标签时，更新卡片列表和索引，并显示第一张卡片
        tagSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (getArguments() == null || cardId == null) {
                    // 获取用户选择的标签
                    String tag = tagSpinner.getSelectedItem().toString();
                    // 查询数据库中该标签下的所有卡片，并打乱顺序
                    cardList = new ArrayList<>();
                    Cursor cursor2 = dbHelper.queryByTag(username, tag);
                    while (cursor2.moveToNext()) {
                        // 创建一个Card对象，并从数据库中获取属性值
                        Card card = new Card(
                                cursor2.getString(cursor2.getColumnIndex("card_id")),
                                cursor2.getString(cursor2.getColumnIndex("title")),
                                cursor2.getString(cursor2.getColumnIndex("content")),
                                cursor2.getString(cursor2.getColumnIndex("tag")),
                                cursor2.getBlob(cursor2.getColumnIndex("photo")),
                                cursor2.getInt(cursor2.getColumnIndex("favorite"))
                        );
                        // 将Card对象添加到列表中
                        cardList.add(card);
                    }
                    cursor2.close();
                    Collections.shuffle(cardList);
                }
                else {//如果是从卡组页面跳转
                    // 获取用户选择的标签
                    Log.d("111111111111111", String.valueOf(index));
                    Log.d("111111111111111",cardId);
                    Cursor cursor_id = dbHelper.queryByID(cardId);
                    cursor_id.moveToFirst();
                    Card first_card = new Card(
                            cursor_id.getString(cursor_id.getColumnIndex("card_id")),
                            cursor_id.getString(cursor_id.getColumnIndex("title")),
                            cursor_id.getString(cursor_id.getColumnIndex("content")),
                            cursor_id.getString(cursor_id.getColumnIndex("tag")),
                            cursor_id.getBlob(cursor_id.getColumnIndex("photo")),
                            cursor_id.getInt(cursor_id.getColumnIndex("favorite"))
                    );
                    String tag = first_card.getTag();
                    tagSpinner.setSelection(tagList.indexOf(tag));
                    cursor_id.close();

                    Cursor cursor2 = dbHelper.queryByTag(username, tag);

                    // 查询数据库中该标签下的所有卡片，并打乱顺序
                    cardList = new ArrayList<>();
                    while (cursor2.moveToNext()) {
                        // 创建一个Card对象，并从数据库中获取属性值
                        Card card = new Card(
                                cursor2.getString(cursor2.getColumnIndex("card_id")),
                                cursor2.getString(cursor2.getColumnIndex("title")),
                                cursor2.getString(cursor2.getColumnIndex("content")),
                                cursor2.getString(cursor2.getColumnIndex("tag")),
                                cursor2.getBlob(cursor2.getColumnIndex("photo")),
                                cursor2.getInt(cursor2.getColumnIndex("favorite"))
                        );
                        // 将Card对象添加到列表中
                        cardList.add(card);
                    }
                    cursor2.close();
                    Collections.shuffle(cardList);
                    // 查找id为cardId的对象在列表中的位置
                    int pos = cardList.indexOf(first_card);
                    // 如果存在，将该对象移除，并插入到第一个位置
                    if (pos != -1) {
                        Card card = cardList.remove(pos);
                        cardList.add(0, card);
                    }
                }
                // 初始化索引
                index = 0;
                // 显示第一张卡片
                showCard();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 如果用户没有选择任何标签，就不显示任何卡片
                cardList = new ArrayList<>();
            }
        });




        // 为三个按钮设置点击事件监听器
        btnKnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = cardList.get(index);
                // 获得当前card统计信息
                getCurrentStat(card.getId());
                // 更新card的统计信息
                know++;
                blur = 4;//如果认识，blur取特殊值4，用于延后复习天数
                updateCard(card.getId());
                // 显示下一张卡片
                showNextCard();
            }
        });

        btnBlur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = cardList.get(index);
                // 获得当前card统计信息
                getCurrentStat(card.getId());
                // 更新card的统计信息
                blur++;
                if (blur == 3){
                    blur = 0;
                    forget++;
                }
                if (blur > 3){//这是之前知道，现在模糊的情况
                    blur = 0;
                }
                updateCard(card.getId());
                // 显示下一张卡片
                showNextCard();
            }
        });

        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Card card = cardList.get(index);
                // 获得当前card统计信息
                getCurrentStat(card.getId());
                // 更新card的统计信息
                forget++;
                blur = 0;
                updateCard(card.getId());
                // 显示下一张卡片
                showNextCard();
            }
        });

        // 收藏/取消收藏
        ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前显示的card对象
                Card card = cardList.get(index);
                //获取ImageView的tag属性，用来判断当前的收藏状态
                int tag = card.getFavorite();
                //判断tag属性的值
                if (tag == 0) {
                    tag = 1;
                } else {
                    tag = 0;
                }
                boolean is = dbHelper.updateFavorite(card.getId(), tag);
                card.setFavorite(tag);
                showCard();
            }
        });

        // 查看大图
        ivPhoto.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                // 创建一个Intent对象，指定要启动的activity的类名
                Intent intent = new Intent (getActivity(), LargeImageActivity.class);
                // 将图片的Uri或者资源ID作为额外数据传递给Intent对象
                Card card = cardList.get(index);
                intent.putExtra ("cardID", card.getId());
                // 启动这个Intent，使用startActivity()方法
                startActivity (intent);
            }
        });

        return view;
    }

    //更新card的统计数据
    @SuppressLint("Range")
    private void updateCard(String id){
        // 查询数据库中是否有card的统计数据
        Cursor cursor3 = dbHelper.queryStatByID(id);
        if (cursor3.getCount() == 0) {
            // 如果没有，就插入一条新的数据，初始值都为0
            dbHelper.insertStat(id, date, 0, 0, 0);
        } else {
            // 如果有，就更新当天的认识，模糊，忘记的卡片数量，并存储到三个变量中
            dbHelper.updateStat(id, date, know, blur, forget);
            cursor3.close();
        }
    }

    //获得card的统计信息 post: change know blur forget lastDate
    @SuppressLint("Range")
    private void getCurrentStat(String id){
        Cursor cursor = dbHelper.queryStatByID(id);
        if (cursor.getCount() == 0) {
            // 如果没有，就插入一条新的数据，初始值都为0
            dbHelper.insertStat(id, date, 0, 0, 0);
            lastDate = date;
            know = 0;
            blur = 0;
            forget = 0;
        }
        else{
            cursor.moveToFirst();
            lastDate = cursor.getString(cursor.getColumnIndex("date"));
            know = cursor.getInt(cursor.getColumnIndex("know"));
            blur = cursor.getInt(cursor.getColumnIndex("blur"));
            forget = cursor.getInt(cursor.getColumnIndex("forget"));
        }
        cursor.close();
    }

    // 定义一个方法，用来显示当前索引对应的卡片
    @SuppressLint("SetTextI18n")
    private void showCard() {
        // 获取当前索引对应的卡片对象
        Card card = cardList.get(index);
        // 设置TextView的文本为卡片的标题和内容
        tvTitle.setText(card.getTitle());
        tvContent.setText(card.getContent());

        //计算复习时间
        Log.d("111111111111111", String.valueOf(index));
        Log.d("111111111111111",card.getId());
        getCurrentStat(card.getId());//此时得到lastDate
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Objects.requireNonNull(dateFormat.parse(lastDate)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Integer> addNum = new ArrayList<>(Arrays.asList(1,2,4,7,7));

        calendar.add(Calendar.DAY_OF_MONTH, addNum.get(blur));//模糊的次数对应增加的天数
        Date nextCalender = calendar.getTime();
        String nextDate = dateFormat.format(nextCalender);
        tvLastReviewTime.setText("最近复习时间：" + lastDate);
        tvNextReviewTime.setText("下次复习时间：" + nextDate);

        // 如果卡片有图片，设置ImageView的图片为卡片的图片；否则，隐藏ImageView
        byte[] image = card.getPhoto();
        if (image != null) {
            // 将字节数组转换为Bitmap对象
            Bitmap bitmap = BitmapFactory.decodeByteArray (image, 0, image.length);
            ivPhoto.setImageBitmap(bitmap);
            ivPhoto.setVisibility(View.VISIBLE);
        } else {
            ivPhoto.setVisibility(View.GONE);
        }

        // 获取卡片的收藏状态，并设置到对应的ImageView中
        int favorite = card.getFavorite();

        if (favorite == 1) {
            // 如果卡片已收藏，使用收藏的图片资源
            ivFavorite.setImageResource(R.drawable.ic_star_yel);
        } else {
            // 如果卡片未收藏，使用未收藏的图片资源
            ivFavorite.setImageResource(R.drawable.ic_star_black);
        }
    }


    // 定义一个方法，用来显示下一张卡片
    private void showNextCard() {
        // 增加索引的值
        index++;
        // 如果索引小于卡片列表的大小，显示下一张卡片；否则，提示用户复习完成
        if (index < cardList.size()) {
            showCard();
        } else {
            Toast.makeText(getActivity(), "复习完成！", Toast.LENGTH_SHORT).show();
            index = 0;
            showCard();
        }
    }

}