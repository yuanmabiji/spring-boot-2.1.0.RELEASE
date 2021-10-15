package com.ymbj.ordertest.configurations.beans;

public class LiteBean {
	private LiteMemberBean liteMemberBean;

	private LiteMemberBean2 liteMemberBean2;

	public LiteBean(LiteMemberBean liteMemberBean, LiteMemberBean2 liteMemberBean2) {
		this.liteMemberBean = liteMemberBean;
		this.liteMemberBean2 = liteMemberBean2;
	}

	public void setLiteMemberBean(LiteMemberBean liteMemberBean) {
		this.liteMemberBean = liteMemberBean;
	}

	public LiteMemberBean getLiteMemberBean() {
		return liteMemberBean;
	}

	public LiteMemberBean2 getLiteMemberBean2() {
		return liteMemberBean2;
	}

	public void setLiteMemberBean2(LiteMemberBean2 liteMemberBean2) {
		this.liteMemberBean2 = liteMemberBean2;
	}
}
