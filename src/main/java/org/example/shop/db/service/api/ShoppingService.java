package org.example.shop.db.service.api;

import org.example.shop.db.service.api.request.BuyProductRequest;
import org.example.shop.db.service.api.response.BuyProductResponse;


public interface ShoppingService {

    BuyProductResponse buyProduct(BuyProductRequest request);
}
