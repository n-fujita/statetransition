package jp.co.biglobe.isp.oss.statetransition.sample.telephone;

import jp.co.biglobe.isp.oss.statetransition.domain.StateTransition;

public class TelephoneStateTransitionFactory {

    public static StateTransition<TelephoneContractState> create() {
        return new StateTransition.Builder<TelephoneContractState>()
                .entryStates(TelephoneContractState.contracted)
                .put(new StateTransition.Builder.Entry<>(TelephoneContractState.contracted).next(TelephoneContractState.end))
                .build();
    }
}
