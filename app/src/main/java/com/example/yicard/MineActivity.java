package com.example.yicard;

import androidx.fragment.app.Fragment;

// 导入相关的类
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Intent;
import androidx.fragment.app.Fragment;



import com.example.yicard.db.DbHelper;

// 定义MineActivity类，继承自Fragment类
public class MineActivity extends Fragment {

    // 定义布局中的控件
    private ImageView ivAvatar;
    private TextView tvNickname;
    private Button btnTheme;
    private Button btnReminder;
    private Button btnHelp;
    private Button btnLogout;
    private String username;

    private DbHelper dbHelper;

    // 定义一个空的构造方法
    public MineActivity() {
        // Required empty public constructor
    }

    public static MineActivity newInstance(String username) {
        MineActivity fragment = new MineActivity();
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

    // 重写onCreateView()方法，用于返回布局视图
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 使用LayoutInflater类的inflate()方法，来加载你的布局文件，并返回一个View对象
        return inflater.inflate(R.layout.activity_mine, container, false);
    }

    // 重写onViewCreated()方法，用于获取控件并添加逻辑
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 使用View类的findViewById()方法，来获取你的图片控件，文本控件，和按钮控件
        ivAvatar = view.findViewById(R.id.iv_avatar);
        tvNickname = view.findViewById(R.id.tv_nickname);
        btnReminder = view.findViewById(R.id.btn_reminder);
        btnHelp = view.findViewById(R.id.btn_help);
        btnLogout = view.findViewById(R.id.btn_logout);

        // 为你的控件设置相应的属性和事件监听器
        // 这里你可以根据你的逻辑来实现你想要的功能，例如：
        // 设置用户头像和昵称
        //ivAvatar.setImageResource(R.drawable.avatar);
        tvNickname.setText(username);



        // 为设置提醒时间按钮添加点击事件监听器
        btnReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlarmActivity2.class);

                // 启动目标活动
                startActivity(intent);
            }
        });

        // 为帮助按钮添加点击事件监听器
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HelpActivity.class);

                startActivity(intent);
            }
        });

        // 为退出登录按钮添加点击事件监听器
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
