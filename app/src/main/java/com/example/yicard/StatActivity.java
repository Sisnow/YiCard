package com.example.yicard;

import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import android.view.View;
import android.widget.TextView;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.example.yicard.db.DbHelper;
import com.github.mikephil.charting.charts.PieChart;
import java.util.ArrayList;
import java.util.List;

public class StatActivity extends Fragment {
    private TextView tvTotalCards;
    private TextView tvRememberedCards;
    private PieChart chart;

    private String username;
    public static StatActivity newInstance(String username) {
        StatActivity fragment = new StatActivity();
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_stat, container, false);

        // 初始化控件
        tvTotalCards = view.findViewById(R.id.tv_total_cards);
        tvRememberedCards = view.findViewById(R.id.tv_remembered_cards);
        chart = view.findViewById(R.id.chart);

        // 获取统计数据
        DbHelper dbHelper = new DbHelper(getContext());
        int totalCards = dbHelper.getTotalCardCount(username);
        int[] stats = dbHelper.getCardStatistics(username); // [记住, 模糊, 忘记]

        // 更新UI
        tvTotalCards.setText("总计卡片数量\n " + totalCards);
        tvRememberedCards.setText("记住卡片数量\n " + stats[0]);

        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(stats[0], "Remembered"));
        entries.add(new PieEntry(stats[1], "Blur"));
        entries.add(new PieEntry(stats[2], "Forgot"));

        PieDataSet dataSet = new PieDataSet(entries, "Card Stats");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS); // 饼图颜色
        PieData data = new PieData(dataSet);

        chart.setData(data);
        chart.getDescription().setEnabled(false); // 隐藏描述
        chart.setEntryLabelColor(Color.BLACK); // 设置标签颜色
        chart.animateY(1400, Easing.EaseInOutQuad); // 设置动画
        chart.invalidate(); // 刷新图表

        return view;
    }
}
