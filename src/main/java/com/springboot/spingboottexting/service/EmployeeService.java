package com.springboot.spingboottexting.service;


import com.springboot.spingboottexting.model.Employee;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<Employee> getAllEmployees();
    @Cacheable("employees")
    Optional<Employee> getEmployeeById(long id);
    @CachePut(value = "employees", key = "#employee.id")
    Employee updateEmployee(Employee updatedEmployee);
    @CacheEvict(value = "employees", key = "#id")
    void deleteEmployee(long id);
}