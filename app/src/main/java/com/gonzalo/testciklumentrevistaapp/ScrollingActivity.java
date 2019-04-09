package com.gonzalo.testciklumentrevistaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ScrollingActivity extends AppCompatActivity {




    RequestQueue requestQueue;
    String URL = "https://ciklumentrevista.appspot.com/holidaypackages";
    String uri;
    HolidayPackagesAdapter adapter;
    RecyclerView recyclerView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        requestQueue = Volley.newRequestQueue(this);
        uri = getIntent().getStringExtra("queryparams");
        recyclerView = findViewById(R.id.recyclerView);
        doGet(URL+uri);


/*
        Log.d("URL",map.get("IATAorigin"));
        Log.d("URL",map.get("IATAarrival"));
        Log.d("URL",map.get("rating"));
        Log.d("URL",map.get("sort1"));
        Log.d("URL",map.get("sort2"));*/


        }

    public void doGet (String url){

        final JsonArrayRequest getRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>()
                {
                    @Override
                    public void onResponse(JSONArray response) {
                        ObjectMapper mapper = new ObjectMapper();
                        try {
                            List<HolidayPackage> holidayPackages =  mapper.readValue(response.toString(),
                                    new TypeReference<List<HolidayPackage>>(){});
                            adapter = new HolidayPackagesAdapter(holidayPackages, new HolidayPackagesAdapter.OnItemClickListener () {
                                @Override
                                public void onItemClick(HolidayPackage item) {
                                    Intent intent = new Intent(ScrollingActivity.this, UpdateDeleteActivity.class);
                                    ObjectMapper mapper = new ObjectMapper();
                                    try {
                                        intent.putExtra("item", mapper.writeValueAsString(item));
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } catch (Exception e) {
                                        Log.e("intenterror", "error launching intent");
                                    }
                                }});

                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(ScrollingActivity.this));
                            recyclerView.setAdapter(adapter);
                            Log.d("Response", response.toString());
                        }catch (Exception e){ Log.e("HiThere","Error parsing JSON");}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("HttpClient", "error: " + error.toString());
                        Toast.makeText(ScrollingActivity.this, "There was an error, please try again", Toast.LENGTH_SHORT).show();
                    }
                })
        {

        };
// add it to the RequestQueue
        requestQueue.add(getRequest);
        Log.d("URL",getRequest.getUrl());
    }

}
