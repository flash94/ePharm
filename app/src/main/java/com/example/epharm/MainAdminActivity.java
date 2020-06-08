package com.example.epharm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.epharm.adapters.TabViewAdapter;
import com.flutterwave.raveandroid.rave_presentation.RavePayManager;
import com.google.android.material.tabs.TabLayout;

public class MainAdminActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    private Boolean exit = false;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_admin);

        tabLayout=(TabLayout)findViewById(R.id.tabLayout);
        viewPager=(ViewPager)findViewById(R.id.viewPager);

        tabLayout.addTab(tabLayout.newTab().setText("Patients"));
        tabLayout.addTab(tabLayout.newTab().setText("Pharmacists"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final TabViewAdapter adapter = new TabViewAdapter(this,getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu
        getMenuInflater().inflate(R.menu.menu_main_admin, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //handle menu items
        int id = item.getItemId();
        if(id == R.id.signOut){
            String[] options = {"No", "Yes"};
            //dialog
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //add items to dialog
            builder.setTitle("Are you sure you want to Logout?")
                    .setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //handle item clicks
                            if (which==0){
                                onResume();
                            }
                            else if(which==1){
                                logOut();
                            }
                        }
                    });
            //show dialog
            builder.create().show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logOut(){
        Intent intent = new Intent(this, RoleSelectionActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        //The Handler here handles accidental back presses,

        if (exit) {
            finish(); // finish activity
        } else {
            // it simply shows a Toast, and if there is another back press within 3 seconds, it closes the application.
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }
}
