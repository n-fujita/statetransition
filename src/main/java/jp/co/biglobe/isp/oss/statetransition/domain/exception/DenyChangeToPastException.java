package jp.co.biglobe.isp.oss.statetransition.domain.exception;

import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime;

public class DenyChangeToPastException extends RuntimeException {
    public DenyChangeToPastException(StateEventDateTime from, StateEventDateTime to) {
        super(String.format("過去への状態遷移: %s -> %s", from.getValue(), to.getValue()));
    }
}
