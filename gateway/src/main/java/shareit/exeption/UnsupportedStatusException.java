package shareit.exeption;

public class UnsupportedStatusException extends RuntimeException {
    public UnsupportedStatusException() {
        super("Unknown state: UNSUPPORTED_STATUS");
    }
}