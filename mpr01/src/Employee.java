import java.util.Objects;

public class Employee {
    private String fullName;
    private String email;
    private String companyName;
    private Position position;
    private double salary;

    public Employee(String fullName, String email, String companyName, Position position, double salary) {
        if (salary < 0) {
            throw new IllegalArgumentException("Pensja nie może być ujemna. "
                    + cannotAddMessage(fullName, email, companyName, position, salary));
        }
        try {
            this.fullName = validateStringData(fullName, "'imię i nazwisko'");
            this.email = validateStringData(email, "'email'");
            this.companyName = validateStringData(companyName, "'nazwa firmy'");
            this.position = ensureNotNullPosition(position);
        } catch(IllegalArgumentException e) {
            throw new IllegalArgumentException(e.getMessage()
                    + cannotAddMessage(fullName, email, companyName, position, salary));
        }
        this.salary = salary;
    }

    public Employee(String fullName, String email, String companyName, Position position) {
        this(fullName, email, companyName, position,
                (position == null ? 0 : position.getBaseSalary()));
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

    public static String toString(String fullName, String email, String companyName, Position position, double salary) {
        return '(' + fullName + ", " + email + ", " + companyName + ", " + position + ", " + salary + ')';
    }

    private String validateStringData(String toValidate, String stringName) {
        stringName = (stringName == null) ? "" : stringName.trim().toLowerCase() + " ";
        if (toValidate == null) {
            throw new IllegalArgumentException("Wartość " + stringName + "nie może być null. ");
        }
        String validated = toValidate.trim();
        if (validated.isEmpty()) {
            throw new IllegalArgumentException("Pole " + stringName + "nie może być puste. ");
        }
        return validated;
    }

    private static Position ensureNotNullPosition(Position position) {
        if (position == null) {
            throw new IllegalArgumentException("Stanowisko nie może być null. ");
        }
        return position;
    }

    private String cannotAddMessage(String fullName, String email, String companyName, Position position, double salary) {
        return "Nie można dodać pracownika: " + Employee.toString(fullName, email, companyName, position, salary);
    }
}