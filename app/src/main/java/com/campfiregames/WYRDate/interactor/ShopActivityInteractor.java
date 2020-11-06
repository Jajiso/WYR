package com.campfiregames.WYRDate.interactor;

import com.android.billingclient.api.Purchase;

import java.util.List;

public class ShopActivityInteractor {

    public boolean isProductOwned(String sku, List<Purchase> purchases) {
        boolean isProductOwned = false;
        for (Purchase purchasedItem: purchases) {
            if (purchasedItem.getSku().equals(sku)){
                isProductOwned = true;
                break;
            }
        }
        return isProductOwned;
    }
}
