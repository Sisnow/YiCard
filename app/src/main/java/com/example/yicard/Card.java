package com.example.yicard;
// 定义卡片类
public class Card {
    // 定义卡片的属性
    private String id; // 卡片的ID
    private String title; // 卡片的标题
    private String content; // 卡片的内容
    private String tag; // 卡片的标签
    private byte[] photo; // 卡片的照片的路径或URL
    private int favorite; // 卡片是否被收藏，0表示未收藏，1表示已收藏

    // 定义卡片的构造方法，用于创建卡片对象
    public Card(String id, String title, String content, String tag, byte[] photo, int favorite) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.tag = tag;
        this.photo = photo;
        this.favorite = favorite;
    }

    //重写equals方法
    @Override
    public boolean equals(Object obj) {
        //检查参数是否为null
        if (obj == null) {
            return false;
        }
        //检查参数是否与当前对象是同一个对象
        if (this == obj) {
            return true;
        }
        //检查参数是否与当前对象是同一个类
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        //将参数转换为当前对象的类型
        Card other = (Card) obj;
        //比较它们的属性
        return this.id.equals(other.id);
    }

    // 定义卡片的getter和setter方法，用于获取和设置卡片的属性
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}
