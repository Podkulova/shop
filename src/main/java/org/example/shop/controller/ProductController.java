package org.example.shop.controller;

import org.example.shop.db.service.api.ProductService;
import org.example.shop.db.service.api.request.UpdateProductRequest;
import org.example.shop.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("product")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity add(@RequestBody Product product) {
        Integer id = productService.add(product);
        if (id != null) {
            return new ResponseEntity<>(id, HttpStatus.CREATED); // 201
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR); // 500
    }

    @GetMapping("{id}")
    public ResponseEntity get(@PathVariable("id") int id) {
        Product product = productService.get(id);
        if (product == null) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);   // 404
        }
        return new ResponseEntity<>(product, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Product>> getAll() {
        List<Product> productList = productService.getProduct();
        return new ResponseEntity<>(productList, HttpStatus.OK); // 200
    }

    @PatchMapping("{id}")
    public ResponseEntity update(@PathVariable("id") int id, @RequestBody UpdateProductRequest request) {
        if (productService.get(id) != null) {
            productService.update(id, request);
            return ResponseEntity.ok().build(); // 200 bez těla
        } else {
            return ResponseEntity
                    .status(HttpStatus.PRECONDITION_FAILED) // 412 id nebylo splněno
                    .body("Product with id: " + id + " does not exist.");
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") int id) {
        if (productService.get(id) != null) {
            productService.delete(id);
            return ResponseEntity.ok().build(); // 200 bez těla
        } else {
            return ResponseEntity
                    .status(HttpStatus.PRECONDITION_FAILED) // 412 id nebylo splněno
                    .body("Product with id: " + id + " does not exist.");
        }
    }
}
