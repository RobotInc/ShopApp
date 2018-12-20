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
import org.json.JSONException;
import org.json.JSONObject;

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
    }
}
