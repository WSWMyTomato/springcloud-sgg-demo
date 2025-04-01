package com.wsw.order;

import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.*;

@SpringBootApplication
public class OrderMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderMainApplication.class, args);
    }

    @Bean
    ApplicationRunner applicationRunner(NacosConfigManager nacosConfigManager){
        return args -> {
            ConfigService configService = nacosConfigManager.getConfigService();
            configService.addListener("service-order.yml", "DEFAULT_GROUP",
                        new Listener() {
                    @Override
                    public Executor getExecutor() {
                        return new ThreadPoolExecutor(
                                5,
                                5,
                                2,
                                TimeUnit.SECONDS,
                                new ArrayBlockingQueue<>(10),
                                Executors.defaultThreadFactory(),
                                new ThreadPoolExecutor.AbortPolicy()
                        );
                    }

                    @Override
                    public void receiveConfigInfo(String configInfo) {
                        System.out.println("变化的配置信息："+configInfo);
                        System.out.println("邮件通知...");
                    }
                });
            System.out.println("-------------------------");
        };
    }

}
