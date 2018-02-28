package com.gfo.gfo_meesterproef.SearchTool;

import android.content.Context;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by O&O on 28-2-2018.
 */

public class ViewDiameter extends AsyncTask<String, Void, List<String>> {

    Context context;
    public ViewDiameter(Context ctx) {
        context = ctx;
    }

    @Override
    protected List<String> doInBackground(String... params) {

        String type = params[0];
        String view_url = "https://mantixcloud.nl/gfo/searchtool/diameters.php";
        String result;
        String[] splitResultArray;
        List<String> splitResultList = new ArrayList<String>();
        if (type.equals("view")){
            try {
                //                connect to database
                URL url = new URL(view_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                //                receive data
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                result = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result += line;
                }
                //                split result at , into array
                splitResultArray = result.split(",");
                splitResultList = (Arrays.asList(splitResultArray));

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return splitResultList;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return splitResultList;
    }
}
