package com.arkapp.carparknaviagation.utility.maps.search;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.arkapp.carparknaviagation.databinding.RvSearchItemBinding;

import org.jetbrains.annotations.NotNull;


public final class SearchLocationViewHolder extends ViewHolder {
    @NotNull
    private final RvSearchItemBinding viewBinding;

    public SearchLocationViewHolder(@NotNull RvSearchItemBinding viewBinding) {
        super(viewBinding.getRoot());
        this.viewBinding = viewBinding;
    }

    @NotNull
    public final RvSearchItemBinding getViewBinding() {
        return this.viewBinding;
    }
}
