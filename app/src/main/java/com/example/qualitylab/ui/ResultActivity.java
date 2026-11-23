package com.example.qualitylab.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qualitylab.R;
import com.example.qualitylab.logic.OrderCalculator;

public class ResultActivity extends AppCompatActivity {

    public static final String EXTRA_TOTAL = "extra_total";
    public static final String EXTRA_QUANTITY = "extra_quantity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        double total = getIntent().getDoubleExtra(EXTRA_TOTAL, 0);
        int quantity = getIntent().getIntExtra(EXTRA_QUANTITY, 0);

        TextView tvTotal = findViewById(R.id.tv_result_total);
        TextView tvDetails = findViewById(R.id.tv_result_details);
        tvTotal.setText(getString(R.string.result_total, OrderCalculator.format(total)));
        tvDetails.setText(getString(R.string.result_details, quantity));

        findViewById(R.id.btn_back).setOnClickListener(v -> finish());
    }
}
