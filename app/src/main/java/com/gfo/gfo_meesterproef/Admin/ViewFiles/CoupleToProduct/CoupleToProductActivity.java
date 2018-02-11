package com.gfo.gfo_meesterproef.Admin.ViewFiles.CoupleToProduct;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import static com.gfo.gfo_meesterproef.Admin.ViewFiles.ViewProductActivity.contextOfViewProduct;

import com.gfo.gfo_meesterproef.Admin.Couple;
import com.gfo.gfo_meesterproef.Admin.Uncouple;
import com.gfo.gfo_meesterproef.Admin.ViewAccount.ViewAccountBackgroundWorker;
import com.gfo.gfo_meesterproef.Admin.ViewFiles.ViewProductActivity;
import com.gfo.gfo_meesterproef.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class CoupleToProductActivity extends AppCompatActivity {

    String product;
    List<String> totalList, alreadyCoupled, toCouple, toUncouple;
    ListView list;
    String allUsernames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list);
//        reset toCouple and toUncouple
        toCouple = new ArrayList<String>();
        toCouple.clear();
        toUncouple = new ArrayList<String>();
        toUncouple.clear();
//        get selected product
        SharedPreferences selectedProductPref = getSharedPreferences("selectedProductPreference", contextOfViewProduct.MODE_PRIVATE);
        product = selectedProductPref.getString("selectedProduct", "");
//        change label
        setTitle("Couple to "+product);
//        get all usernames as String
        totalList = new ArrayList<>();
        try {
            allUsernames = new ViewAccountBackgroundWorker(this).execute("userUsername").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }//                split result at , into array
        String[] allUsernamesArray = allUsernames.split(",");
        totalList = (Arrays.asList(allUsernamesArray));

//        get already coupled usernames
        alreadyCoupled = new ArrayList<>();
        try {
            alreadyCoupled = new CoupledAccount(this).execute("coupledAccount", product).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

//        display all usernames
        list = (ListView) findViewById(R.id.list);
        list.setBackgroundResource(R.color.white);
        ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, totalList);
        list.setAdapter(listAdapter);

//        compare total list and already coupled list
        list.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < totalList.size(); i++) {
//                    set default tag
                    list.getChildAt(i).setTag(R.color.white);
            if (alreadyCoupled.contains(totalList.get(i))) {
                list.getChildAt(i).setBackgroundResource(R.color.blue);
                list.getChildAt(i).setTag(R.color.blue);
            }
        }
            }
        });

        registerNameClickCallBack();
    }

    private void registerNameClickCallBack() {
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View viewClicked, int position, long id) {
//                get username of clicked view
                TextView textView = (TextView) viewClicked;
//                get color tag and add or delete from to (un)couple list
                String accountName = textView.getText().toString();
                int ColorId = Integer.parseInt(viewClicked.getTag().toString());
                if (ColorId==R.color.blue){
                    viewClicked.setBackgroundResource(R.color.red);
                    viewClicked.setTag(R.color.red);
                    toUncouple.add(accountName);
                } else if (ColorId==R.color.red){
                    viewClicked.setBackgroundResource(R.color.blue);
                    viewClicked.setTag(R.color.blue);
                    toUncouple.remove(accountName);
                } else if (ColorId==R.color.green){
                    viewClicked.setBackgroundResource(R.color.white);
                    viewClicked.setTag(R.color.white);
                    toCouple.remove(accountName);
                } else if (ColorId==R.color.white){
                    viewClicked.setBackgroundResource(R.color.green);
                    viewClicked.setTag(R.color.green);
                    toCouple.add(accountName);
                }
            }
        });
    }

    @Override
//    needed for icon in toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.link, menu);
        return true;
    }
    @Override
//    set onClick to icons in toolbar
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_item_link) {
            couple();
            uncouple();
            Intent i = new Intent(CoupleToProductActivity.this, ViewProductActivity.class);
            i.putExtra("adminProduct", product);
            CoupleToProductActivity.this.finish();
            startActivity(i);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void couple() {
//        couple all selected accounts
        Iterator<String> toCoupleIterator = toCouple.iterator();
        while (toCoupleIterator.hasNext()) {
            String name = toCoupleIterator.next();
            Couple couple = new Couple(this);
            try {
                String echo = couple.execute("couple", name, product).get();
                Toast.makeText(this, echo, Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    private void uncouple() {
//        uncouple all selected accounts
        Iterator<String> toUncoupleIterator = toUncouple.iterator();
        while (toUncoupleIterator.hasNext()) {
            String name = toUncoupleIterator.next();
            Uncouple uncouple = new Uncouple(this);
            try {
                String echo = uncouple.execute("uncouple", name, product).get();
                Toast.makeText(this, echo, Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(CoupleToProductActivity.this, ViewProductActivity.class);
        CoupleToProductActivity.this.finish();
        startActivity(i);
    }
}