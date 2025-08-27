package com.leon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Component
public class EndpointLogger implements SmartInitializingSingleton {

    private static final Logger log = LoggerFactory.getLogger(EndpointLogger.class);
    private final ApplicationContext context;

    public EndpointLogger(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public void afterSingletonsInstantiated() {
        try {
            RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);
            log.info("📌 Registered REST endpoints:");
            mapping.getHandlerMethods().forEach((info, method) -> {
                String path = info.getPatternsCondition().getPatterns().stream().findFirst().orElse("N/A");
                String httpMethods = info.getMethodsCondition().getMethods().toString();
                String controller = method.getBeanType().getSimpleName();
                String handler = method.getMethod().getName();

                log.info("  [{}] {} -> {}#{}", httpMethods, path, controller, handler);
            });
        } catch (Exception e) {
            log.warn("RequestMappingHandlerMapping bean not found. Are you using WebFlux?");
        }
    }
}