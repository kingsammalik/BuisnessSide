package com.e.puja.buisnessside;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeActivity extends AppCompatActivity {
    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.getBoolean("ISLOGGED",false)){
            startActivity(new Intent(this,MainActivity.class));
        }
        else {
            switch (sharedpreferences.getString("USERTYPE","")){
                case "1":
                    startActivity(new Intent(WelcomeActivity.this,ProcessorActivity.class)
                            .putExtra("VENDOR_ID",sharedpreferences.getString("USERID","")));
                    break;
                case "2":
                    startActivity(new Intent(WelcomeActivity.this,WarehouseActivity.class)
                            .putExtra("tag","2"));
                    break;
                case "3":
                    startActivity(new Intent(WelcomeActivity.this,WarehouseActivity.class)
                            .putExtra("tag","3"));
                    break;
            }
        }
    }
}
