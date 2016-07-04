package com.qzt360.BASystem;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BAController {

	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping("/baInfo")
	public BAInfo getBAInfo(@RequestParam(value = "strId", defaultValue = "11:22:33:44:55:66") String strId) {
		if (strId.split(":").length == 6) {
			// strId为MAC
		} else {
			// strId为组织结构代码strGroupCode
		}
		System.out.println("strId:" + strId);
		EqpInfo eqp = new EqpInfo("11:22:33:44:55:66", "正常", "已备案", "2016-03-03 12:33:22");
		List<EqpInfo> listEqp = new ArrayList<EqpInfo>();
		listEqp.add(eqp);
		eqp = new EqpInfo("12:23:34:45:56:67", "异常", "未备案", "2016-03-04 11:30:26");
		listEqp.add(eqp);
		BAInfo ba = new BAInfo("0632323-2", "龙光地产股份有限公司汕头龙光喜来登酒店", "汕头市龙湖区金霞街道长平路龙光世纪大厦4层机房内", "汕头市－龙湖区", "酒店",
				listEqp);
		return ba;
	}

	@CrossOrigin(origins = "http://localhost:8081")
	@RequestMapping("/eqpInfo")
	public EqpInfo getEqpInfo(@RequestParam(value = "strEqpId", defaultValue = "11:22:33:44:55:66") String strEqpId) {
		System.out.println("strEqpId:" + strEqpId);
		// UnitInfo unit = new UnitInfo("0632323-2", "龙光地产股份有限公司汕头龙光喜来登酒店",
		// "汕头市龙湖区金霞街道长平路龙光世纪大厦4层机房内", "汕头市－龙湖区", "酒店");
		// UnitInfo unitNull = new UnitInfo("未知", "未知", "未知", "未知", "未知");
		EqpInfo eqp = new EqpInfo(strEqpId, "正常", "已备案", "2016-03-03 12:33:22");
		if (!strEqpId.startsWith("11")) {
			// eqp = new EqpInfo(strEqpId, "未知", "未知", "未知", unitNull);
		}
		return eqp;
	}
}
