package com.example.glorious.warehousemanagement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class customAdapter extends ArrayAdapter<Product> {

    customAdapter(Context context, List<Product> elements) {
        super(context, R.layout.custom_list_view, elements);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // return super.getView(position, convertView, parent);
        LayoutInflater lif = LayoutInflater.from(getContext());
        View customView = lif.inflate(R.layout.custom_list_view, parent, false);
        // Now the elements
        Product product = getItem(position);;
        ImageView imgP = (ImageView) customView.findViewById(R.id.imgProductList);
        TextView pText = (TextView) customView.findViewById(R.id.textViewPname);
        TextView pDesc = (TextView) customView.findViewById(R.id.textViewPDesc);
        TextView pQuan = (TextView) customView.findViewById(R.id.txtQuantity);
        TextView pPric = (TextView) customView.findViewById(R.id.txtPrice);
        // Now map views to elements
        imgP.setImageResource(R.drawable.default_image); // Until we figure it out
        pText.setText(product.getName());
        pDesc.setText(product.getDescription());
        pQuan.setText("Quantity: " + product.getQuantity());
        pPric.setText("Price: " + product.getPrice());
        // return customView
        return customView;
    }
}
