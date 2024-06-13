package com.pratik.cameldynamicroute;

import com.pratik.cameldynamicroute.service.ApiInvokeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;
import java.util.stream.Collectors;
@ComponentScan(
)
@SpringBootApplication
public class CameldynamicrouteApplication {

	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = SpringApplication.run(CameldynamicrouteApplication.class, args);
		ApiInvokeService apiInvokeService = (ApiInvokeService) applicationContext.getBean("apiInvokeService");
		apiInvokeService.callAPI();
	}

}
