package com.example.surrogateshopper.Volunteer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.surrogateshopper.R;

import java.util.ArrayList;

public class AvailableItemAdapter extends BaseAdapter {

  private Context mContext;
  private ArrayList<String> basketName, user, date;
  private LayoutInflater inflater;
  TextView b, u, d;

  public AvailableItemAdapter(
      Context context,
      ArrayList<String> basketName,
      ArrayList<String> user,
      ArrayList<String> date) {
    mContext = context;
    this.basketName = basketName;
    this.user = user;
    this.date = date;
  }

  @Override
  public int getCount() {
    return basketName.size();
  }

  @Override
  public Object getItem(int position) {
    return basketName.get(position);
  }

  @Override
  public long getItemId(int position) {
    return 0;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    if (inflater == null) {
      inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    if (convertView == null) {
      convertView = inflater.inflate(R.layout.aval_basket, null);
      b = convertView.findViewById(R.id.aval_basket_name);
      u = convertView.findViewById(R.id.user_basket);
      d = convertView.findViewById(R.id.basket_date);

      b.setText(basketName.get(position));
      u.setText(user.get(position));
      d.setText(date.get(position));
    }

    return convertView;
  }
}
