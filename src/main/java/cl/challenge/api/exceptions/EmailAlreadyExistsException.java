package cl.challenge.api.exceptions;

public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException() {
        super("El correo ya registrado");
    }
}
