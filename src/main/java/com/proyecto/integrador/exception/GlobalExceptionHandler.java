package com.proyecto.integrador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController//TODO: VERIFICAR
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<String> handleException(CategoryNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsuarioNotFoundException.class)
    public ResponseEntity<String> handleException(UsuarioNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    //Errores inesperados INTERNAL_SERVER_ERROR
    @ExceptionHandler(CategoryUpdateException.class)
    public ResponseEntity<String> handleException(CategoryUpdateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CreateCharacteristicException.class)
    public ResponseEntity<String> handleException(CreateCharacteristicException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteCharacteristicException.class)
    public ResponseEntity<String> handleException(DeleteCharacteristicException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DeleteReserveException.class)
    public ResponseEntity<String> handleException(DeleteReserveException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(DuplicateCategoryException.class)
    public ResponseEntity<String> handleException(DuplicateCategoryException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DuplicateCharacteristicException.class)
    public ResponseEntity<String> handleException(DuplicateCharacteristicException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateInstrumentException.class)
    public ResponseEntity<String> handleException(DuplicateInstrumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }

    //Error inesperado INTERNAL_SERVER_ERROR
    @ExceptionHandler(EditCharacteristicException.class)
    public ResponseEntity<String> handleException(EditCharacteristicException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(GetReserveException.class)
    public ResponseEntity<String> handleException(GetReserveException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);//No tiene usos el error no supe que tipo de error era
    }

    //-----------------------------REVISAR----------------------------------
    //Veo 2 usos uno que es un 500 y otro que puede ser un 400
    @ExceptionHandler(ImageSaveException.class)
    public ResponseEntity<String> handleException(ImageSaveException e) {
        return new ResponseEntity<>(e.getMessage(), e.getHttpStatus()); //Entiendo que es bad request porque se usa cuando se le pasa un null como id y hay otro que es un error más general
    }

    //-----------------------------REVISAR----------------------------------
    //Veo 2 usos uno que es un 500 y otro que puede ser un 400
    @ExceptionHandler(InstrumentGetAllException.class)
    public ResponseEntity<String> handleException(InstrumentGetAllException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InstrumentGetNameException.class)
    public ResponseEntity<String> handleException(InstrumentGetNameException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ListReservesException.class)
    public ResponseEntity<String> handleException(ListReservesException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //400,404, 409
    @ExceptionHandler(NonExistentCategoryException.class)
    public ResponseEntity<String> handleException(NonExistentCategoryException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonExistentCharacteristicException.class)
    public ResponseEntity<String> handleExcpetion(NonExistentCharacteristicException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonExistentInstrumentException.class)
    public ResponseEntity<String> handleException(NonExistentInstrumentException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonExistentReserveException.class)
    public ResponseEntity<String> handleException(NonExistentReserveException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

}