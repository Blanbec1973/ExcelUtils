
package org.heyner.excelutils.directoryparser;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;

@Component
public class DirectoryLister {

    public File[] listXlsx(String directory) {
        File dir = new File(directory);
        FileFilter filter = file -> file.getName().toLowerCase().endsWith(".xlsx");
        File[] files = dir.listFiles(filter);
        return (files == null) ? new File[0] : files;
    }
}