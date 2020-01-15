package com.apps.bit.little.wifiledcontrol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.util.Log;


public class SettingsActivity extends AppCompatActivity {

    private EditText edittext_path;
    private CheckBox checkbox_debug;
    private RadioGroup radiogroup_mode;
    private Button button_back;
    private RadioButton radiobutton_hex;
    private RadioButton radiobutton_rgb;

    public static final String SETTINGS_PREFS = "SettingsPrefsFile";
    private static final String TAG = "debug_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        edittext_path = findViewById(R.id.edittext_path);
        checkbox_debug = findViewById(R.id.checkbox_debug);
        radiogroup_mode = findViewById(R.id.radiogroup_mode);
        button_back = findViewById(R.id.button_save);
        radiobutton_hex = findViewById(R.id.radiobutton_hex);
        radiobutton_rgb = findViewById(R.id.radiobutton_rgb);

        SharedPreferences prefs = getSharedPreferences(SETTINGS_PREFS, MODE_PRIVATE);
        String pref_path = prefs.getString("path", "http://192.168.2.100:5000/color");
        Boolean pref_debug = prefs.getBoolean( "debug", true);
        Boolean pref_hex= prefs.getBoolean("hex", true);


        // Setting the previous settings
        edittext_path.setText(pref_path);
        checkbox_debug.setChecked(pref_debug);
        if (pref_hex)
            radiogroup_mode.check(R.id.radiobutton_hex);
        else
            radiogroup_mode.check(R.id.radiobutton_rgb);

        final SharedPreferences.Editor editor = getSharedPreferences(SETTINGS_PREFS, MODE_PRIVATE).edit();
        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("path", edittext_path.getText().toString());
                editor.putBoolean("debug", checkbox_debug.isChecked());
                boolean hex_or_not = true;
                if(radiogroup_mode.getCheckedRadioButtonId()==R.id.radiobutton_rgb)
                    hex_or_not = false;
                editor.putBoolean("hex", hex_or_not);
                editor.apply();

                Toast.makeText(SettingsActivity.this, "Succesfully saved settings", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }







}
