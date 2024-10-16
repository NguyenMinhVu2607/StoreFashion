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

        View view = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_filter, null);
        setContentView(view);
        applyButton = view.findViewById(R.id.btn_apply);

        applyButton.setOnClickListener(v -> {
            int selectedCategoryId = categoryGroup.getCheckedRadioButtonId();
            int selectedPriceRange = priceRangeSeekBar.getProgress();

            applyFilter(selectedCategoryId, selectedPriceRange);

            dismiss();
        });
    }

    private void applyFilter(int categoryId, int priceRange) {

    }
}
