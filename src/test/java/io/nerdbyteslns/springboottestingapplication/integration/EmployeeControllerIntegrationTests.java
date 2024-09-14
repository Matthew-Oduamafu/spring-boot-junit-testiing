package io.nerdbyteslns.springboottestingapplication.integration;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.nerdbyteslns.springboottestingapplication.model.Employee;
import io.nerdbyteslns.springboottestingapplication.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EmployeeControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private ObjectMapper objectMapper;

    private List<Employee> employees;

    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();

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
                        .build()
        );
    }

    @DisplayName("Junit 5 Test to create a new Employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andExpect(result -> {
            Employee savedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
            assert savedEmployee.getFirstName().equals(employee.getFirstName());
            assert savedEmployee.getLastName().equals(employee.getLastName());
            assert savedEmployee.getEmail().equals(employee.getEmail());
        }).andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Junit 5 Test to get all Employees")
    @Test
    public void givenEmployees_whenGetAllEmployees_thenReturnAllEmployees() throws Exception {
        // given - precondition or setup
        employeeRepository.saveAll(employees);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees"));

        // then - verify the output
        response.andExpect(result -> {
            Employee[] employees = objectMapper.readValue(result.getResponse().getContentAsString(), Employee[].class);
            assert employees.length == this.employees.size();
            assert employees[0].getFirstName().equals(this.employees.get(0).getFirstName());
            assert employees[0].getLastName().equals(this.employees.get(0).getLastName());
            assert employees[0].getEmail().equals(this.employees.get(0).getEmail());
        }).andDo(MockMvcResultHandlers.print());
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }


    @DisplayName("Junit 5 Test to get Employee by Id")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnEmployee() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee emp = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@example.com")
                .build();
        employeeId =  (employeeRepository.save(emp)).getId();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}" , employeeId));

        // then - verify the output
        response.andExpect(result -> {
            Employee employee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
            assert employee.getFirstName().equals("John");
            assert employee.getLastName().equals("Doe");
            assert employee.getEmail().equals("johndoe@example.com");
        }).andDo(MockMvcResultHandlers.print());
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Junit 5 Test to get Employee by Id - Not Found")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}" , employeeId));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isOk());
        response.andExpect(MockMvcResultMatchers.content().string(""));
    }


    @DisplayName("Junit 5 Test to update Employee by Id")
    @Test
    public void givenEmployeeIdAndEmployeeObject_whenUpdateEmployee_thenReturnUpdatedEmployee() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee emp = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();
        employeeId =  (employeeRepository.save(emp)).getId();
        emp.setFirstName("Jane");
        emp.setLastName("Doe");
        emp.setEmail("janedoe@gmail.com");
        employeeRepository.save(emp);

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(emp)));

        // then - verify the output
        response.andExpect(result -> {
            Employee updatedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
            assert updatedEmployee.getFirstName().equals("Jane");
            assert updatedEmployee.getLastName().equals("Doe");
            assert updatedEmployee.getEmail().equals("janedoe@gmail.com");
        }).andDo(MockMvcResultHandlers.print());
    }

    @DisplayName("Junit 5 Test to delete Employee by Id")
    @Test
    public void givenEmployeeId_whenDeleteEmployee_thenNoContent() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;
        Employee emp = Employee.builder()
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe@gmail.com")
                .build();
        employeeId =  (employeeRepository.save(emp)).getId();

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
