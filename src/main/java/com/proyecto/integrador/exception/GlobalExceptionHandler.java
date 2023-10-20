package com.proyecto.integrador.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController//TODO: VERIFICAR
public class GlobalExceptionHandler {


    @ExceptionHandler(InstrumentoNotFoundException.class)
    public ResponseEntity<String> handleInstrumentoNotFoundException(InstrumentoNotFoundException ex) {
        return new ResponseEntity<>("Instrumento no encontrado: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("Ocurrió un error: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}