package jp.co.biglobe.isp.oss.statetransition.domain;

import jp.co.biglobe.isp.oss.statetransition.domain.exception.DenyChangeStateException;
import jp.co.biglobe.isp.oss.statetransition.domain.exception.DenyChangeToPastException;
import jp.co.biglobe.isp.oss.statetransition.domain.exception.DenyEntryStateException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
public class StateAndStateEventDateTime {
    @Getter
    private final StateType stateType;

    private final State state;
    public <T extends State> T getState() {
        return (T)state;
    }

    @Getter
    private final StateEventDateTime stateEventDateTime;

    public Optional<RuntimeException> validateNextState(StateAndStateEventDateTime next) {
        // 状態遷移可否チェック
        if(!stateType.isValidTransition(state, next.state)) {
            return Optional.of(new DenyChangeStateException(state, next.state));
        }
        // 過去への遷移でないかチェック
        if(this.stateEventDateTime.isAfter(next.stateEventDateTime)) {
            return Optional.of(new DenyChangeToPastException(stateEventDateTime, next.stateEventDateTime));
        }
        return Optional.empty();
    }

    public Optional<RuntimeException> validateEntryState() {
        if(!stateType.isEntry(state)) {
            return Optional.of(new DenyEntryStateException(state));
        }
        return Optional.empty();
    }

}
