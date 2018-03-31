package com.e.puja.buisnessside;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e.puja.buisnessside.Response.Data;
import com.e.puja.buisnessside.Response.RegisterResponse;
import com.google.gson.Gson;
import com.shuhart.stepview.StepView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProcessorActivity extends AppCompatActivity {

    Button expiry,reject,submit;
    EditText seed,weight,batch,units;
    Spinner distributor;
    RegisterResponse registerResponse1;
    String expirydate="";
    StepView stepView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processor);
        getDistributors(GenericConstants.getBaseurl()+"getDistributor");
        expiry=(Button) findViewById(R.id.expiry);
        reject=(Button) findViewById(R.id.reject);
        submit=(Button) findViewById(R.id.submit);
        seed=(EditText) findViewById(R.id.seed);
        weight=(EditText) findViewById(R.id.weight);
        batch=(EditText) findViewById(R.id.batch);
        units=(EditText) findViewById(R.id.units);
        distributor=(Spinner) findViewById(R.id.disVen);
        stepView=(StepView) findViewById(R.id.step_view);
        stepView.getState()
                // You should specify only stepsNumber or steps array of strings.
                // In case you specify both steps array is chosen.
                .steps(new ArrayList<String>() {{
                    add("Processing");
                    add("Ware Housing");
                    add("Distributor");
                }})
                // You should specify only steps number or steps array of strings.
                // In case you specify both steps array is chosen.
                .stepsNumber(3)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                .commit();

        final Calendar myCalendar = Calendar.getInstance();
        expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(ProcessorActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            expiry.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                            expirydate=year+"-"+(month+1)+"-"+dayOfMonth;
                    }
                },myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(GenericConstants.getBaseurl()+"addProduct");
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(ProcessorActivity.this,RejectActivity.class));
            }
        });
    }

    private void register(String url){
        Log.e("tag","dis "+registerResponse1.getData().get(distributor.getSelectedItemPosition()).getId());
        final ProgressDialog progressDialog=new ProgressDialog(ProcessorActivity.this);
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("vendor_id", getIntent().getStringExtra("VENDOR_ID"));
        params.put("name", seed.getText().toString());
        params.put("weight", weight.getText().toString());
        params.put("expiry_date", expirydate);
        params.put("no_of_qr", units.getText().toString());
        params.put("distributor_id", registerResponse1.getData().get(distributor.getSelectedItemPosition()).getId());

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.cancel();
                        Log.e("Reger", response.toString());
                    Toast.makeText(ProcessorActivity.this,"Record updated successfully.",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProcessorActivity.this,ProcessorActivity.class));
                    finish();
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

    private void getDistributors(String url){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reger", response.toString());
                        RegisterResponse registerResponse=new Gson().fromJson(response.toString(),RegisterResponse.class);
                        if (registerResponse.getStatus()==200){
                            registerResponse1=registerResponse;
                            String names[] = new String[registerResponse.getData().size()];
                            int i=0;
                            for (Data data: registerResponse.getData()){
                                names[i]=data.getName();
                                i++;
                            }
                            ArrayAdapter<String> adapter=new ArrayAdapter<String>(ProcessorActivity.this,android.R.layout.simple_dropdown_item_1line,names);
                            distributor.setAdapter(adapter);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error"," "+error.toString());
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
