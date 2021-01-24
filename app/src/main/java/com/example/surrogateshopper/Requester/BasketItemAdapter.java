package com.example.surrogateshopper.Requester;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.surrogateshopper.R;

import java.util.ArrayList;

public class BasketItemAdapter extends BaseAdapter {
  private final Context mContext;
  private final ArrayList<String> quantity;
  private final ArrayList<String> items;
  private LayoutInflater inflater;

  public BasketItemAdapter(Context c, ArrayList<String> i, ArrayList<String> q) {
    mContext = c;
    this.items = i;
    this.quantity = q;
  }

  @Override
  public int getCount() {
    return items.size();
  }

  @Override
  public Object getItem(int position) {
    return items.get(position);
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
      convertView = inflater.inflate(R.layout.basket_item_adapter, null);
    }
    TextView l = convertView.findViewById(R.id.item_name);
    TextView q = convertView.findViewById(R.id.item_quantity);
    l.setText((position + 1) + ". " + items.get(position));
    q.setText("Qty: " + quantity.get(position));
    return convertView;
  }
}
