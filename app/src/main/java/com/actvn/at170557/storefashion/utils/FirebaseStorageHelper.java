package com.actvn.at170557.storefashion.utils;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class FirebaseStorageHelper {

    // Phương thức này sẽ lấy URI của ảnh từ Firebase Storage và gọi một callback
    public static void getImageUri(String imagePath, final OnImageUriCallback callback) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        StorageReference imageRef = storageRef.child(imagePath);

        imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Gọi callback khi URI được lấy thành công
                callback.onImageUriReceived(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Log lỗi nếu xảy ra
                Log.e("FirebaseStorage", "Error downloading image", e);
                // Gọi callback với URI null khi có lỗi
                callback.onImageUriReceived(null);
            }
        });
    }

    // Định nghĩa một interface để callback khi URI của ảnh được nhận
    public interface OnImageUriCallback {
        void onImageUriReceived(Uri uri);
    }
}
