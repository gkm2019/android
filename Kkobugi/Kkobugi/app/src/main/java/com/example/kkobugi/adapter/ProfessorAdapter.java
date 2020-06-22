package com.example.kkobugi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kkobugi.DTO.Professor;
import com.example.kkobugi.R;

import java.util.ArrayList;

public class ProfessorAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Professor> professorArrayList;

    public ProfessorAdapter(Context context, ArrayList<Professor> professorArrayList){
        this.professorArrayList = professorArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return professorArrayList.size();
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
            view=viewGroup.inflate(context, R.layout.listview_professor_item,null);

        Log.i("TAG", "enter <ProfessorAdapter> ..position is: "+position);

        TextView text_professorName = (TextView)view.findViewById(R.id.text_professorName);
        TextView text_professorDepartment = (TextView)view.findViewById(R.id.text_professorDepartment);
        TextView text_professorField = (TextView)view.findViewById(R.id.text_professorField);
        TextView text_professorTell = (TextView)view.findViewById(R.id.text_professorTell);
        TextView text_professorAddr = (TextView)view.findViewById(R.id.text_professorAddr);
        TextView text_professorEmail = (TextView)view.findViewById(R.id.text_professorEmail);
        ImageView Image_professor = (ImageView)view.findViewById(R.id.image_professor);
        View view_div = (View)view.findViewById(R.id.view_div);

        if(professorArrayList.size()==1){
            view_div.setVisibility(View.GONE);
        }

        text_professorName.setText(professorArrayList.get(position).getProfessorName());
        text_professorDepartment.setText(professorArrayList.get(position).getDepartmentName());
        text_professorField.setText(professorArrayList.get(position).getProfessorField());
        text_professorTell.setText(professorArrayList.get(position).getProfessorTell());
        text_professorEmail.setText(professorArrayList.get(position).getProfessorEmail());
        text_professorAddr.setText(professorArrayList.get(position).getProfessorAddr());

        String image = professorArrayList.get(position).getProfessorImage();
        int rid = context.getResources().getIdentifier(image,"drawable",context.getPackageName());
        Image_professor.setImageResource(rid);
        Image_professor.setScaleType(ImageView.ScaleType.FIT_XY);
        return view;
    }
}
