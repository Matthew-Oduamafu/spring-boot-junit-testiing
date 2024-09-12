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

import java.util.List;
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
    private List<Employee> employees;

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

        employees = List.of(
                Employee.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .build(),
                Employee.builder()
                        .id(2L)
                        .firstName("Jane")
                        .lastName("Doe")
                        .email("jane.doe@example.com")
                        .build(),
                Employee.builder()
                        .id(3L)
                        .firstName("Bob")
                        .lastName("Smith")
                        .email("bob.smith@example.com")
                        .build(),
                Employee.builder()
                        .id(4L)
                        .firstName("Tom")
                        .lastName("Jerry")
                        .email("tom.jerry@example.com")
                        .build(),
                Employee.builder()
                        .id(5L)
                        .firstName("Alice")
                        .lastName("Wonder")
                        .email("alice.wonder@example.com")
                        .build()
        );
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

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method")
    @Test
    public void givenEmployeesList_whenGetAllEmployees_thenReturnListOfEmployeeObjects() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findAll()).willReturn(employees);

        // when - action or the behaviour that we are going test
        var employees = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employees).isNotEmpty();
        assertThat(employees.size()).isEqualTo(this.employees.size());
    }

    // JUnit test for getAllEmployees method
    @DisplayName("JUnit test for getAllEmployees method given empty list")
    @Test
    public void givenEmptyEmployeesList_whenGetAllEmployees_thenReturnEmptyList() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findAll()).willReturn(List.of());

        // when - action or the behaviour that we are going test
        var employees = employeeService.getAllEmployees();

        // then - verify the output
        assertThat(employees).isEmpty();
    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployeeObject() {
        // given - precondition or setup
        int index = (int) (Math.random() * (employees.size() - 1));
        BDDMockito.given(employeeRepository.findById(employees.get(index).getId())).willReturn(Optional.of(employees.get(index)));

        // when - action or the behaviour that we are going test
        var savedEmployee = employeeService.getEmployeeById(employees.get(index).getId());

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
    }

    // JUnit test for getEmployeeById method
    @DisplayName("JUnit test for getEmployeeById method given invalid id")
    @Test
    public void givenInvalidEmployeeId_whenGetEmployeeById_thenThrowsException() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findById(1L)).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        Optional<Employee> savedEmployee = employeeService.getEmployeeById(1L);

        // then - verify the output
        assertThat(savedEmployee).isEmpty();
    }


    // JUnit test for updateEmployee method
    @DisplayName("JUnit test for updateEmployee method")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployeeObject() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));
        BDDMockito.given(employeeRepository.save(employee)).willReturn(employee);
        employee.setEmail("updated@example.com");
        employee.setFirstName("UpdatedFirstName");
        employee.setLastName("UpdatedLastName");

        // when - action or the behaviour that we are going test
        var updatedEmployee = employeeService.updateEmployee(employee);

        // then - verify the output
        assertThat(updatedEmployee).isNotNull();
        assertThat(updatedEmployee.getEmail()).isEqualTo(employee.getEmail());
        assertThat(updatedEmployee.getFirstName()).isEqualTo(employee.getFirstName());
        assertThat(updatedEmployee.getLastName()).isEqualTo(employee.getLastName());
    }

    // JUnit test for updateEmployee method given invalid id
    @DisplayName("JUnit test for updateEmployee method given invalid id")
    @Test
    public void givenInvalidEmployeeId_whenUpdateEmployee_thenThrowsException() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(employee);
        });

        // then - verify the output
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    // JUnit test for deleteEmployee method
    @DisplayName("JUnit test for deleteEmployee method")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenDeleteEmployeeObject() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.of(employee));

        // when - action or the behaviour that we are going test
        employeeService.deleteEmployee(employee.getId());

        // then - verify the output
        verify(employeeRepository).deleteById(employee.getId());
    }

    // JUnit test for deleteEmployee method given invalid id
    @DisplayName("JUnit test for deleteEmployee method given invalid id")
    @Test
    public void givenInvalidEmployeeId_whenDeleteEmployee_thenThrowsException() {
        // given - precondition or setup
        BDDMockito.given(employeeRepository.findById(employee.getId())).willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployee(employee.getId());
        });

        // then - verify the output
        verify(employeeRepository, never()).deleteById(any(Long.class));
    }
}
