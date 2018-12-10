package javacode.UI;

public class WrongUserInputException extends Exception {

    private String message;

    public WrongUserInputException(String variable, String reason) {
        message = variable + " is invalid\n";
        message = message + "Reason: " + reason;
        message = message + "Please enter values again:\n";
    }

    public String toString() {
        return message;
    }
}
