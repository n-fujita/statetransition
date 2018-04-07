package jp.co.biglobe.isp.oss.statetransition.domain.exception;

import jp.co.biglobe.isp.oss.statetransition.domain.State;

public class DenyEntryStateException extends RuntimeException {
    public DenyEntryStateException(State state) {
        super(String.format("エントリーとして許可されていない状態です: %s", state));
    }
}
