package com.springboot.spingboottexting.service.impl;



import com.springboot.spingboottexting.exception.ResourceNotFoundException;
import com.springboot.spingboottexting.model.Employee;
import com.springboot.spingboottexting.repository.EmployeeRepository;
import com.springboot.spingboottexting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
@EnableCaching
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    @Autowired
    private RedisTemplate<String, Employee> redisTemplate;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return null;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return null;
    }

    @Override
    @Cacheable(value = "employees", key = "#id")
    public Optional<Employee> getEmployeeById(long id) {
        // Fetch employee from the database
        return employeeRepository.findById(id);
    }

    @Override
    @CachePut(value = "employees", key = "#result.id")
    public Employee updateEmployee(Employee updatedEmployee) {
        // Update the employee in the database
        return employeeRepository.save(updatedEmployee);
    }

    @Override
    @CacheEvict(value = "employees", key = "#id")
    public void deleteEmployee(long id) {
        // Delete the employee from the database
        employeeRepository.deleteById(id);
    }

    @Override
    public Employee cacheAndSaveEmployee(Employee employee) {
        // Implement caching logic for save operation
        redisTemplate.opsForValue().set("employee_" + employee.getId(), employee);
        return saveEmployee(employee); // Call the actual save operation
    }

    @Override
    public Employee cacheAndUpdateEmployee(Employee updatedEmployee) {
        // Implement caching logic for update operation
        redisTemplate.opsForValue().set("employee_" + updatedEmployee.getId(), updatedEmployee);
        return updateEmployee(updatedEmployee); // Call the actual update operation
    }

    @Override
    public void cacheAndDeleteEmployee(long id) {
        // Implement caching logic for delete operation
        redisTemplate.delete("employee_" + id);
        deleteEmployee(id); // Call the actual delete operation
    }

    // Other methods like saveEmployee and getAllEmployees go here
}
