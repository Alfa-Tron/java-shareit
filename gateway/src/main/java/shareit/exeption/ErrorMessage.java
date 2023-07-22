package shareit.exeption;

import lombok.Data;

@Data
public class ErrorMessage {

    private String error;


    public ErrorMessage(String errorMessages) {
        error = errorMessages;
    }
}
