package in.beyonitysoftwares.shopapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.config.AppConfig;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.utils.Helper;

public class new_product extends AppCompatActivity {
    Button addproduct;
    private static final String TAG = "new_product";
    EditText pname;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        pname = (EditText) findViewById(R.id.pname);
        addproduct = (Button) findViewById(R.id.addproduct);
        addproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(pname.getText())){
                    AddProduct(pname.getText().toString());
                }else {
                    Toast.makeText(new_product.this, "Product Name Required", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void AddProduct(String name){

        AndroidNetworking.post(AppConfig.ADD_PRODUCT)
                .addBodyParameter("name",name)
                .setTag("Add Product")
                .setPriority(Priority.HIGH)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: "+response);
                        try {
                            boolean error = response.getBoolean("error");
                            if(!error){
                                Toast.makeText(new_product.this, "Successfully added the product "+name, Toast.LENGTH_SHORT).show();
                                pname.setText("");
                                Helper.refreshProductList();
                            }else {
                                Toast.makeText(new_product.this, "Error Adding the product to the list", Toast.LENGTH_SHORT).show();
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

}
