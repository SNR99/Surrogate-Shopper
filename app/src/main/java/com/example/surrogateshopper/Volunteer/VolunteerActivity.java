package com.example.surrogateshopper.Volunteer;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.R;
import com.google.android.material.navigation.NavigationView;
import org.jetbrains.annotations.NotNull;

public class VolunteerActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private DrawerLayout drawerLayout;
  private Toolbar toolbar;
  private NavigationView navigationView;
  private ActionBarDrawerToggle actionDrawerToggle;
  private FragmentManager fragmentManager;
  private FragmentTransaction fragmentTransaction;
  private DatabaseHelper db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_volunteer);

    drawerLayout = findViewById(R.id.VolunteerDrawer);
    toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    navigationView = findViewById(R.id.navigationView1);
    navigationView.setNavigationItemSelectedListener(this);
    View linearLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
    db = new DatabaseHelper(this);

    TextView headerName = linearLayout.findViewById(R.id.headerName);
    TextView headerEmail = linearLayout.findViewById(R.id.headerEmail);
    headerName.setText(db.getFullName());
    headerEmail.setText(db.getEmail());

    actionDrawerToggle =
        new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
    drawerLayout.addDrawerListener(actionDrawerToggle);
    actionDrawerToggle.setDrawerIndicatorEnabled(true);
    actionDrawerToggle.syncState();

    // load Default

    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.container_frag, new VolunteerFragment()).commit();
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
    drawerLayout.closeDrawer(GravityCompat.START);

    if (item.getItemId() == R.id.volHome) {
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.container_frag, new VolunteerFragment()).commit();
    }
    if (item.getItemId() == R.id.volProfile) {
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.container_frag, new VolunteerProfile());
      fragmentTransaction.addToBackStack(null).commit();
    }

    if (item.getItemId() == R.id.volBasket) {
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.container_frag, new VolunteerBasket());
      fragmentTransaction.addToBackStack(null).commit();
    }

    if (item.getItemId() == R.id.volExitApp) {
      System.exit(0);
    }

    return true;
  }
}
