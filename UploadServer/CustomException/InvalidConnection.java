package CustomException;

public class InvalidConnection extends Exception {
    public InvalidConnection() {
        super("This connection does not have a valid request.\nIt is neither HTTP nor come from a recognized host.");
    }
}
