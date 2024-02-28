package com.example.yicard;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.yicard.db.DbHelper;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class AddActivity extends Fragment {

    // 定义布局中的控件
    private EditText titleEditText;
    private EditText contentEditText;
    private EditText tagEditText;
    private ImageView photoImageView;
    private CheckBox favoriteCheckBox;

    // 定义数据库帮助类的实例
    private DbHelper dbHelper;
    private String username;

    // 定义一个空的构造方法
    public AddActivity() {
        // Required empty public constructor
    }

    public static AddActivity newInstance(String username) {
        AddActivity fragment = new AddActivity();
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
        return inflater.inflate(R.layout.activity_add, container, false);
    }

    // 重写onViewCreated()方法，获取控件并添加逻辑
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 获取控件
        titleEditText = view.findViewById(R.id.title_edit_text);
        contentEditText = view.findViewById(R.id.content_edit_text);
        tagEditText = view.findViewById(R.id.tag_edit_text);
        photoImageView = view.findViewById(R.id.photo_image_view);
        favoriteCheckBox = view.findViewById(R.id.favorite_check_box);
        Button saveButton = view.findViewById(R.id.save_button);

        // 创建数据库帮助类的实例
        dbHelper = new DbHelper(getActivity());

        // 注册一个ActivityResultLauncher对象，用于启动选择图片的Intent，并接收返回的结果
        ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // 判断结果是否成功，如果是，就获取返回的数据，即图片的Uri对象
                        if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                            Uri imageUri = result.getData().getData();
                            try {
                                // 使用BitmapFactory类的decodeStream()方法，将Uri转换为Bitmap对象，并显示在你的图片控件上
                                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                                photoImageView.setImageBitmap(bitmap);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );

        // 为图片控件添加点击事件监听器
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 调用选择图片的方法
                selectImage(selectImageLauncher);
            }
        });

        // 为保存按钮添加点击事件监听器
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取用户输入的数据
                String title = titleEditText.getText().toString();
                String content = contentEditText.getText().toString();
                String tag = tagEditText.getText().toString();
                byte[] photo = null; // 这里你需要将你的图片转换为字节数组
                int favorite = favoriteCheckBox.isChecked() ? 1 : 0;

                //条件判断
                if (title.equals("")){
                    Toast.makeText(getActivity(), "请至少填入标题！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (tag.equals("")){
                    tag = "默认";
                }
                if (tag.equals("收藏") || tag.equals("全部")){
                    Toast.makeText(getActivity(), "非法的标签命名！", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 将图片转换为字节数组
                if (photoImageView.getDrawable() != null){
                    Bitmap bitmap = ((BitmapDrawable)photoImageView.getDrawable()).getBitmap();
                    if (bitmap != null){
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        photo = baos.toByteArray();
                        photo = imagemTratada(photo);
                    }
                }

                // 向数据库插入卡片和新的关联
                int newRowID = dbHelper.insertCard(title, content, tag, photo, favorite);
                dbHelper.insertRelate(username, newRowID);

                // 提示用户保存成功
                Toast.makeText(getActivity(), "保存成功，新的卡片ID为" + newRowID, Toast.LENGTH_SHORT).show();

                // 清空输入框
                titleEditText.setText("");
                contentEditText.setText("");
                tagEditText.setText("");
                //photoImageView.setImageBitmap(null);
                favoriteCheckBox.setChecked(false);
            }
        });

    }

    // 定义一个选择图片的方法
    void selectImage(ActivityResultLauncher<Intent> launcher) {
        // 创建一个Intent对象，设置其类型为"image/*"，并使用ACTION_GET_CONTENT来获取内容
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // 使用你注册的ActivityResultLauncher对象，调用launch()方法，传入你的Intent对象，启动选择图片的activity
        launcher.launch(intent);
    }

    //压缩图片
    private byte[] imagemTratada(byte[] imagem_img){

        while (imagem_img.length > 200000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;
    }
}

