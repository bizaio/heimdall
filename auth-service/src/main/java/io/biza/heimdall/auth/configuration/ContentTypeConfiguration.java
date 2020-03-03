package io.biza.heimdall.auth.configuration;

import java.util.List;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ContentTypeConfiguration implements WebMvcConfigurer {


  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> messageConverters) {
    LOG.debug("Registering Jackson message converter");
    messageConverters.add(new MappingJackson2HttpMessageConverter());
    LOG.debug("Registering Form message converter");
    messageConverters.add(new FormHttpMessageConverter());
  }
  
}
