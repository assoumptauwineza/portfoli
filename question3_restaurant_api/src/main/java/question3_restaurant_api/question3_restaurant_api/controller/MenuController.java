package question3_restaurant_api.question3_restaurant_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import question3_restaurant_api.question3_restaurant_api.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private List<MenuItem> menuItems = new ArrayList<>();

    public MenuController() {
        menuItems.add(new MenuItem(1L, "Caesar Salad", "Fresh romaine lettuce with parmesan", 8.99, "Appetizer", true));
        menuItems.add(new MenuItem(2L, "Garlic Bread", "Toasted bread with garlic butter", 5.99, "Appetizer", true));
        menuItems.add(new MenuItem(3L, "Grilled Salmon", "Atlantic salmon with vegetables", 24.99, "Main Course", true));
        menuItems.add(new MenuItem(4L, "Beef Steak", "Premium ribeye steak", 32.99, "Main Course", false));
        menuItems.add(new MenuItem(5L, "Pasta Carbonara", "Creamy pasta with bacon", 16.99, "Main Course", true));
        menuItems.add(new MenuItem(6L, "Chocolate Cake", "Rich chocolate layer cake", 7.99, "Dessert", true));
        menuItems.add(new MenuItem(7L, "Tiramisu", "Classic Italian dessert", 8.99, "Dessert", true));
        menuItems.add(new MenuItem(8L, "Fresh Lemonade", "Homemade lemonade", 3.99, "Beverage", true));
    }

    @GetMapping
    public ResponseEntity<List<MenuItem>> getAllMenuItems() {
        return ResponseEntity.ok(menuItems);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuItem> getMenuItemById(@PathVariable Long id) {
        return menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<List<MenuItem>> getMenuItemsByCategory(@PathVariable String category) {
        List<MenuItem> result = menuItems.stream()
                .filter(item -> item.getCategory().equalsIgnoreCase(category))
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/available")
    public ResponseEntity<List<MenuItem>> getAvailableMenuItems(@RequestParam boolean available) {
        List<MenuItem> result = menuItems.stream()
                .filter(item -> item.isAvailable() == available)
                .toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/search")
    public ResponseEntity<List<MenuItem>> searchMenuItemsByName(@RequestParam String name) {
        List<MenuItem> result = menuItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<MenuItem> addMenuItem(@RequestBody MenuItem menuItem) {
        menuItems.add(menuItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItem);
    }

    @PutMapping("/{id}/availability")
    public ResponseEntity<MenuItem> toggleAvailability(@PathVariable Long id) {
        for (MenuItem item : menuItems) {
            if (item.getId().equals(id)) {
                item.setAvailable(!item.isAvailable());
                return ResponseEntity.ok(item);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MenuItem> deleteMenuItem(@PathVariable Long id) {
        MenuItem deleted = menuItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElse(null);
        if (deleted != null) {
            menuItems.remove(deleted);
            return ResponseEntity.ok(deleted);
        }
        return ResponseEntity.notFound().build();
    }
}
