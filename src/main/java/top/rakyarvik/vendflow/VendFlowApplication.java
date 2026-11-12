package top.rakyarvik.vendflow;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("top.rakyarvik.vendflow.mapper")
@EnableScheduling
public class VendFlowApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendFlowApplication.class, args);
    }

}
