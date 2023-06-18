package com.expatrio.app.repository;

import com.expatrio.app.model.Department;
import com.expatrio.app.model.Role;
import com.expatrio.app.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class  UserRepository  {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Optional<User> findByUsername(String username) {
        try {
            String query = "SELECT * FROM \"User\" WHERE Username = :Username";
            Map<String, Object> params = Collections.singletonMap("Username", username);
            User user = namedParameterJdbcTemplate.queryForObject(query, params, new UserRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existsByUsername(String username) {
        String query = "SELECT COUNT(*) FROM \"User\" WHERE Username = :Username";
        Map<String, Object> params = Collections.singletonMap("Username", username);
        Integer count = namedParameterJdbcTemplate.queryForObject(query, params, Integer.class);
        return count != null && count > 0;
    }

    public double getAverageSalaryByDepartment(Department department) {
        String query = "SELECT AVG(salary) FROM \"User\" WHERE DepartmentId = :DepartmentId";
        Map<String, Object> params = Collections.singletonMap("DepartmentId", department.getId());
        return namedParameterJdbcTemplate.queryForObject(query, params, Double.class);
    }

    public List<User> findByDepartment(Department department) {
        String query = "SELECT * FROM \"User\" WHERE DepartmentId = :DepartmentId";
        Map<String, Object> params = Collections.singletonMap("DepartmentId", department.getId());
        return namedParameterJdbcTemplate.query(query, params, new UserRowMapper());
    }

    public List<User> findByRole(Role role) {
        String query = "SELECT u.*, d.name AS department_name " +
                "FROM \"User\" u " +
                "JOIN Department d ON u.departmentid = d.id " +
                "WHERE u.role = :Role";
        Map<String, Object> params = Collections.singletonMap("Role", role.toString());
        return namedParameterJdbcTemplate.query(query, params, new UserWithDepartmentRowMapper());
    }

    public Optional<User> findByIdAndRole(Long id, Role role) {
        try {
            String query = "SELECT u.*, d.name AS department_name " +
                    "FROM \"User\" u " +
                    "JOIN Department d ON u.departmentid = d.id " +
                    "WHERE u.id = :ID AND u.role = :Role";
            Map<String, Object> params = new HashMap<>();
            params.put("ID", id);
            params.put("Role", role.toString());
            User user = namedParameterJdbcTemplate.queryForObject(query, params, new UserWithDepartmentRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<User> findByDepartmentAndRole(Department department, Role role) {
        String query = "SELECT u.*, d.name AS department_name " +
                "FROM \"User\" u " +
                "JOIN Department d ON u.departmentid = d.id " +
                "WHERE u.DepartmentId = :DepartmentId AND u.Role = :Role";
        Map<String, Object> params = new HashMap<>();
        params.put("DepartmentId", department.getId());
        params.put("Role", role.toString());
        return namedParameterJdbcTemplate.query(query, params, new UserWithDepartmentRowMapper());
    }

    public Optional<User> findById(Long id) {
        try {
            String query = "SELECT * FROM \"User\" WHERE ID = :ID";
            Map<String, Object> params = Collections.singletonMap("ID", id);
            User user = namedParameterJdbcTemplate.queryForObject(query, params, new UserRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void deleteById(Long id) {
        String query = "DELETE FROM \"User\" WHERE ID = :ID";
        Map<String, Object> params = Collections.singletonMap("ID", id);
        namedParameterJdbcTemplate.update(query, params);
    }

    public void save(User user) {
        String query = "INSERT INTO \"User\" (Role, Name, Username, Password, DepartmentID, Salary) " +
                "VALUES (:Role, :Name, :Username, :Password, :DepartmentID, :Salary)";
        Map<String, Object> params = new HashMap<>();
        params.put("Role", user.getRole().toString());
        params.put("Name", user.getName());
        params.put("Username", user.getUsername());
        params.put("Password", user.getPassword());
        params.put("DepartmentID", user.getDepartment().getId());
        params.put("Salary", user.getSalary());
        namedParameterJdbcTemplate.update(query, params);
    }

    public User update(User user) {
        String query = "UPDATE \"User\" SET role = :Role, Name = :Name, Username = :Username, " +
                "Password = :Password, DepartmentID = :DepartmentID, Salary = :Salary WHERE ID = :ID";
        Map<String, Object> params = new HashMap<>();
        params.put("Role", user.getRole().toString());
        params.put("Name", user.getName());
        params.put("Username", user.getUsername());
        params.put("Password", user.getPassword());
        params.put("DepartmentID", user.getDepartment().getId());
        params.put("Salary", user.getSalary());
        params.put("ID", user.getId());
        namedParameterJdbcTemplate.update(query, params);
        return user;
    }
}
