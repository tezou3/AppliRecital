package com.example.tezou.notebookapplication;

import android.app.DialogFragment;
import android.widget.Toast;

import com.example.tezou.notebookapplication.dialog.DialogNotFound;
import com.example.tezou.notebookapplication.dialog.DialogSameName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tezou on 2018/02/04.
 */

public class WordInfo {
    //コンストラクタ
    WordInfo(){

    }

    public String wordName;
    public String wordDesc;
    public int wordId;
    public List<String> tagNameList = new ArrayList();

    // こいつをどこで呼び出すか
    // タグの追加
    private void tagWrite(String tagName) {

        // すでに同じ名前がないか
        for (int i = 0; i <= tagNameList.size() - 1; i++){
            if (tagName.equals(tagNameList.get(i))){
                DialogFragment dialog = new DialogSameName();
                dialog.show(dialog.getFragmentManager(), "DialogSameName");
                return;
            }
        }
        tagNameList.add(tagName);

        return;
    }

    // タグの削除
    private void tagDelete(String tagName) {

        // タグの名前と合致するやつ探す
        for (int i = 0; i <= tagNameList.size() - 1; i++){
            if (tagName.equals(tagNameList.get(i))){
                tagNameList.remove(i);
                return;
            }
            else {
                // 見つかりませんのダイアログ
                DialogFragment dialog = new DialogNotFound();
                dialog.show(dialog.getFragmentManager(), "DialogNotFound");
                return;
            }
        }
    }

}
