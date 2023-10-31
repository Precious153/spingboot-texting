package com.springboot.spingboottexting.controller;

import com.springboot.spingboottexting.model.Employee;
import com.springboot.spingboottexting.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee createdEmployee = employeeService.createEmployee(employee);
        return new ResponseEntity<>(createdEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<Employee> getEmployee(@PathVariable String phoneNumber) {
        Employee employee = employeeService.getEmployeeById(phoneNumber);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("{phoneNumber}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable String phoneNumber, @RequestBody Employee updatedEmployee) {
        Employee employee = employeeService.updateEmployee(updatedEmployee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("{phoneNumber}")
    public ResponseEntity<String> deleteEmployee(@PathVariable String phoneNumber) {
        employeeService.deleteEmployee(phoneNumber);
        return new ResponseEntity<>("Employee with phone number " + phoneNumber + " has been deleted", HttpStatus.OK);
    }
}
