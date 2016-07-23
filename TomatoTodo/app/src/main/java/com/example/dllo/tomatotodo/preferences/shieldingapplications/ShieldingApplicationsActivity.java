package com.example.dllo.tomatotodo.preferences.shieldingapplications;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.dllo.tomatotodo.R;

public class ShieldingApplicationsActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView alreadyList, noneAlreadyList;
    private ImageView returnIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shielding_applications);

        alreadyList = (ListView) findViewById(R.id.shielding_already_list);
        noneAlreadyList = (ListView) findViewById(R.id.shielding_none_already_list);
        returnIv = (ImageView) findViewById(R.id.shielding_return_iv);

        returnIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.shielding_return_iv:
                finish();
                break;
        }
    }
}
