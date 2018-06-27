package com.miracle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

/**
 * Description:这是tracer项目的服务启动类
 *
 * @author guobin On date 2018/6/27.
 * @version 1.0
 * @since jdk 1.8
 */
@SpringBootApplication
public class TracerBootApplication {

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();

            SpringApplication app = new SpringApplication(TracerBootApplication.class);
            app.setDefaultProperties(properties);
            app.run(args);
            System.out.println("Start success!");
        } catch (Exception ex) {
            System.out.println("Fail to start SyncOrderBootApplication due to exception:" + ex.toString());
        }
    }
}
