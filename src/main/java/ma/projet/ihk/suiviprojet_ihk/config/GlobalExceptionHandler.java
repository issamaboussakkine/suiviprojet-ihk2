package ma.projet.ihk.suiviprojet_ihk.config;

import ma.projet.ihk.suiviprojet_ihk.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(OrganismeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleOrganismeNotFoundException(OrganismeNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmployeNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEmployeNotFoundException(EmployeNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ProjetNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleProjetNotFoundException(ProjetNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(PhaseNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePhaseNotFoundException(PhaseNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AffectationNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleAffectationNotFoundException(AffectationNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LivrableNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleLivrableNotFoundException(LivrableNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DocumentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleDocumentNotFoundException(DocumentNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(FactureNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleFactureNotFoundException(FactureNotFoundException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DateInvalideException.class)
    public ResponseEntity<Map<String, Object>> handleDateInvalideException(DateInvalideException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MontantDepasseException.class)
    public ResponseEntity<Map<String, Object>> handleMontantDepasseException(MontantDepasseException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        return buildErrorResponse("Erreur interne du serveur", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, Object>> buildErrorResponse(String message, HttpStatus status) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", status.getReasonPhrase());
        errorResponse.put("message", message);
        return new ResponseEntity<>(errorResponse, status);
    }
}