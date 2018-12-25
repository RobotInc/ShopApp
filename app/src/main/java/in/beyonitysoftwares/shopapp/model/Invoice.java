package in.beyonitysoftwares.shopapp.model;



import java.util.ArrayList;
import java.util.List;

public class Invoice {
    String invoiceId;
    String date;
    String customerid1;
    String customerid2;
    String invoiceNo;
    String transport;
    String discount;
    String others;
    String bales;
    ArrayList<item> items = new ArrayList<>();

    public String getBales() {
        return bales;
    }

    public void setBales(String bales) {
        this.bales = bales;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getOthers() {
        return others;
    }

    public void setOthers(String others) {
        this.others = others;
    }

    public String getCustomerid1() {
        return customerid1;
    }

    public void setCustomerid1(String customerid1) {
        this.customerid1 = customerid1;
    }

    public String getCustomerid2() {
        return customerid2;
    }

    public void setCustomerid2(String customerid2) {
        this.customerid2 = customerid2;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }



    public Invoice() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }


    public ArrayList<item> getItems() {
        return items;
    }

    public void setItems(ArrayList<item> items) {
        this.items = items;
    }
}
