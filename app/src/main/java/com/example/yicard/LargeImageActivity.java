package com.example.yicard;

// 导入相关的类
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yicard.db.DbHelper;

// 定义LargeImageActivity类，继承自AppCompatActivity类
public class LargeImageActivity extends AppCompatActivity {

    private DbHelper dbHelper;

    // 重写onCreate()方法，用于初始化你的布局和控件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置你的布局文件
        setContentView(R.layout.activity_large_image);

        // 获取Intent对象，并从中获取图片的Uri或者资源ID
        Intent intent = getIntent();
        String cardID = intent.getStringExtra("cardID");

        dbHelper = new DbHelper(this);
        Cursor cursor = dbHelper.queryByID(cardID);
        cursor.moveToFirst();
        @SuppressLint("Range") byte[] image = cursor.getBlob(cursor.getColumnIndex("photo"));

        // 获取ImageView控件，并使用setImageURI()或者setImageResource()方法，将图片显示在控件上
        ImageView largeImageView = findViewById(R.id.large_image_view);

        if (image != null) {
            // 将字节数组转换为Bitmap对象
            Bitmap bitmap = BitmapFactory.decodeByteArray (image, 0, image.length);
            largeImageView.setImageBitmap(bitmap);
            largeImageView.setVisibility(View.VISIBLE);
        } else {
            largeImageView.setVisibility(View.GONE);
        }

        // 为ImageView控件添加一个点击事件监听器，当用户点击图片时，结束当前的activity，并返回上一个activity
        largeImageView.setOnClickListener(v -> finish());
    }

}
