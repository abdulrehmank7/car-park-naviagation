package com.arkapp.carparknaviagation.ui.carParkList;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.carparknaviagation.databinding.RvCarParkBinding;

import org.jetbrains.annotations.NotNull;


public final class CarParkListViewHolder extends ViewHolder {
    @NotNull
    private final RvCarParkBinding viewBinding;

    public CarParkListViewHolder(@NotNull RvCarParkBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    @NotNull
    public final RvCarParkBinding getViewBinding() {
        return this.viewBinding;
    }
}
