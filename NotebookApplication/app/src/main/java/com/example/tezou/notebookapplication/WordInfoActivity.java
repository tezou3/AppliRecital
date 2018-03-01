package com.example.tezou.notebookapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordInfoActivity extends AppCompatActivity {

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
}
