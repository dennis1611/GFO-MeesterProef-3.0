package com.gfo.gfo_meesterproef.SearchTool;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.gfo.gfo_meesterproef.R;
import com.gfo.gfo_meesterproef.User.UserActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class LookUpMeterActivity extends AppCompatActivity {

    String gRating, diameter;
    Spinner gRatingSpinner, diameterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_up_meter);
//        get possible gRatings
        List<String> gRatings = new ArrayList<String>();
        try {
            gRatings = new ViewGRating(this).execute("view").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();}
//        get possible diameters
        List<String> diameters = new ArrayList<String>();
        try {
            diameters = new ViewDiameter(this).execute("view").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();}
//        create gRatingSpinner
        gRatingSpinner = (Spinner) findViewById(R.id.gRatingSpinner);
        ArrayAdapter<String> gRatingAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, gRatings);
        gRatingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gRatingSpinner.setAdapter(gRatingAdapter);
//        create diameterSpinner
        diameterSpinner = (Spinner) findViewById(R.id.diameterSpinner);
        ArrayAdapter<String> diameterAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, diameters);
        diameterAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diameterSpinner.setAdapter(diameterAdapter);
    }



    public void confirm(View view){
//        get selected values
        gRating = gRatingSpinner.getSelectedItem().toString();
        diameter = diameterSpinner.getSelectedItem().toString();

//        get url/location
        String result = null;
        try {
            result = new LookUpMeter(this).execute("lookup", gRating, diameter).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
//        error/open specifications
        switch (result){
            case "No Meter Found":
                Toast.makeText(this, "No Meter Found", Toast.LENGTH_SHORT).show();
                break;
            default:
                Intent web = new Intent(Intent.ACTION_VIEW);
                web.setData(Uri.parse(result));
                startActivity(web);
                break;
        }
    }

    @Override
    public void onBackPressed(){
        Intent i = new Intent(this, UserActivity.class);
        this.finish();
        startActivity(i);
    }
}