package com.example.surrogateshopper.Volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment {
  private FragmentManager fragmentManager;
  private FragmentTransaction fragmentTransaction;
  private ArrayList<String> basketArray, idArray, dateArray, userArray;
  private ArrayAdapter<String> adapter;
  private RelativeLayout relativeLayout, relativeLayout1;
  private AvailableItemAdapter availableItemAdapter;

  @Nullable
  @org.jetbrains.annotations.Nullable
  @Override
  public View onCreateView(
      @NonNull @NotNull LayoutInflater inflater,
      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.volunteer_main, container, false);
    relativeLayout = view.findViewById(R.id.volunteer_main_loader);
    relativeLayout1 = view.findViewById(R.id.vol_main_container_list);
    PHPRequests phpRequests = new PHPRequests();
    basketArray = new ArrayList<>();
    idArray = new ArrayList<>();
    userArray = new ArrayList<>();
    dateArray = new ArrayList<>();
    TextView emptyTextVol = view.findViewById(R.id.main_empty);
    ListView availableBaskets = view.findViewById(R.id.available_Basket);
    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, basketArray);
    availableItemAdapter =
        new AvailableItemAdapter(getActivity(), basketArray, userArray, dateArray);
    availableBaskets.setAdapter(availableItemAdapter);
    availableBaskets.setEmptyView(emptyTextVol);

    availableBaskets.setOnItemClickListener(
        (parent, v, position, id) -> {
          TakenBasketsFragment takenBasketsFragment = new TakenBasketsFragment();
          Bundle args = new Bundle();
          args.putString("basket_name", basketArray.get(position));
          args.putString("basket_id", idArray.get(position));
          takenBasketsFragment.setArguments(args);
          fragmentManager = getFragmentManager();
          assert fragmentManager != null;
          fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.replace(R.id.container_frag, takenBasketsFragment);
          fragmentTransaction.addToBackStack(null).commit();
        });

    phpRequests.get_available_baskets(getActivity(), this::processResponse);

    return view;
  }

  private void processResponse(String response) throws JSONException {

    JSONObject jObj = new JSONObject(response);
    String sObj = jObj.getString("response");
    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String name = jsonObject.getString("first_name");
        String surname = jsonObject.getString("last_name");
        String basket_name = jsonObject.getString("basket_name");
        String basket_id = jsonObject.getString("basket_id");
        String basket_date = jsonObject.getString("requested_at");
        basketArray.add(basket_name);
        userArray.add(
            name.substring(0, 1).toUpperCase()
                + name.substring(1)
                + " "
                + surname.substring(0, 1).toUpperCase()
                + surname.substring(1));

        dateArray.add(basket_date);
        idArray.add(basket_id);
      }
    }
    availableItemAdapter.notifyDataSetChanged();
    relativeLayout1.setVisibility(View.VISIBLE);
    relativeLayout.setVisibility(View.GONE);
  }
}
