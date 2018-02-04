package com.example.tezou.notebookapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by tezou on 2018/02/03.
 */

public class RegisterWordActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_word);

        // 登録ボタンを押したときの処理
        findViewById(R.id.regist_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                EditText ew = (EditText)findViewById(R.id.wordName);
                String word = ew.getText().toString();
                EditText ed = (EditText)findViewById(R.id.wordDesc);
                String desc = ed.getText().toString();

                WordInfo wi = new WordInfo();
                wi.wordName = word;
                wi.wordDesc = desc;
                // TODO IDをどうするか
                wi.wordId = 1;
                List<WordInfo> wordList = new ArrayList();
                wordList.add(wi);

                ObjectOutputStream oos = null;

                try {
                    oos = new ObjectOutputStream(new FileOutputStream("wordListTest.xml"));
                    oos.writeObject(wordList);
                } catch (IOException e) {
                } finally {
                    if (oos != null) { try { oos.close(); } catch (Exception ignore) { } }
                }


            }
        });
    }



    public String word;
    public String description;

    public void regist(){}


}
