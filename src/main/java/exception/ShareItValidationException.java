package exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class ShareItValidationException extends RuntimeException {
    public ShareItValidationException(String message) {
        super(message);
    }

}
