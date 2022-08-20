package com.dickli.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        String basePath = System.getProperty("user.dir");
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/dickli", "dickli", "dickli")
                .globalConfig(builder -> {
                    builder.author("dickli") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .disableOpenDir()
                            .outputDir(basePath+"\\provider\\service\\src\\main\\java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.dickli")
                            .entity("model")
                            .service("service")
                            .serviceImpl("service.impl")
                            .mapper("mapper")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, basePath+"\\provider\\service\\src\\main\\resources\\mapper\\xml")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("test_user") // 设置需要生成的表名
                            .addTablePrefix("test_") // 设置过滤表前缀
                            .entityBuilder().enableLombok().enableTableFieldAnnotation().enableFileOverride()
                            .mapperBuilder().mapperAnnotation(org.apache.ibatis.annotations.Mapper.class).enableBaseColumnList().enableBaseResultMap()
                            .serviceBuilder().enableFileOverride()
                            .controllerBuilder().enableFileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateConfig(builder -> {
                    builder.controller(null);
                })
                .execute();
    }
}
