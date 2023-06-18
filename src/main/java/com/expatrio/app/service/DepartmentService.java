package com.expatrio.app.service;

import com.expatrio.app.exception.ResourceNotFoundException;
import com.expatrio.app.model.Department;
import com.expatrio.app.model.User;
import com.expatrio.app.payload.ApiResponse;
import com.expatrio.app.payload.DepartmentResponse;
import com.expatrio.app.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DepartmentService {


    @Autowired
    private DepartmentRepository departmentRepository;

    public List<DepartmentResponse> getAllDepartments() {
        List<Department> departments = departmentRepository.findAll();
        return departments.stream()
                .map(this::mapDepartmentToResponse)
                .collect(Collectors.toList());
    }

    public ResponseEntity<DepartmentResponse> getDepartmentById(Long id) {

        Optional<Department> departmentOptional = departmentRepository.findById(id);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();
            return ResponseEntity.ok(mapDepartmentToResponse(department));
        }

        throw new ResourceNotFoundException("Department", "id", id);
    }

    public ResponseEntity<ApiResponse> createDepartment(String departmentName) {
        Department existingDepartment = departmentRepository.findByName(departmentName);

        if (existingDepartment != null) {
            return  ResponseEntity.badRequest().body(new ApiResponse(false, "Department already exist!"));
        }

        Department department = new Department();
        department.setName(departmentName);
        departmentRepository.save(department);

        return  ResponseEntity.ok(new ApiResponse(true, "Department registered successfully"));
    }

    public ResponseEntity<ApiResponse> updateDepartment(Long id, String name) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);

        if (departmentOptional.isPresent()) {
            Department department = departmentOptional.get();

            Department existingDepartment = departmentRepository.findByName(name);
            if (existingDepartment != null && !existingDepartment.getId().equals(id)) {
                return ResponseEntity.badRequest()
                        .body(new ApiResponse(false, "Department name already exists."));
            }

            department.setName(name);
            Department updatedDepartment = departmentRepository.update(department);
            return ResponseEntity.ok(new ApiResponse(true, "Department updated successfully."));
        }

        throw new ResourceNotFoundException("Department", "id", id);
    }

    public ResponseEntity<ApiResponse> deleteDepartment(Long id) {
        Optional<Department> departmentOptional = departmentRepository.findById(id);

        if (departmentOptional.isPresent()) {
            departmentRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse(true, "Department deleted successfully."));
        }

        throw new ResourceNotFoundException("Department", "id", id);
    }

    private DepartmentResponse mapDepartmentToResponse(Department department) {
        return new DepartmentResponse(department.getId(), department.getName());
    }
}