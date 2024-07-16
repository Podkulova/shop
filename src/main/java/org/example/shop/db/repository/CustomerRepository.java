package org.example.shop.db.repository;

import org.example.shop.db.mapper.CustomerRowMapper;
import org.example.shop.domain.Customer;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;


@Component
public class CustomerRepository {
    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();

    public CustomerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Customer get(int id) {
        final String sql = "select * from customer where customer.id = " + id;
        try {
            return jdbcTemplate.queryForObject(sql, customerRowMapper);
        } catch (EmptyResultDataAccessException e) { // pokud takové id nenajde, vrátí null
            return null;
        }
    }

    public Integer add(Customer customer) {
        final String sql = "insert into customer(name, surname, email, address, age, phone_number) values (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHandler = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS); // aby stihl vrátit vygenerované id
                ps.setString(1, customer.getName());
                ps.setString(2, customer.getSurname());
                ps.setString(3, customer.getEmail());
                ps.setString(4, customer.getAddress());
                if (customer.getAge() != null) {
                    ps.setInt(5, customer.getAge()); // očekává null
                } else {
                    ps.setNull(5, Types.INTEGER);
                }
                ps.setString(6, customer.getPhoneNumber());
                return ps;
            }
        }, keyHandler);

        if (keyHandler.getKey() != null) {         // key je number a my z něj chceme int, protože id je int
            return keyHandler.getKey().intValue();
        } else {
            return null;
        }
    }

    public List<Customer> getAll() {
        final String sql = "select * from customer";
        return jdbcTemplate.query(sql, customerRowMapper);
    }
}

