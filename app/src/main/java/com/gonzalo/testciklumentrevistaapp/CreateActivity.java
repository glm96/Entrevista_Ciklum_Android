package com.gonzalo.testciklumentrevistaapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CreateActivity extends AppCompatActivity {

    EditText ibFC, ibDC, ibDD, ibAC, ibAD, obFC, obDC, obDD, obAC, obAD, hotName, hotCode, hotLat, hotLon, hotRat, pricetv;
    RequestQueue requestQueue;
    final String url = "https://ciklumentrevista.appspot.com/holidaypackages";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        ibFC = (EditText) findViewById(R.id.ibfcode);
        ibDC = (EditText) findViewById(R.id.ibdepcode);
        ibDD = (EditText) findViewById(R.id.ibdepdate);
        ibAC = (EditText) findViewById(R.id.ibarrcode);
        ibAD = (EditText) findViewById(R.id.ibarrdate);
        obFC = (EditText) findViewById(R.id.obfcode);
        obDC = (EditText) findViewById(R.id.obdepcode);
        obDD = (EditText) findViewById(R.id.obdepdate);
        obAC = (EditText) findViewById(R.id.obarrcode);
        obAD = (EditText) findViewById(R.id.obarrdate);
        hotName = (EditText) findViewById(R.id.hotelname);
        hotCode = (EditText) findViewById(R.id.hotelcode);
        hotLat = (EditText) findViewById(R.id.hotellatitude);
        hotLon = (EditText) findViewById(R.id.hotellongitude);
        hotRat = (EditText) findViewById(R.id.hotelrating);
        pricetv = (EditText) findViewById(R.id.price);

        requestQueue = Volley.newRequestQueue(this);

    }

    public void onClickSubmit(View v) {
        String ibfc, ibdc, ibdd, ibac, ibad, obfc, obdc, obdd, obac, obad, hname, hcode, hlat, hlon, hrat, price;
        HolidayPackage hp = null;
        Flight inbound, outbound;
        Hotel hotel;
        GeoLocation geoLocation;

        ibfc = ibFC.getText().toString();
        ibdc = ibDC.getText().toString();
        ibdd = ibDD.getText().toString();
        ibac = ibAC.getText().toString();
        ibad = ibAD.getText().toString();
        obfc = obFC.getText().toString();
        obdc = obDC.getText().toString();
        obdd = obDD.getText().toString();
        obac = obAC.getText().toString();
        obad = obAD.getText().toString();
        hname = hotName.getText().toString();
        hcode = hotCode.getText().toString();
        hlat = hotLat.getText().toString();
        hlon = hotLon.getText().toString();
        hrat = hotRat.getText().toString();
        price = pricetv.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");

        try {
            inbound = new Flight(ibfc, ibdc, ibac, sdf.parse(ibad), sdf.parse(ibdd));
            outbound = new Flight(obfc, obdc, obac, sdf.parse(obad), sdf.parse(obdd));
            geoLocation = new GeoLocation(Double.parseDouble(hlat), Double.parseDouble(hlon));
            hotel = new Hotel(Long.parseLong(hcode), hname, Short.parseShort(hrat), geoLocation);
            hp = new HolidayPackage(inbound, outbound, hotel, Double.parseDouble(price));
        } catch (Exception e) {
            Toast.makeText(this, "Incorrect arguments", Toast.LENGTH_SHORT).show();
        }
       // hp = createDummyHoliday();

        if(hp!= null){
            if(hp.checkCorrect()){
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    if(hp.checkCorrect())
                        createEntry(mapper.writeValueAsString(hp));
                    else
                        Toast.makeText(this, "Wrong data", Toast.LENGTH_SHORT).show();
                }catch(Exception e ){Log.e("error","parseando json");}
            }
            else{
                Log.e("error","1");
                Toast.makeText(this, "Wrong format", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void onClickRandom(View v){
        HolidayPackage hp = createDummyHoliday();
        ObjectMapper mapper = new ObjectMapper();
        try {
            createEntry(mapper.writeValueAsString(hp));
        }catch(Exception e){};
    }


    public void createEntry(String s){
        /*Some example that you can pass your data through request body*/RequestQueue requestQueue = Volley.newRequestQueue(this);
        JSONObject jsonBodyObj = new JSONObject();
        final String requestBody = s;

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if(response.equals("Done"))
                            Toast.makeText(CreateActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(CreateActivity.this, "Error on the petition", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", "");
                    }
                }
        ) {
            @Override
            public byte[] getBody() {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    Log.e("error", "3");
                    return null;
                }
            }
        };

        requestQueue.add(postRequest);
    }

    /**
     * Test method for generating random HolidayPackage instances
     * @return Semi-random instance of HolidayPackage
     */
    public HolidayPackage createDummyHoliday () {
        Flight ib, ob;
        Hotel hotel;
        double precio = 1.11;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        HolidayPackage holiday = new HolidayPackage();
        try {
            Random rand = new Random();
            int fecha = rand.nextInt(20)+1;
            String s1 = Integer.toString(fecha), s2 = Integer.toString(fecha+1);
            Date date2 = sdf.parse(s1+"/06/19 10:34:22");
            Date date1 = sdf.parse(s2+"/06/19 10:34:22");
            ib = new Flight("SU2529", "AGP", "SVO", date1, date2);
            ob = ib;
            hotel = new Hotel((long) 12305314, "Suimeikan", (short) rand.nextInt(5), new GeoLocation(Math.random()*100,Math.random()*100));
            holiday = new HolidayPackage(ib,ob,hotel,precio);
        }catch(Exception e) {e.printStackTrace();}

        return holiday;
    }
}