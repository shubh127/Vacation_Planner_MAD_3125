package com.example.assignment8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.ViewHolder> {

    private final List<Place> places;
    private OnPlaceSelect listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cvParent;
        private final ImageView ivPlaceImg;
        private final TextView tvPlaceName;
        private final TextView tvPlaceCost;

        public ViewHolder(View view) {
            super(view);
            cvParent = view.findViewById(R.id.cv_parent);
            ivPlaceImg = view.findViewById(R.id.iv_place_img);
            tvPlaceName = view.findViewById(R.id.tv_place_name);
            tvPlaceCost = view.findViewById(R.id.tv_place_cost);
        }
    }

    public CountryListAdapter(List<Place> places, OnPlaceSelect listener) {
        this.places = places;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.place_item_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Place place = places.get(position);
        viewHolder.ivPlaceImg.setImageDrawable(ContextCompat.getDrawable(
                viewHolder.ivPlaceImg.getContext(),
                place.getImgId()
        ));
        viewHolder.tvPlaceName.setText(place.getName());
        viewHolder.tvPlaceCost.setText(String.valueOf(place.getVisitCharge()));
        viewHolder.cvParent.setOnClickListener(view -> listener.onPlaceSelected(place));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    interface OnPlaceSelect {
        void onPlaceSelected(Place place);
    }
}
