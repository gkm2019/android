package com.example.kkobugi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kkobugi.DTO.NumberInfo;
import com.example.kkobugi.R;

import java.util.ArrayList;

public class NumberAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<NumberInfo> numberArrayList;

    public NumberAdapter(Context context, ArrayList<NumberInfo> numberArrayList){
        this.numberArrayList = numberArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return numberArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        if(view == null)
            view = viewGroup.inflate(context, R.layout.listview_number_item,null);

        Log.i("TAG", "enter <NumberAdapter> ..position is: "+position);

        NumberInfo numberInfo = numberArrayList.get(position);
        TextView text_numberDepartmentName = (TextView)view.findViewById(R.id.text_numDepartment);
        TextView text_number = (TextView)view.findViewById(R.id.text_number);
        TextView text_numberFax = (TextView)view.findViewById(R.id.text_numberFax);

        View view_div = (View)view.findViewById(R.id.view_div);

        if(numberArrayList.size()==1){
            view_div.setVisibility(View.GONE);
        }
        text_numberDepartmentName.setText(numberInfo.getDepartmentName());
        text_number.setText(numberInfo.getNumber());
        text_numberFax.setText(numberInfo.getNumberFax());

        return view;
    }
}
