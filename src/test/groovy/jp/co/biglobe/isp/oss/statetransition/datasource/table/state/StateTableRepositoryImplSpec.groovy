package jp.co.biglobe.isp.oss.statetransition.datasource.table.state

import jp.co.biglobe.isp.oss.statetransition.datasource.DbSpecCommon
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId
import jp.co.biglobe.isp.oss.statetransition.datasource.db.TestTableSetupMapper
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import spock.lang.Specification

import java.time.LocalDateTime

@SpringBootTest
@TestConfiguration
class StateTableRepositoryImplSpec extends DbSpecCommon {
    @Autowired
    StateTableRepository sut

    def "insertAndFind"() {
        when:
        def container = new UpsertStateContainer(
                new StateEventId("EVENT001"),
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                LocalDateTime.of(2018, 1, 1, 0, 0)
        )
        sut.upsertState(container)

        // 挿入したものが取れること
        def act = sut.find(new FindLatestContainer("ID001", SampleStateType.contract)).get()

        // タイプが違うから取れない
        def badType = sut.find(new FindLatestContainer("ID001", SampleStateType.telephone)).isPresent()
        // IDが違うから取れない
        def badId = sut.find(new FindLatestContainer("ID002", SampleStateType.contract)).isPresent()

        then:
        act.id == "ID001"
        act.eventId.value == "EVENT001"
        act.state == ContractState.ordered
        act.stateType == SampleStateType.contract
        act.stateEventDateTime.value == LocalDateTime.of(2017, 12, 30, 0, 0)
        act.eventDateTime == LocalDateTime.of(2018, 1, 1, 0, 0)

        !badType
        !badId
    }

    def "UpdateAndFind"() {
        when:
        def container1 = new UpsertStateContainer(
                new StateEventId("EVENT001"),
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                LocalDateTime.of(2018, 1, 1, 0, 0)
        )

        def container2 = new UpsertStateContainer(
                new StateEventId("EVENT002"),
                "ID001",
                SampleStateType.contract,
                ContractState.contracted,
                new StateEventDateTime(LocalDateTime.of(2018, 2, 1, 0, 0)),
                LocalDateTime.of(2018, 3, 1, 0, 0)
        )

        sut.upsertState(container1)
        sut.upsertState(container2)

        // 挿入したものが取れること
        def act = sut.find(new FindLatestContainer("ID001", SampleStateType.contract)).get()

        then:
        act.id == "ID001"
        act.eventId.value == "EVENT002"
        act.state == ContractState.contracted
        act.stateType == SampleStateType.contract
        act.stateEventDateTime.value == LocalDateTime.of(2018, 2, 1, 0, 0)
        act.eventDateTime == LocalDateTime.of(2018, 3, 1, 0, 0)
    }

    def "checkTableName"() {
        expect:
        stateTableName == Optional.of("state")
    }
}
