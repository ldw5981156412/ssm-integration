package com.dwliu.ssmintegration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author dwliu
 */
@SpringBootApplication
@MapperScan({"com.dwliu.ssmintegration.dao"})
public class SsmIntegrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(SsmIntegrationApplication.class, args);
    }
}
