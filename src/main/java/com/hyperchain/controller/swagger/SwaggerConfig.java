package com.hyperchain.controller.swagger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;


import com.mangofactory.swagger.configuration.SpringSwaggerConfig;
import com.mangofactory.swagger.plugin.SwaggerSpringMvcPlugin;
import com.mangofactory.swagger.models.dto.ApiInfo;

/**
 * swagger接口浏览器
 *
 * @Author Lizhong Kuang
 * @Modify Date 16/11/2 上午1:27
 */
public class SwaggerConfig {

    @Autowired
    private SpringSwaggerConfig springSwaggerConfig;


    public void setSpringSwaggerConfig(SpringSwaggerConfig springSwaggerConfig) {
        this.springSwaggerConfig = springSwaggerConfig;
    }


    @Bean
    public SwaggerSpringMvcPlugin customImplementation() {
        return new SwaggerSpringMvcPlugin(this.springSwaggerConfig).apiInfo(apiInfo()).includePatterns(".v1.*");
    }

    private ApiInfo apiInfo() {
        ApiInfo apiInfo = new ApiInfo("项目接口文档",
                "接口描述", "供应链金融平台",
                "", "",
                "");
        return apiInfo;
    }
}
