package com.expatrio.app.controller;

import com.expatrio.app.payload.ApiResponse;
import com.expatrio.app.payload.DepartmentResponse;
import com.expatrio.app.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @GetMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    @Secured("ADMIN")
    public List<DepartmentResponse> getAllDepartments() {
        return departmentService.getAllDepartments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<DepartmentResponse> getDepartmentById(@PathVariable Long id) {
        return departmentService.getDepartmentById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createDepartment(@RequestBody String departmentName) {
        return departmentService.createDepartment(departmentName);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateDepartment(@PathVariable Long id, @RequestBody String departmentName) {
        return departmentService.updateDepartment(id, departmentName);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDepartment(@PathVariable Long id) {
        return departmentService.deleteDepartment(id);
    }
}
