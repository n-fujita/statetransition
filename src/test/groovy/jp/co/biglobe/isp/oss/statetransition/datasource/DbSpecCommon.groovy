package jp.co.biglobe.isp.oss.statetransition.datasource

import jp.co.biglobe.isp.oss.statetransition.datasource.db.TestTableSetupMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import spock.lang.Specification

@SpringBootTest
@TestConfiguration
class DbSpecCommon extends Specification {
    @Value("\${statetransition.table.state_table_name:#{null}}")
    Optional<String> stateTableName

    @Value("\${statetransition.table.state_event_table_name:#{null}}")
    Optional<String> stateEventTableName

    @Autowired
    TestTableSetupMapper testTableSetupMapper

    def setup() {
        [stateTableName, stateEventTableName].forEach({testTableSetupMapper.createStateTable(it.get())})

    }

    def cleanup() {
        [stateTableName, stateEventTableName].forEach({testTableSetupMapper.dropTable(it.get())})
    }
}
