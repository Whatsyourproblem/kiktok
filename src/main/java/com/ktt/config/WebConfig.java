package com.ktt.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootConfiguration
public class WebConfig implements WebMvcConfigurer {

   /* @Override
    public void addCorsMappings(CorsRegistry registry) {
        //所有请求都允许跨域，使用这种配置方法就不能在 interceptor 中再配置 header 了
        registry.addMapping("/**")
                .allowCredentials(true)
                //.allowedOrigins("http://localhost:8080")
                .allowedOrigins("http://10.2.179.181:8080")
                .allowedMethods("POST", "GET", "PUT", "OPTIONS", "DELETE")
                .allowedHeaders("*")
                .maxAge(3600);
    }*/



/*    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addAdditionalTomcatConnectors(createStandardConnector());
        return tomcat;
    }

    private Connector createStandardConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        connector.setPort(443);
        return connector;
    }*/

    /**
     * 配置资源映射
     * 意思是：如果请求地址是以“/upload/”开头的，
     * 就给映射到本机的“D:/upload/”这个文件夹内，去找你要的资源
     * 注意：D:/upload/ 后面的 “/”一定要带上
     */
}
