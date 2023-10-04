package com.springboot.spingboottexting.service;


import com.springboot.spingboottexting.model.Employee;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public interface EmployeeService {

    Employee saveEmployee(Employee employee);

    List<Employee> getAllEmployees();
    @Cacheable(value = "employees", key = "#id")

    Optional<Employee> getEmployeeById(long id);
    @CachePut(value = "employees", key = "#result.id")

    Employee updateEmployee(Employee updatedEmployee);
    @CacheEvict(value = "employees", key = "#id")

    void deleteEmployee(long id);
}