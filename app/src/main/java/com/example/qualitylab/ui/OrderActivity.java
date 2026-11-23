package com.example.qualitylab.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qualitylab.R;
import com.example.qualitylab.logic.OrderCalculator;
import com.example.qualitylab.logic.OrderValidator;
import com.example.qualitylab.util.AppLog;

public class OrderActivity extends AppCompatActivity {

    private static final String TAG = "OrderActivity";

    private EditText etPrice;
    private EditText etQuantity;
    private EditText etDiscount;
    private TextView tvError;
    private TextView tvResult;
    private Button btnDetails;

    private double lastTotal;
    private int lastQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        etPrice = findViewById(R.id.et_price);
        etQuantity = findViewById(R.id.et_quantity);
        etDiscount = findViewById(R.id.et_discount);
        tvError = findViewById(R.id.tv_error);
        tvResult = findViewById(R.id.tv_result);
        btnDetails = findViewById(R.id.btn_details);

        findViewById(R.id.btn_calculate).setOnClickListener(v -> calculate());
        btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra(ResultActivity.EXTRA_TOTAL, lastTotal);
            intent.putExtra(ResultActivity.EXTRA_QUANTITY, lastQuantity);
            startActivity(intent);
        });
    }

    private void calculate() {
        String price = etPrice.getText().toString();
        String quantity = etQuantity.getText().toString();
        String discount = etDiscount.getText().toString();

        OrderValidator.Error error = OrderValidator.validate(price, quantity, discount);
        if (error != OrderValidator.Error.NONE) {
            AppLog.w(TAG, "Ошибка валидации: " + error);
            tvError.setText(messageFor(error));
            tvResult.setText("");
            btnDetails.setVisibility(View.GONE);
            return;
        }

        double priceValue = OrderValidator.parseNumber(price);
        int quantityValue = Integer.parseInt(quantity.trim());
        Double discountValue = OrderValidator.parseNumber(discount);
        double discountPercent = discountValue == null ? 0 : discountValue;

        double total = OrderCalculator.calculateTotal(priceValue, quantityValue, discountPercent);
        AppLog.d(TAG, "price=" + priceValue + ", qty=" + quantityValue
                + ", discount=" + discountPercent + " -> total=" + total);

        lastTotal = total;
        lastQuantity = quantityValue;
        tvError.setText("");
        tvResult.setText(getString(R.string.result_total, OrderCalculator.format(total)));
        btnDetails.setVisibility(View.VISIBLE);
    }

    private int messageFor(OrderValidator.Error error) {
        switch (error) {
            case PRICE_EMPTY:
                return R.string.err_price_empty;
            case PRICE_INVALID:
                return R.string.err_price_invalid;
            case QUANTITY_EMPTY:
                return R.string.err_qty_empty;
            case QUANTITY_INVALID:
                return R.string.err_qty_invalid;
            default:
                return R.string.err_discount_invalid;
        }
    }
}
