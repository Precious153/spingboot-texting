package com.springboot.spingboottexting.service.impl;

import com.springboot.spingboottexting.exception.ResourceNotFoundException;
import com.springboot.spingboottexting.model.Employee;
import com.springboot.spingboottexting.repository.EmployeeRedisRepository;
import com.springboot.spingboottexting.repository.EmployeeRepository;
import com.springboot.spingboottexting.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeRepository employeeEntityRepository;
    private final EmployeeRedisRepository employeeRedisRepository;

    public static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeRepository employeeEntityRepository,
            EmployeeRedisRepository employeeRedisRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeEntityRepository = employeeEntityRepository;
        this.employeeRedisRepository = employeeRedisRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        Employee savedEmployee = employeeRepository.save(employee);
        String phoneNumber = savedEmployee.getPhoneNumber();
        String cacheKey = "employee_" + phoneNumber;
        saveEmployeeToCache(cacheKey, savedEmployee);

        return savedEmployee;
    }

    @Override
    public Employee getEmployeeById(String phoneNumber) {
        String cacheKey = "employee_" + phoneNumber;
        Employee cachedEmployee = getEmployeeFromCache(cacheKey);

        if (cachedEmployee != null) {
            return cachedEmployee;
        }
        Employee employee = employeeRepository.findByPhoneNumber(phoneNumber);

        if (employee != null) {
            saveEmployeeToCache(cacheKey, employee);
        } else {
            log.warn("Employee with phoneNumber {} not found.", phoneNumber);
            throw new ResourceNotFoundException("Employee", "phoneNumber", phoneNumber);

        }

        return employee;
    }

    @Override
    public List<Employee> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();

        if (employees.isEmpty()) {
            throw new ResourceNotFoundException("Employee", "employee", "No employees found.");
        }
        for (Employee employee : employees) {
            String cacheKey = "employee_" + employee.getPhoneNumber();
            saveEmployeeToCache(cacheKey, employee);
        }

        return employees;
    }

    @Override
    public Employee updateEmployee(Employee updatedEmployee) {
        String phoneNumber = updatedEmployee.getPhoneNumber();
        String cacheKey = "employee_" + phoneNumber;

        Employee existingEmployee = employeeRepository.findByPhoneNumber(phoneNumber);

        if (existingEmployee != null) {
            existingEmployee.setFirstName(updatedEmployee.getFirstName());
            existingEmployee.setLastName(updatedEmployee.getLastName());
            existingEmployee.setEmail(updatedEmployee.getEmail());
            employeeRepository.save(existingEmployee);
            saveEmployeeToCache(cacheKey, existingEmployee);

            return existingEmployee;
        } else {
            log.info("Employee with phoneNumber {} not found.", phoneNumber);
            throw new ResourceNotFoundException("Employee", "phoneNumber", phoneNumber);
        }
    }

    @Override
    public void deleteEmployee(String phoneNumber) {
        String cacheKey = "employee_" + phoneNumber;
        Employee existingEmployee = employeeRepository.findByPhoneNumber(phoneNumber);
        if (existingEmployee != null) {
            employeeRepository.delete(existingEmployee);
            deleteEmployeeFromCache(cacheKey);
        } else {
            log.info("Employee with phoneNumber {} not found.", phoneNumber);
            throw new ResourceNotFoundException("Employee", "phoneNumber", phoneNumber);
        }
    }
    @Override
    public  void saveEmployeeToCache(String key, Employee employee) {
        employeeRedisRepository.saveEmployee(key, employee);
        log.info("Saved employee to cache with key: " + key);
    }
    @Override

    public  Employee getEmployeeFromCache(String key) {
        Employee cachedEmployee = employeeRedisRepository.getEmployee(key);
        if (cachedEmployee != null) {
            log.info("Retrieved employee from cache with key: " + key);
        }
        return cachedEmployee;
    }
    @Override

    public  void deleteEmployeeFromCache(String key) {
        employeeRedisRepository.deleteEmployee(key);
        log.info("Deleted employee from cache with key: " + key);

    }
}
