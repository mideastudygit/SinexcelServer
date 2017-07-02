package com.net.test;

import com.net.bean.BaseBean;
import com.net.bean.ChargeBean;
import com.net.util.NetUtils;

public class BeanTest {
	public static void main(String[] args) {
		int cmd = 7;
		BaseBean bean = NetUtils.getBean(cmd + 1);
		if (bean instanceof ChargeBean) {
			System.out.println(7);
			
			ChargeBean chargeBean = (ChargeBean)bean;
			chargeBean.setPileCode("test");
		}
		
		System.out.println(bean.getPileCode());
	}
}
