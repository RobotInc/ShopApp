package in.beyonitysoftwares.shopapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.util.TextUtil;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfName;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.annot.PdfAnnotation;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.adapters.customerAdapter;
import in.beyonitysoftwares.shopapp.adapters.itemAdapter;
import in.beyonitysoftwares.shopapp.model.Invoice;
import in.beyonitysoftwares.shopapp.model.Product;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.model.item;
import in.beyonitysoftwares.shopapp.utils.FileUtils;
import in.beyonitysoftwares.shopapp.utils.Helper;
import in.beyonitysoftwares.shopapp.utils.NumbersToWords;

public class new_invoice extends AppCompatActivity {
    DecimalFormat df = new DecimalFormat("#.00");
    EditText invoiceNo, date, bales, discount, others;
    private static final String TAG = "new_invoice";
    TextView yearview;
    String[] tranposrts = {"Select Transport", "VRL", "SRMT", "SLRT", "NAVATHA", "APSRTC", "TSRTC", "HAND"};
    String selectedTrans = "";
    long datetime = 0;
    int discountvalue = -1, othervalues = -1;
    List<customer> customerList = new ArrayList<>();
    List<Product> productList = new ArrayList<>();
    Spinner transportspinner, customerid1, customerid2;
    Calendar myCalendar = Calendar.getInstance();
    RecyclerView rv;
    List<item> itemList = new ArrayList<>();
    itemAdapter itemadapter;
    DatePickerDialog.OnDateSetListener datepicker = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_invoice);
        customer dummy = new customer();
        dummy.setName("Select Customer");
        customerList.add(dummy);
        customerList.addAll(Helper.getCustomerList());
        invoiceNo = (EditText) findViewById(R.id.invoiceno);
        bales = (EditText) findViewById(R.id.bales);
        bales.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.equals("0")){
                    bales.setError("Bales cannot be zero");
                    bales.setText("");

                }
            }
        });
        yearview = (TextView) findViewById(R.id.yearview);
        date = (EditText) findViewById(R.id.invoicedate);
        date.setFocusable(false);
        discount = (EditText) findViewById(R.id.discount);
        others = (EditText) findViewById(R.id.others);
        String myFormat = "MMM dd yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(new Date()));
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(new_invoice.this, datepicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        transportspinner = (Spinner) findViewById(R.id.spinnertransport);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, tranposrts);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        transportspinner.setAdapter(adapter);
        transportspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTrans = transportspinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        customerid1 = (Spinner) findViewById(R.id.spinnerbillto);
        ArrayAdapter<String> cus1adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, customerList);
        Log.d(TAG, "onCreate: " + Helper.getCustomerList().size());
// Specify the layout to use when the list of choices appears
        cus1adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        customerid1.setAdapter(cus1adapter);
        customerid1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        customerid2 = (Spinner) findViewById(R.id.spinnershipto);
        ArrayAdapter<String> cus2adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, customerList);
        Log.d(TAG, "onCreate: " + Helper.getCustomerList().size());
