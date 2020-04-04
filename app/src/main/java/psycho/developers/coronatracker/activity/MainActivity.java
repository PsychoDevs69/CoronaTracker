package psycho.developers.coronatracker.activity;

import android.animation.Animator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.Utils.SessionConfig;
import psycho.developers.coronatracker.adapters.ViewPagerAdapter_Home;
import psycho.developers.coronatracker.fragments.GlobalList;
import psycho.developers.coronatracker.fragments.Precaution;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;

    private Toolbar toolbar;
    private ViewPager viewPager;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;

    private ViewPagerAdapter_Home viewPagerAdapter_home;

    private GlobalList globalList = new GlobalList();
    private Precaution precaution = new Precaution();

    boolean isOpen = false;
    RelativeLayout parentContentMain;
    LinearLayout infoLayout;

    SessionConfig sessionConfig;

    FirebaseFirestore firestore;
    Boolean isUpdateAvail = false;
    long version = 0;
    String selectedCountry = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigation_main);

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        sessionConfig = new SessionConfig(MainActivity.this);

        toolbar = findViewById(R.id.toolbar_main);
        toolbar.setTitle("Corona Tracker");
        toolbar.setTitleTextColor(Color.WHITE);

        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.mainViewPager);
        viewPagerAdapter_home = new ViewPagerAdapter_Home(getSupportFragmentManager(), 1);
        viewPagerAdapter_home.addFragment(globalList, "Global List");
        viewPagerAdapter_home.addFragment(precaution, "Precautions");
        viewPager.setAdapter(viewPagerAdapter_home);

        parentContentMain = findViewById(R.id.parentContentMain);
        navigationView = findViewById(R.id.navigationView_main);
        drawerLayout = findViewById(R.id.drawerLayout_main);
        infoLayout = findViewById(R.id.infoLayout);
        infoLayout.setVisibility(View.GONE);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.hamburger_icon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (isOpen) {
                    startAnimationInfo();
                }

                switch (menuItem.getItemId()) {
                    case R.id.globalTracker_bottomNav:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.precautions_bottomNav:
                        viewPager.setCurrentItem(1);
                        break;
                }
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {

                    case R.id.notificationSD:
                        startActivity(new Intent(MainActivity.this, ImportantAnnouncement.class));
                        break;
                    case R.id.shareSD:
                        ShareApk();
                        break;
                    case R.id.aboutSD:
                        startAnimationInfo();
                        break;
                    case R.id.searchSD:
                        if (viewPager.getCurrentItem() != 0) {
                            viewPager.setCurrentItem(0);
                        }
                        globalList.initSearchCountry();
                        break;
                    case R.id.quizSD:
                        startActivity(new Intent(MainActivity.this, RiskLevel.class));
                        break;
                    case R.id.changeCountrySD:
                        openCountryDialog();
                        break;
                    case R.id.stateWiseIndiaSD:
                        startActivity(new Intent(MainActivity.this, IndiaStateWise.class));
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionCode;
            Log.e("12345678", "onCreate: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        CheckUpdate();
    }

    private void CheckUpdate() {

        try {
            firestore = FirebaseFirestore.getInstance();
            firestore.collection("Update")
                    .document("checkUpdate")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                isUpdateAvail = Objects.requireNonNull(task.getResult()).getBoolean("isUpdateAvail");
                                if (isUpdateAvail) {

                                    try {
                                        long versionC = task.getResult().getLong("versionCode");
                                        if (version < versionC) {

                                            firestore.collection("Update")
                                                    .document("checkUpdate")
                                                    .get()
                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if (task.isSuccessful()) {
                                                                String updateLink = Objects.requireNonNull(task.getResult()).getString("updateLink");

                                                                OpenUpdateDialog(updateLink);
                                                            }
                                                        }
                                                    });
                                        }
                                    } catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    });
            firestore.collection("Share")
                    .document("shareLink")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Boolean isActive = task.getResult().getBoolean("active");
                                if (isActive) {
                                    String shareLink = task.getResult().getString("link");
                                    sessionConfig.setShareLink(shareLink);
                                }
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCountryDialog() {

        try {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.update_dialog, null);
            AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.countrySearchAutoComplete);
            MaterialButton yesAction = view.findViewById(R.id.yesActionButton_UpdateDialog);
            TextView noAction = view.findViewById(R.id.noActionTextView_UpdateDialog);

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,
                    getResources().getStringArray(R.array.countryNames));
            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.setAdapter(adapter);

            final Dialog dialog = new Dialog(MainActivity.this);
            dialog.setContentView(view);
            Objects.requireNonNull(dialog.getWindow()).setLayout(-1, -2);
            dialog.show();


            autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedCountry = adapterView.getItemAtPosition(i).toString();
                }
            });

            yesAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, selectedCountry + " selected", Toast.LENGTH_SHORT).show();
                    sessionConfig.setCountry(selectedCountry);
                    globalList.getCurrentData();
                    if (selectedCountry.equalsIgnoreCase("India"))
                        globalList.getIndianData();
                    dialog.dismiss();
                    globalList.swipeRefreshLayout.setRefreshing(true);

                }
            });

            noAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void OpenUpdateDialog(final String updateLink) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Available")
                .setMessage(getResources().getString(R.string.updateAvailableText))
                .setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = updateLink;
                        Intent updateIntent = new Intent(Intent.ACTION_VIEW);
                        updateIntent.setData(Uri.parse(url));
                        startActivity(updateIntent);
                    }
                }).setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.info:
                startAnimationInfo();
                break;
            case R.id.searchCountry:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                globalList.initSearchCountry();
                break;
            case R.id.shareApk:
                ShareApk();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void ShareApk() {

        Intent shareintent = new Intent();

        shareintent.setAction(Intent.ACTION_SEND);

        shareintent.putExtra(Intent.EXTRA_TEXT,
                "Get the latest update of Corona Virus spread and other useful information on \"Corona Tracker\" ! \n\n Download here : "
                        + sessionConfig.getShareLink());
        shareintent.setType("text/plain");
        startActivity(Intent.createChooser(shareintent, "Share via"));
    }

    public void startAnimationInfo() {
        if (!isOpen) {

            int x = viewPager.getRight();
            int y = viewPager.getTop();

            int startRadius = 0;
            int endRadius = (int) Math.hypot(parentContentMain.getWidth(), parentContentMain.getHeight());

            Animator anim = ViewAnimationUtils.createCircularReveal(infoLayout, x, y, startRadius, endRadius);

            infoLayout.setVisibility(View.VISIBLE);
            anim.start();

            isOpen = true;

        } else {

            int x = infoLayout.getRight();
            int y = infoLayout.getTop();

            int startRadius = Math.max(viewPager.getWidth(), viewPager.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(infoLayout, x, y, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    infoLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            anim.start();

            isOpen = false;
        }

    }

    @Override
    public void onBackPressed() {
        if (isOpen) {
            startAnimationInfo();
        } else if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }
}
