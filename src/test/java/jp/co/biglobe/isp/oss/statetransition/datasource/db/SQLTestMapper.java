package jp.co.biglobe.isp.oss.statetransition.datasource.db;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

public interface SQLTestMapper {
    @Insert("${sql}")
    void insert(@Param("sql") String sql);
}
