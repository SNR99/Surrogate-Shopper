package com.example.surrogateshopper.Requester;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class RequesterProfile extends Fragment {
  private static final int REQUEST_CODE = 100;
  private FusedLocationProviderClient fusedLocationProviderClient;
  private TextView tv;
  private LocationManager locationManager;
  private Dialog dialog;
  private RelativeLayout profile_main;
  private ProgressBar profile_loader, profile_address_loader;
  private Button add_address_btn;
  private TextView my_address;
  private AlertDialog.Builder dialogBuilder;
  private Button add_location;
  private String latitude1, longitude1;

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.requester_profile, container, false);

    DatabaseHelper db = new DatabaseHelper(getActivity());
    PHPRequests phpRequests = new PHPRequests();
    latitude1 = "";
    longitude1 = "";
    profile_main = view.findViewById(R.id.address_prog);
    profile_loader = view.findViewById(R.id.reg_progs);
    TextView fullName = view.findViewById(R.id.regProfileFullName);
    TextView emailAddress = view.findViewById(R.id.regProfileEmail);
    add_address_btn = view.findViewById(R.id.add_address_button);
    my_address = view.findViewById(R.id.my_address);

    fullName.setText(db.getFullName());
    emailAddress.setText(db.getEmail());

    phpRequests.get_my_address(getActivity(), db.getUserID(), this::processAddress);
    fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(Objects.requireNonNull(getActivity()));

    add_address_btn.setOnClickListener(
        s -> {
          dialogBuilder = new AlertDialog.Builder(getActivity());
          View contactPopupView = getLayoutInflater().inflate(R.layout.add_address, null);
          tv = contactPopupView.findViewById(R.id.address_location);
          dialogBuilder.setView(contactPopupView);
          dialog = dialogBuilder.create();
          dialog.show();

          profile_address_loader = contactPopupView.findViewById(R.id.address_location_progressbar);
          add_location = contactPopupView.findViewById(R.id.address_location_btn);
          add_location.setOnClickListener(
              b1 -> {
                if (!latitude1.equals("") && !longitude1.equals("")) phpRequests.add_address(
                        getActivity(),
                        db.getUserID(),
                        latitude1,
                        longitude1,
                        response -> {
                            JSONObject jObj = new JSONObject(response);
                            String res = jObj.getString("response");
                            if (!res.equals("0")) {
                                Toast.makeText(getActivity(), "Address added", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                FragmentManager fragmentManager = getFragmentManager();
                                assert fragmentManager != null;
                                FragmentTransaction fragmentTransaction =
                                        fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_frag, new RequesterProfile());
                                fragmentTransaction.commit();
                            } else Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG)
                                    .show();
                        });
              });
            getLastLocation();
        });

    return view;
  }

  private void processAddress(String response) throws JSONException {
    JSONObject jObj = new JSONObject(response);
    String res = jObj.getString("response");

    if (!res.equals("0")) {
      JSONObject jRes = new JSONObject(res);
      String latitude = jRes.getString("latitude");
      String longitude = jRes.getString("longitude");
      Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
      try {
        List<Address> addresses =
            geocoder.getFromLocation(
                Double.parseDouble(latitude), Double.parseDouble(longitude), 1);

        my_address.setText(addresses.get(0).getAddressLine(0) + "\n");

      } catch (IOException e) {
        e.printStackTrace();
      }

    } else {
      add_address_btn.setVisibility(View.VISIBLE);
      my_address.setText("You haven't added a address");
    }

    profile_loader.setVisibility(View.GONE);
    profile_main.setVisibility(View.VISIBLE);
  }

  private void getLastLocation() {

    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED) fusedLocationProviderClient
            .getLastLocation()
            .addOnSuccessListener(
                    location -> {
                        if (location != null) try {
                            latitude1 = String.valueOf(location.getLatitude());
                            longitude1 = String.valueOf(location.getLongitude());
                            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                            List<Address> addresses =
                                    geocoder.getFromLocation(
                                            location.getLatitude(), location.getLongitude(), 1);

                            tv.setText("Address: " + addresses.get(0).getAddressLine(0));

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
    else askPermission();
  }

  private void askPermission() {

    ActivityCompat.requestPermissions(
            getActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
  }

  @Override
  public void onRequestPermissionsResult(
          int requestCode,
          @NonNull @org.jetbrains.annotations.NotNull String[] permissions,
          @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

    if (requestCode == REQUEST_CODE)
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) getLastLocation();
        else
            Toast.makeText(getActivity(), "Please provide the required permission", Toast.LENGTH_SHORT)
                    .show();

    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }
}
