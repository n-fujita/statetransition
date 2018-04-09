package jp.co.biglobe.isp.oss.statetransition.datasource

import jp.co.biglobe.isp.oss.statetransition.datasource.exception.ListLatestIsNotLastException
import jp.co.biglobe.isp.oss.statetransition.datasource.exception.ListSortException
import jp.co.biglobe.isp.oss.statetransition.domain.StateEventDateTime
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDateTime

import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.契約
import static jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainerBuilder.申込

@Unroll
class StateEventListSpec extends Specification {
    def "validate() #usecase エラーになること"() {
        when:
        def sut = new StateEventList([
                申込("EVENT001").buildToStateEvent(false),
                契約("EVENT002").stateEventDateTime(new StateEventDateTime(secondEventDateTime)).buildToStateEvent(isLatest)
        ])
        def act = sut.validate()

        then:
        act.isPresent()
        expected.isInstance(act.get())

        where:
        usecase                        | secondEventDateTime                | isLatest || expected
        "イベント日時順に並んでいない場合"  | LocalDateTime.of(2017, 1, 1, 0, 0) | true     || ListSortException
        "最終要素がisLatestでない場合 "   | LocalDateTime.of(2018, 3, 1, 0, 0) | false    || ListLatestIsNotLastException
    }

    def "validate() #usecase エラーにならないこと"() {
        when:
        def sut = new StateEventList(list)
        def act = sut.validate()

        then:
        !act.isPresent()

        where:
        usecase | list
        "リストが空の場合" | []
        "最終要素のみisLatestの場合" | [申込("EVENT001").buildToStateEvent(false), 契約("EVENT002").stateEventDateTime(2018, 3, 1).buildToStateEvent(true)]
    }
}
