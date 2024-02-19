package hello.productservice;

import hello.productservice.main.filter.LogFilter;
import hello.productservice.main.interceptor.LogInterceptor;
import hello.productservice.main.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    //spring boot로 filter넣기
    @Bean
    public FilterRegistrationBean logFilter(){
        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
        filterFilterRegistrationBean.setFilter(new LogFilter());
        filterFilterRegistrationBean.setOrder(1);
        //모든 URL에 적용
        filterFilterRegistrationBean.addUrlPatterns("/*");

        return filterFilterRegistrationBean;

    }

//    @Bean
//    public FilterRegistrationBean loginCheckFilter(){
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
//        filterFilterRegistrationBean.setOrder(2);
//        //모든 URL에 적용
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//
//        return filterFilterRegistrationBean;
//
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**")
                .excludePathPatterns("/css/**","/*.ico","/error,/members/login");
//        WebMvcConfigurer.super.addInterceptors(registry);

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/","/members/add","/members/login","/members/logout","/css/**","/*.ico","/error");
    }
}
