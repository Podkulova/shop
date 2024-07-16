package org.example.shop.db.service.api;

import org.example.shop.domain.BoughtProduct;

import java.util.List;

public interface BoughtProductService {
    void add(BoughtProduct boughtProduct);

    List<BoughtProduct> getAllCustomerId(int customerId);
}
