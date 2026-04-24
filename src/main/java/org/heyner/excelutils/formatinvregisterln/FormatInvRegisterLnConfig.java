package org.heyner.excelutils.formatinvregisterln;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@ConfigurationProperties(prefix = "formatinvregisterln")
@Slf4j
public class FormatInvRegisterLnConfig {

    @Setter
    private int lastcolumn;
    @Setter
    private List<Integer> nohidecolumns;

    private Set<Integer> noHideSet;

    @PostConstruct
    public void init() {
        log.debug("Config loaded: lastcolumn = " + lastcolumn);
        this.noHideSet = new HashSet<>(nohidecolumns);
    }



}
