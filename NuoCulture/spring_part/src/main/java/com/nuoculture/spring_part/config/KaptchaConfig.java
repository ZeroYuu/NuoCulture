package com.nuoculture.spring_part.config;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.Properties;

/**
 * 验证码的配置
 */
@Configuration
public class KaptchaConfig {

    @Bean
    public DefaultKaptcha getDefaultKaptcha(){
        //创建一个验证码的实例
        DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
        //创建属性的实例
        Properties properties = new Properties();
        //设置属性
        //设置边框
        properties.setProperty("kaptcha.border","yes");
        //设置边框的颜色
        properties.setProperty("kaptcha.border.color","105,179,90");
        //设置背景颜色
        //背景颜色渐变开始
         properties.setProperty("kaptcha.background.clear.from","29,67,80");
        //背景颜色渐变结束
        properties.setProperty("kaptcha.background.clear.to","164,57,49");
        //字体颜色
        properties.setProperty("kaptcha.textproducer.font.color","233,231,239");
        //文字间隔
        properties.setProperty("kaptcha.textproducer.char.space","10");
        //设置验证码的长度
        properties.setProperty("kaptcha.textproducer.char.length","4");
        //设置图片的宽度
        properties.setProperty("kaptcha.image.width","200");
        //设置图片的高度
        properties.setProperty("kaptcha.image.height","50");
        //设置字体样式
        properties.setProperty("kaptcha.textproducer.font.names","楷体");
        //设置字符串,验证码就是从这里面产生
        properties.setProperty("kaptcha.textproducer.char.string","0123456789abcdefghijkmnopqrstuvwxyz");
        //设置去掉干扰线
//        properties.setProperty("kaptcha.noise.impl","com.google.code.kaptcha.impl.NoNoise");
        //设置字体大小
        properties.setProperty("kaptcha.textproducer.font.size","35");
        //将properties包装到配置中
        Config config = new Config(properties);
        //将配置设置到创建的验证码实例defaultKaptcha
        defaultKaptcha.setConfig(config);
        //返回
        return defaultKaptcha;
    }

    /*public static void main(String[] args) throws Exception{

        KaptchaConfig config = new KaptchaConfig();
        DefaultKaptcha defaultKaptcha = config.getDefaultKaptcha();

        String text = defaultKaptcha.createText();
        System.out.println("text = " + text);

        BufferedImage image = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);


        Base64.Encoder encoder = Base64.getEncoder();
        String base64 = encoder.encodeToString(outputStream.toByteArray());

        String str = "data:image/jpeg;base64,"+base64.replaceAll("\r\n","");
        System.out.println(str);

        //ImageIO.write()



    }*/
}
