package com.example.yicard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

// MainActivity.java
public class MainActivity extends AppCompatActivity {
    private Fragment[] mFragments; // 五个Activity对应的Fragment

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // 初始化Fragment数组
        mFragments = new Fragment[5];
        mFragments[0] = ReviewActivity.newInstance(username, null, null, null);
        mFragments[1] = DeckActivity.newInstance(username);
        mFragments[2] = AddActivity.newInstance(username);
        mFragments[3] = StatActivity.newInstance(username);
        mFragments[4] = MineActivity.newInstance(username);

        // 初始化视图
        initView();
    }

    private void initView() {
        // 底部导航栏
        BottomNavigationView mBottomNavigationView = findViewById(R.id.bottom_navigation_view);

        // 设置BottomNavigationView的监听器，根据选中的按钮切换Fragment
        mBottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_review) {
                switchFragment(0);
            } else if (id == R.id.navigation_deck) {
                switchFragment(1);
            } else if (id == R.id.navigation_add) {
                switchFragment(2);
            } else if (id == R.id.navigation_stat) {
                switchFragment(3);
            } else if (id == R.id.navigation_mine) {
                switchFragment(4);
            }
            return true;
        });



        // 默认选中导航按钮
        mBottomNavigationView.setSelectedItemId(R.id.navigation_deck);
    }

    // 切换Fragment的方法
    private void switchFragment(int index) {
        // 使用FragmentTransaction替换容器中的Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mFragments[index]).commit();

    }
}

