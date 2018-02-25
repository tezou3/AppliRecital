package com.example.tezou.notebookapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tezou on 2018/02/18.
 */

public class WordDBHelper extends SQLiteOpenHelper {

    private final static String DB_NAME = "worddb";
    private final static int VERSION = 01;

    // コンストラクタ
    public WordDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        // SQL文として出力
        // 用語のテーブルを作成
        // create table str:strという名前のテーブルを作成
        db.execSQL("create table WordList(" +
                "_id integer primary key autoincrement," +   // 用語ID
                "wordName text not null," + // 用語名
                "description text not null" +   // 用語の説明
                ");"
        );
        // 用語とタグを紐づけるためのテーブル
        db.execSQL("create table WordLinkTag(" +
                "WORD_ID integer," +
                "TAG_ID integer" +
                ");"
        );
        // タグのテーブル
        db.execSQL("create table TagList(" +
                "_id integer primary key autoincrement," +
                "tagName text not null" +
                ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        // アプリケーションの更新などによって、データベースのバージョンが上がった場合に実行される処理
        // とりあえず空実装
    }


}
