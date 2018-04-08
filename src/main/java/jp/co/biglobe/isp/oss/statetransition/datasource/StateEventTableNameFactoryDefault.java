package jp.co.biglobe.isp.oss.statetransition.datasource;

import jp.co.biglobe.isp.oss.statetransition.domain.StateType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * application.propertiesからテーブル名を取得する
 * キー: statetransition.table.state_event_table_name
 */
@Component
public class StateEventTableNameFactoryDefault implements StateEventTableNameFactory {

    private final Optional<String> stateEventTable;

    public StateEventTableNameFactoryDefault(
            @Value("${statetransition.table.state_event_table_name:#{null}}") Optional<String> stateEventTable
    ) {
        this.stateEventTable = stateEventTable;
    }

    @Override
    public String createStateEventTableName(StateType stateType) {
        return this.stateEventTable.orElseThrow(() -> new RuntimeException("StateEventTable名が見つかりません。application.propertiesにstatetransition.table.state_event_table_nameを設定してください"));
    }
}
