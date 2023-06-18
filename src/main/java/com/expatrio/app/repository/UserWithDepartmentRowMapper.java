package com.expatrio.app.repository;

import com.expatrio.app.model.Department;
import com.expatrio.app.model.Role;
import com.expatrio.app.model.User;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserWithDepartmentRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("ID"));
        user.setRole(Role.valueOf(rs.getString("role")));
        user.setName(rs.getString("Name"));
        user.setUsername(rs.getString("Username"));
        user.setPassword(rs.getString("Password"));
        user.setSalary(rs.getFloat("Salary"));
        // Map the department information
        Department department = new Department();
        department.setId(rs.getLong("departmentid"));
        department.setName(rs.getString("department_name"));
        user.setDepartment(department);

        return user;
    }
}