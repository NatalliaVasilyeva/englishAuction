package main.java.by.epam.javaweb.vasilyevanatali.englishauction.dao.exception;

public class DaoException extends Exception {
    private static final long serialVersionUID = 569812478993L;


    public DaoException() {
        super();
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(Throwable cause) {
        super(cause);
    }

    protected DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
