package com.example.dllo.tomatotodo.potatolist.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dllo.tomatotodo.R;
import com.example.dllo.tomatotodo.base.BaseActivity;
import com.example.dllo.tomatotodo.db.DBTools;
import com.example.dllo.tomatotodo.main.MainActivity;
import com.example.dllo.tomatotodo.potatolist.data.PhtatoListData;
import com.litesuits.orm.db.model.ColumnsValue;

/**
 * Created by dllo on 16/7/28.
 */
public class EditPotatolistActivity extends BaseActivity {
    private EditText editText;
    private ImageView saveIv, backIv;
    private PhtatoListData data;


    @Override
    public int initView() {
        return R.layout.activity_edit_potatolist;
    }

    @Override
    public void initData() {
        data = new PhtatoListData();
        saveIv = (ImageView) findViewById(R.id.aty_edit_potatolist_ok);
        editText = (EditText) findViewById(R.id.aty_edit_potatolist_content_et);
        Intent intent = getIntent();
        String content = intent.getStringExtra("content");
        editText.setText(content);
        String editContent = editText.getText().toString();
        data.setContent(editContent);
//        DBTools.getInstance(this).queryCondition(PhtatoListData.class, "content", data.getContent());
//        DBTools.getInstance(EditPotatolistActivity.this).upDataCondition(editContent);
        saveIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent saveIntent = new Intent(EditPotatolistActivity.this, MainActivity.class);
                Intent sendIntent = new Intent("editContent");
                sendBroadcast(sendIntent);
                startActivity(saveIntent);
            }
        });


        backIv = (ImageView) findViewById(R.id.aty_edit_potatolist_return);
        backIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditPotatolistActivity.this, MainActivity.class));
            }
        });
    }


}
