package com.proyecto.integrador.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleException(UserNotFoundException e) {
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

    @ExceptionHandler(DuplicateReviewException.class)
    public ResponseEntity<String> handleException(DuplicateReviewException e){
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
    @ExceptionHandler(InstrumentGetFavouritesByEmailException.class)
    public ResponseEntity<String> handleException(InstrumentGetFavouritesByEmailException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<String> handleExceptio(UserValidationException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<String> handleException(BadCredentialsException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CreateBookingException.class)
    public ResponseEntity<String> handleException(CreateBookingException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(EditBookingException.class)
    public ResponseEntity<String> handleException(EditBookingException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(CategoryByNameException.class)
    public ResponseEntity<String> handleException(CategoryByNameException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InstrumentAddFavouriteException.class)
    public ResponseEntity<String> handleException(InstrumentAddFavouriteException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InstrumentRemoveFavouriteException.class)
    public ResponseEntity<String> handleException(InstrumentRemoveFavouriteException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CategoryByIdException.class)
    public ResponseEntity<String> handleException(CategoryByIdException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(UserFindAllException.class)
    public ResponseEntity<String> handleException(UserFindAllException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler (UserFindAllAdminUsersException.class)
    public ResponseEntity<String> handleException(UserFindAllAdminUsersException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler (UserFindAllNormalUsersException.class)
    public ResponseEntity<String> handleException(UserFindAllNormalUsersException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler (DeleteUserByIdException.class)
    public ResponseEntity<String> handleException(DeleteUserByIdException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler (UpdateUserByIdException.class)
    public ResponseEntity<String> handleException(UpdateUserByIdException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler (FindUsersByRoleException.class)
    public ResponseEntity<String> handleException(FindUsersByRoleException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler (UserGetRoleByEmailException.class)
    public ResponseEntity<String> handleException(UserGetRoleByEmailException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UserRegisterException.class)
    public ResponseEntity<String> handleException(UserRegisterException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler (UserRegisterByEmailException.class)
    public ResponseEntity<String> handleException(UserRegisterByEmailException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
    }
    @ExceptionHandler (UserGetNameByEmailException.class)
    public ResponseEntity<String> handleException(UserGetNameByEmailException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UserGetLastNameByEmailException.class)
    public ResponseEntity<String> handleException(UserGetLastNameByEmailException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UpdateUserRoleException.class)
    public ResponseEntity<String> handleException(UpdateUserRoleException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (BookingNotFoundException.class)
    public ResponseEntity<String> handleException(BookingNotFoundException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler (DeleteInstrumentException.class)
    public ResponseEntity<String> handleException(DeleteInstrumentException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (InstrumentGetFavouriteException.class)
    public ResponseEntity<String> handleException(InstrumentGetFavouriteException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (InstrumentImageCreationException.class)
    public ResponseEntity<String> handleException(InstrumentImageCreationException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (InstrumentUpdateAvgScoreException.class)
    public ResponseEntity<String> handleException(InstrumentUpdateAvgScoreException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler (UserFindByEmailException.class)
    public ResponseEntity<String> handleException(UserFindByEmailException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler (UserFindByIdException.class)
    public ResponseEntity<String> handleException(UserFindByIdException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


}