package auca.ac.rw.restfullApiAssignment.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import auca.ac.rw.restfullApiAssignment.modal.ecommerce.Product;
import auca.ac.rw.restfullApiAssignment.service.ProductService;

@RestController
@RequestMapping("/api/products")    
public class ProductController {

    @Autowired
    private ProductService productService;
    
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addNewProduct(@RequestBody Product product) {
        String saveProduct = productService.addNewProduct(product);
        if(saveProduct.equals("Product added successfully")){
            return new ResponseEntity<>(saveProduct, HttpStatus.CREATED);
        }else{
            return new ResponseEntity<>(saveProduct, HttpStatus.CONFLICT);
        }
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if(product != null){
            return new ResponseEntity<>(product, HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Product not found", HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        String result = productService.updateProduct(id, product);
        if(result.equals("Product updated successfully")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        String result = productService.deleteProduct(id);
        if(result.equals("Product deleted successfully")){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
        }
    }
}
