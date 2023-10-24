package com.springboot.spingboottexting.service.impl;

import com.springboot.spingboottexting.exception.ResourceNotFoundException;
import com.springboot.spingboottexting.model.Employee;
import com.springboot.spingboottexting.repository.EmployeeRedisRepository;
import com.springboot.spingboottexting.repository.EmployeeRepository;
import com.springboot.spingboottexting.service.EmployeeService;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeRedisRepository employeeRedisRepository;
    private final RedissonClient redissonClient;

    public static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    public EmployeeServiceImpl(
            EmployeeRepository employeeRepository,
            EmployeeRedisRepository employeeRedisRepository,
            RedissonClient redissonClient
    ) {
        this.employeeRepository = employeeRepository;
        this.employeeRedisRepository = employeeRedisRepository;
        this.redissonClient = redissonClient;
    }


@Override
public Employee createEmployee(Employee employee) {
    Employee savedEmployee = employeeRepository.save(employee);
    saveEmployeeToCache("employee_" + savedEmployee.getId(), savedEmployee);
    if (savedEmployee == null) {
        throw new ResourceNotFoundException("Employee", "id", employee.getId());
    }
    return savedEmployee;
}


@Override
public Employee getEmployeeById(String id) {
    Employee cachedEmployee = getEmployeeFromCache(id);
    if (cachedEmployee != null) {
        return cachedEmployee;
    }
    Long employeeId = Long.valueOf(id);
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new ResourceNotFoundException("Employee", "id", id));
    saveEmployeeToCache(id, employee);
    return employee;
}

@Override
public List<Employee> getAllEmployees() {
    List<Employee> employees = employeeRepository.findAll();
    if (employees.isEmpty()) {
        throw new ResourceNotFoundException("Employee", "employee", "No employees found.");
    }

    for (Employee employee : employees) {
        employeeRedisRepository.updateEmployeeInCache("employee_" + employee.getId(), employee);
    }

    return employees;
}

@Override
public Employee updateEmployee(Employee updatedEmployee) {
    Long employeeId = updatedEmployee.getId();
    Employee existingEmployee = getEmployeeById(String.valueOf(employeeId));

    if (existingEmployee != null) {
        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());

        employeeRepository.save(existingEmployee);
        employeeRedisRepository.updateEmployeeInCache("employee_" + existingEmployee.getId(), existingEmployee);

        return existingEmployee;
    } else {
        throw new ResourceNotFoundException("Employee", "id", String.valueOf(employeeId));
    }
}
@Override
public void deleteEmployee(String id) {
    Long employeeId = Long.valueOf(id);
    if (employeeRepository.existsById(employeeId)) {
        employeeRepository.deleteById(employeeId);
        deleteEmployeeFromCache(id);
    } else {
        throw new ResourceNotFoundException("Employee", "id", id);
    }
}


    @Override
    public void saveEmployeeToCache(String key, Employee employee) {
        employeeRedisRepository.saveEmployee(key, employee);
        log.info("Saved employee to cache with key: " + key);
    }

    @Override
    public Employee getEmployeeFromCache(String key) {
        Employee cachedEmployee = employeeRedisRepository.getEmployee(key);
        if (cachedEmployee != null) {
            log.info("Retrieved employee from cache with key: " + key);
        }
        return cachedEmployee;
    }

    @Override
    public void deleteEmployeeFromCache(String key) {
        employeeRedisRepository.deleteEmployee(key);
        log.info("Deleted employee from cache with key: " + key);
    }

}

