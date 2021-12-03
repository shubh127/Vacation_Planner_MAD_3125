package com.example.assignment8;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;

public class WishListActivity extends AppCompatActivity implements WishListAdapter.OnItemRemoved {

    private HashMap<Place, Integer> wishList = new HashMap<>();
    private RecyclerView rvWishList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);

        getDataFromIntent();
        initViews();
        configViews();
    }

    private void getDataFromIntent() {
        wishList = (HashMap<Place, Integer>) getIntent().getSerializableExtra("WISH_LIST");
    }

    private void initViews() {
        rvWishList = findViewById(R.id.rv_wish_list);
    }

    private void configViews() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("WishList");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        rvWishList.setLayoutManager(new LinearLayoutManager(this));
        rvWishList.setAdapter(new WishListAdapter(wishList, this));

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("WISH_LIST", wishList);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        return true;
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("WISH_LIST", wishList);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemRemoved(Place place, double totalCost) {
        wishList.remove(place);
        double budget = Double.parseDouble(getSharedPreferences("PREFERENCE",
                MODE_PRIVATE).
                getString("BUDGET", "0.00"));

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().
                putString("BUDGET", String.valueOf(budget + totalCost)).apply();
        Toast.makeText(this,
                "Destination removed from wishlist",
                Toast.LENGTH_SHORT).show();
        rvWishList.getAdapter().notifyDataSetChanged();
    }
}