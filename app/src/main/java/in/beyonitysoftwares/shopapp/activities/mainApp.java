package in.beyonitysoftwares.shopapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.kernel.pdf.canvas.parser.listener.LocationTextExtractionStrategy;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.adapters.ViewPagerAdapter;
import in.beyonitysoftwares.shopapp.config.AppConfig;
import in.beyonitysoftwares.shopapp.custom.CustomViewPager;
import in.beyonitysoftwares.shopapp.databaseHandler.SQLiteSignInHandler;
import in.beyonitysoftwares.shopapp.databaseHandler.SessionManager;
import in.beyonitysoftwares.shopapp.fragments.customers;
import in.beyonitysoftwares.shopapp.fragments.invoices;
import in.beyonitysoftwares.shopapp.fragments.products;
import in.beyonitysoftwares.shopapp.fragments.settings;
import in.beyonitysoftwares.shopapp.utils.FileUtils;
import in.beyonitysoftwares.shopapp.utils.Helper;

public class mainApp extends AppCompatActivity {
    private static final String TAG = "mainApp";

    final int REQUEST_CODE_PERMISSION = 101;
    ViewPagerAdapter adapter;
    CustomViewPager vg;
    SessionManager session;
    SQLiteSignInHandler db;
    int RC_SIGN_IN = 1;
    boolean isinit = false;
    String[] mPermission = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
    TextView appbar;
    ImageView add;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.invoice:
                    vg.setCurrentItem(0);
                    appbar.setText("M M Textiles - Invoices");
                    return true;
                case R.id.customers:
                    vg.setCurrentItem(1);
                    appbar.setText("M M Textiles - Customers");
                    return true;
                case R.id.products:
                    vg.setCurrentItem(2);
                    appbar.setText("M M Textiles - Products");
                    return true;
                case R.id.settigns:
                    vg.setCurrentItem(3);
                    appbar.setText("M M Textiles - Settings");
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        session = new SessionManager(getApplicationContext());
        db = new SQLiteSignInHandler(getApplicationContext());
        init();

    }


        //   Log.d(TAG, "checkSignIn: "+user.getDisplayName());
        // Log.d(TAG, "checkSignIn: "+user.getUid());
        //Log.d(TAG, "checkSignIn: "+user.getEmail());
        //signIn();


    public void init(){
        Helper.setupHelper();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //createPdf(FileUtils.getAppPath(getApplicationContext()) + "123.pdf");
        appbar = (TextView) findViewById(R.id.appBarTitleTV);

        vg = (CustomViewPager) findViewById(R.id.vg);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(Helper.getInvoice(),"");
        adapter.addFragment(Helper.getCustomer(), "");
        adapter.addFragment(Helper.getProduct(),"");
        adapter.addFragment(new settings(),"");
        vg.setAdapter(adapter);
        vg.setPagingEnabled(false);
        appbar.setText("M M Textiles - Invoices");
        Helper.refreshCustomerList();
        Helper.refreshProductList();
        Helper.refreshInvoiceList();
        checkPermissions();
    }

    public void add(View view) {


        if(vg.getCurrentItem()==1){
            Intent intent = new Intent(mainApp.this,newCustomer.class);
            startActivity(intent);
        }if(vg.getCurrentItem()==2){
            Intent intent = new Intent(mainApp.this,new_product.class);
            startActivity(intent);
        }if (vg.getCurrentItem()==0){
            Intent intent = new Intent(mainApp.this,new_invoice.class);
            startActivity(intent);
        }


        //createPdf(FileUtils.getAppPath(getApplicationContext()) + "123.pdf");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                   /* Intent locationIntent = new Intent(MainActivity.this, MyLocationService.class);
                    startService(locationIntent);
                    bindService(locationIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                    Log.d(TAG, "onRequestPermissionsResult: called service");*/

                } else {
                    Toast.makeText(this, "Please Provide Required permission to work properly", Toast.LENGTH_SHORT).show();
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    public void checkPermissions(){
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != getPackageManager().PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(this,mPermission[1])!=getPackageManager().PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, mPermission,
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }else {

                /*Intent locationIntent = new Intent(MainActivity.this, MyLocationService.class);
                startService(locationIntent);
                bindService(locationIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                Log.d(TAG, "checkPermissions: called service");
*/


            }
        } catch (Exception e) {
            e.printStackTrace();
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

            topright.add(titleright);
            topright.add("GST Invoice No: 185/2018-19");
            topright.add("Date: 22 DEC 2018");
            topright.add("Original Copy");
            topright.add("Transport: VRL");

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

            billingPara.add("Name: Uday Bhaskar\nAddress: Secunderabad\nGSTIN: 9742 6257 3125\nState: Telengana\nCell: 9640085648");
            shippingPara.add("Name: Uday Bhaskar\nAddress: Secunderabad\nGSTIN: 9742 6257 3125\nState: Telengana\nCell: 9640085648");



            billedTo.add(billingPara);
            billedTo.add(new Paragraph("\n From : Pamidi").setFont(titleFont).setFontSize(12));


            billedTo.setBorder(Border.NO_BORDER);
            Cell shippedTo = new Cell();
            shippedTo.setPaddingLeft(75f);
            shippedTo.add(new Paragraph().add("SHIP TO").addStyle(titleStyle));

            shippedTo.add(shippingPara);
            shippedTo.add(new Paragraph("\n To : Secunderabad").setFont(titleFont).setFontSize(12));


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
            sNo.add("1");
            sNo.add("2");
            sNo.add("3");
            sNo.add("3");
            sNo.add("3");
            sNo.add("3");
            sNo.add("3");
            sNo.add("3");

            sNo.setTextAlignment(TextAlignment.CENTER);




            Cell des = new Cell();

            des.add("FANCY");
            des.add("KALYANI");
            des.add("KALYANI MOTHER");
            des.add("KALYANI MOTHER");
            des.add("KALYANI MOTHER");
            des.add("KALYANI MOTHER");
            des.add("KALYANI MOTHER");
            des.add("KALYANI MOTHER");

            des.setTextAlignment(TextAlignment.CENTER);







            Cell qty = new Cell();

            qty.add("1500");
            qty.add("1500");
            qty.add("900");
            qty.add("900");
            qty.add("900");
            qty.add("900");
            qty.add("900");
            qty.add("900");

            qty.setTextAlignment(TextAlignment.CENTER);



            Cell price = new Cell();

            price.add("114.29");
            price.add("113.39");
            price.add("112.49");
            price.add("112.49");
            price.add("112.49");
            price.add("112.49");
            price.add("112.49");
            price.add("112.49");

            price.setTextAlignment(TextAlignment.CENTER);




            Cell amount = new Cell();

            amount.add("99,99,999.99");
            amount.add("99,99,999.99");
            amount.add("99,99,999.99");
            amount.add("99,99,999.99");
            amount.add("99,99,999.99");
            amount.add("99,99,999.99");
            amount.add("99,99,999.99");
            amount.add("99,99,999.99");

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
            noOfBales.add("13 (Bales)");

            Cell noOfItems = new Cell();
            noOfItems.setTextAlignment(TextAlignment.CENTER);
            noOfItems.add("2900");


            Cell totalAmount = new Cell();
            totalAmount.setTextAlignment(TextAlignment.RIGHT);
            totalAmount.setPaddingRight(10f);
            totalAmount.add("99,99,999.00");



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
            accountsCell.add("AXIS BANK\n915010018109450\nIFSC UTIB00000332\n\nSTATE BANK OF INDIA\n915010018109450\nIFSC UTIB0000332").addStyle(normalStyle);


            Cell gstcell= new Cell();
            gstcell.addStyle(normalStyle);

            float[] columnWidthsigst = {50,85};
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

            Table extra = new Table(new float[]{9,42})
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
            igstValue.add("0.00");
            discountValue.add("0.00");
            subValue.add("0.00");
            cgstValue.add("0.00");
            sgstValue.add("0.00");
            extraValue.add("100.00");
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
            invoiceTotalWords.add("Twenty Seven lakh Eighty Seven Thousand Seven Hundreed and Thirty three");
            invoiceTotalWords.setFontSize(9f);
            Cell invoiceTotalcell = new Cell();

            Table invoice = new Table(new float[]{9,42})
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

            invoiceValue.add("99,99,999.00").setFont(titleFont);

            invoice.addCell(invoicecell);
            invoice.addCell(invoiceValue);
            invoiceTotalcell.add(invoice);
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
            document.add( new Paragraph("\n") );
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
