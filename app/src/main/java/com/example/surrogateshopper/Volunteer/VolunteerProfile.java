package com.example.surrogateshopper.Volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.R;

public class VolunteerProfile extends Fragment {

  private DatabaseHelper db;
  private TextView f, e;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.volunteer_profile, container, false);
    db = new DatabaseHelper(getActivity());
    f = view.findViewById(R.id.VolProfileFullName);
    e = view.findViewById(R.id.VolProfileEmail);
    String fullName = db.getFullName();
    String email = db.getEmail();

    f.setText(fullName);
    e.setText(email);
    return view;
  }
}
