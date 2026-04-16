package org.heyner.excelutils;

/**
 * Constantes pour les codes d'erreur utilisés dans l'application ExcelUtils.
 * Ces constantes centralisent tous les codes de sortie pour faciliter la maintenance.
 */
public final class ExitCodes {

    /** Succès */
    public static final int SUCCESS = 0;

    /** Erreur générique pour les problèmes fonctionnels/utilisateur */
    public static final int FUNCTIONAL_ERROR = -1;

    /** Erreur de configuration manquante (commande inconnue) */
    public static final int MISSING_CONFIGURATION = 2;

    /** Erreur inattendue/non gérée */
    public static final int UNEXPECTED_ERROR = 1;

    /** Erreur de validation des arguments */
    public static final int INVALID_ARGUMENTS = -1; // Alias de FUNCTIONAL_ERROR

    /** Erreur de traitement de fichier */
    public static final int FILE_PROCESSING_ERROR = -1; // Alias de FUNCTIONAL_ERROR

    /** Erreur fatale d'application */
    public static final int FATAL_ERROR = -1; // Alias de FUNCTIONAL_ERROR

    /**
     * Classe utilitaire non instanciable.
     */
    private ExitCodes() {
        throw new UnsupportedOperationException("Cette classe utilitaire ne peut pas être instanciée");
    }
}
