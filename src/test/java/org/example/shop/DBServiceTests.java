package org.example.shop;

import org.example.shop.db.service.api.CustomerService;
import org.example.shop.db.service.api.MerchantService;
import org.example.shop.db.service.api.ProductService;
import org.example.shop.db.service.api.request.UpdateProductRequest;
import org.example.shop.domain.Customer;
import org.example.shop.domain.Merchant;
import org.example.shop.domain.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class DBServiceTests {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private ProductService productService;

    private Merchant merchant;

    @Before
    public void createMerchant() {
        if (merchant == null) {
            merchant = new Merchant("Patrik", "patrik@patrik.com", "Nejaka 85");
            Integer id = merchantService.add(merchant);

            assert id != null;
            merchant.setId(id);
        }
    }

    @Test
    public void customer() {
        Customer customer = new Customer("Aleš", "Nekoukal", "ales@nekoukal.cz", "Nekde 25", 20, "987654321");
        Integer id = customerService.add(customer);

        assert id != null;
        customer.setId(id); // bez toho by se id nervnaly

        Customer fromDb = customerService.get(id);
        Assert.assertEquals(customer, fromDb);

        List<Customer> customers = customerService.getCustomers();
        Assert.assertEquals(1, customers.size()); // měl by existovat právě jeden záznam
        Assert.assertEquals(customer, customers.get(0));
    }

    @Test
    public void merchant() {
        // už máme vytvořené výše

        Merchant formDb = merchantService.get(merchant.getId());
        Assert.assertEquals(merchant, formDb);

        List<Merchant> merchants = merchantService.getMerchants();
        Assert.assertEquals(1, merchants.size());
        Assert.assertEquals(merchant, merchants.get(0));
    }


    @Test
    public void product() {
        Product product = new Product(merchant.getId(), "name", "description", 5, 1);

        Integer id = productService.add(product);

        assert id != null;
        product.setId(id); // bez toho by se id nerovnaly

        Product fromDb = productService.get(id);
        Assert.assertEquals(product, fromDb);

        List<Product> products = productService.getProduct();
        Assert.assertEquals(1, products.size()); // měl by existovat právě jeden záznam
        Assert.assertEquals(product, products.get(0));

        // testování update
        product.setAvailable(10);
        UpdateProductRequest productRequest = new UpdateProductRequest(product.getName(), product.getDescription(), product.getPrice(), product.getAvailable());

        productService.update(id, productRequest);

        Product fromDBAfterUpdate = productService.get(id);
        Assert.assertEquals(product, fromDBAfterUpdate);
        Assert.assertNotEquals(fromDb, fromDBAfterUpdate);

        // testování delete
        productService.delete(id);
        Assert.assertEquals(0, productService.getProduct().size());
    }




}
