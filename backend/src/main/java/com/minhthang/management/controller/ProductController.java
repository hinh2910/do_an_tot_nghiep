package com.minhthang.management.controller;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Product;
import com.minhthang.management.entity.ProductCategory;
import com.minhthang.management.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @RequestParam(required = false) String q
    ) {
        ApiResponse<List<Product>> response = (q != null && !q.isBlank())
                ? productService.searchProducts(q)
                : productService.getAllProducts();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProductById(@PathVariable String id) {
        ApiResponse<Product> response = productService.getProductById(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(@RequestBody Product product) {
        ApiResponse<Product> response = productService.createProduct(product);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable String id,
            @RequestBody Product product
    ) {
        ApiResponse<Product> response = productService.updateProduct(id, product);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @PutMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse<Product>> toggleProductActive(@PathVariable String id) {
        ApiResponse<Product> response = productService.toggleProductActive(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllCategories() {
        return ResponseEntity.ok(productService.getAllCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<ApiResponse<ProductCategory>> createCategory(@RequestBody ProductCategory category) {
        return ResponseEntity.ok(productService.createCategory(category));
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<ProductCategory>> updateCategory(
            @PathVariable String id,
            @RequestBody ProductCategory category
    ) {
        ApiResponse<ProductCategory> response = productService.updateCategory(id, category);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCategory(@PathVariable String id) {
        ApiResponse<Void> response = productService.deleteCategory(id);
        return response.isSuccess() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
