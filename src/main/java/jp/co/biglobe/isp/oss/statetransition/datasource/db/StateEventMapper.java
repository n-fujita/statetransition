package jp.co.biglobe.isp.oss.statetransition.datasource.db;

import jp.co.biglobe.isp.oss.statetransition.datasource.StateEventId;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.StateEventAdapter;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.FindContainer;
import jp.co.biglobe.isp.oss.statetransition.datasource.table.stateevent.InsertStateEventContainer;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface StateEventMapper {
    @Insert("INSERT INTO ${tableName} (state_event_id, id, state_type, state, state_event_date_time, event_date_time) VALUES (#{c.eventId.value}, #{c.id}, #{c.stateType.value}, #{c.state.value}, #{c.stateEventDateTime.value}, #{c.now})")
    void insertStateEvent(
            @Param("tableName") String tableName,
            @Param("c") InsertStateEventContainer c
    );

    @Select("SELECT * FROM ${tableName} WHERE id = #{c.id} AND state_type = #{c.stateType.value} AND is_latest = 1")
    StateEventAdapter findLatest(
            @Param("tableName") String tableName,
            @Param("c") FindContainer c
    );

    @Select("SELECT * FROM ${tableName} WHERE id = #{c.id} AND state_type = #{c.stateType.value} ORDER BY state_event_id")
    List<StateEventAdapter> findAllEvent(
            @Param("tableName") String tableName,
            @Param("c") FindContainer c
    );

    @Update("DELETE FROM ${tableName} WHERE state_event_id = #{eventId.value}")
    void delete(
            @Param("tableName") String tableName,
            @Param("eventId") StateEventId eventId
    );

    @Update("UPDATE ${tableName} SET is_latest = 1 WHERE state_event_id = #{eventId.value}")
    void setIsLatest(
            @Param("tableName") String tableName,
            @Param("eventId") StateEventId eventId
    );

    @Update("UPDATE ${tableName} SET is_latest = null WHERE state_event_id = #{eventId.value}")
    void removeIsLatest(
            @Param("tableName") String tableName,
            @Param("eventId") StateEventId eventId
    );
}
