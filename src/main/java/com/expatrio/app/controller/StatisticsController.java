package com.expatrio.app.controller;

import com.expatrio.app.model.Department;
import com.expatrio.app.payload.ApiResponse;
import com.expatrio.app.repository.DepartmentRepository;
import com.expatrio.app.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/average-salary-per-department")
    public ResponseEntity<Map<String, Double>> getAverageSalaryPerDepartment() {
        List<Department> departments = departmentRepository.findAll();
        Map<String, Double> averageSalaries = new HashMap<>();

        for (Department department : departments) {
            double averageSalary = userRepository.getAverageSalaryByDepartment(department);
            averageSalaries.put(department.getName(), averageSalary);
        }

        return ResponseEntity.ok(averageSalaries);
    }

    @GetMapping("/average-salary/{departmentId}")
    public ResponseEntity<Double> getAverageSalaryByDepartment(@PathVariable Long departmentId) {
        Optional<Department> departmentOptional = departmentRepository.findById(departmentId);

        if (departmentOptional.isPresent()) {
            return new ResponseEntity(new ApiResponse(false, "Department does not exist!"),
                    HttpStatus.BAD_REQUEST);
        }

        Department department = departmentOptional.get();
        double averageSalary = userRepository.getAverageSalaryByDepartment(department);

        return ResponseEntity.ok(averageSalary);
    }
}
