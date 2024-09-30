package com.actvn.at170557.storefashion.utils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {

    // Dữ liệu được chia sẻ (ví dụ: UserID)
    private final MutableLiveData<String> userId = new MutableLiveData<>();

    // Phương thức để cập nhật dữ liệu
    public void setUserId(String id) {
        userId.setValue(id);
    }

    // Phương thức để lấy LiveData (được Fragment quan sát)
    public LiveData<String> getUserId() {
        return userId;
    }
}
