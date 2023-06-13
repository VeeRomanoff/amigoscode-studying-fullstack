package com.neil.springbootexample.customer;

import com.neil.springbootexample.exception.ResourceNotFoundException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        String SQL = "SELECT * FROM customer";
        return jdbcTemplate.query(SQL, new BeanPropertyRowMapper<>(Customer.class));
    }

    @Override
    public Customer selectOneCustomer(int id) {
        String SQL = "SELECT * FROM customer WHERE id = ?";

       return jdbcTemplate.query(SQL, new Object[]{id}, new BeanPropertyRowMapper<>(Customer.class))
               .stream()
               .findAny()
               .orElseThrow(() -> new ResourceNotFoundException("Customer with id %s not found".formatted(id)));
    }

    @Override
    public void insertCustomer(Customer customer) {
        String SQL = "INSERT INTO customer(age, email, name) VALUES (?, ?, ?)";
        jdbcTemplate.update(SQL, customer.getAge(), customer.getEmail(), customer.getName());
    }

    @Override
    public boolean exitsPersonWithEmail(String email) {
        return selectAllCustomers().stream()
                .anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public void deleteCustomer(Integer id) {
        String SQL = "DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(SQL, id);
    }

    @Override
    public void updateCustomer(Customer customer) {
        String SQL = "UPDATE customer SET name = ?, age = ? WHERE id = ?";
        jdbcTemplate.update(SQL, customer.getName(), customer.getAge(), customer.getId());
    }

    @Override
    public boolean existCustomerWithId(Integer id) {
        return selectAllCustomers().stream()
                .anyMatch(cus -> cus.getId().equals(id));
    }
}


