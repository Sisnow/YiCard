package com.example.yicard;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {

    // 定义一个接口，用于实现长按事件的回调
    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    // 定义一个变量，用于存储长按事件的监听器
    private OnItemLongClickListener onItemLongClickListener;

    // 定义一个方法，用于设置长按事件的监听器
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }
    // 定义一个接口，用于实现短按事件的回调
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    // 定义一个变量，用于存储短按事件的监听器
    private OnItemClickListener onItemClickListener;

    // 定义一个方法，用于设置短按事件的监听器
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    // 定义一个变量，用于存储卡片数据的列表
    private List<Card> cardList;

    // 定义一个构造方法，用于接收卡片数据的列表
    public CardAdapter(List<Card> cardList) {
        this.cardList = cardList;
    }

    // 定义一个内部类，用于表示卡片的ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        // 定义布局中的控件
        public TextView titleTextView;
        public TextView contentTextView;
        public TextView tagTextView;
        public ImageView photoImageView;

        // 定义一个构造方法，用于初始化控件
        public ViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.title_text_view);
            contentTextView = view.findViewById(R.id.content_text_view);
            tagTextView = view.findViewById(R.id.tag_text_view);
            photoImageView = view.findViewById(R.id.photo_image_view);
        }
    }

    // 重写onCreateViewHolder()方法，创建并返回ViewHolder对象
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 获取一个LayoutInflater对象，用于加载布局
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 加载卡片的布局，返回一个View对象
        View cardView = inflater.inflate(R.layout.card_item, parent, false);

        // 创建并返回一个ViewHolder对象
        ViewHolder viewHolder = new ViewHolder(cardView);
        return viewHolder;
    }

    // 重写onBindViewHolder()方法，绑定ViewHolder对象与数据
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 获取当前位置的卡片对象
        Card card = cardList.get(position);

        // 获取ViewHolder对象中的控件
        TextView titleTextView = holder.titleTextView;
        TextView contentTextView = holder.contentTextView;
        TextView tagTextView = holder.tagTextView;
        ImageView photoImageView = holder.photoImageView;

        // 设置控件的内容
        titleTextView.setText(card.getTitle());
        contentTextView.setText(card.getContent());
        tagTextView.setText(card.getTag());
        photoImageView.setImageBitmap(null); // 这里你需要根据你的逻辑来设置照片的图片

        // 为ViewHolder对象的视图添加长按事件监听器
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // 获取当前的数据位置
                int position = holder.getAdapterPosition();
                // 如果有设置长按事件的监听器，就调用它的方法
                if (onItemLongClickListener != null) {
                    onItemLongClickListener.onItemLongClick(v, position);
                }
                // 返回true表示消费了事件，不会触发其他的事件
                return true;
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 获取当前的数据位置
                int position = holder.getAdapterPosition();
                // 如果有设置短按事件的监听器，就调用它的方法
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });
    }


    // 重写getItemCount()方法，返回数据集的大小
    @Override
    public int getItemCount() {
        return cardList.size();
    }
}

