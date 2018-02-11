package com.gfo.gfo_meesterproef;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.gfo.gfo_meesterproef.Admin.AdminActivity;
import com.gfo.gfo_meesterproef.Support.ConnectionCheck;

import com.gfo.gfo_meesterproef.User.UserActivity;

import java.util.concurrent.ExecutionException;


public class LoginActivity extends AppCompatActivity {

    //    Handler to display loading spinner
    Handler showSpinnerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){progressBar.setVisibility(View.VISIBLE);}
    };
//    Handler to hide spinner
    Handler hideSpinnerHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){progressBar.setVisibility(View.GONE);}
    };

    EditText usernameET, passwordET;
    ProgressBar progressBar;

    public static Context contextOfLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        connect views with id
        usernameET = (EditText) findViewById(R.id.editTextUser);
        passwordET = (EditText) findViewById(R.id.editTextPass);
//        setup progressBar
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        ViewCompat.setTranslationZ(progressBar, 1000);
        progressBar.setVisibility(View.GONE);
//        needed to save username
        contextOfLogin = getApplicationContext();
    }

    public void login(View view) {
        //        create Runnable for showing spinner
        Runnable show = new Runnable() {
            @Override
            public void run() {showSpinnerHandler.sendEmptyMessage(0);}
        };
//        create Runnable for hiding spinner
        Runnable hide = new Runnable() {
            @Override
            public void run() {hideSpinnerHandler.sendEmptyMessage(0);}
        };
//        start displaying Thread
        Thread spinnerThread = new Thread(show);
        spinnerThread.start();

//        check for internet connection
        boolean connection = new ConnectionCheck().test(getApplicationContext());
        if (!connection){return;}

//        check if username is not empty
        String username = usernameET.getText().toString();
        if(TextUtils.isEmpty(username)){
            usernameET.setError("Please enter a username");
            //                switch to hiding Thread
            spinnerThread = new Thread(hide);
            spinnerThread.run();
            return;
        }//        check if password is not empty
        String password = passwordET.getText().toString();
        if(TextUtils.isEmpty(password)){
            passwordET.setError("Please enter a password");
            //                switch to hiding Thread
            spinnerThread = new Thread(hide);
            spinnerThread.run();
            return;
        }

//        save username
        SharedPreferences usernamePref = getSharedPreferences("usernamePreference", contextOfLogin.MODE_PRIVATE);
        usernamePref.edit().putString("username", username).apply();
//        contact database
        String type = "login";
        String adminFlag = null;
        try {
            adminFlag = new LoginBackgroundWorker(this).execute(type, username, password).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        check adminFlag and clear EditTexts
        switch (adminFlag) {
            case "Y"://    admin
                usernameET.getText().clear();
                passwordET.getText().clear();
                Intent i = new Intent(this, AdminActivity.class);
                this.startActivity(i);
                break;
            case "n"://    user
                usernameET.getText().clear();
                passwordET.getText().clear();
                Intent k = new Intent(this, UserActivity.class);
                this.startActivity(k);
                break;
            default:
                Toast.makeText(this,
                        "Incorrect Username/Password", Toast.LENGTH_LONG).show();
//                switch to hiding Thread
                spinnerThread = new Thread(hide);
                spinnerThread.run();
                break;
        }
    }
}