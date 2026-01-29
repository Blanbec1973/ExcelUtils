package org.heyner.excelutils.formatinvregisterln;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ConfigurationProperties(prefix = "formatinvregisterln")
@Slf4j
public class FormatInvRegisterLnConfig {

    private int lastcolumn;
    private List<Integer> nohidecolumns;

    private Set<Integer> noHideSet;

    public int getLastcolumn() {
        return lastcolumn;
    }

    public void setLastcolumn(int lastcolumn) {
        this.lastcolumn = lastcolumn;
    }

    public List<Integer> getNohidecolumns() {
        return nohidecolumns;
    }

    public void setNohidecolumns(List<Integer> nohidecolumns) {
        this.nohidecolumns = nohidecolumns;
    }
    public Set<Integer> getNoHideSet() {
        return noHideSet;
    }

    @PostConstruct
    public void init() {
        log.debug("Config loaded: lastcolumn = " + lastcolumn);
        this.noHideSet = new HashSet<>(nohidecolumns);
    }



}
