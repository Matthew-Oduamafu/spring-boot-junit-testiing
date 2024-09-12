package io.nerdbyteslns.springboottestingapplication.service;

import io.nerdbyteslns.springboottestingapplication.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    Optional<Employee> getEmployeeById(Long id);
    Employee updateEmployee(Employee updatedEmployee);
    void deleteEmployee(Long id);
}
