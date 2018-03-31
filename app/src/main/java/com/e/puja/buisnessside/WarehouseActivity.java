package com.e.puja.buisnessside;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.shuhart.stepview.StepView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class WarehouseActivity extends AppCompatActivity {

    Button expiry,reject,submit,scan;
    EditText weight;
    String expirydate="";
    StepView stepView;
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warehouse);
        expiry=(Button) findViewById(R.id.expiry);
        reject=(Button) findViewById(R.id.reject);
        submit=(Button) findViewById(R.id.submit);
        scan=(Button) findViewById(R.id.scan);
        weight=(EditText) findViewById(R.id.weight);
        title=(TextView) findViewById(R.id.title);


        stepView=(StepView) findViewById(R.id.step_view);
        stepView.getState()
                .selectedTextColor(ContextCompat.getColor(this, R.color.colorAccent))
                .animationType(StepView.ANIMATION_CIRCLE)
                .selectedCircleColor(ContextCompat.getColor(this, R.color.colorAccent))
                .selectedStepNumberColor(ContextCompat.getColor(this, android.R.color.white))
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

        if (getIntent().getStringExtra("tag").equals("2")){
            title.setText("WAREHOUSING");
            stepView.go(1,true);
        }
        else {
            title.setText("DISTRIBUTOR");
            stepView.go(2,true);
        }


        final Calendar myCalendar = Calendar.getInstance();
        expiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(WarehouseActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                startActivity(new Intent(WarehouseActivity.this,RejectActivity.class));
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WarehouseActivity.this,ScanQRActivity.class));
            }
        });
    }

    private void register(String url){
        final ProgressDialog progressDialog=new ProgressDialog(WarehouseActivity.this);
        progressDialog.show();
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        /*params.put("vendor_id", getIntent().getStringExtra("VENDOR_ID"));
        params.put("name", seed.getText().toString());
        params.put("weight", weight.getText().toString());
        params.put("expiry_date", expirydate);
        params.put("no_of_qr", units.getText().toString());
        params.put("distributor_id", registerResponse1.getData().get(distributor.getSelectedItemPosition()).getId());*/

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.cancel();
                        Log.e("Reger", response.toString());
                        Toast.makeText(WarehouseActivity.this,"Record updated successfully.",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(WarehouseActivity.this,ProcessorActivity.class));
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
}
