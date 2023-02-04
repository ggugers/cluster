package com.example.cluster;

import org.apache.catalina.Context;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClusterApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClusterApplication.class, args);
	}

	//원래 다른서버(톰캣이라면 톰캣의 server.xml에 추가설정 필요, was별로 다름)와 세션공유를 위해 web.xml 에 <distributable/> 설정해주는 부분 대체
//	private static boolean distributable;
//
//
//	public static boolean getDistributable() {
//		return distributable;
//	}
//
//	@Bean
//	public ServletWebServerFactory tomcatFactory() {
//		return new TomcatServletWebServerFactory() {
//			@Override
//			protected void postProcessContext(Context context) {
//				context.setDistributable(true);
//				ClusterApplication.distributable = context.getDistributable();
//				System.out.println("distributable is :"+distributable);
//			}
//		};
//	}
	//원래 다른서버(톰캣이라면 톰캣의 server.xml에 추가설정 필요, was별로 다름)와 세션공유를 위해 web.xml 에 <distributable/> 설정해주는 부분 대체 끝

}
