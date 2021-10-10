package com.ymbj.condition;

public class LinuxListService implements ListService {

	@Override
	public String showListLine() {
		return "ls";
	}

}
