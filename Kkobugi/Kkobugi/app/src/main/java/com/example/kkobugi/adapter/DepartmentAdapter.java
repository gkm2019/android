package com.example.kkobugi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kkobugi.DTO.Department;
import com.example.kkobugi.R;

import java.util.ArrayList;

public class DepartmentAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Department> departmentArrayList;

    public DepartmentAdapter(Context context, ArrayList<Department> departmentArrayList){
        this.departmentArrayList = departmentArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return departmentArrayList.size();
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
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if(convertView == null)
            convertView = viewGroup.inflate(context, R.layout.listview_department_item,null);

        Log.i("TAG","enter this and position is => "+position);

        Department department = departmentArrayList.get(position);
        TextView text_departmentName = (TextView)convertView.findViewById(R.id.text_departmentName);
        TextView text_departmentContent = (TextView)convertView.findViewById(R.id.text_departmentContent);

        View view_div = (View)convertView.findViewById(R.id.view_div);

        if(departmentArrayList.size()==1){
            view_div.setVisibility(View.GONE);
        }
        text_departmentName.setText(departmentArrayList.get(position).getDepartmentName());
        text_departmentContent.setText(departmentArrayList.get(position).getDepartmentContent());

        return convertView;
    }
}
