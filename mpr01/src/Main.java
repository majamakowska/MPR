public class Main {
    public static void main(String[] args) {
        EmployeeManager manager = new EmployeeManager();

        manager.addEmployee(new Employee("Tomasz Problem", "tomaszproblem87@klimawent.pl", "Klimawent", Position.MANAGER));
        manager.addEmployee(new Employee("Jessica Kiełbasa", "goldenlabubu@google.com", "Google", Position.PROGRAMISTA, 9000));
        manager.addEmployee(new Employee("Daniel Silva", "danielsilva1904@google.com", "Google", Position.MANAGER, 30000));
        manager.addEmployee(new Employee("Janusz Kowalski", "janusz.kowalski@klimawent.pl", "Klimawent", Position.PREZES, 25000));

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