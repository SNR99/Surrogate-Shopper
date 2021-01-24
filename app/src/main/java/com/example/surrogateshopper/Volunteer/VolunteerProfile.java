package com.example.surrogateshopper.Volunteer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class VolunteerProfile extends Fragment {

  private DatabaseHelper db;
  private TextView name, email;
  private PHPRequests phpRequests;
  private ArrayList<String> commentArrayList;
  private ArrayAdapter<String> adapter;
  private ListView commentsListView;
  private String reqName, reqID;
  private RelativeLayout rl;
  private ProgressBar pb;
  private TextView t;

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.volunteer_profile, container, false);
    db = new DatabaseHelper(getActivity());
    phpRequests = new PHPRequests();
    name = view.findViewById(R.id.VolProfileFullName);
    email = view.findViewById(R.id.VolProfileEmail);
    commentsListView = view.findViewById(R.id.my_comments);
    name.setText(db.getFullName());
    email.setText(db.getEmail());
    commentArrayList = new ArrayList<>();
    t = view.findViewById(R.id.ptv);

    rl = view.findViewById(R.id.Rvol_prog);
    pb = view.findViewById(R.id.vol_progs);

    adapter =
        new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, commentArrayList);
    commentsListView.setAdapter(adapter);
    commentsListView.setEmptyView(t);

    phpRequests.get_vol_comments(
            getActivity(),
        db.getUserID(),
        r -> {
          processComments(r);
        });

    return view;
  }

  private void processComments(String r) throws JSONException {
    commentArrayList.clear();
    JSONObject jObj = new JSONObject(r);
    String sObj = jObj.getString("response");
    if (!sObj.equals("0")) {
      JSONArray jsonArray = new JSONArray(sObj);
      for (int i = 0; i < jsonArray.length(); i++) {
        JSONObject jsonObject = jsonArray.getJSONObject(i);
        String comment = jsonObject.getString("message");
        reqID = jsonObject.getString("requester");
        commentArrayList.add(comment);
      }
    }
    rl.setVisibility(View.VISIBLE);
    pb.setVisibility(View.GONE);

    adapter.notifyDataSetChanged();
  }
}
