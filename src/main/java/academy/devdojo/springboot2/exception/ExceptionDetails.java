package academy.devdojo.springboot2.exception;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@SuperBuilder
public class ExceptionDetails {
    private String title;
    private int status;
    private String detail;
    private LocalDateTime timestamp;
    private String developerMessage;
}
