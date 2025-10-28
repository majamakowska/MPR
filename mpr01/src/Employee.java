import java.util.Objects;

public class Employee {
    private String fullName;
    private String email;
    private String companyName;
    private Position position;
    private double salary;

    public Employee(String fullName, String email, String companyName, Position position, double salary) {
        this.fullName = fullName;
        this.email = email;
        this.companyName = companyName;
        this.position = position;
        this.salary = salary;
    }

    public Employee(String fullName, String email, String companyName, Position position) {
        this.fullName = fullName;
        this.email = email;
        this.companyName = companyName;
        this.position = position;
        this.salary = position.getBaseSalary();
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getCompanyName() {
        return companyName;
    }

    public Position getPosition() {
        return position;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || o.getClass() != Employee.class) return false;
        Employee employee = (Employee) o;
        return email.equals(employee.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return '(' + fullName + ", " + email + ", " + companyName + ", " + position + ", " + salary + ')';
    }
}