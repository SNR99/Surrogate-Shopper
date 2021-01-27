package com.example.surrogateshopper.Volunteer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolunteerBasket extends Fragment {
  private PHPRequests phpRequests;
  private DatabaseHelper db;
  private ArrayList<String> fullName, basketName, basketDate, basketID, userAddress;
  private AvailableItemAdapter availableItemAdapter;
  private ListView lv;
  private TextView tv;
  private RelativeLayout r1, r2;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull @NotNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.volunteer_basket, container, false);
    phpRequests = new PHPRequests();
    db = new DatabaseHelper(getActivity());
    fullName = new ArrayList<>();
    basketName = new ArrayList<>();
    basketDate = new ArrayList<>();
    basketID = new ArrayList<>();
    userAddress = new ArrayList<>();
    String user_id = db.getUserID();
    lv = view.findViewById(R.id.listb);
    tv = view.findViewById(R.id.tcl);
    r1 = view.findViewById(R.id.rel1);
    r2 = view.findViewById(R.id.rel2);

    lv.setOnItemClickListener(
        (parent, view1, position, id) -> {
          to2map(userAddress.get(position));
        });
    availableItemAdapter =
        new AvailableItemAdapter(getActivity(), basketName, fullName, basketDate);
    lv.setAdapter(availableItemAdapter);
    lv.setEmptyView(tv);

    phpRequests.get_vol_basket(getActivity(), user_id, this::processResponse);

    return view;
  }

  private void to2map(String addressID) {
    phpRequests.get_address(
            getActivity(),
        addressID,
        r -> {
          userAddress.clear();
          JSONObject jObj = new JSONObject(r);
          String res = jObj.getString("response");
          if (!res.equals("0")) {
            JSONObject jRes = new JSONObject(res);
            String lat5 = jRes.getString("latitude");
            String lon5 = jRes.getString("longitude");

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:" + lat5 + "," + lon5));
            Intent chooser = Intent.createChooser(intent, "Launch Maps");
            getContext().startActivity(chooser);
          }
        });
  }

  private void processResponse(String r) throws JSONException {
    basketName.clear();
    basketID.clear();
    fullName.clear();
    basketDate.clear();
    userAddress.clear();

    JSONObject jObj = new JSONObject(r);
    String sObj = jObj.getString("response");
    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String name = jsonObject.getString("first_name");
        String surname = jsonObject.getString("last_name");
        String basket_name = jsonObject.getString("basket_name");
        String requested_at = jsonObject.getString("requested_at");
        String basket_id = jsonObject.getString("basket_id");
        String address = jsonObject.getString("address");
        fullName.add(
            name.substring(0, 1).toUpperCase()
                + name.substring(1)
                + " "
                + surname.substring(0, 1).toUpperCase()
                + surname.substring(1));
        userAddress.add(address);
        basketName.add(basket_name);
        basketID.add(basket_id);
        basketDate.add(requested_at);
      }
      r1.setVisibility(View.VISIBLE);
      r2.setVisibility(View.GONE);
      availableItemAdapter.notifyDataSetChanged();
    }
  }
}
