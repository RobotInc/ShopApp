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
import com.squareup.picasso.Picasso;

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
    FirebaseAuth mAuth;
    int RC_SIGN_IN = 1;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseUser user;
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
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        user = FirebaseAuth.getInstance().getCurrentUser();
        checkPermissions();


    }


    public void checkSignIn(){
        if(!session.isLoggedIn()||user==null){
            Log.d(TAG, "checkSignIn: am in sign in");
            signIn();
        }else {
            Log.d(TAG, "checkSignIn: am in else"+user.getDisplayName());
            init();
        }
        //   Log.d(TAG, "checkSignIn: "+user.getDisplayName());
        // Log.d(TAG, "checkSignIn: "+user.getUid());
        //Log.d(TAG, "checkSignIn: "+user.getEmail());
        //signIn();
    }

    public void init(){
        Helper.setupHelper();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //createPdf(FileUtils.getAppPath(getApplicationContext()) + "123.pdf");
        appbar = (TextView) findViewById(R.id.appBarTitleTV);
        add = (ImageView) findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vg.getCurrentItem()==1){
                    Intent intent = new Intent(mainApp.this,newCustomer.class);
                    startActivity(intent);
                }
            }
        });
        vg = (CustomViewPager) findViewById(R.id.vg);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new invoices(),"");
        adapter.addFragment(new customers(), "");
        adapter.addFragment(new products(),"");
        adapter.addFragment(new settings(),"");
        vg.setAdapter(adapter);
        vg.setPagingEnabled(false);
        appbar.setText("M M Textiles - Invoices");

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

    public void checkPermissions(){
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission[0])
                    != getPackageManager().PERMISSION_GRANTED && (ActivityCompat.checkSelfPermission(this,mPermission[1])!=getPackageManager().PERMISSION_GRANTED)){

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
                checkSignIn();


            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                                Toast.makeText(mainApp.this, "Successfully Logged in as "+user.getString("name"), Toast.LENGTH_SHORT).show();

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
