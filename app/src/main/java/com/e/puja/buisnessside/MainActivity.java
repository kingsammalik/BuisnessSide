package com.e.puja.buisnessside;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.e.puja.buisnessside.Response.RegisterResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    EditText userid,password;
    Spinner disVen;
    SharedPreferences sharedpreferences;
    Button login;
    public static final String MyPREFERENCES = "MyPrefs" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userid=(EditText)findViewById(R.id.Userid);
        password=(EditText)findViewById(R.id.Password);

        disVen=(Spinner)findViewById(R.id.disVen);
        login=(Button)findViewById(R.id.loginButton);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        String option[]={"Distributor","Vendor"};

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,option);
        disVen.setAdapter(adapter);
        //makeJsonObjectRequest();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register(GenericConstants.getBaseurl()+"loginDistributor");
            }
        });



    }



    private void register(String url){
        final ProgressDialog progressDialog=new ProgressDialog(MainActivity.this);
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Map<String, String> params = new HashMap<String, String>();
        params.put("password", password.getText().toString());
        params.put("mobile", userid.getText().toString());

        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST,
                url,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Reger", response.toString());
                        RegisterResponse registerResponse=new Gson().fromJson(response.toString(),RegisterResponse.class);
                        if (registerResponse.getStatus()==200){
                            progressDialog.cancel();
                            Toast.makeText(MainActivity.this,registerResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putBoolean("ISLOGGED", true);
                        editor.putString("USERID", registerResponse.getData().get(0).getId());
                        editor.putString("USERTYPE", registerResponse.getData().get(0).getUserType());
                        editor.commit();
                        switch (registerResponse.getData().get(0).getUserType()){
                            case "1":
                                startActivity(new Intent(MainActivity.this,ProcessorActivity.class)
                                        .putExtra("VENDOR_ID",registerResponse.getData().get(0).getId()));
                                break;
                            case "2":
                                startActivity(new Intent(MainActivity.this,WarehouseActivity.class)
                                        .putExtra("VENDOR_ID",registerResponse.getData().get(0).getId())
                                        .putExtra("tag","2"));
                                break;
                            case "3":
                                startActivity(new Intent(MainActivity.this,WarehouseActivity.class)
                                        .putExtra("VENDOR_ID",registerResponse.getData().get(0).getId())
                                        .putExtra("tag","3"));
                                break;
                        }
                        finish();
                        }

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
