package javacode.UI;

public class WrongUserInputException extends Exception {

    private String message;

    public WrongUserInputException(String variable, String reason) {
        message = variable + " is invalid\n";
        message = message + "Reason: " + reason;
    }

    public String toString() {
        return message;
    }
}
