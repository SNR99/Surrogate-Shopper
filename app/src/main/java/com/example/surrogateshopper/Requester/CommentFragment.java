package com.example.surrogateshopper.Requester;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import org.json.JSONException;
import org.json.JSONObject;

public class CommentFragment extends Fragment {
  private String vol_id, bas_id, vol_name, msg;
  private TextView Comment, ty;
  private AlertDialog dialog;
  private Button commentBtn, sendBtn;
  private PHPRequests phpRequests;

  @Override
  public View onCreateView(
      LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.comment_fragment, container, false);
    vol_id = getArguments().getString("vol_id");
    bas_id = getArguments().getString("bas_id");
    msg = " Volunteered to get you the basket of items you requested";

    ty = view.findViewById(R.id.myBasketVol);

    phpRequests = new PHPRequests();
    commentBtn = view.findViewById(R.id.commentButton);

    phpRequests.get_user(
        getActivity(),
        vol_id,
        r -> {
          JSONObject jObj = new JSONObject(r);
          String sObj = jObj.getString("response");
          JSONObject Res = new JSONObject(sObj);
          String f = Res.getString("first_name");
          String l = Res.getString("last_name");
          vol_name =
              f.substring(0, 1).toUpperCase()
                  + f.substring(1)
                  + " "
                  + l.substring(0, 1).toUpperCase()
                  + l.substring(1);

          ty.setText(vol_name + msg);
        });

    commentBtn.setOnClickListener(
        b -> {
          AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
          RelativeLayout r = (RelativeLayout) getLayoutInflater().inflate(R.layout.comment, null);
          dialogBuilder.setView(r);
          dialog = dialogBuilder.create();
          dialog.show();
          sendBtn = r.findViewById(R.id.sendComment);
          Comment = r.findViewById(R.id.edit_text_comment);
          sendBtn.setOnClickListener(
              b1 -> {
                String text = Comment.getText().toString();
                if (!text.equals("")) {
                  phpRequests.add_comment(
                      getActivity(),
                      bas_id,
                      text,
                      rr -> {
                        process(rr);
                      });

                } else {
                  Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
                }
              });
        });

    return view;
  }

  private void process(String rr) throws JSONException {

    JSONObject jObj = new JSONObject(rr);
    String sObj = jObj.getString("response");
    Toast.makeText(getActivity(), sObj, Toast.LENGTH_SHORT).show();

    if (sObj.equals("1")) {
      Toast.makeText(getActivity(), "Comment added", Toast.LENGTH_SHORT).show();

      dialog.dismiss();
    } else {
      Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
    }
  }
}
