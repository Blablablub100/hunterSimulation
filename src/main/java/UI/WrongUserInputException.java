package UI;

/**
 * Thrown if something about the user input is wrong.
 */
class WrongUserInputException extends Exception {

    /**
     * Saves the error message.
     */
    private String message;

    /**
     * Constructor for creating a new Exception.
     * @param variable variable that got the wrong value.
     * @param reason why this value is invalid.
     */
    WrongUserInputException(String variable, String reason) {
        message = "\n" + variable + " is invalid\n";
        message = message + "Reason: " + reason;
        message = message + "Please reenter simulation values\n";
    }

    /**
     * Converts this Exception to a String.
     * @return
     */
    public String toString() {
        /**
         * String value of the message.
         */
        return message;
    }
}
