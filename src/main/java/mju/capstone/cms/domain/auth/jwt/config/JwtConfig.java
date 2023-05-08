package mju.capstone.cms.domain.auth.jwt.config;

import mju.capstone.cms.domain.auth.jwt.filter.JwtFilter;
import mju.capstone.cms.domain.auth.jwt.provider.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class JwtConfig {

    //jwtProvider 수동 빈 등록.
    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider();
    }

    //jwtFilter 등록.
    @Bean
    public FilterRegistrationBean jwtFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new JwtFilter(jwtProvider()));
        filterRegistrationBean.setOrder(1);
        //필터적용해야할 url pattern 추가
        filterRegistrationBean.addUrlPatterns("/me", "/student/*", "/class/*", "/attendance/*");
        return filterRegistrationBean;
    }
}
