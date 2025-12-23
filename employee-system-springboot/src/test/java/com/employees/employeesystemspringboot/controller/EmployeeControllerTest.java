package com.employees.employeesystemspringboot.controller;

import com.employees.employeesystemspringboot.dto.EmployeeDTO;
import com.employees.employeesystemspringboot.exception.DuplicateEmailException;
import com.employees.employeesystemspringboot.exception.EmployeeNotFoundException;
import com.employees.employeesystemspringboot.model.Employee;
import com.employees.employeesystemspringboot.model.EmploymentStatus;
import com.employees.employeesystemspringboot.model.ImportSummary;
import com.employees.employeesystemspringboot.model.Position;
import com.employees.employeesystemspringboot.service.EmployeeService;
import com.employees.employeesystemspringboot.service.ImportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class EmployeeControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private EmployeeService employeeService;

        @Autowired
        private ObjectMapper objectMapper;

        @TestConfiguration
        static class TestConfig {
                @Bean(name = "xmlEmployees")
                public List<Employee> xmlEmployees() {
                        return emptyList();
                }

                @Bean
                public ImportService importService() {
                        ImportService mock = Mockito.mock(ImportService.class);
                        ImportSummary summary = new ImportSummary(
                                        0, emptyList());
                        Mockito.when(mock.importFromCsv(any())).thenReturn(summary);
                        Mockito.when(mock.importFromApi(any())).thenReturn(summary);
                        return mock;
                }
        }

        @Test
        public void shouldReturnAllEmployees() throws Exception {
                Employee employee = new Employee("Jan", "Kowalski", "jan@test.com", "Firma X", Position.PROGRAMISTA, 8000);
                given(employeeService.getAllEmployees()).willReturn(List.of(employee));

                mockMvc.perform(get("/api/employees"))
                                .andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                                .andExpect(jsonPath("$[0].firstName").value("Jan"))
                                .andExpect(jsonPath("$[0].email").value("jan@test.com"));

                verify(employeeService).getAllEmployees();
        }

        @Test
        public void shouldReturnEmployeeByEmail() throws Exception {
                Employee employee = new Employee("Jan", "Kowalski", "jan@test.com", "Firma X", Position.PROGRAMISTA, 8000);
                given(employeeService.findByEmail("jan@test.com")).willReturn(employee);

                mockMvc.perform(get("/api/employees/jan@test.com"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.firstName").value("Jan"))
                                .andExpect(jsonPath("$.email").value("jan@test.com"));

                verify(employeeService).findByEmail(eq("jan@test.com"));
        }

        @Test
        public void shouldThrowWhenEmployeeEmailNotFound() throws Exception {
                given(employeeService.findByEmail("unknown@test.com"))
                                .willThrow(new EmployeeNotFoundException("Nie znaleziono pracownika"));

                mockMvc.perform(get("/api/employees/unknown@test.com"))
                                .andExpect(status().isNotFound());

                verify(employeeService).findByEmail(eq("jan@test.com"));
        }

        @Test
        public void shouldCreateEmployeeSuccessfully() throws Exception {
                EmployeeDTO dto = new EmployeeDTO("Jan", "Kowalski", "jan@test.com", "Firma X", Position.PROGRAMISTA, 8000,
                                EmploymentStatus.ACTIVE);

                mockMvc.perform(post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isCreated())
                                .andExpect(header().string("Location", "/api/employees/jan@test.com"))
                                .andExpect(jsonPath("$.email").value("jan@test.com"));

                verify(employeeService).addEmployee(any(Employee.class));
        }

        @Test
        public void shouldThrowOnCreatingDuplicateEmployee() throws Exception {
                EmployeeDTO dto = new EmployeeDTO("Jan", "Kowalski", "jan@test.com", "Firma X", Position.PROGRAMISTA, 8000,
                                EmploymentStatus.ACTIVE);
                doThrow(new DuplicateEmailException("Email zajęty")).when(employeeService)
                                .addEmployee(any(Employee.class));

                mockMvc.perform(post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dto)))
                                .andExpect(status().isConflict());

                verify(employeeService).addEmployee(any(Employee.class));
        }

        @Test
        public void shouldDeleteEmployeeSuccessfully() throws Exception {
                Employee employee = new Employee("Jan", "Kowalski", "jan@test.com", "Firma X", Position.PROGRAMISTA, 8000);
                given(employeeService.findByEmail("jan@test.com")).willReturn(employee);

                mockMvc.perform(delete("/api/employees/jan@test.com"))
                                .andExpect(status().isNoContent());

                verify(employeeService).removeByEmail(eq("jan@test.com"));
        }

        @Test
        public void shouldReturnEmployeesFromCompany() throws Exception {
                Employee employee = new Employee("Anna", "Nowak", "anna@test.com", "Firma X", Position.MANAGER, 12000);
                given(employeeService.findByCompany("Firma X")).willReturn(List.of(employee));

                mockMvc.perform(get("/api/employees").param("company", "Firma-X"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].companyName").value("Firma X"))
                                .andExpect(jsonPath("$[0].firstName").value("Anna"));

                verify(employeeService).findByCompany(eq("Firma X"));
        }

        @Test
        public void shouldUpdateEmployeeSuccessfully() throws Exception {
            EmployeeDTO dto = new EmployeeDTO(
                    "Jan", "Kowalski", "jan@test.com",
                    "Firma X", Position.PROGRAMISTA, 8000,
                    EmploymentStatus.ON_LEAVE
            );

            mockMvc.perform(put("/api/employees/jan@test.com")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(dto)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value("ON_LEAVE"));

            verify(employeeService).updateEmployee(eq("jan@test.com"), any(Employee.class));
        }
}
