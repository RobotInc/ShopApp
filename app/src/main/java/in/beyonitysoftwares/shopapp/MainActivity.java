package in.beyonitysoftwares.shopapp;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.iid.FirebaseInstanceId;
import com.itextpdf.io.font.FontConstants;
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
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import in.beyonitysoftwares.shopapp.config.AppConfig;
import in.beyonitysoftwares.shopapp.databaseHandler.SQLiteSignInHandler;
import in.beyonitysoftwares.shopapp.databaseHandler.SessionManager;

import static android.gesture.GestureLibraries.fromFile;
import static in.beyonitysoftwares.shopapp.LogUtils.LOGE;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    final int REQUEST_CODE_PERMISSION = 101;
    SessionManager session;
    SQLiteSignInHandler db;
    FirebaseAuth mAuth;
    int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;

    String mPermission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        checkPermissions();



    }

    public void checkSignIn(){
        session = new SessionManager(getApplicationContext());
        db = new SQLiteSignInHandler(getApplicationContext());
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(!session.isLoggedIn()||user==null){
            signIn();
        }else {
            init();
        }
    }

    public void init(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //createPdf(FileUtils.getAppPath(getApplicationContext()) + "123.pdf");

        View view = navigationView.getRootView();
        ImageView profile = (ImageView) view.findViewById(R.id.profile);
        profile.setImageURI(user.getPhotoUrl());
        TextView displayname = (TextView) view.findViewById(R.id.displayname);
        displayname.setText(user.getDisplayName());
        TextView email = (TextView) view.findViewById(R.id.useremail);
        email.setText(user.getEmail());
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            user = mAuth.getCurrentUser();
                            user(user.getEmail(),user.getDisplayName(), FirebaseInstanceId.getInstance().getToken());

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            signinTry();

                        }

                        // ...
                    }
                });
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    public void checkPermissions(){
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != getPackageManager().PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{mPermission},
                        REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will
                // execute every time, else your else part will work
            }else {

                /*Intent locationIntent = new Intent(MainActivity.this, MyLocationService.class);
                startService(locationIntent);
                bindService(locationIntent, serviceConnection, Context.BIND_AUTO_CREATE);
                Log.d(TAG, "checkPermissions: called service");
*/
           checkSignIn();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSignIn();
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

    private void user(String email, String name, String token) {


        AndroidNetworking.post(AppConfig.URL_LOGIN)
                .addBodyParameter("email", email)
                .addBodyParameter("name",name)
                .addBodyParameter("uid",user.getUid())
                .addBodyParameter("fcm",token)
                .setTag("user")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {

                            boolean error = response.getBoolean("error");
                            if (!error) {

                                session.setLogin(true);
                                JSONObject user = response.getJSONObject("user");

                                db.addUser(response.getInt("id"),user.getString("email"),user.getString("name"));
                                Toast.makeText(MainActivity.this, "Successfully Logged in as "+user.getString("name"), Toast.LENGTH_SHORT).show();

                               init();

                            }
                        }catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    @Override
                    public void onError(ANError error) {
                        Log.e(TAG, "onError: "+error.getErrorDetail());
                        Toast.makeText(getApplicationContext(), "error logging in", Toast.LENGTH_SHORT).show();

                        signinTry();
                    }
                });
    }

    public void signinTry() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Error While Connecting");
        builder.setMessage("oops Looks like network issues make sure your internet connection is on and try again... ");
        builder.setNegativeButton("Quit",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        System.exit(1);
                    }
                });
        builder.setPositiveButton("Try again",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        signIn();
                    }
                });

        builder.show();

    }

}
