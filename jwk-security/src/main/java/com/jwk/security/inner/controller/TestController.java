package com.jwk.security.inner.controller;

import com.jwk.api.api.TestService;
import com.jwk.api.exception.InternalApiException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Service
public class TestController implements TestService {

  @Override
  public String queryBrandListByOrganId(Long id) throws InternalApiException {
    return String.valueOf(id);
  }
}
