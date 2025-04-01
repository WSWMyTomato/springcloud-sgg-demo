package com.wsw.product.service.impl;

import com.wsw.product.bean.Product;
import com.wsw.product.service.ProductService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ProductServiceImpl implements ProductService {
    @Override
    public Product getProductById(Long productId) {
        Product product = new Product();

        product.setId(productId);
        product.setProductName("小米" + productId);
        product.setPrice(new BigDecimal(100));
        product.setStock(100);

        return product;
    }
}
