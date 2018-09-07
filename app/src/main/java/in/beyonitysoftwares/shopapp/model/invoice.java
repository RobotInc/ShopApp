package in.beyonitysoftwares.shopapp.model;



import java.util.ArrayList;

public class invoice {
    String date;
    customer billedTo;
    customer shippedTo;
    String invoiceNo;
    String transport;
    String transportMode;

    ArrayList<item> items;


    public invoice(String date, customer billedTo, customer shippedTo, String invoiceNo, String transport, String transportMode, ArrayList<item> items) {
        this.date = date;
        this.billedTo = billedTo;
        this.shippedTo = shippedTo;
        this.invoiceNo = invoiceNo;
        this.transport = transport;
        this.transportMode = transportMode;
        this.items = items;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public customer getBilledTo() {
        return billedTo;
    }

    public void setBilledTo(customer billedTo) {
        this.billedTo = billedTo;
    }

    public customer getShippedTo() {
        return shippedTo;
    }

    public void setShippedTo(customer shippedTo) {
        this.shippedTo = shippedTo;
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

    public String getTransportMode() {
        return transportMode;
    }

    public void setTransportMode(String transportMode) {
        this.transportMode = transportMode;
    }

    public ArrayList<item> getItems() {
        return items;
    }

    public void setItems(ArrayList<item> items) {
        this.items = items;
    }
}
