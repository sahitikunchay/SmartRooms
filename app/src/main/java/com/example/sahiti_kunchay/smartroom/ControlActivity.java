package com.example.sahiti_kunchay.smartroom;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.w3c.dom.Text;

public class ControlActivity extends AppCompatActivity {
    Switch lightsSwitch;
    Switch lightSwitch2;
    Switch lightSwitch3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        lightsSwitch = (Switch) findViewById(R.id.switch1);
        lightsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    volleyStringRequst("http://192.168.4.1/gpio?state_12=1");
                } else if(!b){
                    volleyStringRequst("http://192.168.4.1/gpio?state_12=0");
                }
            }
        });

        lightSwitch2 = (Switch) findViewById(R.id.switch2);
        lightSwitch3 = (Switch) findViewById(R.id.switch3);

        lightSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    volleyStringRequst("http://192.168.4.1/gpio?state_13=1");
                }else if(!b){
                    volleyStringRequst("http://192.168.4.1/gpio?state_13=0");
                }
            }
        });

        lightSwitch3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    volleyStringRequst("http://192.168.4.1/gpio?state_14=1");
                } else if(!b){
                    volleyStringRequst("http://192.168.4.1/gpio?state_14=0");
                }
            }
        });
    }

    public void volleyStringRequst(String url){

        String  REQUEST_TAG = "com.androidtutorialpoint.volleyStringRequest";

        StringRequest strReq = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("blrappy", response.toString());
                //responseText.setText(response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("blrappy", "Error: " + error.getMessage());
            }
        });

        AppSingleton.getInstance(getApplicationContext()).addToRequestQueue(strReq, REQUEST_TAG);
    }
}
