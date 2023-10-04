package com.springboot.spingboottexting.service.impl;



import com.springboot.spingboottexting.exception.ResourceNotFoundException;
import com.springboot.spingboottexting.model.Employee;
import com.springboot.spingboottexting.repository.EmployeeRepository;
import com.springboot.spingboottexting.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        Optional<Employee> savedEmployee = Optional.ofNullable(employeeRepository.findByEmail(employee.getEmail()));
        if(savedEmployee.isPresent()){
            throw new ResourceNotFoundException("Employee", "id", employee);
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(long id) {
        return Optional.ofNullable(employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id)));
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    public void deleteEmployee(long id) {
        employeeRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Employee", "id", id));
        employeeRepository.deleteById(id);
    }
}