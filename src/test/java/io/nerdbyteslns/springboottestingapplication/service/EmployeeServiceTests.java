package io.nerdbyteslns.springboottestingapplication.service;

import io.nerdbyteslns.springboottestingapplication.exception.ResourceNotFoundException;
import io.nerdbyteslns.springboottestingapplication.model.Employee;
import io.nerdbyteslns.springboottestingapplication.repository.EmployeeRepository;
import io.nerdbyteslns.springboottestingapplication.service.impl.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTests {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;

    @BeforeEach
    public void setUp() {
//        employeeRepository = mock(EmployeeRepository.class);
//        employeeService = new EmployeeServiceImpl(employeeRepository);
        employee = Employee.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
    }


    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method")
    @Test
    public void givenEmployeeObject_whenSaveEmployee_thenReturnEmployeeObject() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.empty());
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }


    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method which throws exception")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test

        // then - verify the output
        assertThatThrownBy(() -> employeeService.saveEmployee(employee))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Employee with email " + employee.getEmail() + " already exists");

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });
    }


    // JUnit test for saveEmployee method
    @DisplayName("JUnit test for saveEmployee method which throws exception_2")
    @Test
    public void givenExistingEmail_whenSaveEmployee_thenThrowsException_2() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findByEmail(employee.getEmail())).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.saveEmployee(employee);
        });

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }


}
