package jp.co.biglobe.isp.oss.statetransition.datasource

import jp.co.biglobe.isp.oss.statetransition.datasource.db.EventIdCreateMapper
import jp.co.biglobe.isp.oss.statetransition.datasource.db.TestTableSetupMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import spock.lang.Specification

@SpringBootTest
@TestConfiguration
class DbSpecCommon extends Specification {
    @Autowired
    StateEventTableNameFactory stateEventTableNameFactory

    @Value("\${spring.datasource.url:#{null}}")
    Optional<String> datasource

    @Autowired
    TestTableSetupMapper testTableSetupMapper

    File getDbFile() {
        return datasource
                .map({ it.split("jdbc:sqlite:")[1] })
                .map({ new File(it) })
                .orElseThrow({ new RuntimeException("datasource undefined") })
    }

    def setup() {
        testTableSetupMapper.createStateEventTable(stateEventTableNameFactory.createStateEventTableName(null))
        testTableSetupMapper.createEventIdTable(EventIdCreateMapper.tableName)
    }

    def cleanup() {
        [stateEventTableNameFactory.createStateEventTableName(null), EventIdCreateMapper.tableName].forEach({ testTableSetupMapper.dropTable(it) })
    }
}
