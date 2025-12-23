package com.employees.employeesystemspringboot.controller;

import com.employees.employeesystemspringboot.model.*;
import com.employees.employeesystemspringboot.service.EmployeeService;
import com.employees.employeesystemspringboot.service.ImportService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.context.TestConfiguration;

import java.util.List;
import java.util.Map;

import static java.util.Collections.emptyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StatisticsController.class)
@TestPropertySource(properties = "spring.main.allow-bean-definition-overriding=true")
public class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

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
    public void shouldGetAverageSalaryGlobal() throws Exception {
        given(employeeService.getAverageSalaries()).willReturn(Map.of("Firma X", 5000.0));

        mockMvc.perform(get("/api/statistics/salary/average"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Firma X']").value(5000.0));
    }

    @Test
    public void shouldGetAverageSalaryByCompany() throws Exception {
        given(employeeService.getAverageSalaryByCompanyName("Firma X")).willReturn(5000.0);

        mockMvc.perform(get("/api/statistics/salary/average").param("company", "Firma X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avarageSalary").value(5000.0));
    }

    @Test
    public void shouldGetCompanyStatistics() throws Exception {
        CompanyStatistics stats = new CompanyStatistics("Firma X", 10, 5000.0, 10000.0, "Jan Kowalski");
        given(employeeService.getCompanyStatisticsByCompanyName("Firma X")).willReturn(stats);

        mockMvc.perform(get("/api/statistics/company/Firma-X"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Firma X"))
                .andExpect(jsonPath("$.employeeCount").value(10))
                .andExpect(jsonPath("$.averageSalary").value(5000.0))
                .andExpect(jsonPath("$.highestSalary").value(10000.0))
                .andExpect(jsonPath("$.topEarnerFullName").value("Jan Kowalski"));

        verify(employeeService).getCompanyStatisticsByCompanyName("Firma X");
    }

    @Test
    public void shouldCountByPosition() throws Exception {
        given(employeeService.countByPosition()).willReturn(Map.of(Position.PROGRAMISTA, 5L));

        mockMvc.perform(get("/api/statistics/positions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.PROGRAMISTA").value(5));
    }

    @Test
    public void shouldCountByStatus() throws Exception {
        given(employeeService.countByStatus()).willReturn(Map.of(EmploymentStatus.ACTIVE, 10L));

        mockMvc.perform(get("/api/statistics/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ACTIVE").value(10));
    }
}
