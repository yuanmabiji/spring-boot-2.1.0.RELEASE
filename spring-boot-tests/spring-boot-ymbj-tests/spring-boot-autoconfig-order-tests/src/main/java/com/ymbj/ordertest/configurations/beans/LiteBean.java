package com.ymbj.ordertest.configurations.beans;

public class LiteBean {
	private LiteMemberBean liteMemberBean;

	public LiteBean(LiteMemberBean liteMemberBean) {
		this.liteMemberBean = liteMemberBean;
	}

	public void setLiteMemberBean(LiteMemberBean liteMemberBean) {
		this.liteMemberBean = liteMemberBean;
	}

	public LiteMemberBean getLiteMemberBean() {
		return liteMemberBean;
	}
}
