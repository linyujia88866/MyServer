package cn;

import cn.websocket.RequestListener;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@ServletComponentScan
@MapperScan({"cn.dao"})
@EnableScheduling
@EnableJpaRepositories
@EnableJpaAuditing
//@EnableWebMvc
public class ProductServiceApplication {
	public static void main(String[] args) {
    	int port = 9802;
        new SpringApplicationBuilder(ProductServiceApplication.class).properties("server.port=" + port).run(args);
	}

	@Autowired
	private RequestListener requestListener;
	@Bean
	public ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean() {
		ServletListenerRegistrationBean<RequestListener> servletListenerRegistrationBean = new ServletListenerRegistrationBean<>();
		servletListenerRegistrationBean.setListener(requestListener);
		return servletListenerRegistrationBean;
	}
}
