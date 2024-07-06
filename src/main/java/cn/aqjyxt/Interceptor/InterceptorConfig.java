package cn.aqjyxt.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

/**
 * 注册拦截器
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //下面list是放行swagger
        List<String> l=new ArrayList<String>();
        l.add("/login");
        l.add("/logout");
        l.add("/css/**");
        l.add("/api/**");
        l.add("/js/**");
        l.add("/error");
        l.add("/index.html");
        l.add("favicon.ico");
        l.add("/doc.html");
        l.add("/webjars/**");
        l.add("/swagger-resources/**");
        l.add("/v2/api-docs/**");

        registry.addInterceptor(new JWTInterceptor())
                //拦截 把需要拦截的请求配置
                .addPathPatterns("/**")
                //放行
                .excludePathPatterns(l);
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }
}