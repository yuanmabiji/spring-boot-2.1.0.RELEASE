package com.ymbj.ordertest.autoconfig;


import com.ymbj.ordertest.hellowolrd.autoconfig.HelloWorldComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
@Controller
public class HelloWorldController {
	@Value("${testAtValue}")
	private String value1;

	@Value("${yuanmabiji.number}")
	private int number;

	@Value("${yuanmabiji.profile}")
	private String profile;
	
	@Autowired
	private HelloWorldComponent helloWorldComponent;

	@Autowired
	private Environment environment;

	@Autowired
	private ProfileService profileService;

	@RequestMapping("/helloworld")
	@ResponseBody
	private String helloWorld() {
		// System.out.println(environment.getActiveProfiles()[]);
		 return helloWorldComponent.sayHelloWorld();
	}

	@RequestMapping("/testAtValue")
	@ResponseBody
	private String testAtValue() {
		return value1;
	}

	@RequestMapping("/testRandomNumber")
	@ResponseBody
	private Integer testRandomNumber() {
		return number;
	}

	@RequestMapping("/testProfile")
	@ResponseBody
	private String testProfile() {
		return profile;
	}

	@RequestMapping("/testProfileBean")
	@ResponseBody
	private String testProfileBean() {
		return profileService.toString();
	}
}
