package com.arkapp.carparknaviagation.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.arkapp.carparknaviagation.R;

import static androidx.navigation.fragment.NavHostFragment.findNavController;
import static com.arkapp.carparknaviagation.utility.Constants.SPLASH_TIME;

public class SplashFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadSplash();
    }

    private void loadSplash() {
        Runnable runnable = () -> findNavController(this).navigate(R.id.action_splashFragment_to_homeFragment);
        new Handler().postDelayed(runnable, SPLASH_TIME);
    }

}