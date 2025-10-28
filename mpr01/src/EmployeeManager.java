import java.util.*;
import java.util.stream.Collectors;

public class EmployeeManager {
    private final Set<Employee> employees = new HashSet<>();

    public void addEmployee(Employee employee) {
        if (!employees.add(employee)) {
            throw new IllegalArgumentException("Email powtarza się. Nie można dodać pracownika: " + employee);
        }
    }

    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employees);
    }

    public List<Employee> findByCompany(String company) {
        return employees.stream()
                .filter(e -> e.getCompanyName().equalsIgnoreCase(company)).toList();
    }

    public List<Employee> sortByLastName() {
        return employees.stream()
                .sorted(Comparator.comparing(e -> e.getFullName().split(" ")[1])).toList();
    }

    public Map<Position, List<Employee>> groupByPosition() {
        return employees.stream().collect(Collectors.groupingBy(Employee::getPosition));
    }

    public Map<Position, Long> countByPosition() {
        return employees.stream().collect(Collectors.groupingBy(Employee::getPosition, Collectors.counting()));
    }

    public double averageSalary() {
        return employees.stream().mapToDouble(Employee::getSalary).average().orElse(0);
    }

    public Optional<Employee> highestSalary() {
        return employees.stream().max(Comparator.comparingDouble(Employee::getSalary));
    }
}