package jp.co.biglobe.isp.oss.statetransition.domain;

import java.util.function.Function;

public interface StateType {
    Class<? extends State> getStateClass();
    Function<String, State> getFactory();
    StateTransition getStateTransition();
    String getValue();
    String getLabel();

    default State convert(String value) {
        State result = getFactory().apply(value);
        if(!getStateClass().isInstance(result)) {
            throw new RuntimeException("state not match");
        }
        return result;
    }

    default boolean isValidTransition(State current, State next) {
        return getStateTransition().isValidTransition(current, next);
    }

    default boolean isEntry(State next) {
        return getStateTransition().isEntry(next);
    }
}
