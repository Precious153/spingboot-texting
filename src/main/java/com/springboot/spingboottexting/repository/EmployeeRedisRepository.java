package com.springboot.spingboottexting.repository;

import com.springboot.spingboottexting.model.Employee;

import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class EmployeeRedisRepository {
    private final RedissonClient redissonClient;

    public EmployeeRedisRepository(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public void saveEmployee(String key, Employee employee) {
        RMap<String, Employee> map = redissonClient.getMap("employeeCache");
        map.put(key, employee);
    }

    public Employee getEmployee(String key) {
        RMap<String, Employee> map = redissonClient.getMap("employeeCache");
        return map.get(key);
    }

    public void deleteEmployee(String key) {
        RMap<String, Employee> map = redissonClient.getMap("employeeCache");
        map.remove(key);
    }
}
