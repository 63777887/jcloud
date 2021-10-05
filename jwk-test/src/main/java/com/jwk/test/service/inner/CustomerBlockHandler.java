package com.jwk.test.service.inner;

import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.jwk.common.core.model.RestResponse;
import com.jwk.common.core.model.RestResponse.RestResponseBuilder;
import java.util.*;

/**
 * @auther zzyy
 * @create 2020-02-25 15:32
 */
public class CustomerBlockHandler {

  public static RestResponse handlerException(BlockException exception) {
    return RestResponseBuilder.createFailBuilder("按客戶自定义,global handlerException----1").buidler();
  }

  public static RestResponse handlerException2(BlockException exception) {
    return RestResponseBuilder.createFailBuilder("按客戶自定义,global handlerException----2").buidler();
  }

  public static void main(String[] args) {
    String s = "{\"autoStart\":false,\"createSid\":false,\"inputParams\":{\"viewCols\":{\"annexList\":[{\"annexName\":\"传参.txt\",\"annexUrl\":\"group1/M00/00/31/ClHIG2BsB-uAP0nAAAAJ-R0dCdw597.txt\"}],\"apprId\":\"110009158\",\"busClassName\":\"终止申请\",\"chargeList\":[{\"arrearsMoney\":\"5000\",\"billDate\":\"2021-04-06-2021-04-30\",\"fee\":\"5000.00\",\"feeItemTypeName\":\"HH-物业费\",\"receiveDate\":\"2021-04-06\"},{\"arrearsMoney\":\"6000\",\"billDate\":\"2021-05-01-2021-05-31\",\"fee\":\"6000.00\",\"feeItemTypeName\":\"HH-物业费\",\"receiveDate\":\"2021-05-01\"},{\"arrearsMoney\":\"6000\",\"billDate\":\"2021-06-01-2021-06-30\",\"fee\":\"6000.00\",\"feeItemTypeName\":\"HH-物业费\",\"receiveDate\":\"2021-06-01\"},{\"arrearsMoney\":\"6000\",\"billDate\":\"2021-07-01-2021-07-31\",\"fee\":\"6000.00\",\"feeItemTypeName\":\"HH-物业费\",\"receiveDate\":\"2021-07-01\"},{\"arrearsMoney\":\"1300\",\"billDate\":\"2021-04-06-2021-04-06\",\"fee\":\"1300.00\",\"feeItemTypeName\":\"合约物业费ZY\",\"receiveDate\":\"2021-04-06\"},{\"arrearsMoney\":\"5000\",\"billDate\":\"2021-04-06-2021-04-30\",\"fee\":\"5000.00\",\"feeItemTypeName\":\"物业管理费\",\"receiveDate\":\"2021-04-10\"},{\"arrearsMoney\":\"6000\",\"billDate\":\"2021-05-01-2021-05-31\",\"fee\":\"6000.00\",\"feeItemTypeName\":\"物业管理费\",\"receiveDate\":\"2021-05-05\"},{\"arrearsMoney\":\"6000\",\"billDate\":\"2021-06-01-2021-06-30\",\"fee\":\"6000.00\",\"feeItemTypeName\":\"物业管理费\",\"receiveDate\":\"2021-06-05\"},{\"arrearsMoney\":\"6000\",\"billDate\":\"2021-07-01-2021-07-31\",\"fee\":\"6000.00\",\"feeItemTypeName\":\"物业管理费\",\"receiveDate\":\"2021-07-05\"},{\"arrearsMoney\":\"3786885\",\"billDate\":\"2021-04-15-2021-06-30\",\"fee\":\"3786885.00\",\"feeItemTypeName\":\"租赁租金费用\",\"receiveDate\":\"2021-04-15\"},{\"arrearsMoney\":\"1213043\",\"billDate\":\"2021-07-01-2021-07-31\",\"fee\":\"1213043.00\",\"feeItemTypeName\":\"租赁租金费用\",\"receiveDate\":\"2021-07-01\"}],\"contract\":{\"contDataCount\":\"-115\",\"contractNo\":\"hwhy-2021-04-5956\",\"depositMoneyFmt\":\"123000.00元\",\"endDateFmt\":\"2021-07-31\",\"firstName\":\"hwhy-2021-04-5956\",\"freeDesc\":\"9\",\"leaseMode\":\"整租\",\"overDateFmt\":\"2021-07-31\",\"payCname\":\"刘大炮\",\"rentCalcModeName\":\"固定租金\",\"rentTermTypeFmt\":\"长期\",\"resAllName\":\"王者荣耀楼1楼-D5\",\"secondName\":\"刘大炮\",\"signDateFmt\":\"2021-04-06\",\"standardContract\":\"是\",\"startDateFmt\":\"2021-04-06\",\"totalBuildArea\":\"60.0000㎡/1套\",\"totalRentArea\":\"60.0000㎡/1套\",\"totalRentFmt\":\"4999928.00元\",\"totalUsedArea\":\"0.0000㎡/1套\"},\"createTimeFmt\":\"2021-04-06\",\"createUserName\":\"杜英娟\",\"effectDate\":\"2021-07-31\",\"marginList\":[{\"chargeItem\":\"合约押金\",\"depositAll\":\"3000\",\"depositSurplus\":\"0\",\"receiveDate\":\"2021-04-06\",\"receiveDebtDate\":\"202104\"},{\"chargeItem\":\"合约押金\",\"depositAll\":\"-3000\",\"depositSurplus\":\"0\",\"receiveDate\":\"2021-04-06\",\"receiveDebtDate\":\"202104\"},{\"chargeItem\":\"房屋押金\",\"depositAll\":\"120000\",\"depositSurplus\":\"0\",\"receiveDate\":\"2021-04-06\",\"receiveDebtDate\":\"202104\"},{\"chargeItem\":\"房屋押金\",\"depositAll\":\"-120000\",\"depositSurplus\":\"0\",\"receiveDate\":\"2021-04-06\",\"receiveDebtDate\":\"202104\"}],\"overReason\":\"其他原因\",\"overTypeName\":\"正常终止\",\"remarks\":\"啊啊啊\",\"taskName\":\"刘大炮终止申请\",\"voucher_no\":\"110009158\",\"ywtype\":\"刘大炮终止申请\"}},\"processDefId\":\"SG-0006-ZCHT\",\"title\":\"[110009158]刘大炮终止申请申请审批\",\"uid\":\"00000056\"}";
    Map<String, Object> stringObjectMap = parseJSON2Map(s);
    Set<Map.Entry<String, Object>> entries = stringObjectMap.entrySet();
    for (Map.Entry<String, Object> entry : entries) {
      System.out.println(entry.getKey() + "------" + entry.getValue());
    }
  }

  public static Map<String, Object> parseJSON2Map(String jsonStr) { //最外层解析
    if (jsonStr != null && jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
      Map<String, Object> map = new HashMap<String, Object>();
      JSONObject json = JSON.parseObject(jsonStr);
      for (Object k : json.keySet()) {
        Object v = json.get(k); //如果内层还是数组的话，继续解析
        if (v instanceof JSONArray) {
          List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
          Iterator<Object> it = ((JSONArray) v).iterator();
          while (it.hasNext()) {
            JSONObject json2 = JSON.parseObject(it.next().toString());
            list.add(parseJSON2Map(json2.toString()));
          }
          map.put(k.toString(), list);
        } else if (v instanceof JSONObject) {
          Map<String, Object> stringObjectMap = parseJSON2Map(v.toString());
          map.putAll(stringObjectMap);
        } else {
          Map<String, Object> m = parseJSON2Map(v.toString());
          if (m == null) {
            map.put(k.toString(), v);
          } else {
            map.put(k.toString(), m);
          }

        }
      }
      return map;
    } else {
      return null;
    }

  }
}