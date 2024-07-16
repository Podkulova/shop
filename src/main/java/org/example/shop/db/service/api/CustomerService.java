package org.example.shop.db.service.api;

import org.example.shop.domain.Customer;
import org.springframework.lang.Nullable;

import java.util.List;


public interface CustomerService {

    List<Customer> getCustomers();

    @Nullable
    Customer get(int id);

    @Nullable
    Integer add(Customer customer); // bude vracet generované id
}
