package jp.co.biglobe.isp.oss.statetransition.datasource.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
