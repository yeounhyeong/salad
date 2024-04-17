package com.example.basic.Config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//자원과 외부폴더를 연결설정함.
//사용자 클래스를 Bean에 등록
// WebMvcConfigurer인터페이스 상속을받아서 내용을채움
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //파일업로드 변수 설정 (application에 사용자변수를 읽어옴)
    @Value("${uploadPath}")
    String uploadPath;

    //자원 추가
    //c:/salad/board/를 resources에 images폴더로 연결해서 사용
    //c:/salad/review/를 resources에 images폴더로 연결해서 사용
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/images/**")
                .addResourceLocations(uploadPath); //자원위치 = 물리적위치
    }
}
