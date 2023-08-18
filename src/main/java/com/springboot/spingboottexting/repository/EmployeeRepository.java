package com.springboot.spingboottexting.repository;

import com.springboot.spingboottexting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public Employee findByEmail(String email);
}
