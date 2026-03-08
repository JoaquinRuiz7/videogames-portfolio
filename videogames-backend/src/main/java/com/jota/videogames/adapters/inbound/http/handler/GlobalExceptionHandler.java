package com.jota.videogames.adapters.inbound.http.handler;

import com.jota.videogames.adapters.inbound.http.dto.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(
            ResponseStatusException ex) {
        log.error("Response status exception", ex);
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(new ErrorResponseDTO(
                        ex.getStatusCode().value(),
                        "Response status exception",
                        Instant.now()
                ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDTO> handleIllegalArgument(
            IllegalArgumentException ex) {
        log.error("Illegal argument exception", ex);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        "Illegal argument exception",
                        Instant.now()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex) {
        log.error("Internal server error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Unexpected error",
                        Instant.now()
                ));
    }
}