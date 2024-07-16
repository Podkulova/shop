package org.example.shop.db.service.api;

import org.example.shop.domain.Merchant;
import org.springframework.lang.Nullable;

import java.util.List;

public interface MerchantService {

    List<Merchant> getMerchants();

    @Nullable
    Merchant get(int id);

    @Nullable
    Integer add(Merchant merchant); // vrací vygenerované id
}
