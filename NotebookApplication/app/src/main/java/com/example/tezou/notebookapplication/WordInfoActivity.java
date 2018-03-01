package com.example.tezou.notebookapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class WordInfoActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    protected final Object lockObject = new Object();
    ArrayList<String> tagIDList = new ArrayList<>();
    ArrayList<String> wordIDList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_info);

        Intent intent = getIntent();
        final ArrayList<String> base_info = intent.getStringArrayListExtra(SC.BASE_INFO);
        Log.i("YUZO",SC.BASE_INFO+"="+base_info);
        final ArrayList<String> tag_info = intent.getStringArrayListExtra(SC.TAG_INFO);
        Log.i("YUZO",SC.TAG_INFO+"="+tag_info);

        final TextView wordName = findViewById(R.id.show_wordName);
        final TextView wordDesc = findViewById(R.id.show_wordDesc);
        wordName.setText(base_info.get(1));
        wordDesc.setText(base_info.get(2));

        ArrayAdapter<String> adapter = new ArrayAdapter(getApplicationContext(),R.layout.tag_rowdata,tag_info);
        final ListView tagListView = (ListView) findViewById(R.id.show_tagName);
        tagListView.setAdapter(adapter);

        findViewById(R.id.to_edit_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),WordEditActivity.class);
                intent.putExtra(SC.BASE_INFO, base_info);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        WordDBOpenHelper helper = new WordDBOpenHelper(getApplicationContext());
        final SQLiteDatabase db = helper.getReadableDatabase();

        // UIスレにルーパー作成
        final Handler mHandler = new Handler();

        final String tagItem = adapterView.getItemAtPosition(i).toString();

        // スレッド・ハンドラ作成
        final HandlerThread handlerThread2 = new HandlerThread("other");
        handlerThread2.start();

        // HandlerThreadのLooperをHandlerに渡す
        final Handler handler2 = new Handler(handlerThread2.getLooper());

        // タグ名からタグIDを取得
        handler2.post(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObject) {

                    Log.i("YUZO", "Thread getTag run start.");
                    Cursor cursor = db.query(SC.TABLE_TAGLIST, new String[]{SC.KEY_TAG_ID},
                            SC.KEY_TAGNAME + " = ?", new String[]{tagItem}, null, null, null);
                    boolean mov = cursor.moveToFirst();
                    if (!mov) {
                        return;
                    }
                    int i = 0;
                    while (mov) {
                        tagIDList.add(i, cursor.getString(i));
                        Log.i("load", "index=" + i + ", id =" + cursor.getString(0));
                        mov = cursor.moveToNext();
                        i++;
                    }
                }
            }
        });

        // タグの名前を取得する処理
        handler2.post(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObject) {
                    Log.i("YUZO", "Thread getTagName run start.");
                    String tag_id_array[] = tagIDList.toArray(new String[tagIDList.size()]);

                    // 検索条件の数を調整
                    StringBuilder sb = new StringBuilder();
                    sb.append(" = ? ");
                    for (int i = 0; i < tagIDList.size(); i++) {
                        if (i > 0) {
                            sb.append("OR ? ");
                        }
                    }
                    String selectionArgs = sb.toString();
                    Log.i("YUZO", "selectionArgs build done. selectionArgs:" + selectionArgs);
                    // tag_idの初期化
                    tagIDList = new ArrayList<>();
                    Log.i("YUZO", "tag_id refresh done. tag_id=" + tagIDList);

                    //Log.i("YUZO", "array = "+array+", "+"size="+array.length);
                    Cursor cursor = db.query(SC.TABLE_LINKTAG, new String[]{SC.KEY_TAG_WORDID}, SC.KEY_ID + selectionArgs, tag_id_array, null, null, null);
                    boolean mov = cursor.moveToFirst();
                    if (!mov) {
                        return;
                    }
                    int i = 0;
                    while (mov) {
                        wordIDList.add(i, cursor.getString(0));
                        Log.i("load", "index=" + i + ", " + cursor.getString(0));
                        mov = cursor.moveToNext();
                        i++;
                    }
                    Log.i("YUZO", "Thread getTagName run end.");
                }
            }
        });
    }

}
