package jp.co.biglobe.isp.oss.statetransition.datasource;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("jp.co.biglobe.isp.oss.statetransition.datasource.db")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
