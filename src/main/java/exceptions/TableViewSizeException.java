package exceptions;

public class TableViewSizeException extends  Exception{
    public TableViewSizeException() {
        super("Services list must contain at least 1 element");
    }
}

