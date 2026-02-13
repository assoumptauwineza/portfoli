package question4_e_commerce_api.question4_e_commerce_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import question4_e_commerce_api.question4_e_commerce_api.model.Product;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> products = new ArrayList<>();

    public ProductController() {
        products.add(new Product(1L, "iPhone 14", "Latest Apple smartphone with advanced features", 999.99, "Electronics", 50, "Apple"));
        products.add(new Product(2L, "Samsung Galaxy S23", "Flagship Android smartphone", 899.99, "Electronics", 30, "Samsung"));
        products.add(new Product(3L, "MacBook Pro", "High-performance laptop for professionals", 2499.99, "Electronics", 15, "Apple"));
        products.add(new Product(4L, "Nike Air Max", "Comfortable running shoes", 129.99, "Footwear", 100, "Nike"));
        products.add(new Product(5L, "Adidas Ultraboost", "Premium athletic shoes", 179.99, "Footwear", 0, "Adidas"));
        products.add(new Product(6L, "Sony Headphones", "Noise-cancelling wireless headphones", 349.99, "Electronics", 25, "Sony"));
        products.add(new Product(7L, "Levi's Jeans", "Classic denim jeans", 79.99, "Clothing", 200, "Levi's"));
        products.add(new Product(8L, "Canon Camera", "Professional DSLR camera", 1499.99, "Electronics", 10, "Canon"));
        products.add(new Product(9L, "North Face Jacket", "Waterproof outdoor jacket", 249.99, "Clothing", 45, "North Face"));
        products.add(new Product(10L, "Fitbit Watch", "Fitness tracking smartwatch", 199.99, "Electronics", 60, "Fitbit"));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit) {
        if (page != null && limit != null) {
            int start = page * limit;
            int end = Math.min(start + limit, products.size());
            if (start >= products.size()) {
                return ResponseEntity.ok(new ArrayList<>());
            }
            return ResponseEntity.ok(products.subList(start, end));
        }
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable Long productId) {
        return products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        List<Product> result = products.stream()
                .filter(p -> p.getCategory().equalsIgnoreCase(category))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable String brand) {
        List<Product> result = products.stream()
                .filter(p -> p.getBrand().equalsIgnoreCase(brand))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
        List<Product> result = products.stream()
                .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase()) ||
                        p.getDescription().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<Product>> getProductsByPriceRange(@RequestParam Double min, @RequestParam Double max) {
        List<Product> result = products.stream()
                .filter(p -> p.getPrice() >= min && p.getPrice() <= max)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/in-stock")
    public ResponseEntity<List<Product>> getInStockProducts() {
        List<Product> result = products.stream()
                .filter(p -> p.getStockQuantity() > 0)
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        products.add(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(product);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long productId, @RequestBody Product updatedProduct) {
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getProductId().equals(productId)) {
                updatedProduct.setProductId(productId);
                products.set(i, updatedProduct);
                return ResponseEntity.ok(updatedProduct);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{productId}/stock")
    public ResponseEntity<Product> updateStock(@PathVariable Long productId, @RequestParam int quantity) {
        for (Product product : products) {
            if (product.getProductId().equals(productId)) {
                product.setStockQuantity(quantity);
                return ResponseEntity.ok(product);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Product> deleteProduct(@PathVariable Long productId) {
        Product deleted = products.stream()
                .filter(p -> p.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
        if (deleted != null) {
            products.remove(deleted);
            return ResponseEntity.ok(deleted);
        }
        return ResponseEntity.notFound().build();
    }
}
