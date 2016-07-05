package com.example.bluesky.app.event;

import com.example.bluesky.app.bean.ShoppingCartItem;

import java.util.List;

/**
 * Created by bluesky on 16/6/19.
 */
public class NotifyShoppingCartDeleteEvent {
    List<ShoppingCartItem> shoppingCartItems;

    public NotifyShoppingCartDeleteEvent(List<ShoppingCartItem> shoppingCartItems) {
        this.shoppingCartItems = shoppingCartItems;
    }

    public List<ShoppingCartItem> getShoppingCartItems() {
        return shoppingCartItems;
    }
}