// Specify the layout to use when the list of choices appears
        cus2adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        customerid2.setAdapter(cus2adapter);
        customerid2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        itemadapter = new itemAdapter(itemList, getApplicationContext());
        rv = (RecyclerView) findViewById(R.id.addproductrv);
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL));
        rv.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(itemadapter);
        Product p = new Product();
        p.setName("Select Product");
        productList.add(p);
        productList.addAll(Helper.getProductList());

    }

    private void updateLabel() {
        String myFormat = "MMM dd yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        date.setText(sdf.format(myCalendar.getTime()));
        datetime = myCalendar.getTimeInMillis();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void back(View view) {
        finish();
    }

    public void add(View view) {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.addproduct);

        Spinner addpro = (Spinner) dialog.findViewById(R.id.addpro);
        ArrayAdapter<String> adapter = new ArrayAdapter(this,
                android.R.layout.simple_spinner_item, productList);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        addpro.setAdapter(adapter);

        EditText qty = (EditText) dialog.findViewById(R.id.qty);
        EditText price = (EditText) dialog.findViewById(R.id.price);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button add = (Button) dialog.findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addpro.getSelectedItemPosition() != 0) {
                    if (!TextUtils.isEmpty(qty.getText())) {
                        if (!TextUtils.isEmpty(price.getText())) {
                            item i = new item();
                            i.setProductid(productList.get(addpro.getSelectedItemPosition()).getId());
                            i.setProduct(productList.get(addpro.getSelectedItemPosition()).getName());
                            Log.d(TAG, "onClick: " + productList.get(addpro.getSelectedItemPosition()).getName());
                            i.setQty(qty.getText().toString().trim());
                            i.setPrice(price.getText().toString().trim());

                            itemList.add(i);
                            itemadapter.notifyDataSetChanged();
                            dialog.dismiss();

                        } else {
                            Toast.makeText(new_invoice.this, "Please Enter the Price", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(new_invoice.this, "Please Enter the Quantity", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(new_invoice.this, "Please Select the product", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();


    }

    public void preview(View view) {
        //createPdf(FileUtils.getAppPath(getApplicationContext()) + "123.pdf");
        if (!TextUtils.isEmpty(invoiceNo.getText())) {
            if (transportspinner.getSelectedItemPosition() != 0) {
                if (customerid1.getSelectedItemPosition() != 0) {
                    if (customerid2.getSelectedItemPosition() != 0) {
                        if (itemList.size() != 0) {

                            //FileUtils.getAppPath(getApplicationContext()) + "123.pdf";
                            if (!TextUtils.isEmpty(bales.getText())) {


                                if (!TextUtils.isEmpty(discount.getText())) {
                                    if (!TextUtils.isEmpty(others.getText())) {
                                        discountvalue = Integer.parseInt(discount.getText().toString());
                                        othervalues = Integer.parseInt(others.getText().toString());
                                        Invoice invoice = new Invoice();
                                        invoice.setInvoiceNo(invoiceNo.getText().toString());
                                        invoice.setCustomerid1(String.valueOf(customerList.get(customerid1.getSelectedItemPosition()).getId()));
                                        invoice.setCustomerid2(String.valueOf(customerList.get(customerid2.getSelectedItemPosition()).getId()));
                                        invoice.setDate(String.valueOf(myCalendar.getTimeInMillis()));
                                        invoice.setTransport(tranposrts[transportspinner.getSelectedItemPosition()]);
                                        invoice.setDiscount(String.valueOf(discountvalue));
                                        invoice.setOthers(String.valueOf(othervalues));
                                        invoice.setItems((ArrayList<item>) itemList);
                                        invoice.setBales(bales.getText().toString());
                                        createPdf(FileUtils.getAppPath(getApplicationContext()) + invoice.getInvoiceNo(), invoice);
                                    } else {
                                        discount.setError("Enter other charges if any otherwise enter 0");
                                        Toast.makeText(new_invoice.this, "Please Enter the Price", Toast.LENGTH_SHORT).show();
                                    }
                                } else {

                                    discount.setError("Enter discount if any otherwise enter 0");
                                    Toast.makeText(new_invoice.this, "Please Enter the Quantity", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                bales.setError("Please Enter No of Bales");
                                Toast.makeText(this, "Please Enter No of Bales", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(this, "Please add Purchased Items", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Select Shipping to Customer", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Select Billing to customer", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Select Transport", Toast.LENGTH_SHORT).show();
            }
        } else {
            invoiceNo.setError("Please Enter Invoice No.");
        }
    }


    public void createPdf(String dest, Invoice invoice) {


        if (new File(dest).exists()) {
            new File(dest).delete();
        }
        PdfWriter writer = null;
        try {
            writer = new PdfWriter(dest);

            // Creating a PdfDocument
            PdfDocument pdf = new PdfDocument(writer);
            PageSize ps = PageSize.A4.clone();
            pdf.addNewPage(ps);







            // Define a PdfCanvas instance
            //PdfCanvas canvas = new PdfCanvas(pdf.getFirstPage());
            // Add a rectangle
            //canvas.roundRectangle(20, 20, width - 40, height - 40, 5);
            //  canvas.stroke();
            // Close PdfDocument

            // Creating a Document
            Document document = new Document(pdf);



            Table table = new Table(2);


            Cell topleft = new Cell();
       /* Style gstStyle = new Style();
        PdfFont font = PdfFontFactory.createFont("/assets/fonts/brandon_medium.otf");
        gstStyle.setFont(font).setFontSize(15);*/
            Style titleStyle = new Style();
            PdfFont titleFont = PdfFontFactory.createFont(FontConstants.HELVETICA_BOLD);
            //titleStyle.setFont(titleFont).setFontSize(18);
            titleStyle.setFont(titleFont).setFontSize(15);


            Style normalStyle = new Style();
            PdfFont normalFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
            //normalStyle.setFont(titleFont).setFontSize(18);
            normalStyle.setFont(normalFont).setFontSize(11);


            Paragraph titletext = new Paragraph("M.M. TEXTILES");
            titletext.addStyle(titleStyle);


            Paragraph address1 = new Paragraph("Prop: R.Mohan, #4/217, Utlaman Street,");
            Paragraph address2 = new Paragraph("Pamidi-515775, Anantapur Dist.(A.P.)");

            topleft.add(titletext);
            topleft.add(address1);
            topleft.add(address2);
            topleft.add("GSTIN: 37CUMPM0476N1ZW");
            topleft.add("Cell: 9505953126");
            // cell.addStyle(gstStyle);
            topleft.setTextAlignment(TextAlignment.LEFT);
            topleft.setBorder(Border.NO_BORDER);


            Cell topright = new Cell();
            Paragraph titleright = new Paragraph("GST INVOICE");
            titleright.addStyle(titleStyle);

            String[] copy = {"Original Copy","Duplicate Copy"};

            topright.add(titleright);
            topright.add("GST Invoice No: " + invoice.getInvoiceNo() + "/2018-19");
            topright.add("Date: " + date.getText().toString());
            topright.add(copy[0]);
            topright.add("Transport: " + invoice.getTransport());




            topright.setTextAlignment(TextAlignment.RIGHT);
            topright.setBorder(Border.NO_BORDER);
            table.addCell(topleft);
            table.addCell(topright);

            topright.addStyle(normalStyle);
            titleright.addStyle(titleStyle);


            Table shippingTable = new Table(2);

            Cell billedTo = new Cell();

            billedTo.add(new Paragraph().add("BILL TO").addStyle(titleStyle));

            Paragraph billingPara = new Paragraph();
            Paragraph shippingPara = new Paragraph();


            billingPara.addStyle(normalStyle);
            shippingPara.addStyle(normalStyle);
            customer c1 = customerList.get(customerid1.getSelectedItemPosition());
            customer c2 = customerList.get(customerid2.getSelectedItemPosition());
            Log.d(TAG, "createPdf on: "+c1.getState());
            billingPara.add("Name: " + c1.getName() + "\nAddress: " + c1.getAddress() + "\nGSTIN: " + c1.getGstin() + "\nState: " + c1.getState() + "(" + Helper.getCode(c1.getState()) + ")\nCell: " + c1.getPhone());
            shippingPara.add("Name: " + c2.getName() + "\nAddress: " + c2.getAddress() + "\nGSTIN: " + c2.getGstin() + "\nState: " + c2.getState() + "(" + Helper.getCode(c2.getState()) + ")\nCell: " + c2.getPhone());


            billedTo.add(billingPara);
            billedTo.add(new Paragraph("\n From : Pamidi").setFont(titleFont).setFontSize(12));


            billedTo.setBorder(Border.NO_BORDER);
            Cell shippedTo = new Cell();
            shippedTo.setPaddingLeft(75f);
            shippedTo.add(new Paragraph().add("SHIP TO").addStyle(titleStyle));

            shippedTo.add(shippingPara);
            shippedTo.add(new Paragraph("\n To : " + c2.getAddress()).setFont(titleFont).setFontSize(12));


            shippedTo.setBorder(Border.NO_BORDER);
            shippingTable.addCell(billedTo);
            shippingTable.addCell(shippedTo);


            float[] columnWidths = {20, 65, 20, 20, 55};
            Table itemTable = new Table(columnWidths)
                    .setWidthPercent(100)
                    .setFixedLayout();

            itemTable.addStyle(normalStyle);

            Cell sNoHeader = new Cell();
            sNoHeader.add("SNo.").setTextAlignment(TextAlignment.CENTER).setFont(titleFont);

            Cell descriptionHeader = new Cell();
            descriptionHeader.add("Description").setTextAlignment(TextAlignment.CENTER).setFont(titleFont);


            Cell qtyHeader = new Cell().setTextAlignment(TextAlignment.CENTER);
            qtyHeader.add("qty").setTextAlignment(TextAlignment.CENTER).setFont(titleFont);
            Cell priceHeader = new Cell();
            priceHeader.add("Price").setTextAlignment(TextAlignment.CENTER).setFont(titleFont);
            Cell amountHeader = new Cell();
            amountHeader.add("Amount").setTextAlignment(TextAlignment.CENTER).setFont(titleFont);


            itemTable.addHeaderCell(sNoHeader);
            itemTable.addHeaderCell(descriptionHeader);

            itemTable.addHeaderCell(qtyHeader);
            itemTable.addHeaderCell(priceHeader);
            itemTable.addHeaderCell(amountHeader);

            Cell sNo = new Cell();
            sNo.setTextAlignment(TextAlignment.CENTER);


            Cell des = new Cell();
            des.setTextAlignment(TextAlignment.CENTER);

            Cell qty = new Cell();
            qty.setTextAlignment(TextAlignment.CENTER);

            Cell price = new Cell();
            price.setTextAlignment(TextAlignment.CENTER);

            Cell amount = new Cell();
            amount.setTextAlignment(TextAlignment.RIGHT);
            amount.setPaddingRight(10f);

            int count = 0;
            int qtyTotal = 0;
            double amountTotal = 0;
            for (item i : itemList) {
                sNo.add(String.valueOf(++count));
                des.add(i.getProduct());
                qty.add(i.getQty());
                qtyTotal = qtyTotal + Integer.parseInt(i.getQty());
                double total = Double.parseDouble(i.getQty()) * Double.parseDouble(i.getPrice());
                amountTotal = amountTotal + total;
                price.add(String.valueOf(df.format(Double.parseDouble(i.getPrice()))));
                amount.add(String.valueOf(df.format(total)));
            }

            itemTable.addCell(sNo);
            itemTable.addCell(des);
            itemTable.addCell(qty);
            itemTable.addCell(price);
            itemTable.addCell(amount);

            float[] columnWidthsTotal = {20, 65, 20, 20, 55};
            Table totalTable = new Table(columnWidthsTotal)
                    .setWidthPercent(100)
                    .setFixedLayout();
            totalTable.addStyle(normalStyle);
            Cell TotalCell = new Cell();
            TotalCell.setTextAlignment(TextAlignment.CENTER);
            TotalCell.add("Total");

            Cell noOfBales = new Cell();
            noOfBales.setTextAlignment(TextAlignment.CENTER);
            noOfBales.add(String.valueOf(invoice.getBales()+" (Bales)"));

            Cell noOfItems = new Cell();
            noOfItems.setTextAlignment(TextAlignment.CENTER);
            noOfItems.add(String.valueOf(qtyTotal));


            Cell totalAmount = new Cell();
            totalAmount.setTextAlignment(TextAlignment.RIGHT);
            totalAmount.setPaddingRight(10f);
            totalAmount.add(String.valueOf(df.format(amountTotal)));


            totalTable.addCell(TotalCell);
            totalTable.addCell(noOfBales);
            totalTable.addCell(noOfItems);
            totalTable.addCell(new Cell().setBorderRight(Border.NO_BORDER));
            totalTable.addCell(totalAmount.setBorderLeft(Border.NO_BORDER));

            float[] columnWidthsGst = {150, 45};
            Table gstTable = new Table(columnWidthsGst)
                    .setWidthPercent(100)
                    .setFixedLayout();

            Cell accountsCell = new Cell();
            accountsCell.setTextAlignment(TextAlignment.CENTER);
            accountsCell.add("\n\nAxis Bank\n917020068698671\nIFSC UTIB0000332").addStyle(normalStyle);


            Cell gstcell = new Cell();
            gstcell.addStyle(normalStyle);

            float[] columnWidthsigst = {50, 85};
            Table igstTable = new Table(columnWidthsigst)
                    .setWidthPercent(100)
                    .setFixedLayout()
                    .setMarginLeft(-2f)
                    .setMarginRight(-2f);

            Table discount = new Table(columnWidthsigst)
                    .setWidthPercent(100)
                    .setFixedLayout()
                    .setMarginLeft(-2f)
                    .setMarginTop(-2f)
                    .setMarginRight(-2f);


            Table subtotal = new Table(columnWidthsigst)
                    .setWidthPercent(100)
                    .setFixedLayout()
                    .setMarginLeft(-2f)
                    .setMarginRight(-2f);


            Table cgstTable = new Table(columnWidthsigst)
                    .setWidthPercent(100)
                    .setFixedLayout()
                    .setMarginLeft(-2f)
                    .setMarginRight(-2f);

            Table sgstTable = new Table(columnWidthsigst)
                    .setWidthPercent(100)
                    .setFixedLayout()
                    .setMarginLeft(-2f)
                    .setMarginRight(-2f);

            Table extra = new Table(new float[]{9, 42})
                    .setWidthPercent(100)
                    .setMarginBottom(-3f)
                    .setFixedLayout();

            Cell discountcell = new Cell();
            discountcell.add("Discount");
            discountcell.setBorder(Border.NO_BORDER);
            discountcell.setBorderBottom(new SolidBorder(.5f));
            discountcell.setBorderRight(new SolidBorder(.5f));

            Cell discountValue = new Cell();
            discountValue.setTextAlignment(TextAlignment.RIGHT);
            discountValue.setPaddingRight(10f);

            discountValue.setBorder(Border.NO_BORDER);
            discountValue.setBorderBottom(new SolidBorder(.5f));


            Cell igst = new Cell();

            igst.add("IGST\t5%");
            igst.setBorder(Border.NO_BORDER);
            igst.setBorderBottom(new SolidBorder(.5f));
            igst.setBorderRight(new SolidBorder(.5f));

            Cell igstValue = new Cell();
            igstValue.setTextAlignment(TextAlignment.RIGHT);
            igstValue.setPaddingRight(12f);

            igstValue.setBorder(Border.NO_BORDER);
            igstValue.setBorderBottom(new SolidBorder(.5f));

            Cell subcell = new Cell();
            subcell.add("Subtotal");
            subcell.setBorder(Border.NO_BORDER);
            subcell.setBorderBottom(new SolidBorder(.5f));
            subcell.setBorderRight(new SolidBorder(.5f));
            Cell subValue = new Cell();
            subValue.setTextAlignment(TextAlignment.RIGHT);
            subValue.setPaddingRight(10f);

            subValue.setBorder(Border.NO_BORDER);
            subValue.setBorderBottom(new SolidBorder(.5f));


            Cell cgst = new Cell();
            cgst.add("CGST\t2.5%");
            cgst.setBorder(Border.NO_BORDER);
            cgst.setBorderBottom(new SolidBorder(.5f));
            cgst.setBorderRight(new SolidBorder(.5f));
            Cell cgstValue = new Cell();
            cgstValue.setTextAlignment(TextAlignment.RIGHT);
            cgstValue.setPaddingRight(11.5f);

            cgstValue.setBorder(Border.NO_BORDER);
            cgstValue.setBorderBottom(new SolidBorder(.5f));

            Cell sgst = new Cell();
            sgst.add("SGST\t2.5%");
            sgst.setBorder(Border.NO_BORDER);
            sgst.setBorderBottom(new SolidBorder(.5f));
            sgst.setBorderRight(new SolidBorder(.5f));
            Cell sgstValue = new Cell();
            sgstValue.setTextAlignment(TextAlignment.RIGHT);
            sgstValue.setPaddingRight(11f);

            sgstValue.setBorder(Border.NO_BORDER);
            sgstValue.setBorderBottom(new SolidBorder(.5f));

            Cell extraCharges = new Cell();
            extraCharges.add("Other Charges");
            extraCharges.setBorder(Border.NO_BORDER);

            Cell extraValue = new Cell();
            extraValue.setTextAlignment(TextAlignment.RIGHT);
            extraValue.setPaddingRight(10f);

            extraValue.setBorder(Border.NO_BORDER);
            extraValue.setBorderLeft(new SolidBorder(.5f));

            double TotalInvoice = 0;
            double twopointfive = 0;
            double five = 0;
        if(Helper.getCode(c2.getState()).equals("37")){
            twopointfive = ((amountTotal-discountvalue)*2.5)/100;
            igstValue.add("0.00");
            cgstValue.add(String.valueOf(df.format(twopointfive)));
            sgstValue.add(String.valueOf(df.format(twopointfive)));
            extraValue.add(df.format(Double.parseDouble(invoice.getOthers())));
            five = twopointfive*2;
            Log.d(TAG, "createPdf: "+five);
        }else {
            five = ((amountTotal-discountvalue)*5)/100;
            igstValue.add(String.valueOf((df.format(five))));
            cgstValue.add("0.00");
            sgstValue.add("0.00");
            extraValue.add(df.format(Double.parseDouble(invoice.getOthers())));
        }



            discountValue.add(String.valueOf(df.format(Double.parseDouble(invoice.getDiscount()))));
            subValue.add(String.valueOf(df.format(amountTotal-discountvalue)));
            discount.addCell(discountcell);
            discount.addCell(discountValue);
            subtotal.addCell(subcell);
            subtotal.addCell(subValue);
            igstTable.addCell(igst);
            igstTable.addCell(igstValue);
            cgstTable.addCell(cgst);
            cgstTable.addCell(cgstValue);
            sgstTable.addCell(sgst);
            sgstTable.addCell(sgstValue);
            extra.addCell(extraCharges);
            extra.addCell(extraValue);

            gstcell.setMarginLeft(2f);
            gstcell.add(discount);
            gstcell.add(subtotal);
            gstcell.add(cgstTable);
            gstcell.add(sgstTable);
            gstcell.add(igstTable);
            gstcell.add(extra.setMarginLeft(-2f));


            gstTable.addCell(accountsCell);
            gstTable.addCell(gstcell);

            float[] columnWidthsTotalValue = {150, 45};
            Table invoiceTotal = new Table(columnWidthsTotalValue)
                    .setWidthPercent(100)
                    .setFixedLayout();
            invoiceTotal.setBorder(Border.NO_BORDER);
            Cell invoiceTotalWords = new Cell();
            invoiceTotal.setBorder(Border.NO_BORDER);
            TotalInvoice = ((Double.valueOf(amountTotal)-discountvalue)+five)+Double.parseDouble(invoice.getOthers());

            invoiceTotalWords.add(NumbersToWords.convert((int) TotalInvoice));
            invoiceTotalWords.setFontSize(9f);
            Cell invoiceTotalcell = new Cell();

            Table invoicetable = new Table(new float[]{9, 42})
                    .setWidthPercent(100)
                    .setBorder(Border.NO_BORDER)
                    .setMarginTop(-2f)
                    .setMarginBottom(-2f)
                    .setFixedLayout();

            Cell invoicecell = new Cell();
            invoicecell.add("Invoice Total").setFont(titleFont);
            invoicecell.setBorder(Border.NO_BORDER);

            invoicecell.setBorderRight(new SolidBorder(.5f));
            Cell invoiceValue = new Cell();
            invoiceValue.setTextAlignment(TextAlignment.RIGHT);
            invoiceValue.setPaddingRight(10f);

            invoiceValue.setBorder(Border.NO_BORDER);
            invoiceValue.add(String.valueOf(df.format(TotalInvoice))).setFont(titleFont);
            Log.d(TAG, "createPdf: "+TotalInvoice);
            invoicetable.addCell(invoicecell);
            invoicetable.addCell(invoiceValue);
            invoiceTotalcell.add(invoicetable);
            invoiceTotal.addCell(invoiceTotalWords);
            invoiceTotal.addCell(invoiceTotalcell);

            Paragraph form = new Paragraph();
            form.add("For");
            Paragraph formName = new Paragraph();
            PdfFont forNameFont = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            Style forNameStyle = new Style().setFont(forNameFont).setFontSize(15f);
            formName.add("M.M. TEXTILES");

            formName.addStyle(forNameStyle);
            Cell forCell = new Cell();
            forCell.setBorder(Border.NO_BORDER);
            forCell.setTextAlignment(TextAlignment.RIGHT);
            forCell.setPaddingTop(52f);

            forCell.add(form);
            Cell forNameCell = new Cell();
            forNameCell.setBorder(Border.NO_BORDER);
            forNameCell.add(formName);
            forNameCell.setPaddingTop(50f);
            float[] columnWidthsSignature = {130, 20, 50};
            Table signature = new Table(columnWidthsSignature)
                    .setWidthPercent(100)
                    .setFixedLayout();

            signature.addCell(new Cell().setBorder(Border.NO_BORDER));
            signature.addCell(forCell);
            signature.addCell(forNameCell);


            Cell bCell = new Cell();
            bCell.add("");
            bCell.setBorder(Border.NO_BORDER);
            Cell signatureCell = new Cell();
            signatureCell.add("Singnature");
            signatureCell.setPaddingTop(30f);
            signatureCell.setBorder(Border.NO_BORDER);
            signatureCell.setTextAlignment(TextAlignment.CENTER);
            float[] columnWidthsSignatureText = {130, 70};
            Table signatureText = new Table(columnWidthsSignatureText)
                    .setWidthPercent(100)
                    .setFixedLayout();
            signatureText.addCell(bCell);
            signatureText.addCell(signatureCell);


            document.setMargins(20f, 20f, 20f, 20f);
            document.add(table);
            document.add(new Paragraph("\n"));
            document.add(shippingTable);
            document.add(itemTable);
            document.add(totalTable);
            document.add(gstTable);
            document.add(invoiceTotal);

            document.add(signature);
            document.add(signatureText);

            Table table2 = new Table(2);

            Cell topright2 = new Cell();
            Paragraph titleright2 = new Paragraph("GST INVOICE");
            titleright2.addStyle(titleStyle);



            topright2.add(titleright2);
            topright2.add("GST Invoice No: " + invoice.getInvoiceNo() + "/2018-19");
            topright2.add("Date: " + date.getText().toString());
            topright2.add(copy[1]);
            topright2.add("Transport: " + invoice.getTransport());




            topright2.setTextAlignment(TextAlignment.RIGHT);
            topright2.setBorder(Border.NO_BORDER);
            table.addCell(topleft);
            table.addCell(topright);

            topright.addStyle(normalStyle);
            titleright.addStyle(titleStyle);
            table2.addCell(topleft);
            table2.addCell(topright2);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            document.setMargins(20f, 20f, 20f, 20f);
            document.add(table2);
            document.add(new Paragraph("\n"));
            document.add(shippingTable);
            document.add(itemTable);
            document.add(totalTable);
            document.add(gstTable);
            document.add(invoiceTotal);

            document.add(signature);
            document.add(signatureText);



            document.close();
            try {
                FileUtils.openFile(getApplicationContext(), new File(dest));
            } catch (IOException e) {
                e.printStackTrace();
            }
            //PDFView pdfView = (PDFView) findViewById(R.id.pdfView);
            //pdfView.fromFile(new File(dest));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
