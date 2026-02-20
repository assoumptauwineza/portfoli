package auca.ac.rw.restfullApiAssignment.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import auca.ac.rw.restfullApiAssignment.modal.ecommerce.Product;
import auca.ac.rw.restfullApiAssignment.repository.ProductRepository;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    public String addNewProduct(Product product) {
        productRepository.save(product);
        return "Product added successfully";
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public String updateProduct(Long id, Product product) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isPresent()) {
            Product updatedProduct = existingProduct.get();
            updatedProduct.setName(product.getName());
            updatedProduct.setDescription(product.getDescription());
            updatedProduct.setPrice(product.getPrice());
            updatedProduct.setCategory(product.getCategory());
            updatedProduct.setStockQuantity(product.getStockQuantity());
            updatedProduct.setBrand(product.getBrand());
            productRepository.save(updatedProduct);
            return "Product updated successfully";
        }else{
            return "Product not found";
        }
    }

    public String deleteProduct(Long id) {
        Optional<Product> existingProduct = productRepository.findById(id);
        if(existingProduct.isPresent()) {
            productRepository.deleteById(id);
            return "Product deleted successfully";
        }else{
            return "Product not found";
        }
    }
}
