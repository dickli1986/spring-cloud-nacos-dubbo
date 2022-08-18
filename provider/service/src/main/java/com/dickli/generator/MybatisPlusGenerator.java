package com.dickli.generator;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class MybatisPlusGenerator {
    public static void main(String[] args) {
        System.out.println(System.getProperty("user.dir"));

        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/dickli", "dickli", "dickli")
                .globalConfig(builder -> {
                    builder.author("dickli") // 设置作者
                            .disableOpenDir()
                            .outputDir(System.getProperty("user.dir") + "\\provider\\service\\src\\main\\java\\"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.dickli") // 设置父包名
                            .entity("model")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, System.getProperty("user.dir") + "\\provider\\service\\src\\main\\resources\\xml\\")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("test_user") // 设置需要生成的表名
                            .addTablePrefix("test_")// 设置过滤表前缀
                            .mapperBuilder().mapperAnnotation(org.apache.ibatis.annotations.Mapper.class).enableBaseResultMap().enableBaseColumnList().enableFileOverride()
                            .entityBuilder().enableLombok().enableFileOverride().enableTableFieldAnnotation()
                            .serviceBuilder().enableFileOverride();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateConfig(builder -> builder.controller(""))
                .execute();
    }
}
