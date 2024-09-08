package com.actvn.at170557.storefashion.baseapplication;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

public abstract class BaseFragment<VB extends ViewBinding> extends Fragment {
    protected VB mBinding;

    private ProgressDialog mProgressDialog;

    public abstract int getLayoutFragment();

    protected abstract VB inflateBinding(LayoutInflater inflater, ViewGroup container);

    public void initViews() {
    }

    public void observerData() {
    }

    public void onClickViews() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = inflateBinding(inflater, container);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        onClickViews();
        observerData();
        mProgressDialog = new ProgressDialog(requireActivity());
        mProgressDialog.setCancelable(false);
    }

    public boolean isProgressShowing() {
        return (mProgressDialog != null && mProgressDialog.isShowing());
    }


    public void showLoading(String message) {
        if (isProgressShowing()) {
            return;
        }
        hideLoading();
    }


    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }



}