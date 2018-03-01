package com.example.tezou.notebookapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.text.TextUtils;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener,AdapterView.OnItemClickListener {
    private ListView mListView;
    private List<Map<String, String>> listData = new ArrayList<Map<String, String>>();
    private ArrayList<String> sendData = new ArrayList<>();
    ArrayList<String> tag_id = new ArrayList<String>();
    private ArrayList<String> tagList = new ArrayList<>();

    protected final Object lockObject = new Object();

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
                synchronized (lockObject) {
                    Log.i("YUZO", "Therad getAllWord run start.");
                    Cursor cursor = db.query(SC.TABLE_WORDLIST, new String[]{SC.KEY_ID, SC.KEY_WORD},
                            null, null, null, null, null);

                    boolean mov = cursor.moveToFirst();
                    int i = 0;
                    while (mov) {
                        createListData(cursor.getString(0), cursor.getString(1));
                        Log.i("load", "index=" + i + ", id =" + cursor.getString(0));
                        mov = cursor.moveToNext();
                        i++;
                    }
                    Log.i("YUZO", "Therad getAllWord run end.");
                }
            }
        });

        synchronized (lockObject) {
            Log.i("YUZO", "otherTherad done.");
            SimpleAdapter adapter = new SimpleAdapter(this, listData, R.layout.word_rowdata,
                    new String[]{SC.KEY_ID, "name"}, new int[]{R.id.adapter_id, R.id.adapter_wordName});
            mListView.setAdapter(adapter);
            mListView.setTextFilterEnabled(true);

            mListView.setOnItemClickListener(this);

            // SearchViewの初期表示状態を設定
            searchView.setIconifiedByDefault(false);

            // SearchViewにOnQueryChangeListenerを設定
            searchView.setOnQueryTextListener(this);

            // SearchViewのSubmitボタンを使用不可にする
            searchView.setSubmitButtonEnabled(true);

            // SearchViewに何も入力していない時のテキストを設定
            searchView.setQueryHint("検索文字を入力して下さい。");
        }
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

    private void createListData(String id, String name) {
        Map<String, String> map = new HashMap<String, String>();
        map.put(SC.KEY_ID,id);
        map.put("name",name);
        listData.add(map);
    }

    @Override
    public void onItemClick(final AdapterView<?> adapterView, final View view, final int i, final long l) {
        WordDBOpenHelper helper = new WordDBOpenHelper(getApplicationContext());
        final SQLiteDatabase db = helper.getReadableDatabase();

        // UIスレにルーパー作成
        final Handler mHandler = new Handler();

        final HashMap<Object,Object> adapter = (HashMap<Object, Object>) adapterView.getItemAtPosition(i);
        final String id = (String) adapter.get(SC.KEY_ID);
        final String name = (String) adapter.get("name");

        // スレッド・ハンドラ作成
        final HandlerThread handlerThread2 = new HandlerThread("other");
        handlerThread2.start();

        // HandlerThreadのLooperをHandlerに渡す
        final Handler handler2 = new Handler(handlerThread2.getLooper());

        // 単語の基本情報を取得
        handler2.post(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObject) {
                    // メインスレッドを一時停止
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                    }
//                });

                    Log.i("YUZO", "Thread getWordInfo run start.");
                    Cursor cursor = db.query(SC.TABLE_WORDLIST, new String[]{SC.KEY_DESCRIPTION},
                            SC.KEY_ID + " = ?", new String[]{id}, null, null, null);
                    boolean mov = cursor.moveToFirst();
                    if (!mov) {
                        return;
                    }
                    final String desc = cursor.getString(0);
                    sendData = new ArrayList<>();
                    sendData.add(0, id);
                    sendData.add(1, name);
                    sendData.add(2, desc);
                    Log.i("YUZO", "input sendData done. sendData=" + sendData);
                    Log.i("YUZO", "Thread getWordInfo run end.");
                }
            }
        });

        // 単語のIDからタグのIDを取得する処理
        handler2.post(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObject) {
                    Log.i("YUZO", "Thread getTagID run start.");
                    Cursor cursor = db.query(SC.TABLE_LINKTAG, new String[]{SC.KEY_TAG_ID}, SC.KEY_TAG_WORDID + " = ?", new String[]{id}, null, null, null);
                    boolean mov = cursor.moveToFirst();
                    if (!mov) {
                        return;
                    }
                    int i = 0;
                    while (mov) {
                        tag_id.add(i, cursor.getString(0));
                        Log.i("load", "index=" + i + ", " + cursor.getString(0));
                        mov = cursor.moveToNext();
                        i++;
                    }
                    Log.i("YUZO", "Thread getTagID run end.");
                }
            }
        });

        // タグの名前を取得する処理
        handler2.post(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObject) {
                    Log.i("YUZO", "Thread getTagName run start.");
                    String tag_id_array[] = tag_id.toArray(new String[tag_id.size()]);
//                Log.i("YUZO","tag_id_array="+tag_id_array+", size="+tag_id_array.length);
                    //String array[]=new String[tag_id.size()];
                    // 検索条件の数を調整
                    StringBuilder sb = new StringBuilder();
                    sb.append(" = ? ");
                    for (int i = 0; i < tag_id.size(); i++) {
//                    array[i]=tag_id.get(i);
                        if (i > 0) {
                            sb.append("OR ? ");
                        }
                    }
                    String selectionArgs = sb.toString();
                    Log.i("YUZO", "selectionArgs build done. selectionArgs:" + selectionArgs);
                    // tag_idの初期化
                    tag_id = new ArrayList<>();
                    Log.i("YUZO", "tag_id refresh done. tag_id=" + tag_id);

                    //Log.i("YUZO", "array = "+array+", "+"size="+array.length);
                    Cursor cursor = db.query(SC.TABLE_TAGLIST, new String[]{SC.KEY_TAGNAME}, SC.KEY_ID + selectionArgs, tag_id_array, null, null, null);
                    boolean mov = cursor.moveToFirst();
                    if (!mov) {
                        return;
                    }
                    int i = 0;
                    while (mov) {
                        tagList.add(i, cursor.getString(0));
                        Log.i("load", "index=" + i + ", " + cursor.getString(0));
                        mov = cursor.moveToNext();
                        i++;
                    }
                    Log.i("YUZO", "Thread getTagName run end.");
                }
            }
        });



        Log.i("YUZO","Thread done.");
//        while(!isThreadDone){
//            Log.w("","isThreadDone="+isThreadDone);
//            try {
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        handler2.post(new Runnable() {
            @Override
            public void run() {
                synchronized (lockObject) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            final Intent intent = new Intent(getApplication(),WordInfoActivity.class);
                            Log.i("YUZO", "adapterViewList=" + adapter);
                            Log.i("YUZO", "view=" + view);
                            Log.i("YUZO", "i=" + i);
                            Log.i("YUZO", "l=" + l);

                            Log.i("YUZO", "sendData=" + sendData);
                            Log.i("YUZO", "tagList=" + tagList);
                            intent.putExtra(SC.BASE_INFO, sendData);
                            intent.putExtra(SC.TAG_INFO, tagList);
                            tagList = new ArrayList<>();
                            Log.i("YUZO", "tagList refresh done. tagList=" + tagList);
                            startActivity(intent);
                        }
                    });
                }
            }
        });


    }
}
