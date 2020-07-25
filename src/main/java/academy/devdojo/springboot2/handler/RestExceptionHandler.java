package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.ExceptionDetails;
import academy.devdojo.springboot2.exception.ResourceNotFoundDetails;
import academy.devdojo.springboot2.exception.ResourceNotFoundException;
import academy.devdojo.springboot2.exception.ValidationExceptionDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDetails> handleResourceNotFoundException(ResourceNotFoundException rosourceResourceNotFoundException) {
        return new ResponseEntity(
                ResourceNotFoundDetails.builder()
                        .title("Not Found Exception")
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.NOT_FOUND.value())
                        .detail(rosourceResourceNotFoundException.getMessage())
                        .developerMessage(rosourceResourceNotFoundException.getClass().getName())
                        .build(), HttpStatus.NOT_FOUND
        );
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        String fields = fieldErrors.stream().map(p -> p.getField()).collect(Collectors.joining(", "));
        String fieldsMessage = fieldErrors.stream().map(p -> p.getDefaultMessage()).collect(Collectors.joining(", "));

        return new ResponseEntity(
                ValidationExceptionDetails.builder()
                        .title("Wrong values on fields")
                        .timestamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .detail("See the fields below!")
                        .developerMessage(ex.getClass().getName())
                        .fieldMessage(fieldsMessage)
                        .fields(fields)
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

        body = new ResponseEntity(ExceptionDetails.builder()
                .title(ex.getCause().getMessage())
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .detail(ex.getMessage())
                .developerMessage(ex.getClass().getName())
                .build(), status
        );

        return new ResponseEntity<>(body, headers, status);
    }
}
