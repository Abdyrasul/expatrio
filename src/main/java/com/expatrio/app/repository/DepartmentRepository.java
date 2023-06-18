package com.expatrio.app.repository;

import com.expatrio.app.model.Department;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

@Repository
public class DepartmentRepository   {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DepartmentRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Department> findAll() {
        String sql = "SELECT * FROM Department";
        return namedParameterJdbcTemplate.query(sql, new DepartmentRowMapper());
    }

    public Department findByName(String name) {
        String sql = "SELECT * FROM Department WHERE NAME = :Name";
        Map<String, Object> params = Collections.singletonMap("Name", name);
        List<Department> departments = namedParameterJdbcTemplate.query(sql, params, new DepartmentRowMapper());
        return departments.isEmpty() ? null : departments.get(0);
    }

    public void deleteById(Long id) {
        String sql = "DELETE FROM Department WHERE ID = :ID";
        Map<String, Object> params = Collections.singletonMap("ID", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public Optional<Department> findById(Long id) {
        String sql = "SELECT * FROM Department WHERE ID = :ID";
        Map<String, Object> params = Collections.singletonMap("ID", id);
        List<Department> departments = namedParameterJdbcTemplate.query(sql, params, new DepartmentRowMapper());
        return departments.isEmpty() ? Optional.empty() : Optional.of(departments.get(0));
    }

    public Department save(Department department) {
        String sql = "INSERT INTO Department (NAME) VALUES (:Name)";
        SqlParameterSource parameters = new MapSqlParameterSource("Name", department.getName());
        KeyHolder keyHolder = new GeneratedKeyHolder();

        namedParameterJdbcTemplate.update(sql, parameters, keyHolder);

        Map<String, Object> generatedKeys = keyHolder.getKeys();
        if (generatedKeys != null && !generatedKeys.isEmpty()) {
            Object generatedId = generatedKeys.get("id");
            if (generatedId instanceof Long) {
                Long generatedIdLong = (Long) generatedId;
                department.setId(generatedIdLong);
            } else if (generatedId instanceof Integer) {
                Integer generatedIdInt = (Integer) generatedId;
                department.setId(generatedIdInt.longValue());
            }
        }

        return department;
    }

    public Department update(Department department) {
        String sql = "UPDATE Department SET NAME = :Name WHERE ID = :ID";
        Map<String, Object> params = new HashMap<>();
        params.put("Name", department.getName());
        params.put("ID", department.getId());

        namedParameterJdbcTemplate.update(sql, params);

        return department;
    }

}
