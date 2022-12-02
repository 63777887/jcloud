package com.jwk.api;

import com.jwk.api.api.handler.UpmsRemoteServiceFallBackServiceImpl;
import com.jwk.api.api.handler.UpmsRemoteServiceFallbackFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({UpmsRemoteServiceFallbackFactory.class, UpmsRemoteServiceFallBackServiceImpl.class })
public class ApiAutoConfiguration {

}
