package org.heyner.excelutils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.Arrays;

@SpringBootApplication
@ComponentScan(basePackages = "org.heyner.excelutils")
@Slf4j
public class ExcelUtils {

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        if (log.isInfoEnabled())
            log.info("Main begins with arguments : {}", Arrays.toString(args));

        ConfigurableApplicationContext context = SpringApplication.run(ExcelUtils.class, args);
        int exitCode = SpringApplication.exit(context);

        log.info("Program ends in {} ms.", System.currentTimeMillis()-start);
        System.exit(exitCode);
    }

}
