package com.jwk.upms.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Jiwk
 * @version 0.1.6
 * <p>
 * @date 2023/10/12
 */
@Data
@Getter
@Setter
@EqualsAndHashCode
public class UserImportDto {


	/**
	 * 用户ID
	 */
	@ExcelProperty(value = "用户ID")
	private String userId;

	/**
	 * 组织机构
	 */
	@ExcelProperty(value = "组织机构")
	private String orgName;

	/**
	 * 用户名
	 */
	@ExcelProperty(value = "用户名")
	private String username;

	/**
	 * nickname
	 */
	@ExcelProperty(value = "昵称")
	private String nickname;

	/**
	 * phone
	 */
	@ExcelProperty(value = "手机号")
	private String phone;

	/**
	 * email
	 */
	@ExcelProperty(value = "邮箱")
	private String email;


}
