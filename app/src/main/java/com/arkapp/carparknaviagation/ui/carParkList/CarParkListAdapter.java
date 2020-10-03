package com.arkapp.carparknaviagation.ui.carParkList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.carPark.CarParkAvailability;
import com.arkapp.carparknaviagation.databinding.RvCarParkBinding;
import com.arkapp.carparknaviagation.viewModels.HomePageViewModel;

import java.util.Locale;

import static com.arkapp.carparknaviagation.ui.carParkList.Utils.getChargeString;
import static com.arkapp.carparknaviagation.utility.ViewUtils.isDoubleClicked;

public class CarParkListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private HomePageViewModel viewModel;

    public CarParkListAdapter(HomePageViewModel searchData) {
        viewModel = searchData;
    }

    @Override
    public int getItemCount() {
        return viewModel != null ? viewModel.allFilteredCarPark.size() : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CarParkListViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.rv_car_park,
                parent,
                false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RvCarParkBinding binding = ((CarParkListViewHolder) holder).getViewBinding();
        CarParkAvailability availability = viewModel.allFilteredCarPark.get(position);
        binding.tvName.setText(availability.getCharges().getPpName());
        binding.tvLots.setText(String.format(Locale.ENGLISH, "%s", availability.getLotsAvailable()));
        binding.tvRates.setText(getChargeString(availability.getCharges()));
        binding.tvEta.setText(availability.getEtaDistanceFromOrigin().getDuration().getText());

        binding.btView.setOnClickListener(view -> {
            if (isDoubleClicked(1500)) return;
            if (viewModel.currentSelectedCarParkMarker != null)
                viewModel.currentSelectedCarParkMarker.remove();

            viewModel.currentSelectedCarParkNo = position;
            viewModel.carParkList.dismiss();
            viewModel.listener.setCarParking();
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
