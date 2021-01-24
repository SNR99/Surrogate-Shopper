package com.example.surrogateshopper.Volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import org.jetbrains.annotations.NotNull;

public class VolunteerBasket extends Fragment {
  private PHPRequests phpRequests;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull @NotNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.volunteer_basket, container, false);
    phpRequests = new PHPRequests();

    return view;
  }
}
