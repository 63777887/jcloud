// package com.jwk.upms.web.controller;
//
// import com.jwk.common.core.model.RestResponse;
// import com.jwk.security.security.service.impl.JwtAuthService;
// import com.jwk.security.web.dto.RegisterReq;
// import io.swagger.annotations.Api;
// import javax.annotation.Resource;
// import javax.validation.Valid;
// import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
//
/// **
// * JWT获取令牌和刷新令牌接口
// */
// @RestController()
// @RequestMapping("/admin")
// @Api(tags = "系统-登陆 API", produces = MediaType.APPLICATION_JSON_VALUE)
// public class JwtAuthController {
//
// @Resource
// private JwtAuthService jwtAuthService;
//
// /**
// * 注册用户
// */
// @PostMapping(value = "/register")
// public RestResponse register(@RequestBody @Valid RegisterReq user) {
// jwtAuthService.register(user);
// return RestResponse.RestResponseBuilder.createSuccessBuilder()
// .setResult("注册成功").buidler();
// }
//
// }
