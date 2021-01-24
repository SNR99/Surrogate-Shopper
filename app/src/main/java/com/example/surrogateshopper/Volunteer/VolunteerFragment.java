package com.example.surrogateshopper.Volunteer;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import com.example.surrogateshopper.Requester.BasketItemAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolunteerFragment extends Fragment {
  private ArrayList<String> basketNameArray,
      basketDateArray,
      basketOwnerArray,
      basketIDArray,
      basketItemArray,
      quantityItemArray;
  private ListView availableListView, basketItemListView;
  private RelativeLayout mainLayout, loaderLayout, loaderLayout1;
  private BasketItemAdapter basketItemAdapter;
  private AvailableItemAdapter availableItemAdapter;
  private PHPRequests phpRequests;
  private AlertDialog.Builder dialogBuilder;
  private AlertDialog dialog;
  private RelativeLayout rt, lt;
  private DatabaseHelper databaseHelper;
  private FragmentManager fragmentManager;
  private FragmentTransaction fragmentTransaction;

  @Nullable
  @org.jetbrains.annotations.Nullable
  @Override
  public View onCreateView(
      @NonNull @NotNull LayoutInflater inflater,
      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.volunteer_main, container, false);

    // init
    loaderLayout = view.findViewById(R.id.volunteer_main_loader);
    mainLayout = view.findViewById(R.id.vol_main_container_list);

    availableListView = view.findViewById(R.id.available_Basket);

    phpRequests = new PHPRequests();
    databaseHelper = new DatabaseHelper(getActivity());

    basketNameArray = new ArrayList<>();
    basketDateArray = new ArrayList<>();
    basketOwnerArray = new ArrayList<>();
    basketIDArray = new ArrayList<>();
    basketItemArray = new ArrayList<>();
    quantityItemArray = new ArrayList<>();

    availableItemAdapter =
        new AvailableItemAdapter(getActivity(), basketNameArray, basketOwnerArray, basketDateArray);

    availableListView.setAdapter(availableItemAdapter);
    availableListView.setEmptyView(view.findViewById(R.id.main_empty));

    availableListView.setOnItemClickListener(
        (parent, v, position, id) -> {
          phpRequests.get_basket_items(
                  getActivity(), basketIDArray.get(position), this::processAvailableItems);
          dialogBuilder = new AlertDialog.Builder(getActivity());
          View p = getLayoutInflater().inflate(R.layout.fragment_taken_baskets, null);
          basketItemAdapter =
              new BasketItemAdapter(getActivity(), basketItemArray, quantityItemArray);
          rt = p.findViewById(R.id.ftb);
          Button acceptBtn = p.findViewById(R.id.acceptRequest);
          acceptBtn.setOnClickListener(
              b1 -> {
                phpRequests.accept_request(
                        getActivity(),
                    databaseHelper.getUserID(),
                    basketIDArray.get(position),
                    r1 -> {
                      JSONObject jObj = new JSONObject(r1);
                      String res = jObj.getString("response");
                      if (res.equals("1")) {
                        Toast.makeText(getActivity(), "successful", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        fragmentManager = getFragmentManager();
                        fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.container_frag, new VolunteerBasket());
                        fragmentTransaction.commit();

                      } else Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
                    });
              });

          TextView ts = p.findViewById(R.id.profile_basket);
          ts.setText(basketNameArray.get(position));
          basketItemListView = p.findViewById(R.id.r);
          loaderLayout1 = p.findViewById(R.id.pr);
          basketItemListView.setAdapter(basketItemAdapter);
          dialogBuilder.setView(p);
          dialog = dialogBuilder.create();
          dialog.show();
        });

    // on create

    phpRequests.get_available_baskets(getActivity(), this::processAvailableBaskets);
    return view;
  }

  private void processAvailableItems(String r) throws JSONException {
    basketItemArray.clear();
    quantityItemArray.clear();
    JSONObject jObj = new JSONObject(r);
    String sObj = jObj.getString("response");
    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String item = jsonObject.getString("item");
        String num = jsonObject.getString("quantity");
        basketItemArray.add(item);
        quantityItemArray.add(num);
      }
    }

    rt.setVisibility(View.VISIBLE);
    loaderLayout1.setVisibility(View.GONE);
    basketItemAdapter.notifyDataSetChanged();
  }

  private void processAvailableBaskets(String r) throws JSONException {
    basketNameArray.clear();
    basketIDArray.clear();
    basketOwnerArray.clear();
    basketDateArray.clear();

    JSONObject jObj = new JSONObject(r);
    String sObj = jObj.getString("response");
    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String name = jsonObject.getString("first_name");
        String surname = jsonObject.getString("last_name");
        String basket_name = jsonObject.getString("basket_name");
        String basket_id = jsonObject.getString("basket_id");
        Toast.makeText(getActivity(), basket_name, Toast.LENGTH_LONG);
        String basket_date = jsonObject.getString("requested_at");
        basketNameArray.add(basket_name);
        basketOwnerArray.add(
            name.substring(0, 1).toUpperCase()
                + name.substring(1)
                + " "
                + surname.substring(0, 1).toUpperCase()
                + surname.substring(1));

        basketDateArray.add(basket_date);
        basketIDArray.add(basket_id);
        availableItemAdapter.notifyDataSetChanged();
        mainLayout.setVisibility(View.VISIBLE);
        loaderLayout.setVisibility(View.GONE);
      }
    }
  }
}
