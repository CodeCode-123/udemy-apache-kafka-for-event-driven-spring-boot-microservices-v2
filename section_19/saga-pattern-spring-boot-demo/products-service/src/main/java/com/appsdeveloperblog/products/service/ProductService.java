package com.appsdeveloperblog.products.service;

import java.util.List;
import java.util.UUID;

import com.appsdeveloperblog.products.dto.Product;

public interface ProductService {
    List<Product> findAll();
    Product reserve(Product desiredProduct, UUID orderId);
    void cancelReservation(Product productToCancel, UUID orderId);
    Product save(Product product);
}
