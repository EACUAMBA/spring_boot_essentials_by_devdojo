package academy.devdojo.springboot2.exception;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ValidationExceptionDetails extends ExceptionDetails{
    private String fields;/* The fields who have problems */
    private String fieldMessage; /* The message of the field */
}
