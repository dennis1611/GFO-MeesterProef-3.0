package com.gfo.gfo_meesterproef.User;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.gfo.gfo_meesterproef.LoginActivity;
import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.SearchTool.LookUpMeterActivity;
import com.gfo.gfo_meesterproef.SearchTool.SearchMeterActivity;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
    }

//    Intents to other activities
    public void FetchProductActivity(View view){
        Intent i = new Intent(this, FetchProductActivity.class);
        startActivity(i);
    }
    public void SearchMeterActivity(View view){
        Intent i = new Intent(this, SearchMeterActivity.class);
        startActivity(i);
    }
    public void LookUpMeterActivity(View view){
        Intent i = new Intent(this, LookUpMeterActivity.class);
        startActivity(i);
    }


    @Override//    confirmation for logout
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(UserActivity.this, LoginActivity.class);
                UserActivity.this.finish();
                startActivity(i);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
