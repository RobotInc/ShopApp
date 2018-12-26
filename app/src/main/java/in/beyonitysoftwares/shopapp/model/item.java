package in.beyonitysoftwares.shopapp.model;

public class item {


    String productid;
    String name;
    String qty;
    String price;
    String total;
    String gstTax;
    String totalgst;
    boolean pricewithgst;

    public boolean isPricewithgst() {
        return pricewithgst;
    }

    public void setPricewithgst(boolean pricewithgst) {
        this.pricewithgst = pricewithgst;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
