package com.arkapp.carparknaviagation.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.utility.listeners.GPSListener;

import static com.arkapp.carparknaviagation.utility.maps.others.MapUtils.REQUEST_CHECK_SETTINGS;

public class MainActivity extends AppCompatActivity {

    public static GPSListener gpsListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            gpsListener.onGPSPermissionChecked();
        }

    }
}