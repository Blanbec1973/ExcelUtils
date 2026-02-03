package org.heyner.excelutils;

/**
 * Classe de constantes utilisées dans les traitements Excel.
 * Centralise les noms de feuilles, cellules, colonnes et formats pour éviter les chaînes en dur.
 */
public final class ExcelConstants {

    private ExcelConstants() {
        // Classe utilitaire, pas d’instanciation
    }

    // Noms de feuilles
    public static final String DEFAULT_SHEET = "sheet1";
    public static final String FUSION_SHEET = "Fusion";
    public static final String DATAS_SHEET = "Datas";

    // Cellules spécifiques
    public static final String TRX_CONTRACT_CELL = "B3";
    public static final String ACTIVITY_CONTRACT_CELL = "G3";

    public static final String INV_REGISTER_LN_CONTRACT_CELL = "B3";

    // Colonnes utilisées
    public static final int FOREIGN_AMOUNT_COLUMN = 29;

    // Formats
    public static final String DATE_TEMPLATE = "aaaa-mm-jj";

    // En-têtes spécifiques
    public static final String AR_HISTORIC_HEADER = "AR Historic by client";

    // Autres constantes métier
    public static final String INV_REGISTER_LN_SHEET = "UC_PCB_MS_INV_REGISTER_LN";
    public static final String ACTIVITY_SHEET = "AR_ITEM_ACTIVITY";
    public static final String TRX_SHEET = "UC_PCB_PROJ_TRX";
}