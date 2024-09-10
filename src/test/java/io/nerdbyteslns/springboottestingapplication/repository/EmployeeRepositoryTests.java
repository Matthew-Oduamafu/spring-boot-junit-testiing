package io.nerdbyteslns.springboottestingapplication.repository;


import io.nerdbyteslns.springboottestingapplication.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    public void setup() {
        employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@exmple.com")
                .build();
    }

    // JUnit test for save employee operation
    @DisplayName("JUnit test for save employee operation")
    @Test
    public void givenEmployeeObject_whenSave_thenReturnSavedEmployee() {

        // given - precondition or setup

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.save(employee);

        // then - verify the output
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getId()).isPositive();
    }

    // JUnit test for get all employees operation
    @DisplayName("JUnit test for get all employees operation")
    @Test
    public void givenEmployeesList_whenFindAll_thenReturnEmployeesList() {

        // given - precondition or setup
//        Employee employee1 = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();

        Employee employee2 = Employee.builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("janedoe@exmple.com")
                .build();

        employeeRepository.save(employee);
        employeeRepository.save(employee2);

        // when - action or the behaviour that we are going test
        List<Employee> employeeList = employeeRepository.findAll();

        // then - verify the output
        assertThat(employeeList).isNotNull();
        assertThat(employeeList.size()).isEqualTo(2);
    }

    // JUnit test for get employee by id operation
    @DisplayName("JUnit test for get employee by id operation")
    @Test
    public void givenEmployeeObject_whenFindById_thenReturnEmployeeObject() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        boolean employeeFound = employeeRepository.findById(employee.getId()).isPresent();

        // then - verify the output
        assertThat(employeeFound).isTrue();
    }

    // JUnit test for get employee by email operation
    @DisplayName("JUnit test for get employee by email operation")
    @Test
    public void givenEmployeeEmail_whenFindByEmail_thenReturnEmployeeObject() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        boolean employeeByEmail = employeeRepository.findByEmail(employee.getEmail()).isPresent();

        // then - verify the output
        assertThat(employeeByEmail).isTrue();
    }

    // JUnit test for update employee operation
    @DisplayName("JUnit test for update employee operation")
    @Test
    public void givenEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("XXXXXXXXXXXXXXXXXX")
//                .build();

        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        Employee savedEmployee = employeeRepository.findById(employee.getId()).get();
        savedEmployee.setEmail("janedoe@example.com");
        savedEmployee.setFirstName("Jon");
        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        // then - verify the output
        assertThat(updatedEmployee.getEmail()).isEqualTo("janedoe@example.com");
        assertThat(updatedEmployee.getFirstName()).isEqualTo("Jon");
    }

    // JUnit test for delete employee operation
    @DisplayName("JUnit test for delete employee operation")
    @Test
    public void givenEmployeeObject_whenDeleteEmployee_thenEmployeeShouldBeDeleted() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();
        employeeRepository.save(employee);

        // when - action or the behaviour that we are going test
        employeeRepository.deleteById(employee.getId());

        // then - verify the output
        boolean employeeFound = employeeRepository.findById(employee.getId()).isPresent();
        assertThat(employeeFound).isFalse();
    }

    // JUnit test for custom finder method operation
    @DisplayName("JUnit test for custom finder method operation")
    @Test
    public void givenEmployeeName_whenFindByJPQL_thenReturnEmployeeObject() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();

        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going test
        Employee employeeFound = employeeRepository.findByJPQL(firstName, lastName);

        // then - verify the output
        assertThat(employeeFound).isNotNull();
        assertThat(employeeFound.getFirstName()).isEqualTo(firstName);
        assertThat(employeeFound.getLastName()).isEqualTo(lastName);
    }

    // JUnit test for custom finder method operation with named parameters
    @DisplayName("JUnit test for custom finder method operation with named parameters")
    @Test
    public void givenEmployeeName_whenFindByJPQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();

        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going test
        Employee employeeFound = employeeRepository.findByJPQLNamedParams(firstName, lastName);

        // then - verify the output
        assertThat(employeeFound).isNotNull();
        assertThat(employeeFound.getFirstName()).isEqualTo(firstName);
        assertThat(employeeFound.getLastName()).isEqualTo(lastName);
    }

    // JUnit test for custom finder method operation with native SQL
    @DisplayName("JUnit test for custom finder method operation with native SQL")
    @Test
    public void givenEmployeeName_whenFindByNativeSQL_thenReturnEmployeeObject() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();
        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going test
        Employee employeeFound = employeeRepository.findByNativeSQL(firstName, lastName);

        // then - verify the output
        assertThat(employeeFound).isNotNull();
        assertThat(employeeFound.getFirstName()).isEqualTo(firstName);
        assertThat(employeeFound.getLastName()).isEqualTo(lastName);
    }

    // JUnit test for custom finder method operation with native SQL and named parameters
    @DisplayName("JUnit test for custom finder method operation with native SQL and named parameters")
    @Test
    public void givenEmployeeName_whenFindByNativeSQLNamedParams_thenReturnEmployeeObject() {

        // given - precondition or setup
//        Employee employee = Employee.builder()
//                .firstName("John")
//                .lastName("Doe")
//                .email("johndoe@exmple.com")
//                .build();
        employeeRepository.save(employee);
        String firstName = "John";
        String lastName = "Doe";

        // when - action or the behaviour that we are going test
        Employee employeeFound = employeeRepository.findByNativeSQLNamedParams(firstName, lastName);

        // then - verify the output
        assertThat(employeeFound).isNotNull();
        assertThat(employeeFound.getFirstName()).isEqualTo(firstName);
        assertThat(employeeFound.getLastName()).isEqualTo(lastName);
    }
}
