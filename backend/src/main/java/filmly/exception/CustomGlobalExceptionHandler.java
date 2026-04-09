package filmly.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(this::getErrorMessage)
                .collect(Collectors.toList());

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                errors,
                "Validation failed"
        );

        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = {HttpClientErrorException.class})
    protected ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException ex)
            throws JsonProcessingException {
        String tmdbMessage = new ObjectMapper()
                .readTree(ex.getResponseBodyAsString())
                .get("status_message")
                .asText();

        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                List.of(tmdbMessage),
                tmdbMessage
        );

        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    @ExceptionHandler(value = {RegistrationException.class})
    protected ResponseEntity<ErrorResponse> handleRegistration(RuntimeException ex) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                List.of(ex.getMessage()),
                ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleEntityNotFound(RuntimeException ex) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                List.of(ex.getMessage()),
                ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {AuthenticationException.class})
    protected ResponseEntity<ErrorResponse> handleAuthorization(RuntimeException ex) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.UNAUTHORIZED.value(),
                List.of(ex.getMessage()),
                ex.getMessage()
        );
        return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
    }

    private String getErrorMessage(ObjectError objectError) {
        if (objectError instanceof FieldError) {
            String fieldName = ((FieldError) objectError).getField();
            String errorMessage = objectError.getDefaultMessage();
            return fieldName + " " + errorMessage;
        }
        return objectError.getDefaultMessage();
    }
}
