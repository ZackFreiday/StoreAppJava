package model;

public class InsufficientQuantityException extends RuntimeException {
    public InsufficientQuantityException(String itemName, int requested, int available) {
        super("Insufficient quantity for item: " + itemName +
              ". Requested: " + requested + ", Available: " + available);
    }
}
