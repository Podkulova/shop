package org.example.shop.db.service.api;

import org.example.shop.db.service.api.request.UpdateProductRequest;
import org.example.shop.domain.Product;
import org.springframework.lang.Nullable;

import java.util.List;

public interface ProductService {
    List<Product> getProduct();

    @Nullable
    Product get(int id);

    @Nullable
    Integer add(Product product); // vrací vygenerované id

    void delete(int id);

    void update(int id, UpdateProductRequest request);

    void updateAvailableInternal(int id, int newAvailable);
}
