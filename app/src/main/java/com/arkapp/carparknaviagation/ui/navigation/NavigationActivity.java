package com.arkapp.carparknaviagation.ui.navigation;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.utility.maps.navigation.NavigationUtils;

public class NavigationActivity extends AppCompatActivity {

    private NavigationUtils mapFragmentView;
    private PrefRepository prefRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        prefRepository = new PrefRepository(this);
        setupMapFragmentView();
    }

    private void setupMapFragmentView() {
        mapFragmentView = new NavigationUtils(this, prefRepository);
    }

    @Override
    public void onDestroy() {
        mapFragmentView.onDestroy();
        super.onDestroy();
    }
}