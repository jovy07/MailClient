package com.example.mailclient.decodePacket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Configuration
public class DecodingClass  extends DelegatingWebMvcConfiguration {

	
	 @Bean
	    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
	        RequestMappingHandlerMapping handlerMapping = super.requestMappingHandlerMapping();
	        handlerMapping.setUrlDecode(false);
	        return handlerMapping;
	    }
}
