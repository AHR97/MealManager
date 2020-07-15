package com.example.mealmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class MealDataAdapter extends ArrayAdapter<MealDataListItem> {

    private Context mContext;
    private int mResource;
    private int lastPosition = -1;
   
    /**
     * Holds variables in a View
     */
    private static class ViewHolder {
        TextView userName;
        TextView cost;
        TextView date;
    }


    /**
     * Default constructor for the MealDataAdapter
     * @param context
     * @param resource
     * @param objects
     */




    public MealDataAdapter(Context context, int resource, ArrayList<MealDataListItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get the persons information
        String userName = getItem(position).getUserName();
        String cost = getItem(position).getCost();
        String date = getItem(position).getDate();

        //Create the person object with the information
        MealDataListItem person = new MealDataListItem(date,userName,cost);

        //create the view result for showing the animation
        final View result;

        //ViewHolder object
        ViewHolder holder;


        if(convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder= new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.mealUserName);
            holder.cost = (TextView) convertView.findViewById(R.id.cost);
            holder.date = (TextView) convertView.findViewById(R.id.mealDate);

            result = convertView;

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }


        Animation animation = AnimationUtils.loadAnimation(mContext,
                (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.userName.setText(person.getUserName());
        holder.date.setText(person.getDate());
        holder.cost.setText(person.getCost());



        return convertView;
    }}
