package application.model.models.exceptions;

public class MailDejaUtiliseException extends Exception {
    public MailDejaUtiliseException(String mail) {
        super("Le mail : "+mail+" est déjà utilisé.");
    }
}
