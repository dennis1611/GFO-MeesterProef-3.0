package com.gfo.gfo_meesterproef;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.concurrent.ExecutionException;

public class OpenFileActivity extends AppCompatActivity {

    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_file);

        //        get selected group from ViewProductActivity
        String clickedFile = getIntent().getExtras().getString("file", "");

        //contact database for files
        String type = "view";
        String url = null;
        try{
            url = new OpenFileBackgroundWorker(this).execute(type, clickedFile).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace(); }

       Intent web = new Intent(Intent.ACTION_VIEW);
       web.setData(Uri.parse(url));
       startActivity(web);

    }
    @Override
    public void onBackPressed(){
        this.finish();
    }
}