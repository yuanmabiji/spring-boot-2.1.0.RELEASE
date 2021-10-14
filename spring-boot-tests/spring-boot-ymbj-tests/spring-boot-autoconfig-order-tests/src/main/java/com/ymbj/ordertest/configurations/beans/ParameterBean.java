package com.ymbj.ordertest.configurations.beans;

public class ParameterBean {
	private ParamMemberBean paramMemberBean;

	public ParameterBean(ParamMemberBean paramMemberBean) {
		this.paramMemberBean = paramMemberBean;
	}

	public ParamMemberBean getParamMemberBean() {
		return paramMemberBean;
	}

	public void setParamMemberBean(ParamMemberBean paramMemberBean) {
		this.paramMemberBean = paramMemberBean;
	}
}
