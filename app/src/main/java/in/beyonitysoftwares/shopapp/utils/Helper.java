package in.beyonitysoftwares.shopapp.utils;

import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import in.beyonitysoftwares.shopapp.activities.mainApp;
import in.beyonitysoftwares.shopapp.config.AppConfig;
import in.beyonitysoftwares.shopapp.fragments.customers;
import in.beyonitysoftwares.shopapp.fragments.invoices;
import in.beyonitysoftwares.shopapp.fragments.products;
import in.beyonitysoftwares.shopapp.model.Invoice;
import in.beyonitysoftwares.shopapp.model.Product;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.model.item;

public class Helper {

    static SortedMap<String,String> statecode = new TreeMap<>();
    static List<customer> customerList = new ArrayList<>();
    static List<Product> productList = new ArrayList<>();
    static List<Invoice> invoiceList = new ArrayList<>();
    static invoices invoice;
    static customers customer;
    static products product;
    private static final String TAG = "Helper";

    public static invoices getInvoice() {
        return invoice;
    }

    public static customers getCustomer() {
        return customer;
    }

    public static products getProduct() {
        return product;
    }

    public static void setupHelper(){
        invoice = new invoices();
        customer = new customers();
        product = new products();

        statecode.put("Andaman and Nicobar Islands","35");
        statecode.put("Andhra Pradesh","37");
        statecode.put("Arunachal Pradesh","12");
        statecode.put("Assam","18");
        statecode.put("Bihar","10");
        statecode.put("Chandigarh","04");
        statecode.put("Chattisgarh","22");
        statecode.put("Dadra and Nagar Haveli","26");
        statecode.put("Daman and Diu","25");
        statecode.put("Delhi","07");
        statecode.put("Goa","30");
        statecode.put("Gujarat","24");
        statecode.put("Haryana","06");
        statecode.put("Himachal Pradesh","02");
        statecode.put("Jammu Kashmir","01");
        statecode.put("Jharkhand","20");
        statecode.put("Karnataka","29");
        statecode.put("Kerala","32");
        statecode.put("Lakshadweep Islands","31");
        statecode.put("Madhya Pradesh","23");
        statecode.put("Maharastra","27");
        statecode.put("Manipur","14");
        statecode.put("Meghalaya","17");
        statecode.put("Mizoram","15");
        statecode.put("Nagaland","13");
        statecode.put("Odisha","21");
        statecode.put("Pondicherry","34");
        statecode.put("Punjab","03");
        statecode.put("Rajasthan","08");
        statecode.put("Sikkim","11");
        statecode.put("Tamil Nadu","33");
        statecode.put("Telengana","36");
        statecode.put("Tripura","16");
        statecode.put("Uttar Pradesh","09");
        statecode.put("Uttarakhand","05");
        statecode.put("West Bengal","19");

    }
    public static SortedMap<String,String> getStatecode(){
        return statecode;
    }

    public static String getCode(String stateName){
        return statecode.get(stateName);
    }

    public static void refreshCustomerList(){
        AndroidNetworking.post(AppConfig.GET_CUSTOMER)
                .addBodyParameter("name","name")
                .setTag("Customers")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        customerList.clear();
                        try {
                            boolean error = response.getBoolean("error");
                            if(!error){
                                JSONArray array = response.getJSONArray("customers");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    customer c = new customer();
                                    c.setName(object.getString("name"));
                                    c.setAddress(object.getString("address"));
                                    customerList.add(c);
                                }

                               //mainApp.refreshCutomers();
                                customer.onResume();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });

    }

    public static void refreshProductList(){
        AndroidNetworking.post(AppConfig.GET_PRODUCT)
                .addBodyParameter("name","name")
                .setTag("Products")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        productList.clear();
                        try {
                            boolean error = response.getBoolean("error");
                            if(!error){
                                JSONArray array = response.getJSONArray("products");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    Product p = new Product();
                                    p.setName(object.getString("name"));
                                    productList.add(p);
                                }

                               // mainApp.refreshProducts();
                                product.onResume();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });

    }
    public static void refreshInvoiceList(){
        AndroidNetworking.post(AppConfig.GET_INVOICE)
                .addBodyParameter("name","name")
                .setTag("Products")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        productList.clear();
                        try {
                            boolean error = response.getBoolean("error");
                            if(!error){
                                JSONArray array = response.getJSONArray("invoices");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    Invoice invoice = new Invoice();
                                    invoice.setInvoiceId(object.getString("id"));
                                    invoice.setInvoiceNo(object.getString("invoice_no"));
                                    invoice.setDate(object.getString("invoice_date"));
                                    invoice.setCustomerid1(object.getString("customerid1"));
                                    invoice.setCustomerid2(object.getString("customerid2"));
                                    invoice.setTransport(object.getString("transport"));
                                    getItemList(object.getString("id"));
                                }

                                // mainApp.refreshProducts();
                                //product.onResume();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });

    }

    public static void getItemList(String invoiceID){
        ArrayList<item> itemList = new ArrayList<>();
        AndroidNetworking.post(AppConfig.GET_INVOICEITEMSBYID)
                .addBodyParameter("id",invoiceID)
                .setTag("Products")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        productList.clear();
                        try {
                            boolean error = response.getBoolean("error");
                            if(!error){
                                JSONArray array = response.getJSONArray("invoiceitems");
                                for(int a = 0;a<array.length();a++){
                                    JSONObject object = array.getJSONObject(a);
                                    item i = new item();
                                    i.setProduct(object.getString("productID"));
                                    i.setQty(object.getString("qty"));
                                    i.setPrice(object.getString("price"));
                                    itemList.add(i);

                                }

                                for(Invoice in : invoiceList){
                                    if(in.getInvoiceId().equals(invoiceID)){
                                        invoiceList.get(invoiceList.indexOf(in)).setItems(itemList);
                                        break;
                                    }
                                }

                                // mainApp.refreshProducts();
                                //product.onResume();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: "+anError.getErrorDetail());
                    }
                });

    }


    public static List<customer> getCustomerList() {
        return customerList;
    }

    public static List<Product> getProductList() {
        return productList;
    }

    public static List<Invoice> getInvoiceList() {
        return invoiceList;
    }
}
