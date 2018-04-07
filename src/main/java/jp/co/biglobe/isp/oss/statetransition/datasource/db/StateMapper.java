package jp.co.biglobe.isp.oss.statetransition.datasource.db;

import jp.co.biglobe.isp.oss.statetransition.datasource.table.StateEventAdapter;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.state.FindLatestContainer;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.state.UpsertStateContainer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface StateMapper {
    @Insert("INSERT INTO ${tableName} (state_event_id, id, state_type, state, state_event_date_time, event_date_time) VALUES (#{c.eventId.value}, #{c.id}, #{c.stateType.value}, #{c.state.value}, #{c.stateEventDateTime.value}, #{c.now})")
    void insertState(
            @Param("tableName") String tableName,
            @Param("c") UpsertStateContainer c
    );

    @Update("UPDATE ${tableName} SET state_event_id = #{c.eventId.value}, state = #{c.state.value}, state_event_date_time = #{c.stateEventDateTime.value}, event_date_time = #{c.now} WHERE id = #{c.id} AND state_type = #{c.stateType.value}")
    void updateState(
            @Param("tableName") String tableName,
            @Param("c") UpsertStateContainer c
    );

    @Select("SELECT * FROM ${tableName} WHERE id = #{c.id} AND state_type = #{c.stateType.value}")
    StateEventAdapter find(
            @Param("tableName") String tableName,
            @Param("c") FindLatestContainer c
    );
}
