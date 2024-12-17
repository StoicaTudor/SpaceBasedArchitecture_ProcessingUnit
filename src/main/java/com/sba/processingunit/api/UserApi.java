package com.sba.processingunit.api;

import com.sba.processingunit.data.User;
import com.sba.processingunit.service.UserCRUDService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "UserApi", description = "Main entrypoint of this demo app.")
@RestController
@RequestMapping(UrlMappings.User.ROOT)
public class UserApi {
    private final UserCRUDService userCRUDService;

    public UserApi(UserCRUDService userCRUDService) {
        this.userCRUDService = userCRUDService;
    }

    @Operation(summary = "Create a new user", description = "This API creates a new user in the system")
    @ApiResponse(responseCode = "200", description = "User created successfully")
    @PostMapping(UrlMappings.User.CREATE_USER)
    public ResponseEntity<String> createUser(final @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(userCRUDService.createUser(user).id());
    }

    @Operation(summary = "Updates a user", description = "This API updates a user in the system")
    @ApiResponse(responseCode = "200", description = "User updated successfully")
    @PutMapping(UrlMappings.User.UPDATE_USER)
    public ResponseEntity<Void> updateUser(final @RequestBody User user) {
        userCRUDService.updateUser(user);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Deletes a user", description = "This API deletes a user in the system")
    @ApiResponse(responseCode = "200", description = "User deleted successfully")
    @DeleteMapping(UrlMappings.User.DELETE_USER)
    public ResponseEntity<Void> deleteUser(final @RequestParam String id) {
        userCRUDService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "Get all users", description = "This API fetches the all user in the system.")
    @ApiResponse(responseCode = "200", description = "All users fetched successfully")
    @GetMapping(UrlMappings.User.GET_ALL_USERS)
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userCRUDService.getAllUsers());
    }

    @Operation(summary = "Get user by ID", description = "This API fetches the corresponding user in the system by its ID.")
    @ApiResponse(responseCode = "200", description = "User fetched successfully.")
    @GetMapping(UrlMappings.User.GET_USER)
    public ResponseEntity<User> getUser(@RequestParam String id) {
        return ResponseEntity.status(HttpStatus.OK).body(userCRUDService.getUserByID(id));
    }
}