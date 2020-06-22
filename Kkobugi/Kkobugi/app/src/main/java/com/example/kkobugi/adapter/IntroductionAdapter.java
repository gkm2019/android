package com.example.kkobugi.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kkobugi.DTO.Introduction;
import com.example.kkobugi.R;

import java.util.ArrayList;

public class IntroductionAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Introduction> introductionArrayList;

    public IntroductionAdapter(Context context, ArrayList<Introduction> introductionArrayList){
        this.introductionArrayList = introductionArrayList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return introductionArrayList.size();
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
            convertView = viewGroup.inflate(context, R.layout.listview_introduction_item,null);

        Log.i("TAG", "enter <IntroductionAdapter> ..position is: "+position);

        Introduction introduction = introductionArrayList.get(position);
        TextView text_introductionName = (TextView)convertView.findViewById(R.id.text_introductionName);
        TextView text_introductionContent = (TextView)convertView.findViewById(R.id.text_introductionContent);
        ImageView Image_introImage = (ImageView)convertView.findViewById(R.id.image_intro);

        View view_div = (View)convertView.findViewById(R.id.view_div);
        if(introductionArrayList.size()==1){
            view_div.setVisibility(View.GONE);
        }
        text_introductionName.setText(introduction.getIntroductionName());
        text_introductionContent.setText(introduction.getIntroductionContent());

        String image = introductionArrayList.get(position).getIntroImage();
        if(image!=null) {
            int rid = context.getResources().getIdentifier(image, "drawable", context.getPackageName());
            Image_introImage.setImageResource(rid);
            Image_introImage.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return convertView;
    }

}
