package org.example.shop.domain;


import java.util.Objects;

public class CustomerAccount {
    private int customerId;

    private double money;

    // Konstruktor
    public CustomerAccount() {
    }

    public CustomerAccount(int customerId, double money) {
        this.customerId = customerId;
        this.money = money;
    }

    // Getter a Setter
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    // equals a hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerAccount that = (CustomerAccount) o;
        return customerId == that.customerId && Double.compare(money, that.money) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, money);
    }
}
