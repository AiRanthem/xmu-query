package cn.airanthem.xmu.query;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(value = "cn.airanthem.xmu.query.mapper")
public class XmuQueryBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(XmuQueryBootStarterApplication.class, args);
    }

}
