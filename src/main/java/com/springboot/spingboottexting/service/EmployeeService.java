package com.springboot.spingboottexting.service;

import com.springboot.spingboottexting.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee createEmployee(Employee employee);

    Employee getEmployeeById(String id);

    List<Employee> getAllEmployees();

    Employee updateEmployee(Employee updatedEmployee);

    void deleteEmployee(String id);

    void saveEmployeeToCache(String key, Employee employee);

    Employee getEmployeeFromCache(String key);

    void deleteEmployeeFromCache(String key);
}
