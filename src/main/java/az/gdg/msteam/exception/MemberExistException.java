package az.gdg.msteam.exception;

public class MemberExistException extends RuntimeException {
    public MemberExistException(String message) {
        super(message);
    }
}
