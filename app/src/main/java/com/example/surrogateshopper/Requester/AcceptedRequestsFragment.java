package com.example.surrogateshopper.Requester;

import android.app.AlertDialog;
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

public class AcceptedRequestsFragment extends Fragment {
  private DatabaseHelper db;
  private AlertDialog.Builder dialogBuilder;
  private AlertDialog dialog;
  private ListView listView;
  private ArrayAdapter<String> adapter;
  private ArrayList<String> basketName, volID, basketID;
  private PHPRequests phpRequests;
  private RelativeLayout relativeLayout1, relativeLayout2;

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.accepted_requests_fragment, container, false);
    final String msg = " volunteered to get you the basket of items you requested";

    relativeLayout1 = view.findViewById(R.id.relativeLayoutOne);
    relativeLayout2 = view.findViewById(R.id.relativeLayoutTwo);

    db = new DatabaseHelper(getActivity());
    phpRequests = new PHPRequests();

    basketName = new ArrayList<>();
    volID = new ArrayList<>();
    basketID = new ArrayList<>();

    listView = view.findViewById(R.id.accepted_list_view);
    adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, basketName);
    listView.setAdapter(adapter);
    listView.setEmptyView(view.findViewById(R.id.yrk));
    listView.setOnItemClickListener(
        (parent, view1, position, id) -> {
          dialogBuilder = new AlertDialog.Builder(getActivity());
          View v1 = getLayoutInflater().inflate(R.layout.comment, null);
          RelativeLayout rl1;
          RelativeLayout rl2;
          rl1 = v1.findViewById(R.id.comment_main_xml);
          rl2 = v1.findViewById(R.id.commentProgress);
          Button ac = v1.findViewById(R.id.sendCommentBtn);
          EditText Comment = v1.findViewById(R.id.edit_text_comment);
          TextView h = v1.findViewById(R.id.volNameReg);
          phpRequests.get_user(
                  getActivity(),
              volID.get(position),
              w -> {
                JSONObject jObj = new JSONObject(w);
                String sObj = jObj.getString("response");
                JSONObject j = new JSONObject(sObj);
                String f = j.getString("first_name");
                h.setText(f.substring(0, 1).toUpperCase() + f.substring(1) + msg);
                rl1.setVisibility(View.VISIBLE);
                rl2.setVisibility(View.GONE);
              });

          ac.setOnClickListener(
              b1 -> {
                String co = Comment.getText().toString();
                if (!co.equals("")) phpRequests.add_comment(
                        getActivity(),
                        basketID.get(position),
                        co,
                        a -> {
                            JSONObject jObj = new JSONObject(a);
                            String sObj = jObj.getString("response");

                            if (sObj.equals("1")) {
                                Toast.makeText(getActivity(), "Comment added", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();

                                dialog.dismiss();
                            } else Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT)
                                    .show();
                        });
                else Toast.makeText(getActivity(), "Comment field Empty", Toast.LENGTH_LONG).show();
              });

          dialogBuilder.setView(v1);
          dialog = dialogBuilder.create();
          dialog.show();
        });

    phpRequests.accepted_requests(getActivity(), db.getUserID(), this::processAccepted);

    return view;
  }

  private void processAccepted(String r) throws JSONException {
    JSONObject jObj = new JSONObject(r);
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
      }
    }
    adapter.notifyDataSetChanged();
    relativeLayout1.setVisibility(View.VISIBLE);
    relativeLayout2.setVisibility(View.GONE);
  }
}
