package in.beyonitysoftwares.shopapp.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

        /*
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
        */

        createPdf(FileUtils.getAppPath(getApplicationContext()) + "123.pdf");
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
            PdfFont titleFont = PdfFontFactory.createFont(FontConstants.TIMES_BOLD);
            titleStyle.setFont(titleFont).setFontSize(25);

            Paragraph title = new Paragraph("M.M. TEXTILES");
            title.addStyle(titleStyle);


            Paragraph address1 = new Paragraph("Prop: R.Mohan, #4/217, Utlaman Street,");
            Paragraph address2 = new Paragraph("Pamidi-515775, Anantapur Dist.(A.P.)");

            topleft.add(title);
            topleft.add(address1);
            topleft.add(address2);
            topleft.add("GSTIN: 37CUMPM0476N1ZW");
            topleft.add("CELL: 9505953126");
            // cell.addStyle(gstStyle);
            topleft.setTextAlignment(TextAlignment.LEFT);
            topleft.setBorder(Border.NO_BORDER);


            Cell topright = new Cell();
            Paragraph titleright = new Paragraph("GST INVOICE");
            titleright.addStyle(titleStyle);

            topright.add(titleright);
            topright.add("GST INVOICE NO: 185/2018-19");
            topright.add("DATE: 22 DEC 2018");
            topright.add("ORIGINAL COPY");
            topright.add("TRANSPORT: VRL");

            topright.setTextAlignment(TextAlignment.RIGHT);
            topright.setBorder(Border.NO_BORDER);
            table.addCell(topleft);
            table.addCell(topright);


            Table shippingTable = new Table(2);

            Cell billedTo = new Cell();

            billedTo.add(new Paragraph().add("BILL TO").setFont(titleFont).setFontSize(15));
            Style para = new Style();
            para.setFontSize(13f);
            Paragraph billingPara = new Paragraph();
            Paragraph shippingPara = new Paragraph();


            billingPara.addStyle(para);
            shippingPara.addStyle(para);

            billingPara.add("NAME: UDAY BHASKAR\nADDRESS: SECUNDERABAD\nGSTIN: 9742 6257 3125\nSTATE: TELENGANA\nCELL: 9640085648\n\n FROM : PAMIDI");
            shippingPara.add("NAME: UDAY BHASKAR\nADDRESS: SECUNDERABAD\nGSTIN: 9742 6257 3125\nSTATE: TELENGANA\nCELL: 9640085648\n\n TO : BARGUR");
            billedTo.add(billingPara);


            billedTo.setBorder(Border.NO_BORDER);
            Cell shippedTo = new Cell();
            shippedTo.setPaddingLeft(75f);
            shippedTo.add(new Paragraph().add("SHIP TO").setFont(titleFont).setFontSize(15));

            shippedTo.add(shippingPara);


            shippedTo.setBorder(Border.NO_BORDER);
            shippingTable.addCell(billedTo);
            shippingTable.addCell(shippedTo);


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
