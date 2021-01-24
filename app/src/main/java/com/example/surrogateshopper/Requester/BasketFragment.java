package com.example.surrogateshopper.Requester;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.surrogateshopper.DatabaseHelper;
import com.example.surrogateshopper.PHPRequests;
import com.example.surrogateshopper.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BasketFragment extends Fragment {

  private AlertDialog dialog;
  private ArrayList<String> arrayList, arrayQuantity;
  private ArrayAdapter<String> adapter;
  private BasketItemAdapter basketItemAdapter;
  private EditText editItemView, basket_name;
  private PHPRequests phpRequests;
  private FragmentManager fragmentManager;
  private FragmentTransaction fragmentTransaction;

  @Override
  public View onCreateView(
          LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_basket, container, false);
    phpRequests = new PHPRequests();
    DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
    ListView itemList = view.findViewById(R.id.itemList);
    TextView itemEmpty = view.findViewById(R.id.itemEmpty);
    FloatingActionButton itemFloatBtn = view.findViewById(R.id.floatingActionButtonItem);
    FloatingActionButton saveFloatBtn = view.findViewById(R.id.floatingActionButtonSave);
    arrayList = new ArrayList<>();
    arrayQuantity = new ArrayList<>();
    basketItemAdapter = new BasketItemAdapter(getActivity(), arrayList, arrayQuantity);
    itemList.setAdapter(basketItemAdapter);
    itemList.setEmptyView(itemEmpty);
    String user_id = databaseHelper.getUserID();
    basket_name = view.findViewById(R.id.basketName);

    itemFloatBtn.setOnClickListener(v -> showPopUp());
    saveFloatBtn.setOnClickListener(
        v1 -> {
          String basketName = basket_name.getText().toString();

          if (arrayList.size() != 0 && !basketName.equals("")) phpRequests.add_basket(
                  getActivity(),
                  basketName,
                  user_id,
                  arrayList,
                  arrayQuantity,
                  response -> {
                      processResponse(response);
                  });
          else Toast.makeText(getActivity(), "Your Basket is empty", Toast.LENGTH_SHORT).show();
        });

    return view;
  }

  private void processResponse(String response) throws JSONException {
    JSONObject jsonObject = new JSONObject(response);
    String str = jsonObject.getString("response");
    if (str.equals("1")) {
      fragmentManager = getFragmentManager();
      fragmentTransaction = fragmentManager.beginTransaction();
      fragmentTransaction.replace(R.id.container_frag, new RequesterFragment());
      fragmentTransaction.commit();

    } else Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
  }

  private void showPopUp() {

    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
    View contactPopupView = getLayoutInflater().inflate(R.layout.item_popup, null);
    Button addItemButton = contactPopupView.findViewById(R.id.add_item_btn);
    Button increaseBtn = contactPopupView.findViewById(R.id.increase);
    Button decreaseBtn = contactPopupView.findViewById(R.id.decrease);
    TextView quantity_counter = contactPopupView.findViewById(R.id.counterView);

    increaseBtn.setOnClickListener(
        b1 -> {
          String cc = quantity_counter.getText().toString();

          int c = Integer.parseInt(cc);
          quantity_counter.setText((c + 1) + "");
        });
    decreaseBtn.setOnClickListener(
        b1 -> {
          String cc = quantity_counter.getText().toString();
          int c = Integer.parseInt(cc);
          if (c != 0) quantity_counter.setText((c - 1) + "");
        });

    editItemView = contactPopupView.findViewById(R.id.edit_item);

    dialogBuilder.setView(contactPopupView);
    dialog = dialogBuilder.create();
    dialog.show();

    addItemButton.setOnClickListener(
        v1 -> {
          String s = editItemView.getText().toString();
          String item_num = quantity_counter.getText().toString();

          if (!s.equals("") && !item_num.equals("0")) {
            arrayList.add(s);
            arrayQuantity.add(item_num);
            editItemView.setText("");
            basketItemAdapter.notifyDataSetChanged();

            dialog.dismiss();
          } else if (s.equals("")) Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
          else if (item_num.equals("0"))
              Toast.makeText(getActivity(), "How many do you want?", Toast.LENGTH_SHORT).show();
        });
  }
}
