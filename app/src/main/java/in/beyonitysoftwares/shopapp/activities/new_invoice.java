package in.beyonitysoftwares.shopapp.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.utils.Helper;

public class new_invoice extends AppCompatActivity {
    EditText invoiceNo,date;
    private static final String TAG = "new_invoice";
    TextView yearview;
    String[] tranposrts = {"Select Transport","VRL","SRMT","SLRT","NAVATHA","APSRTC","TSRTC","HAND"};
    String selectedTrans = "";
    long datetime = 0;
    List<customer> customerList = new ArrayList<>();
    Spinner transportspinner,customerid1,customerid2;
    Calendar myCalendar = Calendar.getInstance();
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
                dialog.dismiss();
            }
        });

        dialog.show();


    }
}
