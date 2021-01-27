package com.example.surrogateshopper.Requester;

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

public class RequesterActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {
  private DatabaseHelper db;
  private NavigationView navigationView;
  private DrawerLayout drawerLayout;
  private FragmentManager fragmentManager;
  private FragmentTransaction fragmentTransaction;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_requester);
    drawerLayout = findViewById(R.id.RequesterDrawer);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    db = new DatabaseHelper(this);

    navigationView = findViewById(R.id.navigationView);
    navigationView.setNavigationItemSelectedListener(this);
    View linearLayout = navigationView.inflateHeaderView(R.layout.drawer_header);
    TextView headerName = linearLayout.findViewById(R.id.headerName);
    TextView headerEmail = linearLayout.findViewById(R.id.headerEmail);

    headerName.setText(db.getFullName());
    headerEmail.setText(db.getEmail());

    ActionBarDrawerToggle actionDrawerToggle =
        new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
    drawerLayout.addDrawerListener(actionDrawerToggle);
    actionDrawerToggle.setDrawerIndicatorEnabled(true);
    actionDrawerToggle.syncState();

    // load Default

    fragmentManager = getSupportFragmentManager();
    fragmentTransaction = fragmentManager.beginTransaction();
    fragmentTransaction.add(R.id.container_frag, new RequesterFragment()).commit();
  }

  @Override
  public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
    drawerLayout.closeDrawer(GravityCompat.START);

    if (item.getItemId() == R.id.reqHome) {
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.container_frag, new RequesterFragment()).commit();
    }

    if (item.getItemId() == R.id.reqProfile) {
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.container_frag, new RequesterProfile());
      fragmentTransaction.commit();
    }

    if (item.getItemId() == R.id.reqBasket) {
      fragmentManager = getSupportFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.container_frag, new AcceptedRequestsFragment());
      fragmentTransaction.commit();
    }

    if (item.getItemId() == R.id.reqExitApp) System.exit(0);
    return true;
  }
}
