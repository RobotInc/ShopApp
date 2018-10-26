package in.beyonitysoftwares.shopapp.model;

public class item {


    String productid;
    String qty;
    String price;
    String total;
    String gstTax;
    String totalgst;

    public item() {
    }

    public String getGstTax() {
        return gstTax;
    }

    public void setGstTax(String gstTax) {
        this.gstTax = gstTax;
    }

    public String getTotalgst() {
        return totalgst;
    }

    public void setTotalgst(String totalgst) {
        this.totalgst = totalgst;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public item(String productid, String qty, String price, String total, String gstTax, String totalgst) {

        this.productid = productid;

        this.qty = qty;
        this.price = price;
        this.total = total;
        this.gstTax = gstTax;
        this.totalgst = totalgst;
    }


    public String getProduct() {
        return productid;
    }

    public void setProduct(String product) {
        this.productid = product;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
