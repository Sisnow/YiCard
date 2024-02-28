package com.example.yicard.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

// 创建SQLiteOpenHelper子类
public class DbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "MemoryCard.db";

    //card表
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME_CARD = "card";
    public static final String COLUMN_ID = "card_id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final String COLUMN_TAG = "tag";
    public static final String COLUMN_PHOTO = "photo";
    public static final String COLUMN_FAVORITE = "favorite";

    //statistics表
    public static final String TABLE_NAME_STAT = "statistics";
    public static final String COLUMN_ID_STAT = "statid";
    public static final String COLUMN_DATE_STAT = "date";
    public static final String COLUMN_KNOW_STAT = "know";
    public static final String COLUMN_BLUR_STAT = "blur";
    public static final String COLUMN_FORGET_STAT = "forget";

    //user表
    public static final String TABLE_NAME_USER = "user";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_AVATAR = "avatar";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_PASSWORD = "password";

    //relate表
    public static final String TABLE_NAME_RELATE = "relate";
    public static final String R_COLUMN_USER_NAME = "username";
    public static final String R_COLUMN_CARD_ID = "card_id";


    //创建统计表, 每个card对应一个统计信息
    public static final String SQL_CREATE_TABLE_STAT = "CREATE TABLE " + TABLE_NAME_STAT + " (" +
            COLUMN_ID_STAT + " INTEGER PRIMARY KEY," +
            COLUMN_ID + " TEXT," +
            COLUMN_DATE_STAT + " TEXT," +
            COLUMN_KNOW_STAT + " INTEGER," +
            COLUMN_BLUR_STAT + " INTEGER," +
            COLUMN_FORGET_STAT + " INTEGER)";



    // 创建卡片表的SQL语句
    public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME_CARD + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY," +
            COLUMN_TITLE + " TEXT," +
            COLUMN_CONTENT + " TEXT," +
            COLUMN_TAG + " TEXT," +
            COLUMN_PHOTO + " TEXT," +
            COLUMN_FAVORITE + " INTEGER)";

    // 创建用户表的SQL语句
    public static final String SQL_CREATE_TABLE_USER = "CREATE TABLE " + TABLE_NAME_USER + " (" +
            COLUMN_USER_ID + " INTEGER PRIMARY KEY," +
            COLUMN_USERNAME + " TEXT," +
            COLUMN_AVATAR + " TEXT," +
            COLUMN_PHONE + " TEXT," +
            COLUMN_PASSWORD + " TEXT)";


    // 创建关联表的SQL语句
    public static final String SQL_CREATE_TABLE_RELATE = "CREATE TABLE " + TABLE_NAME_RELATE + " (" +
            R_COLUMN_USER_NAME + " TEXT," +
            R_COLUMN_CARD_ID + " INTEGER)";

    // 删除表的SQL语句
    public static final String SQL_DELETE_TABLE_STAT = "DROP TABLE IF EXISTS " + TABLE_NAME_STAT;
    public static final String SQL_DELETE_TABLE_USER = "DROP TABLE IF EXISTS " + TABLE_NAME_USER;
    public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME_CARD;
    public static final String SQL_DELETE_TABLE_RELATE = "DROP TABLE IF EXISTS " + TABLE_NAME_RELATE;
    // 构造方法
    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建数据库的方法
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 执行创建卡片表的SQL语句
        db.execSQL(SQL_CREATE_TABLE);
        // 执行创建统计表的SQL语句
        db.execSQL(SQL_CREATE_TABLE_STAT);
        // 执行创建用户表的SQL语句
        db.execSQL(SQL_CREATE_TABLE_USER);
        // 执行创建关联表的SQL语句
        db.execSQL(SQL_CREATE_TABLE_RELATE);
    }


    // 升级数据库的方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 执行删除卡片表的SQL语句
        db.execSQL(SQL_DELETE_TABLE);
        // 执行删除统计表的SQL语句
        db.execSQL(SQL_DELETE_TABLE_STAT);
        // 执行删除用户表的SQL语句
        db.execSQL(SQL_DELETE_TABLE_USER);
        // 执行删除关联表的SQL语句
        db.execSQL(SQL_DELETE_TABLE_RELATE);
        // 调用onCreate()方法，重新创建表
        onCreate(db);
    }

    public int insertCard(String title, String content, String tag, byte[] photo, int favorite) {
        SQLiteDatabase db = this.getWritableDatabase();
        // 创建一个ContentValues对象，用于存储卡片的信息
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_CONTENT, content);
        values.put(COLUMN_TAG, tag);
        values.put(COLUMN_PHOTO, photo);
        values.put(COLUMN_FAVORITE, favorite);

        // 插入一条新的数据，并返回新的行的ID
        return (int) db.insert(TABLE_NAME_CARD, null, values);
    }

    // 插入一条统计数据
    public void insertStat(String id, String date, int know, int blur, int forget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, id);
        values.put(COLUMN_DATE_STAT, date);
        values.put(COLUMN_KNOW_STAT, know);
        values.put(COLUMN_BLUR_STAT, blur);
        values.put(COLUMN_FORGET_STAT, forget);
        db.insert(TABLE_NAME_STAT, null, values);
    }

    // 查询所有的统计数据
    public Cursor queryAllStat() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME_STAT, null, null, null, null, null, null);
    }

    // 查询某一天的统计数据
    public Cursor queryStatByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_DATE_STAT + " = ?";
        String[] selectionArgs = { date };
        return db.query(TABLE_NAME_STAT, null, selection, selectionArgs, null, null, null);
    }

    // 查询某card的统计数据
    public Cursor queryStatByID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { id };
        return db.query(TABLE_NAME_STAT, null, selection, selectionArgs, null, null, null);
    }


    // 更新某一天的统计数据
    public void updateStat(String id, String date, int know, int blur, int forget) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE_STAT, date);
        values.put(COLUMN_KNOW_STAT, know);
        values.put(COLUMN_BLUR_STAT, blur);
        values.put(COLUMN_FORGET_STAT, forget);
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = { id };
        db.update(TABLE_NAME_STAT, values, whereClause, whereArgs);
    }

    //更新收藏信息
    public boolean updateFavorite(String id, int fav) {
        //检查参数的合法性
        if (id == null || id.isEmpty()) {
            return false; //id不能为空
        }
        if (fav != 0 && fav != 1) {
            return false; //fav只能为0或1
        }
        //获取一个可写的数据库对象
        SQLiteDatabase db = this.getWritableDatabase();
        //创建一个ContentValues对象，用来存放要更新的列和值
        ContentValues values = new ContentValues();
        values.put(COLUMN_FAVORITE, fav);
        //创建一个更新条件，用来匹配id
        String whereClause = COLUMN_ID + " = ?";
        String[] whereArgs = { id };
        //更新数据库中的收藏状态，返回影响的行数
        int rows = db.update(TABLE_NAME_CARD, values, whereClause, whereArgs);
        //关闭数据库对象
        db.close();
        //返回更新是否成功
        return rows > 0;
    }

    public Cursor queryAllCards(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        // 定义查询的条件
        String selection = "username = ?";
        String[] selectionArgs = { username};
        // 定义SQL语句，查询两个表的自然连接，返回所有列
        String sql = "SELECT DISTINCT * FROM " + TABLE_NAME_CARD + " NATURAL JOIN " + TABLE_NAME_RELATE + " WHERE " + selection;
        // 执行查询，并获取结果集
        return db.rawQuery(sql, selectionArgs);
    }

    public Cursor queryByFav(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        // 定义查询的条件
        String selection = "username = ? and favorite = ?";
        String[] selectionArgs = { username, "1" };
        // 定义SQL语句，查询两个表的自然连接，返回所有列
        String sql = "SELECT DISTINCT * FROM " + TABLE_NAME_CARD + " NATURAL JOIN " + TABLE_NAME_RELATE + " WHERE " + selection;
        // 执行查询，并获取结果集
        return db.rawQuery(sql, selectionArgs);
    }

    // 根据标签查询卡片表中的数据
    public Cursor queryByTag(String username, String tag) {
        SQLiteDatabase db = this.getReadableDatabase();
        // 定义查询的条件
        String selection = "username = ? and tag = ?";
        String[] selectionArgs = { username, tag };
        // 定义SQL语句，查询两个表的自然连接，返回所有列
        String sql = "SELECT DISTINCT * FROM " + TABLE_NAME_CARD + " NATURAL JOIN " + TABLE_NAME_RELATE + " WHERE " + selection;
        // 执行查询，并获取结果集
        return db.rawQuery(sql, selectionArgs);
    }


    public Cursor queryByID(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_ID + " = ?";
        String[] selectionArgs = { id };
        return db.query(TABLE_NAME_CARD, null, selection, selectionArgs, null, null, null);
    }

    public Cursor queryAllTags() {
        // 获取一个可读的数据库对象
        SQLiteDatabase db = this.getReadableDatabase();
        // 调用query方法，查询标签表中的所有数据，只返回标签列，不设置其他条件
        // 返回Cursor对象
        return db.query(true, TABLE_NAME_CARD, new String[] {COLUMN_TAG}, null, null, null, null, null, null);
    }

    public Cursor queryAllTagsWithUser(String username) {
        // 获取一个可读的数据库对象
        SQLiteDatabase db = this.getReadableDatabase();
        // 定义SQL语句，查询两个表的自然连接，返回所有列
        String sql = "SELECT DISTINCT " + COLUMN_TAG + " FROM " + TABLE_NAME_CARD + " NATURAL JOIN " + TABLE_NAME_RELATE + " WHERE " + R_COLUMN_USER_NAME + " = " + username;
        // 调用rawQuery方法，执行SQL语句，返回Cursor对象
        return db.rawQuery(sql, null);
    }

    public Cursor queryAllWithUser() {
        // 获取一个可读的数据库对象
        SQLiteDatabase db = this.getReadableDatabase();
        // 定义SQL语句，查询两个表的自然连接，返回所有列
        String sql = "SELECT * FROM " + TABLE_NAME_CARD + " NATURAL JOIN " + TABLE_NAME_RELATE + " WHERE " + COLUMN_ID + " = " + R_COLUMN_CARD_ID;
        // 调用rawQuery方法，执行SQL语句，返回Cursor对象
        return db.rawQuery(sql, null);
    }


    public Cursor queryUserByName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = { username };
        return db.query(TABLE_NAME_USER, null, selection, selectionArgs, null, null, null);
    }
    public int getTotalCardCount(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Use a JOIN query to relate the card table with the relate table on card ID, and filter by username
        String query = "SELECT COUNT(*) FROM " + TABLE_NAME_CARD + " INNER JOIN " + TABLE_NAME_RELATE +
                " ON " + TABLE_NAME_CARD + "." + COLUMN_ID + " = " + TABLE_NAME_RELATE + "." + R_COLUMN_CARD_ID +
                " WHERE " + TABLE_NAME_RELATE + "." + R_COLUMN_USER_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    public int[] getCardStatistics(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int[] stats = new int[3]; // [记住, 模糊, 忘记]
        String monthAgo = getDateStringOneMonthAgo();

        // Define the base query with the required JOIN and WHERE clause
        String baseQuery = "SELECT COUNT(*) FROM " + TABLE_NAME_STAT +
                " INNER JOIN " + TABLE_NAME_RELATE +
                " ON " + TABLE_NAME_STAT + "." + COLUMN_ID + " = " + TABLE_NAME_RELATE + "." + R_COLUMN_CARD_ID +
                " WHERE " + TABLE_NAME_RELATE + "." + R_COLUMN_USER_NAME + " = ?" +
                " AND " + COLUMN_DATE_STAT + " > ?";

        // 统计记住的卡片数
        Cursor cursor = db.rawQuery(baseQuery + " AND " + COLUMN_KNOW_STAT + " > 0", new String[]{username, monthAgo});
        if (cursor.moveToFirst()) {
            stats[0] = cursor.getInt(0);
        }
        cursor.close();

        // 统计模糊的卡片数
        cursor = db.rawQuery(baseQuery + " AND " + COLUMN_BLUR_STAT + " > 0", new String[]{username, monthAgo});
        if (cursor.moveToFirst()) {
            stats[1] = cursor.getInt(0);
        }
        cursor.close();

        // 统计忘记的卡片数
        cursor = db.rawQuery(baseQuery + " AND " + COLUMN_FORGET_STAT + " > 0", new String[]{username, monthAgo});
        if (cursor.moveToFirst()) {
            stats[2] = cursor.getInt(0);
        }
        cursor.close();

        return stats;
    }

    private String getDateStringOneMonthAgo() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    // 定义一个方法，用于检查用户名和密码是否存在和匹配
    public boolean checkUser(String phone, String password) {
        // 获取可读的数据库对象
        SQLiteDatabase db = this.getReadableDatabase();
        // 定义查询的列名
        String[] columns = {COLUMN_PHONE};
        // 定义查询的条件
        String selection = COLUMN_PHONE + " = ?" + " AND " + COLUMN_PASSWORD + " = ?";
        // 定义查询的参数
        String[] selectionArgs = {phone, password};
        // 执行查询，返回一个Cursor对象
        Cursor cursor = db.query(TABLE_NAME_USER, columns, selection, selectionArgs, null, null, null);
        // 判断Cursor对象是否为空
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            // 如果不为空，说明查询到了对应的用户名和密码，返回true
            cursor.close();
            return true;
        } else {
            // 如果为空，说明没有查询到对应的用户名和密码，返回false
            cursor.close();
            return false;
        }
    }

    // 定义一个方法，用于插入新的用户信息
    public boolean insertUser(String username, String avatar, String phone, String password) {
        // 获取可写的数据库对象
        SQLiteDatabase db = this.getWritableDatabase();
        // 定义一个ContentValues对象，用于存储要插入的数据
        ContentValues values = new ContentValues();
        // 向ContentValues对象中添加用户名和密码
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_AVATAR, avatar);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_PASSWORD, password);
        // 执行插入操作，返回一个long值，表示插入的行数
        long result = db.insert(TABLE_NAME_USER, null, values);
        db.close();
        // 判断插入是否成功
        if (result == -1) {
            // 如果返回-1，说明插入失败，返回false
            return false;
        } else {
            // 如果返回其他值，说明插入成功，返回true
            return true;
        }
    }

    public void insertRelate(String ui, int ci){
        SQLiteDatabase db = this.getWritableDatabase();
        // 定义一个ContentValues对象，用于存储要插入的数据
        ContentValues values = new ContentValues();
        values.put(R_COLUMN_USER_NAME, ui);
        values.put(R_COLUMN_CARD_ID, ci);

        db.insert(TABLE_NAME_RELATE, null, values);
        db.close();
    }
}
