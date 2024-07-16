package org.example.shop;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.shop.db.service.api.request.UpdateProductRequest;
import org.example.shop.domain.Customer;
import org.example.shop.domain.Merchant;
import org.example.shop.domain.Product;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@AutoConfigureMockMvc
public class RestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private Merchant merchant;

    @Before
    public void createMerchant() throws Exception {
        if (merchant == null) {
            merchant = new Merchant("name", "email", "address");

            String id = mockMvc.perform(post("/merchant")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(merchant)))
                    .andExpect(status().isCreated()) // 201
                    .andReturn().getResponse().getContentAsString();
            merchant.setId(objectMapper.readValue(id, Integer.class));
        }
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void customer() throws Exception {
        Customer customer = new Customer("Ferko", "Mrkvicka", "email", "address", 21, "xxx");

        // Add customer
        String id = mockMvc.perform(post("/customer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customer)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        customer.setId(objectMapper.readValue(id, Integer.class));

        // Get customer
        String customerJson = mockMvc.perform(get("/customer/" + customer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Customer returnedCustomer = objectMapper.readValue(customerJson, Customer.class);
        Assertions.assertEquals(customer, returnedCustomer);

        // Get all customers
        String listJson = mockMvc.perform(get("/customer")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<Customer> customers = objectMapper.readValue(listJson, new TypeReference<List<Customer>>() {
        });
        assert customers.size() == 1;
        Assertions.assertEquals(customer, customers.get(0));
    }

    @Test
    public void merchant() throws Exception {
        // Add merchant
        // is already created

        // Get merchant
        String merchantJson = mockMvc.perform(get("/merchant/" + merchant.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200
                .andReturn().getResponse().getContentAsString();

        Merchant returndMerchant = objectMapper.readValue(merchantJson, Merchant.class);
        Assert.assertEquals(merchant, returndMerchant);

        // Get all merchant
        String listJson = mockMvc.perform(get("/merchant")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // 200
                .andReturn().getResponse().getContentAsString();

        List<Merchant> merchants = objectMapper.readValue(listJson, new TypeReference<List<Merchant>>() {
        });

        assert merchants.size() == 1;

        Assert.assertEquals(merchant, merchants.get(0));

    }

    @Test
    public void product() throws Exception {
        Product product = new Product(merchant.getId(), "klavesnica", "mega lacna klavesnica", 1, 10);

        // Add product
        String id = mockMvc.perform(post("/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        product.setId(objectMapper.readValue(id, Integer.class));

        // Get product
        String returnedProduct = mockMvc.perform(get("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Product productFromRest = objectMapper.readValue(returnedProduct, Product.class);
        Assertions.assertEquals(product, productFromRest);

        // Get all products
        String listJson = mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Product> products = objectMapper.readValue(listJson, new TypeReference<List<Product>>() {
        });

        assert products.size() == 1;
        Assertions.assertEquals(product, products.get(0));

        // Update product
        double updatePrice = product.getPrice() + 1;
        int updatedAvailable = product.getAvailable() + 5;
        UpdateProductRequest updateProductRequest = new UpdateProductRequest(product.getName(), product.getDescription(), updatePrice, updatedAvailable);

        mockMvc.perform(patch("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequest)))
                .andExpect(status().isOk());

        String returnedUpdatedProduct = mockMvc.perform(get("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Product updatedProduct = objectMapper.readValue(returnedUpdatedProduct, Product.class);
        assert updatePrice == updatedProduct.getPrice();
        assert updatedAvailable == updatedProduct.getAvailable();

        // Delete product
        mockMvc.perform(delete("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/product/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        String listJson2 = mockMvc.perform(get("/product")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        List<Product> products2 = objectMapper.readValue(listJson2, new TypeReference<List<Product>>() {
        });

        assert products2.size() == 0;
    }
}
