package jp.co.biglobe.isp.oss.statetransition.domain.exception;

import jp.co.biglobe.isp.oss.statetransition.domain.State;

public class DenyChangeStateException extends RuntimeException {
    public DenyChangeStateException(State fromState, State toState) {
        super(String.format("許可されていない状態遷移: %s -> %s", fromState, toState));
    }
}
