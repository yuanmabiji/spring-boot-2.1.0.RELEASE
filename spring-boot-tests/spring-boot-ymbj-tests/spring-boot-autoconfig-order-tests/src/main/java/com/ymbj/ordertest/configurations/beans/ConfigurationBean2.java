package com.ymbj.ordertest.configurations.beans;

public class ConfigurationBean2 {
	private MemberBean memberBean;

	public ConfigurationBean2(MemberBean memberBean) {
		this.memberBean = memberBean;
	}

	public void setMemberBean(MemberBean memberBean) {
		this.memberBean = memberBean;
	}

	public MemberBean getMemberBean() {
		return memberBean;
	}
}
