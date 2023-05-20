package com.example.ecochoice;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ecochoice.fragment.HomeFragment;
import com.example.ecochoice.fragment.ProductInformationFragment;
import com.example.ecochoice.fragment.ProductScanFragment;
import com.example.ecochoice.fragment.ScannedProductListFragment;
import com.example.ecochoice.login_register.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private static final String TAG_HOME = "home";
    private static final String TAG_PRODUCT_SCAN = "product_scan";
    private static final String TAG_PRODUCT_INFORMATION = "product_information";
    private static final String TAG_SCANNED_PRODUCT_LIST = "scanned_product_list";

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        fragmentManager = getSupportFragmentManager();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    navigateToHome();
                    break;
                case R.id.navigation_scan_barcode:
                    navigateToProductScan();
                    break;
                case R.id.navigation_product_list:
                    navigateToScannedProductList();
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + item.getItemId());
            }
            return true;
        });

        // Set the default selected fragment
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Logged out successfully.", Toast.LENGTH_SHORT).show();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void navigateToHome() {
        HomeFragment homeFragment = new HomeFragment();
        replaceFragment(homeFragment, TAG_HOME);
    }

    public void navigateToProductScan() {
        ProductScanFragment productScanFragment = new ProductScanFragment();
        replaceFragment(productScanFragment, TAG_PRODUCT_SCAN);

//        // Initiate scanner
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                productScanFragment.initiateBarcodeScan();
//            }
//        }, 100);
    }

    public void navigateToScannedProductList() {
        ScannedProductListFragment scannedProductListFragment = new ScannedProductListFragment();
        replaceFragment(scannedProductListFragment, TAG_SCANNED_PRODUCT_LIST);
    }

    public void navigateToProductInformation() {
        ProductInformationFragment productInformationFragment = new ProductInformationFragment();
        replaceFragment(productInformationFragment, TAG_PRODUCT_INFORMATION);
    }

    private void replaceFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment, tag);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount();
        if (backStackEntryCount > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
