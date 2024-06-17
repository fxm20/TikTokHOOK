package com.fxm.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "DPIHookPrefs";
    private static final String KEY_DPI = "dpi_value";
    private static final int DEFAULT_DPI = 200;
    private static final String TAG = "DPIHook";
    private static final String ACTION_UPDATE_DPI = "com.fxm.myapplication.UPDATE_DPI";
    private static final String EXTRA_DPI = "dpi_value";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText dpiInput = findViewById(R.id.dpi_input);
        Button btnSetDpi = findViewById(R.id.btn_set_dpi);
        Button btnResetDpi = findViewById(R.id.btn_reset_dpi);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedDpi = prefs.getInt(KEY_DPI, DEFAULT_DPI);
        dpiInput.setText(String.valueOf(savedDpi));

        btnSetDpi.setOnClickListener(v -> {
            try {
                int dpi = Integer.parseInt(dpiInput.getText().toString());
                prefs.edit().putInt(KEY_DPI, dpi).apply();
                sendDpiUpdateBroadcast(dpi);
                Log.d(TAG, "DPI value saved: " + dpi);
                Toast.makeText(MainActivity.this, "DPI value saved: " + dpi, Toast.LENGTH_SHORT).show();
            } catch (NumberFormatException e) {
                Log.e(TAG, "Invalid DPI value entered", e);
                Toast.makeText(MainActivity.this, "Invalid DPI value entered", Toast.LENGTH_SHORT).show();
            }
        });

        btnResetDpi.setOnClickListener(v -> {
            prefs.edit().putInt(KEY_DPI, DEFAULT_DPI).apply();
            dpiInput.setText(String.valueOf(DEFAULT_DPI));
            sendDpiUpdateBroadcast(DEFAULT_DPI);
            Log.d(TAG, "DPI value reset to default: " + DEFAULT_DPI);
            Toast.makeText(MainActivity.this, "DPI reset to default: " + DEFAULT_DPI, Toast.LENGTH_SHORT).show();
        });
    }

    private void sendDpiUpdateBroadcast(int dpi) {
        Intent intent = new Intent(ACTION_UPDATE_DPI);
        intent.putExtra(EXTRA_DPI, dpi);
        sendBroadcast(intent);
    }
}
