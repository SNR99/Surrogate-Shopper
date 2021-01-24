package com.example.surrogateshopper.Requester;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AcceptedRequestsFragment extends Fragment {
  private DatabaseHelper databaseHelper;
  private PHPRequests phpRequests;
  private TextView textView;
  private ArrayAdapter<String> adapter;
  private ArrayList<String> volID, basketID, basketName;
  private ListView listView;

  private Fragment commentFragment;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.accepted_requests_fragment, container, false);
    databaseHelper = new DatabaseHelper(getActivity());
    phpRequests = new PHPRequests();
    textView = view.findViewById(R.id.yrk);
    listView = view.findViewById(R.id.accepted_list_view);
    listView.setEmptyView(textView);
    listView.setOnItemClickListener(
        (parent, v, position, id) -> {
          String vol_id = volID.get(position) + "";
          String bas_id = basketID.get(position) + "";
          Bundle bundle = new Bundle();
          bundle.putString("vol_id", vol_id);
          bundle.putString("bas_id", bas_id);
          commentFragment = new CommentFragment();
          commentFragment.setArguments(bundle);
          getFragmentManager()
              .beginTransaction()
              .replace(R.id.container_frag, commentFragment)
              .addToBackStack(null)
              .commit();
        });

    volID = new ArrayList<>();
    basketID = new ArrayList<>();
    basketName = new ArrayList<>();
    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, basketName);
    listView.setAdapter(adapter);
    String user_id = databaseHelper.getUserID();
    phpRequests.accepted_requests(
        getActivity(),
        user_id,
        r -> {
          process(r);
        });

    return view;
  }

  private void process(String response) throws JSONException {
    JSONObject jObj = new JSONObject(response);
    String sObj = jObj.getString("response");
    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);

      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String basket_name = jsonObject.getString("basket_name");
        String basket_id = jsonObject.getString("basket_id");
        String vol = jsonObject.getString("volunteer");
        volID.add(vol);
        basketID.add(basket_id);
        basketName.add(basket_name);
        adapter.notifyDataSetChanged();
      }
    }
  }
}
