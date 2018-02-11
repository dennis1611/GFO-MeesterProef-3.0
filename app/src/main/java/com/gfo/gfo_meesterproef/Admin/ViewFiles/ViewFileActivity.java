package com.gfo.gfo_meesterproef.Admin.ViewFiles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.gfo.gfo_meesterproef.Admin.ViewFiles.CoupleToProduct.CoupleToProductActivity;
import com.gfo.gfo_meesterproef.OpenFileBackgroundWorker;
import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewFileActivity extends AppCompatActivity {

    public static Context fileContext;

    String clickedFile;
    ListView adminProductList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
        fileContext = getApplicationContext();

//        get selected product from ViewProductActivity
        String product = getIntent().getExtras().getString("adminProduct", "");

//        set label
        setTitle("View Files from "+product);

//        contact database for products
        String type = "view";
        List<String> products = new ArrayList<>();
        try {
            products = new ViewFile(this).execute(type, product).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // fill listView with List
        adminProductList = (ListView) findViewById(R.id.list);
        ArrayAdapter<String> productAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, products);
        adminProductList.setAdapter(productAdapter);

        registerFileClickCallBack();
    }

    private void registerFileClickCallBack() {
        adminProductList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get selected file
                TextView textView = (TextView) viewClicked;
                clickedFile = textView.getText().toString();
//                contact database for files
                String url = null;
                try{
                    url = new OpenFileBackgroundWorker(ViewFileActivity.this).execute("view", clickedFile).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace(); }
//                        open file
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse(url));
                startActivity(web);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewFileActivity.this, ViewProductActivity.class);
        ViewFileActivity.this.finish();
        startActivity(i);
    }
}
