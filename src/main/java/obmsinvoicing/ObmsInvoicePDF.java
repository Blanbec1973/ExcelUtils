package obmsinvoicing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObmsInvoicePDF {
    private static final Logger logger = LogManager.getLogger(ObmsInvoicePDF.class);
    private final PDDocument pdfDocument;
    private final String[] text;

    public ObmsInvoicePDF(File file, ObmsInvoice obmsInvoice) throws IOException {
        pdfDocument = Loader.loadPDF(file);
        PDFTextStripper pdfStripper = new PDFTextStripper();
        text= pdfStripper.getText(pdfDocument).split("\n");

        if (logger.isDebugEnabled())
            logger.debug(pdfStripper.getText(pdfDocument));

        obmsInvoice.setProjectRefence(findProjectReference());
        obmsInvoice.setDateFrom(findDateFrom());
        obmsInvoice.setDateTo(findDateTo());
        obmsInvoice.setOrderReference(findOrderReference());
        obmsInvoice.setTotalAmount(findTotalAmount());
        obmsInvoice.setTechnicalContact(findTechnicalContact());
        obmsInvoice.setNumberOfDays(findNumberOfDays());
        obmsInvoice.setPricePerDay(findPricePerDay());
    }

    private String findProjectReference() {
        logger.debug ("findProjectReference : {}",text[2]);
        if (logger.isDebugEnabled())
            logger.debug("findProjectReference : {}",text[2].substring(22, 22+16));

        return text[2].substring(22, 22+16);
    }

    private LocalDate dateFormatFromString(String dateEdit) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(dateEdit,formatter);
        } catch (DateTimeParseException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
        return LocalDate.now();
    }
    private LocalDate findDateFrom() {
        logger.debug("findDateFrom : {}",text[3]);
        if (logger.isDebugEnabled())
            logger.debug("findDateFrom : {}",text[3].substring(25,25+10));

        return dateFormatFromString(text[3].substring(25,25+10));
    }

    private LocalDate findDateTo() {
        logger.debug("findDateTo : {}",text[3]);
        if (logger.isDebugEnabled())
            logger.debug("findDateTo : {}",text[3].substring(39,39+10));

        return dateFormatFromString(text[3].substring(39,39+10));
    }

    private String findOrderReference() {
        logger.debug ("findOrderReference : {}",text[4]);
        if (logger.isDebugEnabled())
            logger.debug("findOrderReference : {}",text[4].substring(21, 21+13));

        return text[4].substring(21, 21+13);
    }

    private Float amountFormatToString(String amountString) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.FRANCE);
        symbols.setGroupingSeparator(' ');
        symbols.setDecimalSeparator('.');
        String pattern = "#,#";
        DecimalFormat decimalFormat=new DecimalFormat(pattern,symbols);
        try {
            Number number = decimalFormat.parse(amountString);
            return number.floatValue();
        } catch (ParseException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }
        return Float.valueOf(0);
    }
    private Float findTotalAmount() {
        logger.debug ("findTotalAmount : {}",text[5]);
        Pattern pattern = Pattern.compile(":\\s*([\\d\\s]+)\\s*€");
        Matcher matcher = pattern.matcher(text[5]);

        if (matcher.find()) {
            // Retourner le montant trouvé sans les espaces
            String findAmount =  matcher.group(1).replaceAll("\\s", "");
            logger.debug("findTotalAmount : {}",findAmount);
            return Float.parseFloat(findAmount);
        }

        return Float.valueOf(0);
    }

    private String findTechnicalContact() {
        logger.debug ("findTechnicalContact : {}",text[7]);
        if (logger.isDebugEnabled())
            logger.debug ("findTechnicalContact : {}",text[7].substring(26));

        return text[7].substring(26);
    }
    private Float findPricePerDay() {
        logger.debug ("findPricePerDay : {}",text[14]);
        Pattern pattern = Pattern.compile("x\\s*([\\d\\s]+)\\s*]");
        Matcher matcher = pattern.matcher(text[14]);

        if (matcher.find()) {
            // Retourner le montant trouvé sans les espaces
            String findPrice =  matcher.group(1).replaceAll("\\s", "");
            logger.debug("findPricePerDay : {}",findPrice);
            return Float.parseFloat(findPrice);
        }

        return Float.valueOf(0);
    }

    private Float findNumberOfDays() {
        logger.debug ("findNumberOfDays : {}", text[14]);
        Pattern pattern = Pattern.compile("\\[\\s*(\\d+)\\s*x");
        Matcher matcher = pattern.matcher(text[14]);

        if (matcher.find()) {
            // Retourner le montant trouvé sans les espaces
            String numberOfDays =  matcher.group(1).replaceAll("\\s", "");
            logger.debug("findNumberOfDays : {}", numberOfDays);
            return Float.parseFloat(numberOfDays);
        }

        return Float.valueOf(0);
    }










}
