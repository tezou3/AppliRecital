package com.example.tezou.notebookapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.text.TextUtils;

public class WordListActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);

        mListView = (ListView)findViewById(R.id.wordList);
        SearchView searchView = (SearchView) findViewById(R.id.searchView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.activity_word_list, array_adapter_data);
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
