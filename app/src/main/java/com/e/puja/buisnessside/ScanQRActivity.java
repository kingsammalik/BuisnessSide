package com.e.puja.buisnessside;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e.puja.buisnessside.Response.ScanResponse;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ScanQRActivity extends AppCompatActivity {

    private com.google.zxing.integration.android.IntentIntegrator qrScan;

    TextView name,weight,packedon,expiry,processor;
    ScanResponse scanResponse;
    Button accept,reject;
    String id="";
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
        name=(TextView)findViewById(R.id.name);
        weight=(TextView)findViewById(R.id.weight);
        packedon=(TextView)findViewById(R.id.packedon);
        expiry=(TextView)findViewById(R.id.expiry);
        processor=(TextView)findViewById(R.id.processor);
        accept=(Button) findViewById(R.id.accept);
        reject=(Button) findViewById(R.id.reject);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        qrScan = new IntentIntegrator(this);
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept.setVisibility(View.INVISIBLE);
                reject.setVisibility(View.INVISIBLE);
                startActivity(new Intent(ScanQRActivity.this,RejectActivity.class).putExtra("QRID",id));
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRecord(GenericConstants.getBaseurl()+"updateStatus");
            }
        });
        qrScan.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data

            String temp[]=result.getContents().split(",");
            id=temp[0];
            Log.e("tag","value "+temp[0]+" "+temp[1]);
            trackRecord(GenericConstants.getBaseurl()+"trackProduct",temp[0]);
            //trackProduct type=id, id=1st
                //type=update
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void trackRecord(String url,String id){
        final ProgressDialog progressDialog=new ProgressDialog(ScanQRActivity.this);
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("type", "id");

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.cancel();
                        Log.e("Reger", response.toString());
                        Toast.makeText(ScanQRActivity.this,"Record recieved successfully.",Toast.LENGTH_SHORT).show();
                         scanResponse=new Gson().fromJson(response.toString(),ScanResponse.class);
                        setUI();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error"," "+error.toString());
                progressDialog.cancel();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(jor);
    }

    private void setUI() {
        name.setText(scanResponse.getData().get(0).getName());
        weight.setText(scanResponse.getData().get(0).getWeight());
        packedon.setText(scanResponse.getData().get(0).getPackedOn());
        expiry.setText(scanResponse.getData().get(0).getExpiryDate());
        processor.setText(scanResponse.getData().get(0).getProcessor());
    }

    private void acceptRecord(String url){
        final ProgressDialog progressDialog=new ProgressDialog(ScanQRActivity.this);
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_type", sharedpreferences.getString("USERID",""));
        params.put("qr_id", id);
        params.put("comment", "");
        params.put("reason", "");
        params.put("type", "update");

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.cancel();
                        Log.e("Reger", response.toString());
                        Toast.makeText(ScanQRActivity.this,"Record recieved successfully.",Toast.LENGTH_SHORT).show();
                        accept.setVisibility(View.INVISIBLE);
                        reject.setVisibility(View.INVISIBLE);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error"," "+error.toString());
                progressDialog.cancel();
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        requestQueue.add(jor);
    }

}
