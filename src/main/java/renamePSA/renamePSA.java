package renamePSA;

import commun.FichierExcel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Objects;

public class renamePSA {
    private static final Logger logger = LogManager.getLogger(renamePSA.class);

    public static void main(String[] args) throws IOException {
        logger.info("Démarrage RenamePSA");
        // args[0] : parsing directory
        // args[1] : start file name
        // args[2] & args[3] : sheet & cell for getting prefix

        File dossier = new File(args[0]);
        FileFilter filter = (file) -> file.getName().toLowerCase().endsWith(".xlsx") &&
                                      file.getName().startsWith(args[1]);
        File[] listeFichiers = dossier.listFiles(filter);
        if (listeFichiers.length == 0) {
            logger.info("No file "+args[1] + " to process in "+args[0]);
            System.exit(0);
        }

        for (File fichier : Objects.requireNonNull(listeFichiers)) {
            logger.info("Fichier : " + fichier.toString());
            FichierExcel fichierExcel = new FichierExcel(fichier.toString());
            String prefix = fichierExcel.getCellValue(args[2],args[3]);
            fichierExcel.close();
            String newName = args[0] + "\\"+ prefix + "-"+fichier.getName();
            File dest = new File(newName);
            if (fichier.renameTo(dest)) logger.info("Nouveau nom : " + newName);
        }
    }
}
