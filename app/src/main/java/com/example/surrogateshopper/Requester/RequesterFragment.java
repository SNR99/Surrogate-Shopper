package com.example.surrogateshopper.Requester;

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
import com.example.surrogateshopper.Volunteer.AvailableItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequesterFragment extends Fragment {
  private FragmentManager fragmentManager;
  private FragmentTransaction fragmentTransaction;
  private PHPRequests phpRequests;
  private RelativeLayout loader, main;
  private TextView emptyListss;
  private ArrayList<String> arrayList, arrayItem, basketName, quan, dat, hol;
  private ArrayList<Integer> basketID;
  private ArrayAdapter<String> adapter, adapter1;
  private BasketItemAdapter basketItemAdapter;
  private ProgressBar loaders;
  private ListView itemView;
  private AvailableItemAdapter availableItemAdapter;

  @Nullable
  @org.jetbrains.annotations.Nullable
  @Override
  public View onCreateView(
      @NonNull @NotNull LayoutInflater inflater,
      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.requester_main, container, false);
    main = view.findViewById(R.id.requester_main);
    loader = view.findViewById(R.id.requester_main_loader);
    ListView listView = view.findViewById(R.id.requester_listview);
    emptyListss = view.findViewById(R.id.noBasket);

    main.setVisibility(View.GONE);
    loader.setVisibility(View.VISIBLE);

    phpRequests = new PHPRequests();
    DatabaseHelper db = new DatabaseHelper(getActivity());
    String userID = db.getUserID();
    arrayList = new ArrayList<>();
    arrayItem = new ArrayList<>();
    basketID = new ArrayList<>();
    basketName = new ArrayList<>();
    dat = new ArrayList<>();
    hol = new ArrayList<>();
    quan = new ArrayList<>();
    listView.setOnItemClickListener((parent, v, position, id) -> showPopUp(position));

    // adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, basketName);
    basketItemAdapter = new BasketItemAdapter(getActivity(), arrayItem, quan);
    availableItemAdapter = new AvailableItemAdapter(getActivity(), basketName, hol, dat);
    adapter1 = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, arrayItem);

    listView.setAdapter(availableItemAdapter);
    listView.setEmptyView(emptyListss);

    phpRequests.get_my_basket(getActivity(), userID, this::processResponse);

    FloatingActionButton floatingActionButton = view.findViewById(R.id.floatingActionButton);
    floatingActionButton.setOnClickListener(
        v1 -> {
          fragmentManager = getFragmentManager();
          assert fragmentManager != null;
          fragmentTransaction = fragmentManager.beginTransaction();
          fragmentTransaction.replace(R.id.container_frag, new BasketFragment());
          fragmentTransaction.addToBackStack(null).commit();
        });
    return view;
  }

  private void showPopUp(int position) {
    String basketId = "" + basketID.get(position);

    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
    final View v1 = getLayoutInflater().inflate(R.layout.view_basket_items, null);
    dialogBuilder.setView(v1);
    itemView = v1.findViewById(R.id.popItems);
    loaders = v1.findViewById(R.id.itemsProgress);

    itemView.setAdapter(basketItemAdapter);

    AlertDialog dialog = dialogBuilder.create();
    dialog.show();
    phpRequests.get_basket_items(getActivity(), "" + basketId, this::processResponse1);
  }

  private void processResponse1(String res) throws JSONException {

    arrayItem.clear();
    JSONObject jObj = new JSONObject(res);
    String sObj = jObj.getString("response");
    loaders.setVisibility(View.GONE);
    itemView.setVisibility(View.VISIBLE);

    JSONArray jsonArray = new JSONArray(sObj);
    for (int i = 0; i < jsonArray.length(); i++) {
      JSONObject jsonObject = jsonArray.getJSONObject(i);
      String item = jsonObject.getString("item");
      String q = jsonObject.getString("quantity");
      arrayItem.add(item);
      quan.add(q);
    }
    basketItemAdapter.notifyDataSetChanged();
  }

  private void processResponse(String response) throws JSONException {
    arrayList.clear();
    JSONObject jObj = new JSONObject(response);

    String sObj = jObj.getString("response");

    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String basket_name = jsonObject.getString("basket_name");
        String basket_id = jsonObject.getString("basket_id");
        String d = jsonObject.getString("requested_at");
        String h = "";
        basketName.add(basket_name);
        dat.add(d);
        hol.add(h);
        basketID.add(Integer.parseInt(basket_id));
        arrayList.add("Basket  " + (i + 1));
      }
      availableItemAdapter.notifyDataSetChanged();
    }

    main.setVisibility(View.VISIBLE);
    loader.setVisibility(View.GONE);
  }
}
