package jp.co.biglobe.isp.oss.statetransition.domain;

import java.util.Optional;

/**
 * 状態遷移可能かどうかをチェックするロジック
 */
public class StateChangeLogic {
    public static Optional<RuntimeException> validate(
            Optional<StateAndStateEventDateTime> current,
            StateAndStateEventDateTime next
    ) {
        if(current.isPresent()) {// transition
            return current.get().validateNextState(next);
        } else {// エントリーチェック
            return next.validateEntryState();
        }
    }
}
