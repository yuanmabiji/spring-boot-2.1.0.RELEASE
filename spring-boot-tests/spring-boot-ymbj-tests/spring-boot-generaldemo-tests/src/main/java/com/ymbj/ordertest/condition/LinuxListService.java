package com.ymbj.ordertest.condition;

public class LinuxListService implements ListService {

	@Override
	public String showListLine() {
		return "ls";
	}

}
