package obmsinvoicing;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class ObmsInvoice {
    private static final Logger logger = LogManager.getLogger(ObmsInvoice.class);

    private String projectRefence;
    private LocalDate dateFrom;
    private LocalDate dateTo;
    private String orderReference;
    private Float totalAmount;
    private String technicalContact;
    private Float pricePerDay;
    private Float numberOfDays;

    public ObmsInvoice (File file) {
        try {
            new ObmsInvoicePDF(file, this);
        } catch (IOException e) {
            logger.error(e.getMessage());
            System.exit(-1);
        }

    }



    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public String getOrderReference() {
        return orderReference;
    }

    public void setOrderReference(String orderReference) {
        this.orderReference = orderReference;
    }

    public Float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTechnicalContact() {
        return technicalContact;
    }

    public void setTechnicalContact(String technicalContact) {
        this.technicalContact = technicalContact;
    }

    public Float getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Float pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Float getNumberOfDays() {
        return numberOfDays;
    }

    public void setNumberOfDays(Float numberOfDays) {
        this.numberOfDays = numberOfDays;
    }

    public String getProjectRefence() {
        return projectRefence;
    }

    public void setProjectRefence(String projectRefence) {
        this.projectRefence = projectRefence;
    }


    public void check() {
        float result = numberOfDays * pricePerDay ;
        if (result != totalAmount) {
            logger.error("Integrity control KO : nbj * PVJ <> totalAmount.");
            logger.error("nbj = {}, PVJ = {}, totalAmount = {}", numberOfDays, pricePerDay, totalAmount);
        }
    }

    public String buildInvoiceDescription() {
        StringBuilder invoiceDescription = new StringBuilder();
        invoiceDescription.append("Projet : ");
        invoiceDescription.append(projectRefence);
        invoiceDescription.append(", commande : ");
        invoiceDescription.append(orderReference);
        invoiceDescription.append(", ");
        invoiceDescription.append(buildMonthLabel(dateFrom));
        return invoiceDescription.toString();
    }

    private String buildMonthLabel(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRANCE);
        String nomMois = date.format(formatter);
        return nomMois.substring(0,1).toUpperCase()+nomMois.substring(1);
    }
}
