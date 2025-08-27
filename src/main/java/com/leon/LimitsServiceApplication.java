package com.leon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class LimitsServiceApplication
{
    private static final Logger log = LoggerFactory.getLogger(LimitsServiceApplication.class);
    public static void main(String[] args)
    {
        SpringApplication.run(LimitsServiceApplication.class, args);
    }

    @Bean
    public ApplicationRunner logRestEndpoints(ApplicationContext context) {
        System.out.println("🚀 Starting LimitsServiceApplication...");

        return args -> {
            try {
                RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
                log.info("📌 Registered REST endpoints:");
                mapping.getHandlerMethods().forEach((info, method) -> {
                    log.info("  {} -> {}#{}", info, method.getBeanType().getSimpleName(), method.getMethod().getName());
                });
            } catch (Exception e) {
                log.warn("RequestMappingHandlerMapping bean not found. Are you using WebFlux?");
            }
            log.info("🔍 Listing all beans in context:");
            for (String beanName : context.getBeanDefinitionNames()) {
                log.info("  Bean: {}", beanName);
            }
        };
    }
}