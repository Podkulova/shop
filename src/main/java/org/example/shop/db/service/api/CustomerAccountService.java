package org.example.shop.db.service.api;

import org.example.shop.domain.CustomerAccount;

public interface CustomerAccountService {
    void addCustomerAccount(CustomerAccount customerAccount);

    Double getMoney(int customerId);

    void setMoney(int customerId, double money);

}
