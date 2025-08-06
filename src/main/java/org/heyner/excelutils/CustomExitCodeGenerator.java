package org.heyner.excelutils;


import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomExitCodeGenerator implements ExitCodeGenerator {

    private int exitCode = 0;

    public void setExitCode(int exitCode) {
        this.exitCode = exitCode;
    }

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
