package com.jwk.upms.web.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.util.MapUtils;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jwk.common.core.constant.CharConstants;
import com.jwk.common.core.constant.JwkSecurityConstants;
import com.jwk.common.core.excel.ExcelHeadReq;
import com.jwk.common.core.excel.ExcelReq;
import com.jwk.common.core.utils.DateHelper;
import com.jwk.common.core.utils.DateUtil;
import com.jwk.common.core.utils.UrlUtil;
import com.jwk.upms.base.entity.SysUser;
import com.jwk.upms.constants.ExcelConstants;
import com.jwk.upms.dto.UserDto;
import com.jwk.upms.listener.JwkPageReadListener;
import com.jwk.upms.dto.UserImportDto;
import com.jwk.upms.web.service.AuthService;
import com.jwk.upms.web.service.ExcelService;
import com.jwk.upms.web.service.SysUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserExcelServiceImpl implements ExcelService {

    private final AuthService authService;

    private final SysUserService sysUserService;

    private final int BATCH_COUNT = 100;

    @Override
    public Boolean importData(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream()).head(UserImportDto.class).registerReadListener(new JwkPageReadListener<UserImportDto>(datalist -> {
                log.info("导入数据：{}", datalist);
                authService.registerImportUsers(datalist);
            }, 50)).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Boolean.TRUE;
    }


    @Override
    public Boolean support(@Nullable String type) {
        return ExcelConstants.USER.equals(type);
    }

    @Override
    public String getFileName() {
        return "用户名单" + CharConstants.DASHED + DateHelper.getLongDate(DateHelper.nowDate());
    }

    @Override
    public void exportData(HttpServletResponse response, ExcelReq excelReq) {
        List<SysUser> userList;
        if (!excelReq.getIsAll()) {
            UserDto userDto = JSON.parseObject(JSON.toJSONString(excelReq.getConditions()), UserDto.class);
            userList = sysUserService.lambdaQuery()
                    .like(StrUtil.isNotBlank(userDto.getUsername()), SysUser::getUsername, userDto.getUsername()).list();
        } else {
            userList = sysUserService.list();
        }

        List<UserImportDto> userImportDtoList = Convert.toList(UserImportDto.class, userList);

        try {
            setDownloadExcelHeader(response);
            Set<String> includeColumnFiledNames = new HashSet<>();
            // 动态获取header
            excelReq.getExportFields().forEach(t -> {
                includeColumnFiledNames.add(t);
            });


            EasyExcel.write(response.getOutputStream(), UserImportDto.class)
//                    自动长宽格式
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
//                    生成excel类型
                    .excelType(ExcelTypeEnum.XLSX)
//                    动态导出的字段
                    .includeColumnFiledNames(includeColumnFiledNames)
//                    sheet名
                    .sheet("用户信息")
//                    数据
                    .doWrite(userImportDtoList);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding(JwkSecurityConstants.UTF8);
            Map<String, String> map = MapUtils.newHashMap();
            map.put("code", "-1");
            map.put("msg", "下载文件失败" + e.getMessage());
            try {
                response.getWriter().println(JSON.toJSONString(map));
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void setDownloadExcelHeader(HttpServletResponse response) {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding(JwkSecurityConstants.UTF8);
        response.setHeader("Content-Disposition", "attachment;filename=" + UrlUtil.encode(getFileName(),StandardCharsets.UTF_8) + ".xlsx");
        response.setHeader("Content-Download", "true");
    }
}
