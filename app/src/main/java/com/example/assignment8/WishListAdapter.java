package com.example.assignment8;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Locale;

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.ViewHolder> {

    private final HashMap<Place, Integer> wishList;
    private final OnItemRemoved listener;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvName;
        private final TextView tvPersonCount;
        private final TextView tvCost;
        private final TextView tvRemove;

        public ViewHolder(View view) {
            super(view);
            tvName = view.findViewById(R.id.tv_name);
            tvPersonCount = view.findViewById(R.id.tv_person_count);
            tvCost = view.findViewById(R.id.tv_visit_cost);
            tvRemove = view.findViewById(R.id.tv_remove);
        }
    }

    public WishListAdapter(HashMap<Place, Integer> wishList, OnItemRemoved listener) {
        this.wishList = wishList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public WishListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_wish_list, viewGroup, false);

        return new WishListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Place place = (Place) wishList.keySet().toArray()[position];
        Integer costPerPerson = wishList.get(place);
        double totalCost = place.getVisitCharge() * costPerPerson;
        viewHolder.tvName.setText(place.getName());
        viewHolder.tvPersonCount.setText(String.format(Locale.getDefault(),
                "Number of persons is:%d", costPerPerson));
        viewHolder.tvCost.setText(String.format("Total cost of the trip is: $%s",
                totalCost));

        viewHolder.tvRemove.setOnClickListener(view -> listener.onItemRemoved(place,totalCost));
    }

    @Override
    public int getItemCount() {
        return wishList.size();
    }

    interface OnItemRemoved {
        void onItemRemoved(Place place, double totalCost);
    }
}
