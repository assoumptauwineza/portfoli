package question6_user_profile_api.question6_user_profile_api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import question6_user_profile_api.question6_user_profile_api.model.ApiResponse;
import question6_user_profile_api.question6_user_profile_api.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    private List<UserProfile> users = new ArrayList<>();

    public UserProfileController() {
        users.add(new UserProfile(1L, "john_doe", "john@example.com", "John Doe", 28, "USA", "Software developer", true));
        users.add(new UserProfile(2L, "jane_smith", "jane@example.com", "Jane Smith", 32, "Canada", "Data scientist", true));
        users.add(new UserProfile(3L, "mike_wilson", "mike@example.com", "Mike Wilson", 25, "UK", "Web designer", false));
        users.add(new UserProfile(4L, "sarah_jones", "sarah@example.com", "Sarah Jones", 30, "USA", "Product manager", true));
        users.add(new UserProfile(5L, "tom_brown", "tom@example.com", "Tom Brown", 35, "Australia", "DevOps engineer", true));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserProfile>>> getAllUsers() {
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved successfully", users));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserById(@PathVariable Long userId) {
        return users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User found", user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null)));
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<ApiResponse<UserProfile>> getUserByUsername(@PathVariable String username) {
        return users.stream()
                .filter(u -> u.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .map(user -> ResponseEntity.ok(new ApiResponse<>(true, "User found", user)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>(false, "User not found", null)));
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getUsersByCountry(@PathVariable String country) {
        List<UserProfile> result = users.stream()
                .filter(u -> u.getCountry().equalsIgnoreCase(country))
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved by country", result));
    }

    @GetMapping("/age-range")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getUsersByAgeRange(@RequestParam int min, @RequestParam int max) {
        List<UserProfile> result = users.stream()
                .filter(u -> u.getAge() >= min && u.getAge() <= max)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Users retrieved by age range", result));
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<UserProfile>>> getActiveUsers() {
        List<UserProfile> result = users.stream()
                .filter(UserProfile::isActive)
                .toList();
        return ResponseEntity.ok(new ApiResponse<>(true, "Active users retrieved", result));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<UserProfile>> createUser(@RequestBody UserProfile user) {
        users.add(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "User profile created successfully", user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> updateUser(@PathVariable Long userId, @RequestBody UserProfile updatedUser) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserId().equals(userId)) {
                updatedUser.setUserId(userId);
                users.set(i, updatedUser);
                return ResponseEntity.ok(new ApiResponse<>(true, "User profile updated successfully", updatedUser));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }

    @PatchMapping("/{userId}/activate")
    public ResponseEntity<ApiResponse<UserProfile>> activateUser(@PathVariable Long userId) {
        for (UserProfile user : users) {
            if (user.getUserId().equals(userId)) {
                user.setActive(true);
                return ResponseEntity.ok(new ApiResponse<>(true, "User activated successfully", user));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }

    @PatchMapping("/{userId}/deactivate")
    public ResponseEntity<ApiResponse<UserProfile>> deactivateUser(@PathVariable Long userId) {
        for (UserProfile user : users) {
            if (user.getUserId().equals(userId)) {
                user.setActive(false);
                return ResponseEntity.ok(new ApiResponse<>(true, "User deactivated successfully", user));
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserProfile>> deleteUser(@PathVariable Long userId) {
        UserProfile deleted = users.stream()
                .filter(u -> u.getUserId().equals(userId))
                .findFirst()
                .orElse(null);
        if (deleted != null) {
            users.remove(deleted);
            return ResponseEntity.ok(new ApiResponse<>(true, "User deleted successfully", deleted));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, "User not found", null));
    }
}
