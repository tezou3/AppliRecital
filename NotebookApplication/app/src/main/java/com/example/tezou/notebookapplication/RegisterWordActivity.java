package com.example.tezou.notebookapplication;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by tezou on 2018/02/03.
 */

public class RegisterWordActivity extends AppCompatActivity{

    private final String TABLE_WORDLIST = "WordList";
    private final String KEY_WORD = "wordName";
    private final String KEY_DESCRIPTION = "description";
    private final String TABLE_TAGLIST = "TagList";
    private final String KEY_TAGNAME = "tagName";
    private final String TABLE_LINKTAG = "WordLinkTag";
    private final String KEY_TAG_WORDID = "WORD_ID";
    private final String KEY_TAG_ID = "TAG_ID";

    private List<Long> tagList = new ArrayList<>();

    private final Object lockObject = new Object();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_word);
        final View tagView = findViewById(R.id.tagLayout);
        //tagView.setVisibility(tagView.VISIBLE);

        // データベース作成
        WordDBHelper helper = new WordDBHelper(getApplicationContext());
        final SQLiteDatabase db = helper.getWritableDatabase();

        // スレッド・ハンドラ作成
        final HandlerThread handlerThread = new HandlerThread("other");
        handlerThread.start();

        // HandlerThreadのLooperをHandlerに渡す
        final Handler handler = new Handler(handlerThread.getLooper());

        final EditText ew = (EditText)findViewById(R.id.wordName);
        final EditText ed = (EditText)findViewById(R.id.wordDesc);
        // TODO ここにタグも

        // 登録ボタンを押したときの処理
        findViewById(R.id.regist_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                System.out.println(Thread.currentThread().getName());
                final String word = ew.getText().toString();
                final String desc = ed.getText().toString();

                if (word.equals("")){
                    Toast.makeText(getApplicationContext(), "名前を入力してください。",Toast.LENGTH_SHORT).show();
                }
                else if (desc.equals("")){
                    Toast.makeText(getApplicationContext(), "説明を入力してください。",Toast.LENGTH_SHORT).show();
                }
                else{
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.v("Regist ",Thread.currentThread().getName());
                            // 用語テーブルに追加
                            ContentValues values = new ContentValues();
                            values.put(KEY_WORD,word);
                            values.put(KEY_DESCRIPTION,desc);
                            long id = db.insert(TABLE_WORDLIST,null,values);
                            Log.v("wordRegister","wordID="+id+", "+"word="+word);

                            if (id < 0) {
                                //TODO
                                // -1が返却された時のエラー処理
                                return;
                            }

                            // タグを登録する場合の処理
                            // タグの紐づけ処理
                            if (0 < tagList.size()) {
                                for (int i = 0; i < tagList.size(); i++){
                                    ContentValues twValues = new ContentValues();
                                    twValues.put(KEY_TAG_WORDID,id);
                                    twValues.put(KEY_TAG_ID,tagList.get(i));
                                    Log.v("WordLinkTag","wordID="+id+" toLink "+"tagID="+tagList.get(i));

                                    long id2 = db.insert(TABLE_LINKTAG,null,twValues);
                                    if (id2 < 0) {
                                        //TODO
                                        // -1が返却された時のエラー処理
                                        return;
                                    }
                                }
                                // 保持してたタグを消去
                                tagList.removeAll(tagList);
                            }
                        }
                    });

                    EditText eew = (EditText)findViewById(R.id.wordName);
                    EditText eed = (EditText)findViewById(R.id.wordDesc);
                    eew.getEditableText().clear();
                    eed.getEditableText().clear();
                }
//                WordInfo wi = new WordInfo();
//                wi.wordName = word;
//                wi.wordDesc = desc;
//                // TODO IDをどうするか
//                wi.wordId = 1;
//                List<WordInfo> wordList = new ArrayList();
//                wordList.add(wi);
//
//                ObjectOutputStream oos = null;
//
//                try {
//                    oos = new ObjectOutputStream(new FileOutputStream("wordListTest.xml"));
//                    oos.writeObject(wordList);
//                } catch (IOException e) {
//                } finally {
//                    if (oos != null) { try { oos.close(); } catch (Exception ignore) { } }
//                }
            }
        });

        // タグ追加ボタンの処理
        findViewById(R.id.tagregist_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tagView.setVisibility(tagView.VISIBLE);
            }
        });

        // タグ登録ボタンの処理
        findViewById(R.id.tagregist_button2).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final EditText et = (EditText)findViewById(R.id.tagName_text);
                final String tagName = et.getText().toString();
                if (tagName.equals("")){
                    Toast.makeText(getApplicationContext(), "1文字以上入力してください。",Toast.LENGTH_SHORT).show();
                }
                else {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                            ContentValues values = new ContentValues();
                            values.put(KEY_TAGNAME, tagName);
                            long id = db.insert(TABLE_TAGLIST, null, values);
                            Log.v("tagRegister", "tag_id=" + id + ", " + "tagName=" + tagName);
                            if (id < 0) {
                                //TODO
                                // -1が返却された時のエラー処理
                                return;
                            }
                            // タグを保持
                            tagList.add(id);
                        }
                    });
                    EditText eee = (EditText)findViewById(R.id.tagName_text);
                    eee.getEditableText().clear();

                }
            }
        });

        // タグレイアウト閉じる
        findViewById(R.id.tag_close_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                tagView.setVisibility(tagView.INVISIBLE);
            }
        });

    }
    // onCreateここまで

}
