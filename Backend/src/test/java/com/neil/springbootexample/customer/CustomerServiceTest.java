package com.neil.springbootexample.customer;

import com.neil.springbootexample.exception.DuplicateResourceException;
import com.neil.springbootexample.exception.RequestValidationException;
import com.neil.springbootexample.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        // WHEN
        underTest.getAllCustomers();

        // THEN
        verify(customerDao)
                .selectAllCustomers();
    }

    @Test
    void canGetOneCustomer() {
        // GIVEN
        int id = 10;
        Customer customer = new Customer(id, "Robert", "robert@gmail.com", 22);
        when(customerDao.selectOneCustomer(id)).thenReturn(customer);

        // WHEN
        Customer actual = underTest.getOneCustomer(10);

        // THEN
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnsNull() {
        // GIVEN
        int id = 10;
        when(customerDao.selectOneCustomer(id)).thenReturn(null);

        // WHEN
        // THEN
        assertThatThrownBy(() -> underTest.getOneCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("There's no such customer with id [%s]".formatted(id));
    }

    @Test
    void addCustomer() {
        // GIVEN
        int id = 10;
        String email = "robert@gmail.com";
        when(customerDao.exitsPersonWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "robert", email, 22
        );

        // WHEN
        underTest.addCustomer(request);

        // THEN

        // capturing
        ArgumentCaptor<Customer> captor = ArgumentCaptor.forClass(
                Customer.class
        );

        // verify that insertCustomer method was invoked  // we capture the value
        verify(customerDao).insertCustomer(captor.capture());

        // extraction
        Customer capturedCustomer = captor.getValue();

        // isNull because the id generated automatically by the Databasae
        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getAge()).isEqualTo(request.age());
        assertThat(capturedCustomer.getName()).isEqualTo(request.name());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.email());
    }


    @Test
    void willThrowWhenEmailExistsWhileAddingCustomer() {
        // GIVEN
        int id = 10;
        String email = "robert@gmail.com";
        when(customerDao.exitsPersonWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest(
                "robert", email, 22
        );

        // WHEN
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // THEN

        //verify that it'll never execute insertCustomer method
        verify(customerDao, never()).insertCustomer(any());
    }


    @Test
    void deleteCustomer() {
        // GIVEN
        int id = 10;
        when(customerDao.existCustomerWithId(id)).thenReturn(true);

        // WHEN
        underTest.deleteCustomer(id);

        // THEN
        verify(customerDao).deleteCustomer(id);
    }

    @Test
    void willThrowWhenDeleteCustomerDoesntExist() {
        // GIVEN
        int id = 10;
        when(customerDao.existCustomerWithId(id)).thenReturn(false);

        // WHEN
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        // THEN
        verify(customerDao, never()).deleteCustomer(any());
    }

    @Test
    void updateCustomer_shouldUpdateName() {
        // given
        Integer id = 1;
        Customer customer = new Customer(id, "John", "john@example.com", 30);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("Jane", null, null);
        when(customerDao.selectOneCustomer(id)).thenReturn(customer);

        // when
        underTest.updateCustomer(id, updateRequest);

        // then
        assertThat(customer.getName()).isEqualTo("Jane");
        verify(customerDao).updateCustomer(customer);
    }

    @Test
    void updateCustomer_shouldUpdateAge() {
        // given
        Integer id = 1;
        Customer customer = new Customer(id, "John", "john@example.com", 30);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, 25, null);
        when(customerDao.selectOneCustomer(id)).thenReturn(customer);

        // when
        underTest.updateCustomer(id, updateRequest);

        // then
        assertThat(customer.getAge()).isEqualTo(25);
        verify(customerDao).updateCustomer(customer);
    }

    @Test
    void updateCustomer_shouldUpdateEmail() {
        // given
        Integer id = 1;
        Customer customer = new Customer(id, "John", "john@example.com", 30);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, "jane@example.com");
        when(customerDao.selectOneCustomer(id)).thenReturn(customer);
        when(customerDao.exitsPersonWithEmail("jane@example.com")).thenReturn(false);

        // when
        underTest.updateCustomer(id, updateRequest);

        // then
        assertThat(customer.getEmail()).isEqualTo("jane@example.com");
        verify(customerDao).updateCustomer(customer);
    }

    @Test
    void updateCustomer_shouldThrowDuplicateResourceException() {
        // given
        Integer id = 1;
        Customer customer = new Customer(id, "John", "john@example.com", 30);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, "jane@example.com");
        when(customerDao.selectOneCustomer(id)).thenReturn(customer);
        when(customerDao.exitsPersonWithEmail("jane@example.com")).thenReturn(true);

        // when and then
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
        verify(customerDao, never()).updateCustomer(customer);
    }

    @Test
    void updateCustomer_shouldThrowRequestValidationException() {
        // given
        Integer id = 1;
        Customer customer = new Customer(id, "John", "john@example.com", 30);
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, null);
        when(customerDao.selectOneCustomer(id)).thenReturn(customer);

        // when and then
        assertThatThrownBy(() -> underTest.updateCustomer(id, updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no changes were made");
        verify(customerDao, never()).updateCustomer(customer);
    }
}
