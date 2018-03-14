package com.example.tezou.notebookapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class WordUpdateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_update);

        // データベース作成
        WordDBOpenHelper helper = new WordDBOpenHelper(getApplicationContext());
        final SQLiteDatabase db = helper.getWritableDatabase();

        // スレッド・ハンドラ作成
        final HandlerThread handlerThread = new HandlerThread("other");
        handlerThread.start();

        // HandlerThreadのLooperをHandlerに渡す
        final Handler handler = new Handler(handlerThread.getLooper());

        // メインスレッド
        final Handler mHandler = new Handler();

        Intent intent = getIntent();
        final ArrayList<String> base_info = intent.getStringArrayListExtra(SC.BASE_INFO);
        Log.i("YUZO",SC.BASE_INFO+"="+base_info);

        final EditText ew = (EditText)findViewById(R.id.reinput_wordName);
        final EditText ed = (EditText)findViewById(R.id.reinput_wordDesc);
        ew.setText(base_info.get(1));
        ed.setText(base_info.get(2));


        // 登録ボタンを押したときの処理
        findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String word = ew.getText().toString();
                final String desc = ed.getText().toString();

                if (word.equals("")) {
                    Toast.makeText(getApplicationContext(), "名前を入力してください。", Toast.LENGTH_SHORT).show();
                } else if (desc.equals("")) {
                    Toast.makeText(getApplicationContext(), "説明を入力してください。", Toast.LENGTH_SHORT).show();
                } else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Regist ", Thread.currentThread().getName());

                            String id = base_info.get(0);
                            // 用語テーブルに追加
                            ContentValues values = new ContentValues();
                            values.put(SC.KEY_WORD, word);
                            values.put(SC.KEY_DESCRIPTION, desc);
                            db.update(SC.TABLE_WORDLIST, values, "_id = ?", new String[]{id});

                            // タグを登録する場合の処理
                            // タグの紐づけ処理
//                            if (0 < tagList.size()) {
//                                for (int i = 0; i < tagList.size(); i++){
//                                    ContentValues twValues = new ContentValues();
//                                    twValues.put(SC.KEY_TAG_WORDID,id);
//                                    twValues.put(SC.KEY_TAG_ID,tagList.get(i));
//                                    Log.v("WordLinkTag","wordID="+id+" toLink "+"tagID="+tagList.get(i));
//
//                                    long id2 = db.insert(SC.TABLE_LINKTAG,null,twValues);
//                                    if (id2 < 0) {
//                                        //TODO
//                                        // -1が返却された時のエラー処理
//                                        return;
//                                    }
//                                }
//                                // 保持してたタグを消去
//                                tagList.removeAll(tagList);
//                            }
                        }
                    });

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ew.getEditableText().clear();
                                    ed.getEditableText().clear();
                                    Toast.makeText(getApplicationContext(), "更新が完了しました",Toast.LENGTH_SHORT).show();
                                    finish();

                                    Intent intent = new Intent(getApplication(), WordListActivity.class);
                                    startActivity(intent);

                                }
                            });
                        }
                    });



                }
            }
        });

    }
}
