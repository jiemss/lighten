package com.jiem.lighten;

import com.jiem.lighten.common.util.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 提速
 *
 * @author: haojunjie
 * @date: 2023/10/18 21:08
 */
@Slf4j
@SpringBootApplication
public class LightenApplication {
    public static void main(String[] args) {
        System.setProperty("host", IpUtils.getHostIp());
        SpringApplication.run(LightenApplication.class, args);
        log.info("(♥◠‿◠)ﾉﾞ 启动成功   ლ(´ڡ`ლ)ﾞ  ");
    }
}
