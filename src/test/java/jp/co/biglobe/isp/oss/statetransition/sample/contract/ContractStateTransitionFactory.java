package jp.co.biglobe.isp.oss.statetransition.sample.contract;

import jp.co.biglobe.isp.oss.statetransition.domain.StateTransition;

public class ContractStateTransitionFactory {

    public static StateTransition<ContractState> create() {
        return new StateTransition.Builder<ContractState>()
                .entryStates(ContractState.ordered)
                .put(new StateTransition.Builder.Entry<>(ContractState.ordered).next(ContractState.orderedCancel, ContractState.contracted))
                .put(new StateTransition.Builder.Entry<>(ContractState.contracted).next(ContractState.end))
                .build();
    }
}
