package com.springboot.spingboottexting.repository;

import com.springboot.spingboottexting.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.stereotype.Repository;

import java.util.Optional;
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public Employee findByEmail(String email);
}
