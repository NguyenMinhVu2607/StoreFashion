package com.actvn.at170557.storefashion.ui.main.home;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.actvn.at170557.storefashion.R;

public class BottomFilter extends BottomSheetDialog {

    private Context context;
    private RadioGroup categoryGroup;
    private SeekBar priceRangeSeekBar;
    private Button applyButton;

    public BottomFilter(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the bottom sheet layout
        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_filter, null);
        setContentView(view);

        // Initialize views from layout
//        categoryGroup = view.findViewById(R.id.category_group); // Replace with the correct id
//        priceRangeSeekBar = view.findViewById(R.id.seekbar_price_range); // Replace with the correct id
        applyButton = view.findViewById(R.id.btn_apply);

        // Handle button click
        applyButton.setOnClickListener(v -> {
            // Implement your filter logic here
            // For example, get selected category and price range
            int selectedCategoryId = categoryGroup.getCheckedRadioButtonId();
            int selectedPriceRange = priceRangeSeekBar.getProgress();

            // Apply the filter
            applyFilter(selectedCategoryId, selectedPriceRange);

            // Close the BottomSheetDialog
            dismiss();
        });
    }

    private void applyFilter(int categoryId, int priceRange) {
        // Implement your filter logic
        // Example: Show results based on selected category and price range
    }
}
