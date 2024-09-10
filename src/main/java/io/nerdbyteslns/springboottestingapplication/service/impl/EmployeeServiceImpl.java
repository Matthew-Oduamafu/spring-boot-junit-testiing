package io.nerdbyteslns.springboottestingapplication.service.impl;

import io.nerdbyteslns.springboottestingapplication.exception.ResourceNotFoundException;
import io.nerdbyteslns.springboottestingapplication.model.Employee;
import io.nerdbyteslns.springboottestingapplication.repository.EmployeeRepository;
import io.nerdbyteslns.springboottestingapplication.service.EmployeeService;
import org.springframework.stereotype.Service;


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
}
