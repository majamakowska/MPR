public enum Position {
    PREZES(25000),
    WICEPREZES(18000),
    MANAGER(12000),
    PROGRAMISTA(8000),
    STAZYSTA(3000);

    private final double baseSalary;

    Position(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getBaseSalary() {
        return baseSalary;
    }
}