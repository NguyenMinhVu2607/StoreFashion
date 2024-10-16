package com.actvn.at170557.storefashion.ui.main;

import com.actvn.at170557.storefashion.ui.main.mycart.CartItem;

import java.util.List;

public interface OnCartItemActionListener {
    void onAddQuantity(CartItem cartItem, int position);
    void onRemoveQuantity(CartItem cartItem, int position);
    void onItemSelected(List<CartItem> selectedItems);
    void onTotalAmountUpdated();
}
