package com.expatrio.app.controller;

import com.expatrio.app.model.Department;
import com.expatrio.app.model.Role;
import com.expatrio.app.model.User;
import com.expatrio.app.payload.ApiResponse;
import com.expatrio.app.payload.SignUpRequest;
import com.expatrio.app.payload.UserResponse;
import com.expatrio.app.repository.DepartmentRepository;
import com.expatrio.app.repository.UserRepository;
import com.expatrio.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;


@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponse> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") Long id) {
        return userService.getUserById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("id") Long id) {
        return userService.deleteUser(id);
    }

    @GetMapping("/department/{departmentName}")
    public List<UserResponse> getUsersByDepartment(@PathVariable("departmentName") String departmentName) {
        return userService.getUsersByDepartment(departmentName);
    }

    @PostMapping("/registerUser")
    public ResponseEntity<ApiResponse> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        return userService.registerUser(signUpRequest);

    }
}
