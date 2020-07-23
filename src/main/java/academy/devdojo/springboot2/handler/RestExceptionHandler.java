package academy.devdojo.springboot2.handler;

import academy.devdojo.springboot2.exception.ResourceNotFoundDetails;
import academy.devdojo.springboot2.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundDetails> handleResourceNotFoundException(ResourceNotFoundException rosourceResourceNotFoundException){
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
}
