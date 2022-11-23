package exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice
public class ErrorHandler {



    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleEntityNotFoundException(final EntityNotFoundException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(
                "error", e.getMessage()
        );
    }

    @ExceptionHandler(ShareItValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleIncorrectValidation(final ShareItValidationException e) {
        log.debug(e.getMessage());
        return new ErrorResponse(
                "error", e.getMessage()
        );
    }

}