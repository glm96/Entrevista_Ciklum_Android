package com.gonzalo.testciklumentrevistaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadActivity extends AppCompatActivity {

    EditText arrAirport, depAirport, starRating;
    Spinner sort1spinner,sort2spinner;
    RequestQueue requestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        arrAirport = (EditText) findViewById(R.id.arrAirport);
        depAirport = (EditText) findViewById(R.id.depAirport);
        starRating = (EditText) findViewById(R.id.starRating);
        sort1spinner = (Spinner) findViewById(R.id.sort1spinner);
        sort2spinner = (Spinner) findViewById(R.id.sort2spinner);

        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Ascending departure date");
        spinnerArray.add("Descending departure date");
        spinnerArray.add("Ascending star rating");
        spinnerArray.add("Descending star rating");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sort1spinner.setAdapter(adapter);
        sort2spinner.setAdapter(adapter);

        final String url = "https://ciklumentrevista.appspot.com/holidaypackages/";

        requestQueue = Volley.newRequestQueue(this);

    }

    public void onClickSubmit(View v){
        String arr, dep, star,sort1,sort2, URL = "?";
        arr = arrAirport.getText().toString();
        dep = depAirport.getText().toString();
        star = starRating.getText().toString();
        sort1 = sort1spinner.getSelectedItem().toString();
        sort2 = sort2spinner.getSelectedItem().toString();
        sort1 = ammendSort(sort1,1);
        sort2 = ammendSort(sort2,2);

        if(!arr.equals(""))
            URL += "IATAarrival="+arr + "&";
        if(!dep.equals(""))
            URL += "IATAorigin="+dep+"&";
        if(!star.equals(""))
            URL += "rating="+star+"&";
        URL += "sort1="+sort1+"&";
        URL += "sort2="+sort2;

        Intent i = new Intent(this,ScrollingActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.putExtra("queryparams", URL);
        startActivity(i);

    }

    private String ammendSort(String s, int i){
        String res = (i==1) ? "-lodging.starRating":"outbound.departureDate";
        switch(s){
            case "Ascending departure date":
                res = "outbound.departureDate";
                break;
            case "Descending departure date":
                res = "-outbound.departureDate";
                break;
            case "Ascending star rating":
                res = "lodging.starRating";
                break;
            case "Descending star rating":
                res = "-lodging.starRating";
                break;
            default:
                break;
        }
        return res;
    }

}
