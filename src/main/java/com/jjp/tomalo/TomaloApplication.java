package com.jjp.tomalo;

import io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class TomaloApplication {

    public static void main(String[] args) {
        SpringApplication.run(TomaloApplication.class, args);
    }

}
