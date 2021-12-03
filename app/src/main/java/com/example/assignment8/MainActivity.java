package com.example.assignment8;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.Group;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        CountryListAdapter.OnPlaceSelect {

    private Spinner spCountries;
    private final Map<Place, String> placesMap = new HashMap<>();
    private final List<String> countries = new ArrayList<>();
    private final List<Place> places = new ArrayList<>();
    private RecyclerView rvCountries;
    private Group groupDesc;
    private TextView tvDesc;
    private Button btnShowMore;
    private Button btnAddToWishList;
    private HashMap<Place, Integer> wishList = new HashMap<>();
    private EditText etPersonCount;
    private TextView tvRemainingBudget;
    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        if (data != null) {
                            wishList = (HashMap<Place, Integer>)
                                    data.getSerializableExtra("WISH_LIST");
                        }
                    }
                });

        populateData();
        initViews();
        configViews();
    }

    private void populateData() {
        placesMap.put(new Place("Taj",
                        25,
                        R.drawable.taj1,
                        R.drawable.taj2,
                        R.drawable.taj3,
                        R.drawable.taj4,
                        getString(R.string.taj_mahal_desc)),
                "India");
        placesMap.put(new Place("Red fort",
                        35,
                        R.drawable.rf1,
                        R.drawable.rf2,
                        R.drawable.rf3,
                        R.drawable.rf4,
                        getString(R.string.red_fort_desc)),
                "India");
        placesMap.put(new Place("Golden Temple",
                        10,
                        R.drawable.gt1,
                        R.drawable.gt2,
                        R.drawable.gt3,
                        R.drawable.gt4,
                        getString(R.string.golden_temple_desc)),
                "India");

        placesMap.put(new Place("CN Tower",
                        120,
                        R.drawable.cn1,
                        R.drawable.cn2,
                        R.drawable.cn3,
                        R.drawable.cn4,
                        getString(R.string.cn_tower_desc)),
                "Canada");
        placesMap.put(new Place("Niagara falls",
                        23,
                        R.drawable.niagra1,
                        R.drawable.niagra2,
                        R.drawable.niagra3,
                        R.drawable.niagra4,
                        getString(R.string.niagra_falls_desc)),
                "Canada");

        for (String value : placesMap.values()) {
            if (!countries.contains(value)) {
                countries.add(value);
            }
        }
    }

    private void initViews() {
        spCountries = findViewById(R.id.sp_countries);
        rvCountries = findViewById(R.id.rv_countries);
        groupDesc = findViewById(R.id.group_desc);
        tvDesc = findViewById(R.id.tv_desc);
        btnShowMore = findViewById(R.id.btn_show_more);
        btnAddToWishList = findViewById(R.id.btn_add_place);
        etPersonCount = findViewById(R.id.et_person_count);
        tvRemainingBudget = findViewById(R.id.tv_remaining_budget);
    }


    private void configViews() {
        tvRemainingBudget.setText(String.format("Remaining Budget: $%s", getSharedPreferences("PREFERENCE",
                MODE_PRIVATE).
                getString("BUDGET", "0.00")));
        spCountries.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountries.setAdapter(aa);

        rvCountries.setLayoutManager(new LinearLayoutManager(this));
        filterPlaces(0);
        rvCountries.setAdapter(new CountryListAdapter(places, this));
    }

    private void filterPlaces(int i) {
        places.clear();
        for (Map.Entry<Place, String> entry : placesMap.entrySet()) {
            if (entry.getValue().equalsIgnoreCase(countries.get(i))) {
                places.add(entry.getKey());
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        groupDesc.setVisibility(View.GONE);
        filterPlaces(i);
        rvCountries.setAdapter(new CountryListAdapter(places, this));
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //no op
    }

    @Override
    public void onPlaceSelected(Place place) {
        groupDesc.setVisibility(View.VISIBLE);
        tvDesc.setText(place.getDesc());

        btnShowMore.setOnClickListener(view -> {
            Intent detailActivity = new Intent(this, DetailActivity.class);
            detailActivity.putExtra("PLACE_DETAIL", place);
            detailActivity.putExtra("WISH_LIST", wishList);
            launcher.launch(detailActivity);
        });

        btnAddToWishList.setOnClickListener(view -> checkIfAlreadyExist(place));
    }

    private void checkIfAlreadyExist(Place place) {
        boolean isFound = false;
        for (Place p : wishList.keySet()) {
            if (p.getName().equalsIgnoreCase(place.getName())) {
                isFound = true;
                break;
            }
        }

        if (isFound) {
            Toast.makeText(MainActivity.this,
                    "The place is already there in the wishlist",
                    Toast.LENGTH_SHORT).show();
        } else {
            validatePersonCount(place);
        }
    }

    private void validatePersonCount(Place place) {
        if (TextUtils.isEmpty(etPersonCount.getText()) ||
                Integer.parseInt(etPersonCount.getText().toString()) < 1) {
            Toast.makeText(MainActivity.this,
                    "No of persons are invalid!!!",
                    Toast.LENGTH_SHORT).show();
        } else {
            applyBudgetCheck(place);
        }
    }

    private void applyBudgetCheck(Place place) {
        double cost = place.getVisitCharge() *
                Integer.parseInt(etPersonCount.getText().toString());
        double budget = Double.parseDouble(getSharedPreferences("PREFERENCE",
                MODE_PRIVATE).
                getString("BUDGET", "0.00"));
        if (budget - cost < 0) {
            Toast.makeText(MainActivity.this,
                    "The Cost is exceeding the budget",
                    Toast.LENGTH_SHORT).show();
        } else {
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().
                    putString("BUDGET", String.valueOf(budget - cost)).apply();
            Toast.makeText(MainActivity.this,
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_show_wishlist) {
            openWishListActivity();
            return true;
        } else if (itemId == R.id.action_exit) {
            finishAffinity();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tvRemainingBudget.setText(String.format("Remaining Budget: $%s", getSharedPreferences("PREFERENCE",
                MODE_PRIVATE).
                getString("BUDGET", "0.00")));
    }

    private void openWishListActivity() {
        Intent detailActivity = new Intent(this, WishListActivity.class);
        detailActivity.putExtra("WISH_LIST", wishList);
        launcher.launch(detailActivity);
    }
}