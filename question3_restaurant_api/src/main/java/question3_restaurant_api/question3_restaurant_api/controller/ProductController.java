package question3_restaurant_api.question3_restaurant_api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import question3_restaurant_api.question3_restaurant_api.model.Product;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private List<Product> products = new ArrayList<>();

    public ProductController() {
        products.add(new Product(1L, "Product A", 100));
        products.add(new Product(2L, "Product B", 50));
        products.add(new Product(3L, "Product C", 75));
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(products);
    }

    @GetMapping("/{id}/stock")
    public ResponseEntity<Integer> getProductStock(@PathVariable Long id) {
        return products.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .map(p -> ResponseEntity.ok(p.getStock()))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Product> updateProductStock(@PathVariable Long id, @RequestParam Integer stock) {
        for (Product p : products) {
            if (p.getId().equals(id)) {
                p.setStock(stock);
                return ResponseEntity.ok(p);
            }
        }
        return ResponseEntity.notFound().build();
    }
}
