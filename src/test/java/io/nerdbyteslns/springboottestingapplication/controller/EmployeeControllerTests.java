package io.nerdbyteslns.springboottestingapplication.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.nerdbyteslns.springboottestingapplication.model.Employee;
import io.nerdbyteslns.springboottestingapplication.service.EmployeeService;
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

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private EmployeeService employeeService;
    @Autowired
    private ObjectMapper objectMapper;


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
}
