package com.zj.retrieval.master;

public class BizUser extends AbstractCRUDHandler {
	
	public boolean verifyUser(String name, String pwd) {
		String sql = "SELECT COUNT(*) FROM `T_USER` WHERE `U_NAME`=? AND `U_PASSWORD`=? AND U_IS_ACTIVE=?";
		return template.queryForInt(sql, name, pwd, 1) > 0;
	}

	public static BizUser getInstance() {
		return (BizUser) Configuration.getBean("bizUser");
	}
}
