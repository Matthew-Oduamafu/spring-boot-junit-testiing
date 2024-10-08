package io.nerdbyteslns.springboottestingapplication.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.nerdbyteslns.springboottestingapplication.model.Employee;
import io.nerdbyteslns.springboottestingapplication.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;

    private List<Employee> employees;

    @BeforeEach
    void setUp() {
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


    @DisplayName("Junit 5 Test to create a new Employee")
    @Test
    public void givenEmployeeObject_whenCreateEmployee_thenReturnSavedEmployee() throws Exception {
        // given - precondition or setup
        Employee employee = Employee.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
        BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post("/api/employees")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(employee)));

        // then - verify the output
        response.andExpect(result -> {
            Employee savedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
//            assert savedEmployee.getId() != 0;
            assert savedEmployee.getFirstName().equals(employee.getFirstName());
            assert savedEmployee.getLastName().equals(employee.getLastName());
            assert savedEmployee.getEmail().equals(employee.getEmail());
        }).andDo(MockMvcResultHandlers.print());
//        response.andExpect(MockMvcResultMatchers.status().isCreated())
//                .andExpect(MockMvcResultMatchers.jsonPath("id").exists())
//                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName", CoreMatchers.is(employee.getFirstName())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName", CoreMatchers.is(employee.getLastName())))
//                .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(employee.getEmail())))
//                .andDo(MockMvcResultHandlers.print());
    }


    @DisplayName("Junit 5 Test to get all Employees")
    @Test
    public void givenEmployees_whenGetAllEmployees_thenReturnAllEmployees() throws Exception {
        // given - precondition or setup


        BDDMockito.given(employeeService.getAllEmployees())
                .willReturn(employees);

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
                .id(employeeId)
                .firstName("John")
                .lastName("Doe")
                .email("johndoe#example.com")
                .build();

        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(java.util.Optional.of(emp));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(result -> {
            Employee employee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
            assert employee.getId() == 1L;
            assert employee.getFirstName().equals("John");
            assert employee.getLastName().equals("Doe");
            assert employee.getEmail().equals("johndoe#example.com");
        }).andDo(MockMvcResultHandlers.print());
        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Junit 5 Test to get Employee by Id - Not Found")
    @Test
    public void givenEmployeeId_whenGetEmployeeById_thenReturnNotFound() throws Exception {
        // given - precondition or setup
        long employeeId = 1L;

        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(Optional.empty());

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get("/api/employees/{id}", employeeId));

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
        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(java.util.Optional.of(emp));
        emp.setFirstName("Jane");
        emp.setLastName("Doe");
        emp.setEmail("janedoe@gmail.com");
        BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(Employee.class)))
                .willAnswer((invocation) -> invocation.getArgument(0));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put("/api/employees/{id}", employeeId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(emp)));

        // then - verify the output
        response.andExpect(result -> {
            Employee updatedEmployee = objectMapper.readValue(result.getResponse().getContentAsString(), Employee.class);
            assert updatedEmployee.getId() == 1L;
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
        BDDMockito.given(employeeService.getEmployeeById(employeeId))
                .willReturn(java.util.Optional.of(emp));

        // when - action or the behaviour that we are going test
        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete("/api/employees/{id}", employeeId));

        // then - verify the output
        response.andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
