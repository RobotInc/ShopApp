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

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.config.AppConfig;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.utils.Helper;

public class newCustomer extends AppCompatActivity {
    private static final String TAG = "newCustomer";
    ImageView back;
    Spinner states;
    ArrayAdapter stateAdapter;
    List<String> stateList = new ArrayList<>();
    Button addcustomer;
    EditText tradename,gstin,address,pincode,phone;
    boolean isRegistered = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reg_customer);
        back = (ImageView) findViewById(R.id.back);


        tradename = (EditText) findViewById(R.id.tradename);

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
                if(TextUtils.isEmpty(tradename.getText())||TextUtils.isEmpty(address.getText())||TextUtils.isEmpty(gstin.getText())||TextUtils.isEmpty(pincode.getText())||TextUtils.isEmpty(phone.getText())){
                    Toast.makeText(getApplicationContext(),"All Fields Required",Toast.LENGTH_LONG).show();
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
                    }else {
                        isRegistered = false;
                    }

                    customer c = new customer();
                    c.setName(tradename.getText().toString());
                    c.setGstin(gstin.getText().toString());
                    c.setAddress(address.getText().toString());
                    c.setState(states.getSelectedItem().toString());
                    c.setPincode(pincode.getText().toString());
                    c.setPhone(phone.getText().toString());
                    c.setRegistered(isRegistered);
                    Log.d(TAG, "onClick: "+c);
                    regCustomer(c);

                }else {
                    gstin.setError("Enter Valid Text");
                }
            }
        });


    }


    public void regCustomer(customer c){
        int r = 0;
        if(c.isRegistered()){
            r = 1;
        }
        AndroidNetworking.post(AppConfig.REG_CUSTOMER)
                .addBodyParameter("name",c.getName())
                .addBodyParameter("gstin",c.getGstin())
                .addBodyParameter("registered",String.valueOf(r ))
                .addBodyParameter("address",c.getAddress())
                .addBodyParameter("state",c.getState())
                .addBodyParameter("pincode",c.getPincode())
                .addBodyParameter("phone",c.getPhone())
                .setTag("Register Customer")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            boolean error = response.getBoolean("error");
                            if(!error){
                                Toast.makeText(newCustomer.this, "Successfully added Customer", Toast.LENGTH_SHORT).show();
                                Helper.refreshCustomerList();
                            }else {
                                Toast.makeText(newCustomer.this, "Error Adding the customer to the list", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d(TAG, "onError: customer "+anError.getResponse());
                    }
                });


    }
}
