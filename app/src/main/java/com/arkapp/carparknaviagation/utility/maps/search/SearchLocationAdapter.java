package com.arkapp.carparknaviagation.utility.maps.search;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.PlaceAutoComplete;
import com.arkapp.carparknaviagation.data.models.SearchedHistory;
import com.arkapp.carparknaviagation.databinding.RvSearchItemBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;

import static com.arkapp.carparknaviagation.utility.Constants.GOOGLE_KEY;
import static com.arkapp.carparknaviagation.utility.ViewUtils.hide;
import static com.arkapp.carparknaviagation.utility.ViewUtils.printLog;
import static com.arkapp.carparknaviagation.utility.ViewUtils.show;

public class SearchLocationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final CharacterStyle STYLE_BOLD = new StyleSpan(Typeface.BOLD);
    private static ClickListener clickListener;
    public PlacesClient placesClient;
    private Context context;
    private AutocompleteSessionToken token;
    public ArrayList<PlaceAutoComplete> resultList;
    public ArrayList<SearchedHistory> historyList = new ArrayList<>();

    public boolean showHistory = false;

    public RecyclerView searchRecyclerView;

    public SearchLocationAdapter(ArrayList<PlaceAutoComplete> searchData, Context context) {
        resultList = searchData;
        this.context = context;

        initPlacesClient();
    }

    @Override
    public int getItemCount() {
        if (showHistory)
            return historyList != null ? historyList.size() : 0;
        return resultList != null ? resultList.size() : 0;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return (RecyclerView.ViewHolder)
                (new SearchLocationViewHolder((RvSearchItemBinding) DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.rv_search_item,
                        parent,
                        false)));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RvSearchItemBinding binding = ((SearchLocationViewHolder) holder).getViewBinding();
        if (showHistory) {
            final SearchedHistory placeData = historyList.get(position);
            binding.recentIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_history));
            binding.parent.setOnClickListener(view -> clickListener.onItemClick(position, binding.parent));
            binding.tvAddress.setText(placeData.area);
            binding.tvTitle.setText(placeData.description);
        } else {
            final PlaceAutoComplete placeData = resultList.get(position);
            binding.recentIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_marker2));
            binding.parent.setOnClickListener(view -> clickListener.onItemClick(position, binding.parent));
            binding.tvAddress.setText(placeData.area);
            binding.tvTitle.setText(placeData.description);
        }
    }

    public PlaceAutoComplete getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public void clear() {
        int size = resultList.size();
        resultList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void clearHistory() {
        int size = historyList.size();
        historyList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void selectFirstAddress() {
        if (resultList.size() > 0) {
            clickListener.onItemClick(0, null);
        }
    }

    private void initPlacesClient() {

        // Initialize Places.
        Places.initialize(context.getApplicationContext(), GOOGLE_KEY);

        // Create a new Places client instance.
        placesClient = Places.createClient(context);

        token = AutocompleteSessionToken.newInstance();
    }

    public void getSearchResult(String searchText) {

        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                //.setLocationBias(BOUNDS_HYDERABAD_BIAS)
                .setCountry("sgp")
                .setSessionToken(token)
                .setQuery(searchText)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            if (!response.getAutocompletePredictions().isEmpty()) {
                show(searchRecyclerView);
                resultList.clear();
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    resultList.add(
                            new PlaceAutoComplete(prediction.getPlaceId(),
                                                  prediction.getPrimaryText(STYLE_BOLD),
                                                  prediction.getSecondaryText(STYLE_BOLD)));
                }
            }
            if (!searchText.equals(""))
                notifyDataSetChanged();
            else
                hide(searchRecyclerView);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                printLog("Place not found: " + apiException.getStatusCode());
            }
        });
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        searchRecyclerView = recyclerView;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SearchLocationAdapter.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
