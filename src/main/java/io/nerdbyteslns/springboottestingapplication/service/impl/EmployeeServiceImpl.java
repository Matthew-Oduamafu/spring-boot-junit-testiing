package io.nerdbyteslns.springboottestingapplication.service.impl;

import io.nerdbyteslns.springboottestingapplication.exception.ResourceNotFoundException;
import io.nerdbyteslns.springboottestingapplication.model.Employee;
import io.nerdbyteslns.springboottestingapplication.repository.EmployeeRepository;
import io.nerdbyteslns.springboottestingapplication.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        boolean savedEmployee = employeeRepository.findByEmail(employee.getEmail()).isPresent();
        if (savedEmployee) {
            throw new ResourceNotFoundException("Employee with email " + employee.getEmail() + " already exists");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        Optional<Employee> employee = employeeRepository.findById(updatedEmployee.getId());
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + updatedEmployee.getId() + " not found");
        }
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        if (employee.isEmpty()) {
            throw new ResourceNotFoundException("Employee with id " + id + " not found");
        }
        employeeRepository.deleteById(id);
    }
}
