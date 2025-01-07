/**
 * 应用程序入口类
 *
 * @author Gray
 * @date 2024/1/9
 */
package com.hgh.tingyue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class TingyueApplication {

    public static void main(String[] args) {
        SpringApplication.run(TingyueApplication.class, args);
        System.out.println("项目启动成功");
    }

}
