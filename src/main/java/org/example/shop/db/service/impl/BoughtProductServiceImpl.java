package org.example.shop.db.service.impl;

import org.example.shop.db.repository.BoughtProductRepository;
import org.example.shop.db.service.api.BoughtProductService;
import org.example.shop.domain.BoughtProduct;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BoughtProductServiceImpl implements BoughtProductService {

    private final BoughtProductRepository boughtProductRepository;

    public BoughtProductServiceImpl(BoughtProductRepository boughtProductRepository) {
        this.boughtProductRepository = boughtProductRepository;
    }

    @Override
    public void add(BoughtProduct boughtProduct) {
        boughtProductRepository.add(boughtProduct);
    }

    @Override
    public List<BoughtProduct> getAllCustomerId(int customerId) {
        return boughtProductRepository.getAllByCustomerId(customerId);
    }
}
