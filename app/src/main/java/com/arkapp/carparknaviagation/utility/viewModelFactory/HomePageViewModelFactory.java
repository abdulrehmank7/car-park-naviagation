package com.arkapp.carparknaviagation.utility.viewModelFactory;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.arkapp.carparknaviagation.data.repository.MapRepository;
import com.arkapp.carparknaviagation.viewModels.HomePageViewModel;

/**
 * Created by Abdul Rehman on 27-09-2020.
 * Contact email - abdulrehman0796@gmail.com
 */
public class HomePageViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private MapRepository repository;

    public HomePageViewModelFactory(MapRepository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new HomePageViewModel(repository);
    }
}
