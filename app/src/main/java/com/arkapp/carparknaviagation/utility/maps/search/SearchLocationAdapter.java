package com.arkapp.carparknaviagation.utility.maps.search;

import android.content.Context;
import android.graphics.Typeface;
import android.text.style.CharacterStyle;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.arkapp.carparknaviagation.R;
import com.arkapp.carparknaviagation.data.models.PlaceAutoComplete;
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
    private ArrayList<PlaceAutoComplete> mResultList;
    private RecyclerView searchRecyclerView;

    public SearchLocationAdapter(ArrayList<PlaceAutoComplete> searchData, Context context) {
        mResultList = searchData;
        this.context = context;

        initPlacesClient();
    }

    @Override
    public int getItemCount() {
        if (mResultList != null)
            printLog("get item count" + mResultList.size());
        return mResultList != null ? mResultList.size() : 0;
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
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        searchRecyclerView = recyclerView;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        RvSearchItemBinding binding = ((SearchLocationViewHolder) holder).getViewBinding();
        final PlaceAutoComplete placeData = (PlaceAutoComplete) this.mResultList.get(position);
        binding.parent.setOnClickListener(view -> clickListener.onItemClick(position, binding.parent));
        binding.tvAddress.setText(placeData.area);
        binding.tvTitle.setText(placeData.description);
    }

    public PlaceAutoComplete getItem(int position) {
        return mResultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) position;
    }

    public void clear() {
        int size = mResultList.size();
        mResultList.clear();
        notifyItemRangeRemoved(0, size);
    }

    public void selectFirstAddress() {
        if (mResultList.size() > 0) {
            clickListener.onItemClick(0, null);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        SearchLocationAdapter.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
                mResultList.clear();
                for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                    mResultList.add(
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
}
