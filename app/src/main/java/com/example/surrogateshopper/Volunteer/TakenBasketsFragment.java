package com.example.surrogateshopper.Volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import com.example.surrogateshopper.Requester.BasketItemAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TakenBasketsFragment extends Fragment {
  private PHPRequests phpRequests;
  private ArrayList<String> basketArray, quantityArray;
  private ListView listView;
  private ArrayAdapter<String> adapter;
  private RelativeLayout relativeLayout;
  private Button acceptBtn;
  private DatabaseHelper db;
  private BasketItemAdapter basketItemAdapter;

  @Nullable
  @org.jetbrains.annotations.Nullable
  @Override
  public View onCreateView(
      @NonNull @NotNull LayoutInflater inflater,
      @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
      @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_taken_baskets, container, false);
    db = new DatabaseHelper(getActivity());
    String user_id = db.getUserID();

    phpRequests = new PHPRequests();
    basketArray = new ArrayList<>();
    quantityArray = new ArrayList<>();
    acceptBtn = view.findViewById(R.id.acceptRequest);

    relativeLayout = view.findViewById(R.id.pr);
    TextView textView = view.findViewById(R.id.profile_basket);
    listView = view.findViewById(R.id.r);
    listView.setEmptyView(relativeLayout);
    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, basketArray);
    basketItemAdapter = new BasketItemAdapter(getActivity(), basketArray, quantityArray);
    listView.setAdapter(basketItemAdapter);
    assert getArguments() != null;
    String basket_id = getArguments().getString("basket_id");
    String basket_name = getArguments().getString("basket_name");
    textView.setText(basket_name + " Items");
    phpRequests.get_basket_items(getActivity(), basket_id, this::processResponse);
    acceptBtn.setOnClickListener(
        (v1) ->
            phpRequests.accept_request(getActivity(), user_id, basket_id, this::processResponse1));

    return view;
  }

  private void processResponse1(String r) throws JSONException {
    JSONObject jObj = new JSONObject(r);
    String res = jObj.getString("response");
    if (res.equals("1")) {
      Toast.makeText(getActivity(), "successful", Toast.LENGTH_SHORT).show();
    } else {
      Toast.makeText(getActivity(), "failed", Toast.LENGTH_SHORT).show();
    }
  }

  private void processResponse(String response) throws JSONException {
    JSONObject jObj = new JSONObject(response);
    String sObj = jObj.getString("response");
    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String item = jsonObject.getString("item");
        String num = jsonObject.getString("quantity");
        basketArray.add(item);
        quantityArray.add(num);
      }
    }
    basketItemAdapter.notifyDataSetChanged();
  }
}
