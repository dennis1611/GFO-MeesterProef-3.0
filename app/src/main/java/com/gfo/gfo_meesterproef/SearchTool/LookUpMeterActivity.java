package com.gfo.gfo_meesterproef.SearchTool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.User.UserActivity;

public class LookUpMeterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up_meter);
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, UserActivity.class);
        this.finish();
        startActivity(i);
    }
}