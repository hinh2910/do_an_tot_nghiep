package com.minhthang.management.service;

import com.minhthang.management.dto.ApiResponse;
import com.minhthang.management.entity.Product;
import com.minhthang.management.entity.ProductCategory;
import com.minhthang.management.repository.ProductCategoryRepository;
import com.minhthang.management.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    public ApiResponse<List<Product>> getAllProducts() {
        return ApiResponse.success("Lấy danh sách sản phẩm thành công", productRepository.findAll());
    }

    public ApiResponse<List<Product>> searchProducts(String q) {
        return ApiResponse.success("Tìm kiếm sản phẩm thành công", productRepository.search(q));
    }

    public ApiResponse<Product> getProductById(String id) {
        return productRepository.findById(id)
                .map(p -> ApiResponse.success("Lấy sản phẩm thành công", p))
                .orElse(ApiResponse.error("Không tìm thấy sản phẩm"));
    }

    @Transactional
    public ApiResponse<Product> createProduct(Product product) {
        Product saved = productRepository.save(product);
        return ApiResponse.success("Tạo sản phẩm thành công", saved);
    }

    @Transactional
    public ApiResponse<Product> updateProduct(String id, Product product) {
        return productRepository.findById(id)
                .map(existing -> {
                    existing.setName(product.getName());
                    existing.setSku(product.getSku());
                    existing.setDescription(product.getDescription());
                    existing.setCategory(product.getCategory());
                    existing.setPrice(product.getPrice());
                    existing.setCostPrice(product.getCostPrice());
                    existing.setUnit(product.getUnit());
                    existing.setImageUrl(product.getImageUrl());
                    return ApiResponse.success("Cập nhật sản phẩm thành công", productRepository.save(existing));
                })
                .orElse(ApiResponse.error("Không tìm thấy sản phẩm"));
    }

    @Transactional
    public ApiResponse<Product> toggleProductActive(String id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(!product.isActive());
                    return ApiResponse.success(
                            product.isActive() ? "Kích hoạt sản phẩm thành công" : "Vô hiệu hóa sản phẩm thành công",
                            productRepository.save(product));
                })
                .orElse(ApiResponse.error("Không tìm thấy sản phẩm"));
    }

    public ApiResponse<List<ProductCategory>> getAllCategories() {
        return ApiResponse.success("Lấy danh sách danh mục thành công", productCategoryRepository.findAll());
    }

    @Transactional
    public ApiResponse<ProductCategory> createCategory(ProductCategory category) {
        ProductCategory saved = productCategoryRepository.save(category);
        return ApiResponse.success("Tạo danh mục thành công", saved);
    }

    @Transactional
    public ApiResponse<ProductCategory> updateCategory(String id, ProductCategory category) {
        return productCategoryRepository.findById(id)
                .map(existing -> {
                    existing.setName(category.getName());
                    existing.setDescription(category.getDescription());
                    existing.setActive(category.isActive());
                    return ApiResponse.success("Cập nhật danh mục thành công", productCategoryRepository.save(existing));
                })
                .orElse(ApiResponse.error("Không tìm thấy danh mục"));
    }

    @Transactional
    public ApiResponse<Void> deleteCategory(String id) {
        if (!productCategoryRepository.existsById(id)) {
            return ApiResponse.error("Không tìm thấy danh mục");
        }
        productCategoryRepository.deleteById(id);
        return ApiResponse.success("Xóa danh mục thành công");
    }
}
