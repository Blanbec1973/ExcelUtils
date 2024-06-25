package renamepsa;

import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Objects;

public class RenamePSA {
    private static final Logger logger = LogManager.getLogger(RenamePSA.class);

    public static void main(String[] args) throws IOException {
        // args[0] : parsing directory
        // args[1] : start file name
        // args[2] & args[3] : sheet & cell for getting prefix

        renamePSA(args);
    }

    static void renamePSA(String[] args) throws IOException {
        logger.info("Beginning RenamePSA");
        File dossier = new File(args[0]);
        FileFilter filter = file -> file.getName().toLowerCase().endsWith(".xlsx") &&
                                    file.getName().startsWith(args[1]);
        File[] listeFichiers = dossier.listFiles(filter);

        try {
            if (listeFichiers.length == 0) {
                logger.info("No file {} to process in {}", args[1], args[0]);
                System.exit(0);
            }
        } catch (NullPointerException e) {
            logger.error(e.getMessage());
        }

        for (File fichier : Objects.requireNonNull(listeFichiers)) {
            logger.info("Fichier : {}", fichier);
            FichierExcel fichierExcel = new FichierExcel(fichier.toString());
            String prefix = fichierExcel.getCellValue(args[2], args[3]);
            fichierExcel.close();
            String newName = args[0] + "\\"+ prefix + "-"+fichier.getName();
            File dest = new File(newName);
            if (fichier.renameTo(dest)) logger.info("Nouveau nom : {}",newName);
        }
    }
}
