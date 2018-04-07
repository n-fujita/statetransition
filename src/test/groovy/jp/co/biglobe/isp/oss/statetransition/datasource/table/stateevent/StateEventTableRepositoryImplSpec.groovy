package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent

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
class StateEventTableRepositoryImplSpec extends Specification {
    @Autowired
    StateEventTableRepository sut

    @Value("\${statetransition.table.state_event_table_name:#{null}}") Optional<String> stateEventTableName

    @Autowired
    TestTableSetupMapper testTableSetupMapper

    def setup() {
        testTableSetupMapper.createStateTable(stateEventTableName.get())
    }

    def cleanup() {
        testTableSetupMapper.dropTable(stateEventTableName.get())
    }

    def "insertAndFind"() {
        when:
        def container = new InsertStateEventContainer(
                new StateEventId("EVENT001"),
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                LocalDateTime.of(2018, 1, 1, 0, 0)
        )
        sut.insertStateEvent(container)

        // 挿入したものが取れること
        def act = sut.findAllEvent(new FindLatestContainer("ID001", SampleStateType.contract))

        then:
        act.size() == 1
        act[0].id == "ID001"
        act[0].eventId.value == "EVENT001"
        act[0].state == ContractState.ordered
        act[0].stateType == SampleStateType.contract
        act[0].stateEventDateTime.value == LocalDateTime.of(2017, 12, 30, 0, 0)
        act[0].eventDateTime == LocalDateTime.of(2018, 1, 1, 0, 0)
    }

    def "insert2AndFind"() {
        when:
        def container = new InsertStateEventContainer(
                new StateEventId("EVENT001"),
                "ID001",
                SampleStateType.contract,
                ContractState.ordered,
                new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0)),
                LocalDateTime.of(2018, 1, 1, 0, 0)
        )
        def container2 = new InsertStateEventContainer(
                new StateEventId("EVENT002"),
                "ID001",
                SampleStateType.contract,
                ContractState.contracted,
                new StateEventDateTime(LocalDateTime.of(2018, 2, 1, 0, 0)),
                LocalDateTime.of(2018, 3, 1, 0, 0)
        )
        sut.insertStateEvent(container)
        sut.insertStateEvent(container2)

        // 挿入したものが取れること
        def act = sut.findAllEvent(new FindLatestContainer("ID001", SampleStateType.contract))

        then:
        act.size() == 2
        act[0].id == "ID001"
        act[0].eventId.value == "EVENT001"
        act[0].state == ContractState.ordered
        act[0].stateType == SampleStateType.contract
        act[0].stateEventDateTime.value == LocalDateTime.of(2017, 12, 30, 0, 0)
        act[0].eventDateTime == LocalDateTime.of(2018, 1, 1, 0, 0)

        act[1].id == "ID001"
        act[1].eventId.value == "EVENT002"
        act[1].state == ContractState.contracted
        act[1].stateType == SampleStateType.contract
        act[1].stateEventDateTime.value == LocalDateTime.of(2018, 2, 1, 0, 0)
        act[1].eventDateTime == LocalDateTime.of(2018, 3, 1, 0, 0)
    }

    def "checkTableName"() {
        expect:
        stateEventTableName == Optional.of("state_event")
    }
}
