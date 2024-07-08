package cn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
@MapperScan({"cn.dao"})
//@EnableWebMvc
public class ProductServiceApplication {
	public static void main(String[] args) {
    	int port = 9802;
//		if(!NetUtil.isUsableLocalPort(port)) {
//			System.err.printf("端口%d被占用了，无法启动%n", port );
//    		System.exit(1);
//    	}
        new SpringApplicationBuilder(ProductServiceApplication.class).properties("server.port=" + port).run(args);
	}
}
