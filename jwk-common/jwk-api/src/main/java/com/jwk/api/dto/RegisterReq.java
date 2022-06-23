package com.jwk.api.dto;

import java.io.Serializable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author Jiwk
 * @date 2022/6/11
 * @version 0.1.0
 * <p>
 * 注册用户请求参数
 */
@Data
public class RegisterReq implements Serializable {

  private static final long serialVersionUID = -9137840398633566755L;

  /**
   * orgId
   */
  @NotNull
  private Long orgId;

  /**
   * username
   */
  @NotBlank(message = "用户名不能为空")
  private String username;

  /**
   * password
   */
  @NotBlank(message = "密码不能为空")
  private String password;

  /**
   * phone
   */
  @NotBlank(message = "手机号不能为空")
  private String phone;

  /**
   * email
   */
  private String email;

}
