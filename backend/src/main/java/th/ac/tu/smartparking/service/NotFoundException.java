package th.ac.tu.smartparking.service;

/** Thrown by services when there is nothing to return; the controller turns it into 404. */
public class NotFoundException extends RuntimeException {

 public NotFoundException(String message) {
 super(message);
 }
}