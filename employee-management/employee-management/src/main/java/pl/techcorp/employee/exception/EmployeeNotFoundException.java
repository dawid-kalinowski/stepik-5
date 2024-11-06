package pl.techcorp.employee.exception;

public class EmployeeNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public EmployeeNotFoundException(int id) {
        super("Pracownik o ID " + id + " nie istnieje");
    }
}
