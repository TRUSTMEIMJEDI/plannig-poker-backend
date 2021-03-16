//package com.marcinfriedrich.planningpoker.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//import org.springframework.web.servlet.resource.PathResourceResolver;
//
//@Configuration
//public class WebMvcConfig implements WebMvcConfigurer {
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:static").resourceChain(true)
//                .addResolver(new PathResourceResolver() {
//                    @Override
//                    protected Resource getResource(String resourcePath, Resource location) {
//                        String path = "static/";
//                        path += (resourcePath.contains(".") && !resourcePath.equals("index.html")) ? resourcePath : "index.html";
//
//                        Resource resource = new ClassPathResource(path);
//                        return resource.exists() ? resource : null;
//                    }
//                });
//    }
//}
