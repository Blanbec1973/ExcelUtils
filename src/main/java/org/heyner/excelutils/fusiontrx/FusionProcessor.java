package org.heyner.excelutils.fusiontrx;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Sheet;
import org.heyner.common.excelfile.ExcelFile;
import org.heyner.excelutils.ExcelConstants;
import org.heyner.excelutils.exceptions.FatalApplicationException;
import org.heyner.excelutils.exceptions.FusionSheetMissingException;
import org.heyner.excelutils.exceptions.GracefulExitException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

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

        Path dir = Path.of(directoryToProcess);

        List<Path> trxFiles = listTrxFiles(dir);
        if (trxFiles.isEmpty()) {
            throw new GracefulExitException("No file to process in "+ directoryToProcess,0);
        }

        createFusionFile(outputPath, trxFiles);

    }

    private void createFusionFile(String outputPath, List<Path> trxFiles) {
        Path out = Path.of(outputPath).resolve("FusionTRX.xlsx");
        try (ExcelFile fusion = ExcelFile.create(out.toString())) {

            Sheet sheetFusion = fusion.createSheet("Fusion");
            int rowOffset = 0;
            boolean ignoreFirstLine = false;

            for (Path path : trxFiles) {
                rowOffset = mergeFile(path, sheetFusion, ignoreFirstLine, rowOffset);
                ignoreFirstLine = true;
            }
            fusion.writeFichierExcel();
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(), -1);
        }
    }

    private List<Path> listTrxFiles(Path dir) {
        try (Stream<Path> paths = Files.list(dir)) {
            return paths
                    .filter(p -> p.getFileName().toString().contains("TRX"))
                    .filter(p -> p.getFileName().toString().toLowerCase().endsWith(".xlsx"))
                    .toList();
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(), -1);
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
    private int mergeFile(Path file, Sheet sheetFusion, boolean ignoreFirstLine, int rowOffset) {
        try (ExcelFile excelIn = ExcelFile.open(file.toString())) {
            log.info("File {} opened.",file);
            Sheet sheetIn = excelIn.getWorkBook().getSheet(ExcelConstants.DEFAULT_SHEET);
            if (sheetIn == null) {
                throw new FusionSheetMissingException(file.toString(),-1);
            }
            return excelIn.copySheet(sheetIn, sheetFusion, ignoreFirstLine,rowOffset);
        } catch (FusionSheetMissingException e) {
            log.warn("Skipping file {} due to missing sheet 'sheet1'", file);
            return rowOffset;
        } catch (IOException e) {
            throw new FatalApplicationException(e.getMessage(),-1);
        }
    }

}
