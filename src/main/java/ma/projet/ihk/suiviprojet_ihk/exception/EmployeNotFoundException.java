package ma.projet.ihk.suiviprojet_ihk.exception;

public class EmployeNotFoundException extends RuntimeException {
    public EmployeNotFoundException(String message) {
        super(message);
    }
}