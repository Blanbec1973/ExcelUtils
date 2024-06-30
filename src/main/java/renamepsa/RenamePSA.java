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

        RenamePSA.action(args);
    }

    public static void action(String[] args) throws IOException {
        logger.info("Beginning RenamePSA : {}",args[0]);
        File dossier = new File(args[0]);
        FileFilter filter = file -> file.getName().toLowerCase().endsWith(".xlsx") &&
                                    file.getName().startsWith(args[1]);
        File[] listeFichiers = dossier.listFiles(filter);

        try {
            assert listeFichiers != null;
            if (listeFichiers.length == 0) {
                logger.info("No file {} to process in {}", args[1], args[0]);
                System.exit(0);
            }
        } catch (NullPointerException | AssertionError e) {
            logger.error(e.getMessage());
        }

        for (File fichier : Objects.requireNonNull(listeFichiers)) {
            logger.info("Fichier : {}", fichier);
            if (checkAbsenceOfPrefix(fichier)) {
                String prefix = retrievePrefix(args, fichier);
                String newName = args[0] + "\\"+ prefix + "-"+fichier.getName();
                renameFile(fichier, newName);
            }
            else {
                logger.info("Existing prefix in file name ==> No action.");
            }
        }
    }

    public static boolean checkAbsenceOfPrefix(File fichier) {
        String fileName = fichier.getName();
        if (fileName.length()<15) return true;
        String prefix = fileName.substring(0,15);
        return (!prefix.matches("\\d+")) ;
    }

    private static void renameFile(File fichier, String newName) {
        File dest = new File(newName);
        if (fichier.renameTo(dest)) logger.info("Nouveau nom : {}", newName);
    }

    private static String retrievePrefix(String[] args, File fichier) throws IOException {
        FichierExcel fichierExcel = new FichierExcel(fichier.toString());
        String prefix = fichierExcel.getCellValue(args[2], args[3]);
        fichierExcel.close();
        return prefix;
    }
}
