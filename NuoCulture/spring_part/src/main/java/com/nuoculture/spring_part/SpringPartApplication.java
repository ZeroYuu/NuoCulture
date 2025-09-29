package com.nuoculture.spring_part;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;

@SpringBootApplication
//导入Mapper层
@MapperScan({"com.nuoculture.spring_part.BackEnd.*.*.mapper", "com.nuoculture.spring_part.FrontEnd.*.mapper", "com.nuoculture.spring_part.*.mapper"})

public class SpringPartApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringPartApplication.class, args);
    }

}
