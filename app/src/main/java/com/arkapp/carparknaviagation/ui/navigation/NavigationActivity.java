package com.arkapp.carparknaviagation.ui.navigation;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;

import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.databinding.ActivityNavigationBinding;
import com.arkapp.carparknaviagation.utility.maps.navigation.NavigationUtils;

public class NavigationActivity extends AppCompatActivity {

    private NavigationUtils mapFragmentView;
    private PrefRepository prefRepository;
    private ActivityNavigationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        prefRepository = new PrefRepository(this);
        setupMapFragmentView();
    }

    private void setupMapFragmentView() {
        mapFragmentView = new NavigationUtils(this, prefRepository, binding);
    }

    @Override
    public void onDestroy() {
        mapFragmentView.onDestroy();
        super.onDestroy();
    }
}