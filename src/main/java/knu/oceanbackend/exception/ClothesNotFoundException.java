package knu.oceanbackend.exception;

public class ClothesNotFoundException extends RuntimeException {
    public ClothesNotFoundException(String message) {
        super(message);
    }
}