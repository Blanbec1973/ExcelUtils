package org.heyner.excelutils;


import lombok.Setter;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Setter
@Component
public class CustomExitCodeGenerator implements ExitCodeGenerator {

    private int exitCode = 0;

    @Override
    public int getExitCode() {
        return exitCode;
    }
}
