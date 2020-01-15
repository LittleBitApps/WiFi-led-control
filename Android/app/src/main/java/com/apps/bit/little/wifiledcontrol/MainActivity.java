package com.apps.bit.little.wifiledcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;


import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    //views
    private TextView textview_debug_title;
    private TextView textView_sent1;
    private TextView textView_sent2;
    private TextView textView_received1;
    private TextView textView_received2;
    private Button button_led_off;
    private Button button_quit_app;
    private ColorPicker colorpicker;
    private SVBar svBar;

    //other
    private static final String TAG = "debug_tag";
    private RequestQueue queue;
    public static final String SETTINGS_PREFS = "SettingsPrefsFile";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    textview_debug_title = findViewById(R.id.textview_debug_title);
    textView_sent1 = findViewById(R.id.textview_sent1);
    textView_sent2 = findViewById(R.id.textview_sent2);
    textView_received1 = findViewById(R.id.textview_received1);
    textView_received2 = findViewById(R.id.textview_received2);
    button_led_off = findViewById(R.id.button_led_off);
    button_quit_app = findViewById(R.id.button_quit_app);
    colorpicker = findViewById(R.id.colorpicker);
    svBar = findViewById(R.id.svbar);

    SharedPreferences prefs = getSharedPreferences(SETTINGS_PREFS, MODE_PRIVATE);
    final String pref_path = prefs.getString("path", "http://192.168.2.100:5000/color");
    Boolean pref_debug = prefs.getBoolean( "debug", true);
    final Boolean pref_hex= prefs.getBoolean("hex", true);

    //hide debug info if pref_debug is false
    if(pref_debug==false){
        textview_debug_title.setVisibility(View.INVISIBLE);
        textView_sent1.setVisibility(View.INVISIBLE);
        textView_sent2.setVisibility(View.INVISIBLE);
        textView_received1.setVisibility(View.INVISIBLE);
        textView_received2.setVisibility(View.INVISIBLE);
    }

    colorpicker.addSVBar(svBar);
    queue = Volley.newRequestQueue(this);


    colorpicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener() {
        @Override
        public void onColorChanged(int color) {
            colorpicker.setOldCenterColor(colorpicker.getColor());
            Log.d("colorpicker", Integer.toString(colorpicker.getColor()));
            String hex_string = Integer.toHexString(colorpicker.getColor());
            hex_string = hex_string.substring(2, 8);
            send_requests(pref_path, hex_string, queue);

        }
    });

    button_led_off.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            colorpicker.setOldCenterColor(-16777216);
            colorpicker.setNewCenterColor(-16777216);
            send_requests(pref_path, "000000", queue);
        }
    });

    button_quit_app.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    });

    }



    //function for sending requests
    public void send_requests(String url, final String some_color, RequestQueue qeue){
        queue.cancelAll(new RequestQueue.RequestFilter(){
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }

        });

        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        textView_received2.setText(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        textView_received2.setText(error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("color", some_color);
                return params;
            }
        };
        String request_text = "POST "+ "{\"color\":\""+some_color+"\"} to "+ url;
        textView_sent2.setText(request_text);
        textView_received2.setText("");
        queue.add(postRequest);

    }

    //menu for settings
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_menu:
                // Do whatever you want to do on logout click.
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
