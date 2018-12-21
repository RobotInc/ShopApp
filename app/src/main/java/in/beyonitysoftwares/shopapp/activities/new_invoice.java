package in.beyonitysoftwares.shopapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
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
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.adapters.customerAdapter;
import in.beyonitysoftwares.shopapp.adapters.itemAdapter;
import in.beyonitysoftwares.shopapp.model.Product;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.model.item;
import in.beyonitysoftwares.shopapp.utils.FileUtils;
import in.beyonitysoftwares.shopapp.utils.Helper;

public class new_invoice extends AppCompatActivity {
    EditText invoiceNo,date;
    private static final String TAG = "new_invoice";
    TextView yearview;
    String[] tranposrts = {"Select Transport","VRL","SRMT","SLRT","NAVATHA","APSRTC","TSRTC","HAND"};
    String selectedTrans = "";
    long datetime = 0;
    List<customer> customerList = new ArrayList<>();
    List<Product> productList= new ArrayList<>();
    Spinner transportspinner,customerid1,customerid2;
    Calendar myCalendar = Calendar.getInstance();
    RecyclerView rv;
    List<item> itemList= new ArrayList<>();
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
        yearview = (TextView) findViewById(R.id.yearview);
        date = (EditText) findViewById(R.id.invoicedate);
        date.setFocusable(false);
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
                android.R.layout.simple_spinner_item,tranposrts);
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
        ArrayAdapter<String> cus1adapter= new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,customerList);
        Log.d(TAG, "onCreate: "+Helper.getCustomerList().size());
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
        ArrayAdapter<String> cus2adapter= new ArrayAdapter(this,
                android.R.layout.simple_spinner_item,customerList);
        Log.d(TAG, "onCreate: "+Helper.getCustomerList().size());
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
        itemadapter= new itemAdapter(itemList,getApplicationContext());
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
                android.R.layout.simple_spinner_item,productList);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        addpro.setAdapter(adapter);

        EditText qty = (EditText) dialog.findViewById(R.id.qty);
        EditText price = (EditText) dialog.findViewById(R.id.price);
        Button cancel= (Button) dialog.findViewById(R.id.cancel);
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
                if(addpro.getSelectedItemPosition()!=0){
                    if(!TextUtils.isEmpty(qty.getText())){
                        if(!TextUtils.isEmpty(price.getText())){
                            item i = new item();
                            i.setProductid(productList.get(addpro.getSelectedItemPosition()).getId());
                            i.setProduct(productList.get(addpro.getSelectedItemPosition()).getName());
                            Log.d(TAG, "onClick: "+productList.get(addpro.getSelectedItemPosition()).getName());
                            i.setQty(qty.getText().toString().trim());
                            i.setPrice(price.getText().toString().trim());

                            itemList.add(i);
                            itemadapter.notifyDataSetChanged();
                            dialog.dismiss();

                        }else {
                            Toast.makeText(new_invoice.this, "Please Enter the Price", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(new_invoice.this, "Please Enter the Quantity", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(new_invoice.this, "Please Select the product", Toast.LENGTH_SHORT).show();
                }

            }
        });

        dialog.show();


    }

    public void preview(View view) {
        createPdf(FileUtils.getAppPath(getApplicationContext()) + "123.pdf");
        if(!TextUtils.isEmpty(invoiceNo.getText())){
            if(transportspinner.getSelectedItemPosition()!=0){
                if(customerid1.getSelectedItemPosition()!=0){
                    if(customerid2.getSelectedItemPosition()!=0){
                        if(itemList.size()!=0){

                            //FileUtils.getAppPath(getApplicationContext()) + "123.pdf";
                            Toast.makeText(this, "Perfect", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(this, "Please add Purchased Items", Toast.LENGTH_SHORT).show();
                        }

                    }else {
                        Toast.makeText(this, "Select Shipping to Customer", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Select Billing to customer", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Select Transport", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(this, "Enter Invoice No", Toast.LENGTH_SHORT).show();
        }
    }



    public void createPdf(String dest) {

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


            float width = pdf.getDefaultPageSize().getWidth();
            float height = pdf.getDefaultPageSize().getHeight();
            // Define a PdfCanvas instance
            PdfCanvas canvas = new PdfCanvas(pdf.getFirstPage());
            // Add a rectangle
            canvas.roundRectangle(20, 20, width - 40, height - 40, 5);
            canvas.stroke();
            // Close PdfDocument

            // Creating a Document
            Document document = new Document(pdf);

            Table table = new Table(2);


            Cell cell = new Cell();
       /* Style gstStyle = new Style();
        PdfFont font = PdfFontFactory.createFont("/assets/fonts/brandon_medium.otf");
        gstStyle.setFont(font).setFontSize(15);*/


            cell.add("GSTIN: 37CUMPM0476N1ZW");
            // cell.addStyle(gstStyle);
            cell.setTextAlignment(TextAlignment.LEFT);
            cell.setBorder(Border.NO_BORDER);

            Cell shopname = new Cell();
            shopname.add("CELL: 9505953126");
            shopname.setTextAlignment(TextAlignment.RIGHT);
            shopname.setBorder(Border.NO_BORDER);
            table.addCell(cell);
            table.addCell(shopname);

            Style titleStyle = new Style();
            PdfFont titleFont = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            titleStyle.setFont(titleFont).setFontSize(25);

            Paragraph title = new Paragraph("M.M. TEXTILES");
            title.addStyle(titleStyle);
            title.setTextAlignment(TextAlignment.CENTER);
            title.setPaddingTop(15f);

            Paragraph address = new Paragraph("Prop: R.Mohan, #4/217, Utlaman Street, Pamidi-515775, Anantapur Dist.(A.P.)");
            address.setTextAlignment(TextAlignment.CENTER);
            address.setPaddingTop(-15f);

            Table invoiceTop = new Table(3);

            Cell invoiceNo = new Cell();
            invoiceNo.add("INVOICE NO : "+invoiceNo);
            invoiceNo.setFontSize(10f);
            invoiceNo.setTextAlignment(TextAlignment.LEFT);
            invoiceNo.setBorder(Border.NO_BORDER);
            invoiceNo.setBorderTop(new SolidBorder(1));
            invoiceNo.setBorderBottom(new SolidBorder(1));


            Cell taxInvoice = new Cell();
            taxInvoice.add("GST INVOICE");
            taxInvoice.setTextAlignment(TextAlignment.CENTER);
            taxInvoice.setBorder(Border.NO_BORDER);
            //taxInvoice.setFont(font);
            taxInvoice.setBorderTop(new SolidBorder(1));
            taxInvoice.setBorderBottom(new SolidBorder(1));


            Cell invoiceDate = new Cell();
            invoiceDate.add("INVOICE Date: "+new Date());
            invoiceDate.setTextAlignment(TextAlignment.RIGHT);
            invoiceDate.setFontSize(10f);
            invoiceDate.setBorder(Border.NO_BORDER);
            invoiceDate.setBorderTop(new SolidBorder(1));
            invoiceDate.setBorderBottom(new SolidBorder(1));


            invoiceTop.setPaddingTop(10f);
            invoiceTop.addCell(invoiceNo);
            invoiceTop.addCell(taxInvoice);
            invoiceTop.addCell(invoiceDate);


            Table shippingTable = new Table(3);

            Cell billedTo = new Cell();
            billedTo.setHeight(100f);
            billedTo.add("Billed to:");
            Style para = new Style();
            para.setFontSize(10f);
            Paragraph billingPara = new Paragraph();
            Paragraph shippingPara = new Paragraph();

            billingPara.setPaddingLeft(20f);
            shippingPara.setPaddingLeft(20f);

            billingPara.addStyle(para);
            shippingPara.addStyle(para);

            billingPara.add("M/s. \n GSTIN: \n ().");
            shippingPara.add("M/s. ,\n,\nGSTIN: ,\n ().");
            billedTo.add(billingPara);

            Cell shippedTo = new Cell();
            shippedTo.add("Shipped to:");
            shippedTo.setHeight(100f);
            shippedTo.add(shippingPara);


            Cell trasport = new Cell();
            trasport.setHeight(100f);
            Table insideTrans = new Table(2);
            Cell transC1 = new Cell();
            transC1.setFontSize(10f);
            transC1.setBorder(Border.NO_BORDER);
            transC1.add("Trasnport ");
            transC1.add("Date of Supply ");
            transC1.add("Place of Supply  ");
            transC1.add("Station ");


            Cell transC2 = new Cell();
            transC2.add(": transport");
            transC2.add(": date");
            transC2.add(": address");
            transC2.add(": Pamidi to ");

            transC2.setBorder(Border.NO_BORDER);
            transC2.setFontSize(10f);

            insideTrans.addCell(transC1);
            insideTrans.addCell(transC2);

            trasport.add(insideTrans);


            shippingTable.addCell(billedTo);
            shippingTable.addCell(shippedTo);
            shippingTable.addCell(trasport);

            float[] columnWidths = {15, 50, 25, 25, 25, 40};
            Table itemTable = new Table(columnWidths)
                    .setWidthPercent(100)
                    .setFixedLayout();

            Cell sNoHeader = new Cell();
            sNoHeader.add("SNO.").setTextAlignment(TextAlignment.CENTER);

            Cell descriptionHeader = new Cell();
            descriptionHeader.add("Description").setTextAlignment(TextAlignment.CENTER);

            Cell baleHeader = new Cell();
            baleHeader.add("Bale No.").setTextAlignment(TextAlignment.CENTER);

            Cell qtyHeader = new Cell().setTextAlignment(TextAlignment.CENTER);
            qtyHeader.add("qty").setTextAlignment(TextAlignment.CENTER);
            Cell priceHeader = new Cell();
            priceHeader.add("Price").setTextAlignment(TextAlignment.CENTER);
            Cell amountHeader = new Cell();
            amountHeader.add("Amount").setTextAlignment(TextAlignment.CENTER);


            itemTable.addHeaderCell(sNoHeader);
            itemTable.addHeaderCell(descriptionHeader);
            itemTable.addHeaderCell(baleHeader);
            itemTable.addHeaderCell(qtyHeader);
            itemTable.addHeaderCell(priceHeader);
            itemTable.addHeaderCell(amountHeader);

            Cell sNo = new Cell();
            sNo.setTextAlignment(TextAlignment.CENTER);
            sNo.setHeight(200f);



            Cell des = new Cell();
            des.setHeight(200f);
            des.setTextAlignment(TextAlignment.CENTER);



            Cell baleNo = new Cell();
            baleNo.setHeight(200f);
            baleNo.setTextAlignment(TextAlignment.CENTER);




            Cell qty = new Cell();
            qty.setHeight(200f);
            qty.setTextAlignment(TextAlignment.CENTER);



            Cell price = new Cell();
            price.setHeight(200f);
            price.setTextAlignment(TextAlignment.CENTER);




            Cell amount = new Cell();
            amount.setHeight(200f);
            amount.setTextAlignment(TextAlignment.RIGHT);
            amount.setPaddingRight(10f);

            int count = 0;
            int qtyTotal = 0;
            double amountTotal = 0;
       /* for(item i : items){
            sNo.add(String.valueOf(++count));
            des.add(i.getProduct());
            baleNo.add(i.getBaleNo());
            qty.add(i.getQty());
            qtyTotal = qtyTotal + Integer.parseInt(i.getQty());
            amountTotal = amountTotal + Double.parseDouble(i.getTotal());
            price.add(i.getPrice());
            amount.add(i.getTotal());
        }*/

            itemTable.addCell("Sno");
            itemTable.addCell("des");
            itemTable.addCell("baleNo");
            itemTable.addCell("qty");
            itemTable.addCell("price");
            itemTable.addCell("amount");

            float[] columnWidthsTotal = {65, 25, 25,25, 40};
            Table totalTable = new Table(columnWidthsTotal)
                    .setWidthPercent(100)
                    .setFixedLayout();

            Cell TotalCell = new Cell();
            TotalCell.setTextAlignment(TextAlignment.CENTER);
            TotalCell.add("Total");

            Cell noOfBales = new Cell();
            noOfBales.setTextAlignment(TextAlignment.CENTER);
            noOfBales.add("1");

            Cell noOfItems = new Cell();
            noOfItems.setTextAlignment(TextAlignment.CENTER);
            noOfItems.add(String.valueOf(qtyTotal));

            Cell blankCell = new Cell();
            Cell totalAmount = new Cell();
            totalAmount.setTextAlignment(TextAlignment.RIGHT);
            totalAmount.setPaddingRight(10f);
            totalAmount.add(String.valueOf(amountTotal));



            totalTable.addCell(TotalCell);
            totalTable.addCell(noOfBales);
            totalTable.addCell(noOfItems);
            totalTable.addCell(blankCell);
            totalTable.addCell(totalAmount);

            float[] columnWidthsGst = {90, 90};
            Table gstTable = new Table(columnWidthsGst)
                    .setWidthPercent(100)
                    .setFixedLayout();

            Cell accountsCell = new Cell();
            accountsCell.setTextAlignment(TextAlignment.CENTER);
            accountsCell.add("915010018109450\nIFSC UTIB00000332\n\n915010018109450\nIFSC UTIB0000332");
            accountsCell.setHeight(90f);

            Cell gstcell= new Cell();


            float[] columnWidthsigst = {50,40};
            Table igstTable = new Table(columnWidthsigst)
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

            Table extra = new Table(columnWidthsigst)
                    .setWidthPercent(100)
                    .setFixedLayout();


            Cell igst = new Cell();

            igst.add("IGST\t5%");
            igst.setBorder(Border.NO_BORDER);
            igst.setBorderBottom(new SolidBorder(.5f));
            igst.setBorderRight(new SolidBorder(.5f));

            Cell igstValue = new Cell();
            igstValue.setTextAlignment(TextAlignment.RIGHT);
            igstValue.setPaddingRight(10f);

            igstValue.setBorder(Border.NO_BORDER);
            igstValue.setBorderBottom(new SolidBorder(.5f));


            Cell cgst = new Cell();
            cgst.add("CGST\t2.5%");
            cgst.setBorder(Border.NO_BORDER);
            cgst.setBorderBottom(new SolidBorder(.5f));
            cgst.setBorderRight(new SolidBorder(.5f));
            Cell cgstValue = new Cell();
            cgstValue.setTextAlignment(TextAlignment.RIGHT);
            cgstValue.setPaddingRight(10f);

            cgstValue.setBorder(Border.NO_BORDER);
            cgstValue.setBorderBottom(new SolidBorder(.5f));

            Cell sgst = new Cell();
            sgst.add("SGST\t2.5%");
            sgst.setBorder(Border.NO_BORDER);
            sgst.setBorderBottom(new SolidBorder(.5f));
            sgst.setBorderRight(new SolidBorder(.5f));
            Cell sgstValue = new Cell();
            sgstValue.setTextAlignment(TextAlignment.RIGHT);
            sgstValue.setPaddingRight(10f);

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
      /*  if(stateCode2.equals("37")){
            twopointfive = (amountTotal*2.5)/100;
            igstValue.add("0.00");
            cgstValue.add(String.valueOf(twopointfive));
            sgstValue.add(String.valueOf(twopointfive));
            extraValue.add(otherCharges);
        }else {
            five = (amountTotal*5)/100;
            igstValue.add(String.valueOf(five));
            cgstValue.add("0.00");
            sgstValue.add("0.00");
            extraValue.add(otherCharges);
        }*/
            five = (amountTotal*5)/100;
            igstValue.add(String.valueOf(five));
            cgstValue.add("0.00");
            sgstValue.add("0.00");
            extraValue.add("100");
            igstTable.addCell(igst);
            igstTable.addCell(igstValue);
            cgstTable.addCell(cgst);
            cgstTable.addCell(cgstValue);
            sgstTable.addCell(sgst);
            sgstTable.addCell(sgstValue);
            extra.addCell(extraCharges);
            extra.addCell(extraValue);



            gstcell.add(cgstTable);
            gstcell.add(sgstTable);
            gstcell.add(igstTable);
            gstcell.add(extra);



            gstTable.addCell(accountsCell);
            gstTable.addCell(gstcell);

            float[] columnWidthsTotalValue = {90,50,40};
            Table invoiceTotal = new Table(columnWidthsTotalValue)
                    .setWidthPercent(100)
                    .setFixedLayout();
            Cell invoiceTotalWords = new Cell();
            invoiceTotalWords.add("Twenty Seven lakh Eighty Seven Thousand Seven Hundreed and Thirty three only");
            invoiceTotalWords.setFontSize(9f);
            Cell invoiceTotalcell = new Cell();
            invoiceTotalcell.add("Invoice Total");
            invoiceTotalcell.setTextAlignment(TextAlignment.LEFT);

            Cell invoiceTotalAmount = new Cell();
    /*    if(stateCode2.equals("37")){
            double value = (twopointfive*2)+ Double.parseDouble(otherCharges)+amountTotal;
            invoiceTotalAmount.add(String.valueOf(value));

        }else {
            double value = (five)+ Double.parseDouble(otherCharges)+amountTotal;
            invoiceTotalAmount.add(String.valueOf(value));
        }*/
            double value = (five)+ Double.parseDouble("100")+amountTotal;
            invoiceTotalAmount.add(String.valueOf(value));
            invoiceTotalAmount.setTextAlignment(TextAlignment.RIGHT);
            invoiceTotalAmount.setPaddingRight(10f);
            invoiceTotal.addCell(invoiceTotalWords);
            invoiceTotal.addCell(invoiceTotalcell);
            invoiceTotal.addCell(invoiceTotalAmount);

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
            float[] columnWidthsSignature= {130,20,50};
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
            float[] columnWidthsSignatureText= {130,70};
            Table signatureText = new Table(columnWidthsSignatureText)
                    .setWidthPercent(100)
                    .setFixedLayout();
            signatureText.addCell(bCell);
            signatureText.addCell(signatureCell);




            document.setMargins(20f, 20f, 20f, 20f);
            document.add(table);
            document.add(title);
            document.add(address);
            document.add(invoiceTop);
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
