package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.example.todoapp.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Home extends AppCompatActivity {


    ActivityMainBinding binding;
    ArrayList<String> callList;
    ArrayAdapter<String> listAdaper;
    Handler callListhandler = new Handler();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initializeUserlist();
        binding.callListBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                new fetchdata().start();

            }

            
        });


    }

    private void initializeUserlist() {

        callList = new ArrayList<>();
        listAdaper = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,callList);
//        binding.callList.setAdapter(listAdaper);

    }

    class fetchdata extends Thread{

        String data = "";

        @Override
        public void run() {
            super.run();

            callListhandler.post(new Runnable() {
                @Override
                public void run() {

                    progressDialog = new ProgressDialog(Home.this);
                    progressDialog.setMessage("Fetching Data");
                    progressDialog.setCancelable(false);
                    progressDialog.show();


                }
            });

            try {
                URL url = new URL(  "https://my-json-server.typicode.com/imkhan334/demo-1/call");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;

                while ((line = bufferedReader.readLine()) != null){
                    data = data + line;
                }

                if(!data.isEmpty()){

                    JSONObject jsonObject = new JSONObject(data);
                    JSONArray users = jsonObject.getJSONArray("call");
                    callList.clear();
                    for(int i = 0; i< users.length(); i++){

                        JSONObject names = users.getJSONObject(i);
                        String name = names.getString("names");
                        callList.add(name);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            callListhandler.post(new Runnable() {
                @Override
                public void run() {

                    if (progressDialog.isShowing())
                        progressDialog.dismiss();
                    listAdaper.notifyDataSetChanged();

                }
            });
        }
    }


    Button call_btn = (Button) findViewById(R.id.call_list_btn);
    Button buy_btn = (Button) findViewById(R.id.buy_list_btn);
    Button sell_btn = (Button) findViewById(R.id.sell_list_btn);
}