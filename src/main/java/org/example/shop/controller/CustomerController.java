package org.example.shop.controller;

import org.example.shop.db.service.api.CustomerAccountService;
import org.example.shop.db.service.api.CustomerService;
import org.example.shop.domain.Customer;
import org.example.shop.domain.CustomerAccount;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("customer")
public class CustomerController {
    private final CustomerService customerService;
    private final CustomerAccountService customerAccountService;

    public CustomerController(CustomerService customerService, CustomerAccountService customerAccountService) {
        this.customerService = customerService;
        this.customerAccountService = customerAccountService;
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Customer customer) {
        Integer id = customerService.add(customer);
        if (id != null) {
            return new ResponseEntity<>(id, HttpStatus.CREATED); // 201
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500 chyba v naší app
    }


    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") int id) {
        Customer customer = customerService.get(id);
        if (customer == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND); // 404
        }
        return new ResponseEntity<>(customer, HttpStatus.OK);

    }

    @GetMapping()
    public ResponseEntity<List<Customer>> getAll() {
        List<Customer> customerList = customerService.getCustomers();
        return new ResponseEntity<>(customerList, HttpStatus.OK); // 200
    }

    @PostMapping("/account")
    public ResponseEntity addMoney(@RequestBody CustomerAccount customerAccount) {
        customerAccountService.addCustomerAccount(customerAccount);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }
}
