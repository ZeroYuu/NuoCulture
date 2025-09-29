package com.nuoculture.spring_part.config;

import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.io.resource.Resource;
import com.github.houbb.sensitive.word.api.IWordAllow;
import com.github.houbb.sensitive.word.api.IWordDeny;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import com.github.houbb.sensitive.word.support.allow.WordAllows;
import com.github.houbb.sensitive.word.support.deny.WordDenys;
import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Configuration
public class WordDenyConfig {
    private static final Logger logger = LoggerFactory.getLogger(WordDenyConfig.class);

    public class MyWordDeny implements IWordDeny {

        @Override
        public List<String> deny() {
            List<String> list = new ArrayList<>();
            Supplier<String> stringSupplier = () -> "读取敏感词文件错误";
            try (InputStream inputStream = new ClassPathResource("mySensitiveWords.txt").getStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line.trim());
                }
            } catch (IOException ioException) {
                logger.error(stringSupplier, ioException);
            }
            return list;
        }

    }

    public class MyWordAllow implements IWordAllow {

        @Override
        public List<String> allow() {
            List<String> list = new ArrayList<>();
            Supplier<String> stringSupplier = () -> "读取非敏感词文件错误";
            try (InputStream inputStream = new ClassPathResource("myAllowWords.txt").getStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    list.add(line.trim());
                }
            } catch (IOException ioException) {
                logger.error(stringSupplier, ioException);
            }
            return list;
        }

    }


    // 配置默认敏感词 + 自定义敏感词
    IWordDeny wordDeny = WordDenys.chains(WordDenys.system(), new MyWordDeny());
    // 配置默认非敏感词 + 自定义非敏感词
    IWordAllow wordAllow = WordAllows.chains(WordAllows.system(), new MyWordAllow());

    @Bean
    public SensitiveWordBs sensitiveWordBs(){
        return SensitiveWordBs.newInstance()
                // 忽略大小写
                .ignoreCase(true)
                // 忽略半角圆角
                .ignoreWidth(true)
                // 忽略数字的写法
                .ignoreNumStyle(true)
                // 忽略中文的书写格式：简繁体
                .ignoreChineseStyle(true)
                // 忽略英文的书写格式
                .ignoreEnglishStyle(true)
                // 忽略重复词
                .ignoreRepeat(false)
                // 是否启用数字检测
                .enableNumCheck(true)
                // 是否启用邮箱检测
                .enableEmailCheck(true)
                // 是否启用链接检测
                .enableUrlCheck(true)
                // 数字检测，自定义指定长度
                .numCheckLen(8)
                // 配置自定义敏感词
                .wordDeny(wordDeny)
                // 配置非自定义敏感词
                .wordAllow(wordAllow)
                .init();
    }



}
