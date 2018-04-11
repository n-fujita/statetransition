package jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent

import groovy.transform.CompileStatic
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEvent
import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId
import jp.co.biglobe.isp.oss.statetransition.domain.State
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime
import jp.co.biglobe.isp.oss.statetransition.domain.StateType
import jp.co.biglobe.isp.oss.statetransition.sample.SampleStateType
import jp.co.biglobe.isp.oss.statetransition.sample.contract.ContractState

import java.time.LocalDateTime

@CompileStatic
class InsertStateEventContainerBuilder {
    StateEventId eventId = new StateEventId("EVENT001")
    String id = "ID001"
    StateType stateType = SampleStateType.contract
    State state = ContractState.ordered
    StateEventDateTime stateEventDateTime = new StateEventDateTime(LocalDateTime.of(2017, 12, 30, 0, 0))
    LocalDateTime eventDateTime = LocalDateTime.of(2018, 1, 1, 0, 0)

    InsertStateEventContainerBuilder eventId(StateEventId eventId) {
        this.eventId = eventId
        return this
    }

    InsertStateEventContainerBuilder id(String id) {
        this.id = id
        return this
    }

    InsertStateEventContainerBuilder stateType(StateType stateType) {
        this.stateType = stateType
        return this
    }

    InsertStateEventContainerBuilder state(State state) {
        this.state = state
        return this
    }

    InsertStateEventContainerBuilder stateEventDateTime(StateEventDateTime stateEventDateTime) {
        this.stateEventDateTime = stateEventDateTime
        return this
    }

    InsertStateEventContainerBuilder stateEventDateTime(int y, int m, int d = 1, int h = 0, int mi = 0, int s = 0) {
        this.stateEventDateTime = new StateEventDateTime(LocalDateTime.of(y, m, d, h, mi, s))
        return this
    }

    InsertStateEventContainerBuilder eventDateTime(LocalDateTime eventDateTime) {
        this.eventDateTime = eventDateTime
        return this
    }

    InsertStateEventContainerBuilder eventDateTime(int y, int m, int d = 1, int h = 0, int mi = 0, int s = 0) {
        this.eventDateTime = LocalDateTime.of(y, m, d, h, mi, s)
        return this
    }

    InsertStateEventContainer build() {
        return new InsertStateEventContainer(
                eventId,
                id,
                stateType,
                state,
                stateEventDateTime,
                eventDateTime
        )
    }

    StateEvent buildToStateEvent(boolean isLatest = false) {
        return new StateEvent(
                eventId,
                id,
                state,
                stateType,
                stateEventDateTime,
                eventDateTime,
                isLatest
        )
    }

    static InsertStateEventContainerBuilder 申込(String eventId = "EVENT001") {
        return new InsertStateEventContainerBuilder().eventId(new StateEventId(eventId)).state(ContractState.ordered)
    }

    static InsertStateEventContainerBuilder 契約(String eventId = "EVENT002") {
        return new InsertStateEventContainerBuilder().eventId(new StateEventId(eventId)).state(ContractState.contracted)
    }

    static InsertStateEventContainerBuilder 解約(String eventId = "EVENT003") {
        return new InsertStateEventContainerBuilder().eventId(new StateEventId(eventId)).state(ContractState.end)
    }
}
