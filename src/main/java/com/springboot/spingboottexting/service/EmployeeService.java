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

    Optional<Employee> getEmployeeById(long id);

    Employee updateEmployee(Employee updatedEmployee);

    void deleteEmployee(long id);

    // Custom methods for Redis caching
    Employee cacheAndSaveEmployee(Employee employee);

    Employee cacheAndUpdateEmployee(Employee updatedEmployee);

    void cacheAndDeleteEmployee(long id);
}