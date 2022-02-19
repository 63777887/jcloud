package com.jwk.api;

import com.jwk.api.api.handler.UaaRemoteServiceFallBackService;
import com.jwk.api.api.handler.UaaRemoteServiceFallbackFactory;
import com.jwk.api.api.handler.UpmsRemoteServiceFallBackService;
import com.jwk.api.api.handler.UpmsRemoteServiceFallbackFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({UaaRemoteServiceFallbackFactory.class, UaaRemoteServiceFallBackService.class,
    UpmsRemoteServiceFallbackFactory.class, UpmsRemoteServiceFallBackService.class})
public class ApiAutoConfiguration {


}
