package com.arkapp.carparknaviagation.ui.carParkList;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.myTransportCarPark.MyTransportCarParkAvailability;
import com.arkapp.carparknaviagation.data.models.uraCarPark.UraCarParkAvailability;
import com.arkapp.carparknaviagation.databinding.RvCarParkBinding;
import com.arkapp.carparknaviagation.viewModels.HomePageViewModel;

import java.util.Locale;

import static com.arkapp.carparknaviagation.ui.carParkList.Utils.getMyTransportChargeString;
import static com.arkapp.carparknaviagation.ui.carParkList.Utils.getUraChargeString;
import static com.arkapp.carparknaviagation.ui.home.Utils.isUraCarPark;
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
        if (isUraCarPark(viewModel.allFilteredCarPark.get(position))) {
            UraCarParkAvailability availability = (UraCarParkAvailability) viewModel.allFilteredCarPark.get(position);
            binding.tvName.setText(availability.getCharges().getPpName());
            binding.tvLots.setText(String.format(Locale.ENGLISH, "%s", availability.getLotsAvailable()));
            binding.tvRates.setText(getUraChargeString(availability.getCharges()));
            binding.tvEta.setText(availability.getEtaDistanceFromOrigin().getDuration().getText());


        } else {
            MyTransportCarParkAvailability availability = (MyTransportCarParkAvailability) viewModel.allFilteredCarPark.get(position);
            binding.tvName.setText(availability.getDevelopment());
            binding.tvLots.setText(String.format(Locale.ENGLISH, "%s", availability.getAvailableLots()));
            binding.tvRates.setText(getMyTransportChargeString(availability));
            binding.tvEta.setText(availability.getEtaDistanceFromOrigin().getDuration().getText());
        }

        binding.btView.setOnClickListener(view -> {
            if (isDoubleClicked(1500)) return;
            if (viewModel.currentSelectedCarParkMarker != null)
                viewModel.currentSelectedCarParkMarker.remove();

            viewModel.currentSelectedCarParkNo = position;
            viewModel.carParkList.dismiss();
            viewModel.listener.setCarParking();
        });

        binding.btNavigate.setOnClickListener(view -> {
            if (isDoubleClicked(1500)) return;

            viewModel.startNavigation = true;
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
