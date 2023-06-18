package com.expatrio.app.service;

import com.expatrio.app.exception.ResourceNotFoundException;
import com.expatrio.app.model.Department;
import com.expatrio.app.model.Role;
import com.expatrio.app.model.User;
import com.expatrio.app.payload.ApiResponse;
import com.expatrio.app.payload.SignUpRequest;
import com.expatrio.app.payload.UserResponse;
import com.expatrio.app.repository.DepartmentRepository;
import com.expatrio.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findByRole(Role.EMPLOYEE);
        return users.stream()
                .map(this::mapUserToResponse)
                .collect(Collectors.toList());
    }

    public ResponseEntity<?> getUserById(Long id) {
        Optional<User> userOptional = userRepository.findByIdAndRole(id, Role.EMPLOYEE);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return ResponseEntity.ok(mapUserToResponse(user));
        }

        throw new ResourceNotFoundException("User", "id", id);
    }


    public ResponseEntity<ApiResponse> deleteUser(Long id) {
        Optional<User> userOptional = userRepository.findById(id);

        if (userOptional.isPresent()) {
            userRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(true, "User deleted successfully."));
        }

        throw new ResourceNotFoundException("User", "id", id);
    }

    public List<UserResponse> getUsersByDepartment(String departmentName) {
        Department department = departmentRepository.findByName(departmentName);

        if (department != null) {
            List<User> users = userRepository.findByDepartmentAndRole(department, Role.EMPLOYEE);
            return users.stream()
                    .map(this::mapUserToResponse)
                    .collect(Collectors.toList());
        }

        throw new ResourceNotFoundException("Department", "name", departmentName);
    }

    public ResponseEntity<?> updateUser(Long id, User user) {
        User existingUser = userRepository.findById(id)
                .orElse(null);

        if (existingUser != null) {
            existingUser.setName(user.getName());
            existingUser.setRole(user.getRole());
            existingUser.setSalary(user.getSalary());
            existingUser.setDepartment(user.getDepartment());

            userRepository.update(existingUser);
            return ResponseEntity.ok().body(new ApiResponse(true, "User updated successfully"));
        }

        throw new ResourceNotFoundException("User", "id", id);
    }
    public ResponseEntity<ApiResponse> registerUser(SignUpRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity(new ApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Check if the department exists
        Department department = departmentRepository.findByName(signUpRequest.getDepartmentName());
        if (department == null) {
            return new ResponseEntity(new ApiResponse(false, "Department does not exist!"),
                    HttpStatus.BAD_REQUEST);
        }

        System.out.println("department id: "+department.getId());

        // Creating user's account
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getPassword(), signUpRequest.getSalary(), department);

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if(signUpRequest.getRole().equals("ADMIN")){
            user.setRole(Role.ADMIN);
        } else {
            user.setRole(Role.EMPLOYEE);
        }

        userRepository.save(user);

        return ResponseEntity.ok().body(new ApiResponse(true, "User registered successfully"));
    }

    private UserResponse mapUserToResponse(User user) {
        String departmentName = user.getDepartment() != null ? user.getDepartment().getName() : null;
        return new UserResponse(user.getId(), user.getUsername(), user.getName(),user.getSalary(), departmentName);
    }

}
