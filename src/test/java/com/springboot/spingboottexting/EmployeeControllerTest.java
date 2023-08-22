package com.springboot.spingboottexting;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.springboot.spingboottexting.controller.EmployeeController;
import com.springboot.spingboottexting.model.Employee;
import com.springboot.spingboottexting.service.EmployeeService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;


    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;



    @BeforeEach
    @Test
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(new EmployeeController(employeeService)).build();
    }


    @Test
    public void testCreateEmployee() throws Exception {
        Employee employee = new Employee("John", "Doe", "john@example.com");

        when(employeeService.saveEmployee(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"John\",\"lastName\":\"Doe\",\"email\":\"john@example.com\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(employeeService, times(1)).saveEmployee(any(Employee.class));
    }
    @Test
    public void testGetAllEmployees() throws Exception {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("John", "Doe", "john@example.com"));
        employees.add(new Employee("Jane", "Smith", "jane@example.com"));

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    public void testGetEmployeeById() throws Exception {
        Employee employee = new Employee("John", "Doe", "john@example.com");

        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(employee));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    public void testGetNonExistentEmployeeById() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        Employee existingEmployee = new Employee("John", "Doe", "john@example.com");
        Employee updatedEmployee = new Employee("Updated", "Employee", "updated@example.com");

        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.of(existingEmployee));
        when(employeeService.updateEmployee(any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated\",\"lastName\":\"Employee\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Employee"))
                .andExpect(jsonPath("$.email").value("updated@example.com"));

        verify(employeeService, times(1)).getEmployeeById(1L);
        verify(employeeService, times(1)).updateEmployee(any(Employee.class));
    }

    @Test
    public void testUpdateNonExistentEmployee() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Updated\",\"lastName\":\"Employee\",\"email\":\"updated@example.com\"}"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById(1L);
        verify(employeeService, times(0)).updateEmployee(any(Employee.class));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully!."));

        verify(employeeService, times(1)).deleteEmployee(1L);
    }
}
