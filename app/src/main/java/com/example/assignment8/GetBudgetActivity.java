package com.example.assignment8;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class GetBudgetActivity extends AppCompatActivity {
    private EditText etBudget;
    private Button btnConfirmBudget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_budget);

        initViews();
        configViews();
    }

    private void initViews() {
        etBudget = findViewById(R.id.et_budget);
        btnConfirmBudget = findViewById(R.id.btn_confirm_budget);
    }

    private void configViews() {
        btnConfirmBudget.setOnClickListener(view -> {
            if (TextUtils.isEmpty(etBudget.getText()) ||
                    Double.parseDouble(etBudget.getText().toString()) < 1) {
                Toast.makeText(GetBudgetActivity.this,
                        "Invalid Budget Amount!!!",
                        Toast.LENGTH_SHORT).show();
            } else {
                getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().
                        putString("BUDGET", etBudget.getText().toString()).apply();
                startActivity(new Intent(GetBudgetActivity.this, MainActivity.class));
                finish();
            }
        });
    }
}