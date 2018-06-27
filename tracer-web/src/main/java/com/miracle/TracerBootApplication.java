package com.miracle;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOG = LoggerFactory.getLogger(TracerBootApplication.class);

    public static void main(String[] args) {
        try {
            Properties properties = new Properties();
            properties.load(TracerBootApplication.class.getResourceAsStream("/common.properties"));

            SpringApplication app = new SpringApplication(TracerBootApplication.class);
            app.setDefaultProperties(properties);
            app.run(args);
            LOG.info("Success to start application!");
        } catch (Exception ex) {
            LOG.error("Failed to start application.", ex);
        }
    }
}
