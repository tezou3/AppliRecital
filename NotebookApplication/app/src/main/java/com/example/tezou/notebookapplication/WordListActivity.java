package com.example.tezou.notebookapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

public class WordListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private final String TABLE_WORDLIST = "WordList";
    private final String KEY_WORD = "wordName";
    private final String KEY_DESCRIPTION = "description";
    private final String TABLE_TAGLIST = "TagList";
    private final String KEY_TAGNAME = "tagName";
    private final String TABLE_LINKTAG = "WordLinkTag";
    private final String KEY_TAG_WORDID = "WORD_ID";
    private final String KEY_TAG_ID = "TAG_ID";

    private ListView mListView;
    private List<String> array_adapter_data = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        // データベース作成
        WordDBOpenHelper helper = new WordDBOpenHelper(getApplicationContext());
        final SQLiteDatabase db = helper.getReadableDatabase();

        // スレッド・ハンドラ作成
        final HandlerThread handlerThread = new HandlerThread("other");
        handlerThread.start();

        // HandlerThreadのLooperをHandlerに渡す
        final Handler handler = new Handler(handlerThread.getLooper());

        mListView = (ListView)findViewById(R.id.wordList);
        final SearchView searchView = (SearchView) findViewById(R.id.searchView);

        handler.post(new Runnable() {
                         @Override
                         public void run() {
                             Cursor cursor = db.query(TABLE_WORDLIST,new String[]{KEY_WORD},
                                     null,null,null,null,null);

                             boolean mov = cursor.moveToFirst();
                             int i = 0;
                             while(mov) {
                                 array_adapter_data.add(i,cursor.getString(0));
                                 Log.i("load","index="+i+", "+cursor.getString(0));
                                 mov = cursor.moveToNext();
                                 Log.i("","mov="+mov+"aaa123");
                                 Log.i("","リストの中身="+array_adapter_data.get(i));
                                 i++;
                             }


                         }
                     });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.word_rowdata, array_adapter_data);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);

        // SearchViewの初期表示状態を設定
        searchView.setIconifiedByDefault(false);

        // SearchViewにOnQueryChangeListenerを設定
        searchView.setOnQueryTextListener(this);

        // SearchViewのSubmitボタンを使用不可にする
        searchView.setSubmitButtonEnabled(true);

        // SearchViewに何も入力していない時のテキストを設定
        searchView.setQueryHint("検索文字を入力して下さい。");


    }

    // SearchViewにテキストを入力する度に呼ばれるイベント
    @Override
    public boolean onQueryTextChange(String queryText) {
        if (TextUtils.isEmpty(queryText)) {
            mListView.clearTextFilter();
        } else {
            mListView.setFilterText(queryText.toString());
        }
        return true;
    }
    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
