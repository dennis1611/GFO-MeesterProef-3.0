package com.gfo.gfo_meesterproef.Admin.ViewFiles;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gfo.gfo_meesterproef.Admin.AdminActivity;
import com.gfo.gfo_meesterproef.Admin.ViewFiles.CoupleToProduct.CoupleToProductActivity;
import com.gfo.gfo_meesterproef.Custom.FolderAdapter;
import com.gfo.gfo_meesterproef.OpenFileBackgroundWorker;
import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ViewProductActivity extends AppCompatActivity {

    GridView adminProductGrid;
    String selectedProduct;

    public static Context contextOfViewProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);

//      needed to save selectedUsername
        contextOfViewProduct = getApplicationContext();

//        contact database
        String type = "view";
        List<String> products = new ArrayList<String>();
        try {
            products = new ViewProduct(this).execute(type).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        fill listView with (array)List
        adminProductGrid = (GridView) findViewById(R.id.grid);
        FolderAdapter productAdapter = new FolderAdapter(this, products);
        adminProductGrid.setAdapter(productAdapter);

        registerProductClickCallback();
    }

//    select product
    private void registerProductClickCallback() {
        adminProductGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
//                get selected product
                selectedProduct = (String) adminProductGrid.getItemAtPosition(position);

//                choice to couple product or view files
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewProductActivity.this);
                builder.setTitle(selectedProduct);
//                builder.setMessage("What do you want to do?");
                builder.setPositiveButton("Couple Product", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                save selected product for couple to file
                        SharedPreferences selectedProductPref = getSharedPreferences("selectedProductPreference", contextOfViewProduct.MODE_PRIVATE);
                        selectedProductPref.edit().putString("selectedProduct", selectedProduct).apply();
//                        start couple activity
                        Intent i = new Intent(ViewProductActivity.this, CoupleToProductActivity.class);
                        startActivity(i);
                    }
                });
                builder.setNegativeButton("View Files", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                start ViewFileActivity
                        Intent i = new Intent(ViewProductActivity.this, ViewFileActivity.class);
                        i.putExtra("adminProduct", selectedProduct);
                        startActivity(i);}
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
            }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewProductActivity.this, AdminActivity.class);
        ViewProductActivity.this.finish();
        startActivity(i);
    }
}