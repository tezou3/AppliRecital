package com.example.tezou.notebookapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button registerButton = (Button)findViewById(R.id.to_register_button);
        Button wordlistButton = (Button)findViewById(R.id.to_wordlist_button);
        Button testButton = (Button)findViewById(R.id.to_test_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
//                処理
                Intent intent = new Intent(MainActivity.this, RegisterWordActivity.class);
                // Activityの移動
                startActivity(intent);
            }
        });

        wordlistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
//                処理
                Intent intent = new Intent(MainActivity.this, RegisterWordActivity.class);
                // Activityの移動
                startActivity(intent);
            }
        });

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
//                処理
                Intent intent = new Intent(MainActivity.this, RegisterWordActivity.class);
                // Activityの移動
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "onBackPressed", Toast.LENGTH_SHORT).show();
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Toast.makeText(this, event.toString(), Toast.LENGTH_SHORT).show();
        return super.onTouchEvent(event);
    }


//    @Override
//    protected void onStart() {
//        super.onStart();
//        Toast.makeText(this, "onStart", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Toast.makeText(this, "onResume", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        Toast.makeText(this, "onPause", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//    ここに保存する処理を入れる
//        Toast.makeText(this, "onStop", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    protected void onDestroy() {
//        Toast.makeText(this, "onDestroy", Toast.LENGTH_SHORT).show();
//        super.onDestroy();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        Toast.makeText(this, "onRestart", Toast.LENGTH_SHORT).show();
//    }

}
