package com.example.assignment8;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private EditText etBudget;
    private double budget;
    private Spinner spCountries;
    private final Map<Place, String> placesMap = new HashMap<>();
    private final List<String> countries = new ArrayList<>();
    private final List<Place> places = new ArrayList<>();
    private RecyclerView rvCountries;
    private Group groupDesc;
    private TextView tvDesc;
    private Button btnShowMore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        populateData();
        initViews();
        configViews();
    }

    private void populateData() {
        placesMap.put(new Place("Taj",
                        0,
                        R.drawable.taj,
                        getString(R.string.lorem_ipsum)),
                "India");
        placesMap.put(new Place("Red fort",
                        0,
                        R.drawable.redfort,
                        getString(R.string.lorem_ipsum)),
                "India");
        placesMap.put(new Place("Golden Temple",
                        0,
                        R.drawable.gt,
                        getString(R.string.lorem_ipsum)),
                "India");

        placesMap.put(new Place("CN Tower",
                        0,
                        R.drawable.cn,
                        getString(R.string.lorem_ipsum)),
                "Canada");
        placesMap.put(new Place("Niagra falls",
                        0,
                        R.drawable.niagra,
                        getString(R.string.lorem_ipsum)),
                "Canada");

        for (String value : placesMap.values()) {
            if (!countries.contains(value)) {
                countries.add(value);
            }
        }
    }


    private void initViews() {
        etBudget = findViewById(R.id.et_budget);
        spCountries = findViewById(R.id.sp_countries);
        rvCountries = findViewById(R.id.rv_countries);
        groupDesc = findViewById(R.id.group_desc);
        tvDesc = findViewById(R.id.tv_desc);
        btnShowMore = findViewById(R.id.btn_show_more);
    }


    private void configViews() {

        spCountries.setOnItemSelectedListener(this);

        ArrayAdapter<String> aa = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, countries);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCountries.setAdapter(aa);


        etBudget.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //no op
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                budget = Double.parseDouble(etBudget.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //no op
            }
        });

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

//        btnShowMore.setOnClickListener(view -> Toast.makeText(MainActivity.this, "lalalalal", Toast.LENGTH_SHORT).show());
    }
}