package org.heyner.excelutils;

import jakarta.annotation.PostConstruct;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Component;

@Component
public class ApachePoiConfigurer {

    @PostConstruct
    public void configure() {
        ZipSecureFile.setMinInflateRatio(0.001);
        IOUtils.setByteArrayMaxOverride(200_000_000);
    }
}

