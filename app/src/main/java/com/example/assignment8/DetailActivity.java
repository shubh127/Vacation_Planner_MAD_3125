package com.example.assignment8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.HashMap;

public class DetailActivity extends AppCompatActivity {
    private Place place;
    private ImageView ivImgLarge;
    private ImageView ivImg1;
    private ImageView ivImg2;
    private ImageView ivImg3;
    private TextView tvDesc;
    private Button btnAddToWishList;
    private HashMap<Place, Integer> wishList = new HashMap<>();
    private EditText etPersonCount;
    private TextView tvRemainingBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getDataFromIntent();
        initViews();
        configViews();
    }

    private void getDataFromIntent() {
        place = getIntent().getParcelableExtra("PLACE_DETAIL");
        wishList = (HashMap<Place, Integer>) getIntent().getSerializableExtra("WISH_LIST");
    }

    private void initViews() {
        ivImgLarge = findViewById(R.id.iv_large);
        ivImg1 = findViewById(R.id.iv_first);
        ivImg2 = findViewById(R.id.iv_second);
        ivImg3 = findViewById(R.id.iv_third);
        tvDesc = findViewById(R.id.tv_desc);
        btnAddToWishList = findViewById(R.id.btn_add_place);
        etPersonCount = findViewById(R.id.et_person_count);
        tvRemainingBudget = findViewById(R.id.tv_remaining_budget);
    }

    private void configViews() {
        tvRemainingBudget.setText(String.format("Remaining Budget: $%s", getSharedPreferences("PREFERENCE",
                MODE_PRIVATE).
                getString("BUDGET", "0.00")));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(place.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        ivImgLarge.setImageDrawable(ContextCompat.getDrawable(this, place.getImgId1()));
        ivImg1.setImageDrawable(ContextCompat.getDrawable(this, place.getImgId2()));
        ivImg2.setImageDrawable(ContextCompat.getDrawable(this, place.getImgId3()));
        ivImg3.setImageDrawable(ContextCompat.getDrawable(this, place.getImgId4()));

        ivImgLarge.setTag(place.getImgId1());
        ivImg1.setTag(place.getImgId2());
        ivImg2.setTag(place.getImgId3());
        ivImg3.setTag(place.getImgId4());

        ivImg1.setOnClickListener(view -> swapImages(ivImg1));

        ivImg2.setOnClickListener(view -> swapImages(ivImg2));

        ivImg3.setOnClickListener(view -> swapImages(ivImg3));

        tvDesc.setText(place.getDesc());

        btnAddToWishList.setOnClickListener(view -> checkIfAlreadyExist());
    }

    private void swapImages(ImageView imageView) {
        ivImgLarge.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,
                (int) imageView.getTag()
        ));
        imageView.setImageDrawable(ContextCompat.getDrawable(DetailActivity.this,
                (int) ivImgLarge.getTag()));

        int temp = (int) ivImgLarge.getTag();
        ivImgLarge.setTag(imageView.getTag());
        imageView.setTag(temp);
    }

    private void checkIfAlreadyExist() {
        boolean isFound = false;
        for (Place p : wishList.keySet()) {
            if (p.getName().equalsIgnoreCase(place.getName())) {
                isFound = true;
                break;
            }
        }

        if (isFound) {
            Toast.makeText(this,
                    "The place is already there in the wishlist",
                    Toast.LENGTH_SHORT).show();
        } else {
            validatePersonCount();
        }
    }

    private void validatePersonCount() {
        if (TextUtils.isEmpty(etPersonCount.getText()) ||
                Integer.parseInt(etPersonCount.getText().toString()) < 1) {
            Toast.makeText(this,
                    "No of persons are invalid!!!",
                    Toast.LENGTH_SHORT).show();
        } else {
            applyBudgetCheck();
        }
    }

    private void applyBudgetCheck() {
        double cost = place.getVisitCharge() *
                Integer.parseInt(etPersonCount.getText().toString());
        double budget = Double.parseDouble(getSharedPreferences("PREFERENCE",
                MODE_PRIVATE).
                getString("BUDGET", "0.00"));
        if (budget - cost < 0) {
            Toast.makeText(this,
                    "The Cost is exceeding the budget",
                    Toast.LENGTH_SHORT).show();
        } else {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().
                    putString("BUDGET", String.valueOf(budget - cost)).apply();
            Toast.makeText(this,
                    "Destination added to wishlist",
                    Toast.LENGTH_SHORT).show();
            wishList.put(place, Integer.parseInt(etPersonCount.getText().toString()));
            tvRemainingBudget.setText(String.format("Remaining Budget: $%s",
                    getSharedPreferences("PREFERENCE",
                            MODE_PRIVATE).
                            getString("BUDGET", "0.00")));

            etPersonCount.getText().clear();
        }
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
}