package org.heyner.excelutils.fusiontrx;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.ExcelFile;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.exceptions.FatalApplicationException;
import org.heyner.excelutils.exceptions.FusionSheetMissingException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

@Component
@Slf4j
public class FusionProcessor {
    /**
     * Exécute la commande de fusion des fichiers TRX.
     * <p>
     * Cette méthode parcourt un répertoire donné, filtre les fichiers Excel contenant "TRX",
     * et fusionne leur contenu dans un fichier de sortie nommé "FusionTRX.xlsx".
     *
     * @param directoryToProcess le dossier à parcourir
     * @param outputPath le chemin du fichier fusionné
     * @throws GracefulExitException si aucun fichier TRX n’est trouvé
     * @throws FatalApplicationException en cas d’erreur technique lors de la fusion
     */
    public void process(String directoryToProcess, String outputPath) {

        File myDirectory = new File(directoryToProcess);
        FileFilter filter = file -> file.getName().contains("TRX") && file.getName().toLowerCase().endsWith(".xlsx");
        File[] listFiles = myDirectory.listFiles(filter);
        assert listFiles != null;
        if (listFiles.length == 0) {
            throw new GracefulExitException("No file to process in {}"+ directoryToProcess,0);
        }

        try (ExcelFile fusion = new ExcelFile(outputPath + "FusionTRX.xlsx")) {
            Sheet sheetFusion = fusion.createSheet("Fusion");
            int rowOffset = 0;
            boolean ignoreFirstLine = false;

            for (File file : listFiles) {
                rowOffset = mergeFile(file, sheetFusion, ignoreFirstLine, rowOffset);
                ignoreFirstLine = true;
            }
            fusion.writeFichierExcel();
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }

    /**
     * Fusionne le contenu d’un fichier Excel TRX dans une feuille de fusion.
     * <p>
     * Cette méthode ouvre le fichier, vérifie la présence de la feuille "sheet1",
     * et copie son contenu dans la feuille de fusion en respectant l’offset de ligne.
     *
     * @param file le fichier Excel source à fusionner
     * @param sheetFusion la feuille cible dans le fichier de fusion
     * @param ignoreFirstLine indique s’il faut ignorer la première ligne (en-têtes)
     * @param rowOffset l’index de ligne à partir duquel insérer les données
     * @return le nouvel offset de ligne après insertion
     * @throws FatalApplicationException en cas d’erreur d’ouverture ou de lecture du fichier
     * @throws FusionSheetMissingException si la feuille "sheet1" est absente
     */
    private int mergeFile(File file, Sheet sheetFusion, boolean ignoreFirstLine, int rowOffset) {
        try (ExcelFile excelIn = new ExcelFile(file.getAbsolutePath())) {
            log.info("File {} opened.",file.getName());
            Sheet sheetIn = excelIn.getWorkBook().getSheet(ExcelConstants.DEFAULT_SHEET);
            if (sheetIn == null) {
                throw new FusionSheetMissingException(file.getName(),-1);
            }
            return excelIn.copySheet(sheetIn, sheetFusion, ignoreFirstLine,rowOffset);
        } catch (FusionSheetMissingException e) {
            log.warn("Skipping file {} due to missing sheet 'sheet1'", file.getName());
            return rowOffset;
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }

}
