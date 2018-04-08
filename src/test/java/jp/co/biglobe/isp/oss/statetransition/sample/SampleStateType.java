package jp.co.biglobe.isp.oss.statetransition.sample;


import jp.co.biglobe.isp.oss.statetransition.domain.State;
import jp.co.biglobe.isp.oss.statetransition.domain.StateTransition;
import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState;
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractStateTransitionFactory;
import jp.co.biglobe.isp.oss.statetransition.sample.telephone.TelephoneContractState;
import jp.co.biglobe.isp.oss.statetransition.sample.telephone.TelephoneStateTransitionFactory;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@AllArgsConstructor
public enum SampleStateType implements StateType {
    contract(
            "契約",
            ContractState.class,
            ContractState::create,
            ContractStateTransitionFactory.create()
    ),
    telephone(
            "電話",
            TelephoneContractState.class,
            TelephoneContractState::create,
            TelephoneStateTransitionFactory.create()
    ),
    ;
    @Getter
    private final String label;
    @Getter
    private final Class<? extends State> stateClass;
    @Getter
    private final Function<String, State> factory;
    @Getter
    private final StateTransition stateTransition;

    @Override
    public String getValue() {
        return name();
    }
}
