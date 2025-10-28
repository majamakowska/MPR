import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EmployeeManager manager = new EmployeeManager();

        Object[][] employeeData = {
                {"Tomasz Problem", "tomasz@gmail.com", "Klimawent", Position.MANAGER, null},
                {"Tomasz Zapasowy", "tomasz@gmail.com", "Allegro", Position.PROGRAMISTA, null},
                {"Jessica Kiełbasa", "goldenlabubu@google.com", "Google", Position.PROGRAMISTA, 9000.0},
                {"Daniel Silva", "danielsilva1904@riotgames.com", "Riot Games", Position.MANAGER, 30000.0},
                {"Janusz Kowalski", "janusz.kowalski@klimawent.pl", "Klimawent", Position.PREZES, 25000.0},
                {"Jan Biedak", "jan.biednak@wp.pl", "Klimawent", Position.PROGRAMISTA, -21.37}
        };

        List<String> errors = new ArrayList<>();

        for (Object[] data : employeeData) {
            try {
                Employee e;
                if (data[4] == null) {
                    e = new Employee((String)data[0], (String)data[1], (String)data[2], (Position) data[3]);
                } else {
                    e = new Employee((String)data[0], (String)data[1], (String)data[2], (Position)data[3], (Double)data[4]);
                }
                manager.addEmployee(e);
            } catch (Exception ex) {
                errors.add(ex.getMessage());
            }
        }

        if (!errors.isEmpty()) {
            System.out.println("***");
            errors.forEach(System.out::println);
            System.out.println("***");
        }

        System.out.println("\nWszyscy pracownicy:");
        manager.getAllEmployees().forEach(System.out::println);

        System.out.println("\nPracownicy w firmie Klimawent:");
        manager.findByCompany("Klimawent").forEach(System.out::println);

        System.out.println("\nPracownicy posortowani alfabetycznie:");
        manager.sortByLastName().forEach(System.out::println);

        System.out.println("\nPracownicy według stanowiska:");
        manager.groupByPosition().forEach((position, list) -> System.out.println(position + ": " + list));

        System.out.println("\nLiczba pracowników na stanowiskach:");
        manager.countByPosition().forEach((position, count) -> System.out.println(position + ": " + count));

        System.out.println("\nŚrednie wynagrodzenie:");
        System.out.println(manager.averageSalary());

        System.out.println("\nPracownik z najwyższym wynagrodzeniem:");
        manager.highestSalary().ifPresent(System.out::println);
    }
}