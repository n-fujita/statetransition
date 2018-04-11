package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent

import jp.co.biglobe.isp.oss.statetransition.domain.State
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState
import spock.lang.Specification
import spock.lang.Unroll

import static jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState.contracted
import static jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState.ordered

@Unroll
class StateCustomSelectorContainerSpec extends Specification {
    def "test"() {
        when:

        def sut = new StateCustomSelectorContainer(SampleStateType.contract, states as State[])
        def act = sut.getStateScript()

        then:
        act == expected

        where:
        states || expected
        [] || ""
        [contracted] || " AND (state = 'contracted')"
        [contracted, ordered] || " AND (state = 'contracted' OR state = 'ordered')"
    }
}
