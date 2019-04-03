package com.gonzalo.testciklumentrevistaapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class UpdateDeleteActivity extends AppCompatActivity {

    EditText ibFC, ibDC, ibDD, ibAC, ibAD, obFC, obDC, obDD, obAC, obAD, hotName, hotCode, hotLat, hotLon, hotRat, pricetv;
    RequestQueue requestQueue;
    final String url = "https://ciklumentrevista.appspot.com/holidaypackages";
    HolidayPackage hp;
    ObjectMapper mapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);

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
        mapper = new ObjectMapper();
        Intent intent = getIntent();

        try {
            hp = mapper.readValue(intent.getStringExtra("item"), HolidayPackage.class);
            fillBoxes(hp);
        }catch (Exception e){
            Log.e("hola", "error parsing from json");}

    }

    private void fillBoxes (HolidayPackage hp) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm:ss");
        Flight ib = hp.getInbound(), ob = hp.getOutbound();
        Hotel hot = hp.getLodging();

        ibFC.setText(ib.getFcode());
        ibDC.setText(ib.getDepartureCode());
        ibDD.setText(sdf.format(ib.getDepartureDate()));
        ibAC.setText(ib.getArrivalCode());
        ibAD.setText(sdf.format(ib.getArrivalDate()));
        obFC.setText(ob.getFcode());
        obDC.setText(ob.getDepartureCode());
        obDD.setText(sdf.format(ob.getDepartureDate()));
        obAC.setText(ob.getArrivalCode());
        obAD.setText(sdf.format(ob.getArrivalDate()));
        hotName.setText(hot.getName());
        hotCode.setText(Long.toString(hot.getCode()));
        hotLat.setText(Double.toString(hot.getGeoLocation().getLatitude()));
        hotLon.setText(Double.toString(hot.getGeoLocation().getLongitude()));
        hotRat.setText(Short.toString(hot.getStarRating()));
        pricetv.setText(Double.toString(hp.getPrice()));

    }

    public void onClickDelete(View v){
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, url+"/"+Long.toString(hp.getId()),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response2", response);
                        Log.d("Response2", response.trim().equals("Done") ? "Yes":"No");
                        if(response.trim().equals("Done"))
                            Toast.makeText(UpdateDeleteActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(UpdateDeleteActivity.this, "Error on the petition", Toast.LENGTH_SHORT).show();
                        getOut();
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
        );

        requestQueue.add(postRequest);
    }

    public void onClickUpdate(View v){
        Long id = hp.getId();
        hp = grabData();
        hp.setId(id);
        try {
            String JSONstring = mapper.writeValueAsString(hp);
            Log.d("update",mapper.writeValueAsString(hp));
            if(hp.checkCorrect()) {
                doDelete(JSONstring, id);
            }
            else
                Toast.makeText(this, "Wrong format", Toast.LENGTH_SHORT).show();
        }catch (Exception e){Log.e("error","Error parsing to JSON");}

    }
    private void doDelete(String s, Long id){
        JSONObject jsonBodyObj = new JSONObject();
        final String requestBody = s;
        Log.d("debugDelete",s);
        final Long idfinal = id;

        StringRequest putRequest = new StringRequest(Request.Method.PUT, url+"/"+Long.toString(hp.getId() ),
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if(response.equals("Done"))
                            Toast.makeText(UpdateDeleteActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(UpdateDeleteActivity.this, "Error on the petition", Toast.LENGTH_SHORT).show();
                        getOut();
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
        ){
         /*   @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", Long.toString(idfinal));
                return map;
            }*/
            @Override
            public byte[] getBody() {
                try {
                    // Log.d("request",requestBody);
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody, "utf-8");
                    Log.e("error", "3");
                    return null;
                }
            }
        };
        requestQueue.add(putRequest);

    }

    private HolidayPackage grabData(){
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
        return hp;
    }

    private void getOut(){
        Intent i = new Intent(UpdateDeleteActivity.this,MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }


}
