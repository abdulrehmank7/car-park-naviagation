package com.arkapp.carparknaviagation.utility.viewModelFactory;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.arkapp.carparknaviagation.data.repository.PrefRepository;
import com.arkapp.carparknaviagation.viewModels.SplashViewModel;

/**
 * Created by Abdul Rehman on 12-10-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class SplashViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private PrefRepository prefRepository;
    private SharedPreferences preferences;

    public SplashViewModelFactory(PrefRepository prefRepository, SharedPreferences preferences) {
        this.prefRepository = prefRepository;
        this.preferences = preferences;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SplashViewModel(prefRepository, preferences);
    }
}