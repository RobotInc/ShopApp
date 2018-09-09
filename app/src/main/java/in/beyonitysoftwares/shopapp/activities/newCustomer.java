package in.beyonitysoftwares.shopapp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.utils.Helper;

public class newCustomer extends AppCompatActivity {
    private static final String TAG = "newCustomer";
    ImageView back;
    Spinner states;
    ArrayAdapter stateAdapter;
    List<String> stateList = new ArrayList<>();
    Button addcustomer;
    EditText legalname,tradename,gstin,address,pincode,phone;
    boolean isRegistered = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_customer);
        back = (ImageView) findViewById(R.id.back);


        tradename = (EditText) findViewById(R.id.tradename);
        legalname = (EditText) findViewById(R.id.legalname);
        gstin= (EditText) findViewById(R.id.gstin);
        address= (EditText) findViewById(R.id.Address);
        phone = (EditText) findViewById(R.id.phone);
        pincode = (EditText) findViewById(R.id.pincode);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(newCustomer.this,mainApp.class);
                startActivity(intent);
                finish();
            }
        });

        states = (Spinner) findViewById(R.id.state);
        states.setPrompt("States");
        stateList.add("Select State");
        stateAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item, stateList);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setSelection(0);
        states.setAdapter(stateAdapter);
        Log.d(TAG, "onCreate: "+Helper.getStatecode().size());
        for (String names : Helper.getStatecode().keySet()){
            Log.d(TAG, "onCreate: ");
            stateList.add(names);
        }
        
        stateAdapter.notifyDataSetChanged();
        addcustomer = (Button) findViewById(R.id.addcustomer);
        addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(tradename.getText())||TextUtils.isEmpty(legalname.getText())||TextUtils.isEmpty(address.getText())||TextUtils.isEmpty(gstin.getText())||TextUtils.isEmpty(pincode.getText())||TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(getApplicationContext(),"All Fields Required",Toast.LENGTH_LONG).show();
                    return;
                }


                if(legalname.getText().length()<3){
                    legalname.setError("Not a valid name");
                    return;
                }
                if(tradename.getText().length()<3){
                    tradename.setError("Not a valid name");
                    return;
                }
                int gstinlength = gstin.getText().length();


                if(states.getSelectedItem().toString().equals("Select State")){
                    Toast.makeText(newCustomer.this, "Please Select State", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pincode.getText().length()!=6){
                    pincode.setError("Invalid Pincode");
                    return;
                }
                if(phone.getText().length()!=10){
                    phone.setError("Invalid phone number");
                    return;
                }

                if(gstinlength==15||gstinlength==10||gstinlength==12){

                    if(gstinlength==15){
                        isRegistered = true;
                    }
                    Toast.makeText(newCustomer.this, "Perfect", Toast.LENGTH_SHORT).show();
                }else {
                    gstin.setError("Enter Valid Text");
                }
            }
        });


    }
}
