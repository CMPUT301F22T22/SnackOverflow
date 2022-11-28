package com.example.snackoverflow;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
/**
 * MealdayAdapter to view the meals in ExpandableList view
 * @see Mealday
 * */
public class MealdayAdapter extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<Mealday> mealdays = new ArrayList<Mealday>();
    private FragmentManager fm;

    /**
     * Constuctor for MealdayAdapter
     * @param mealdays All planned meals for the week
     * @see Context
     * @see FragmentManager
     * */
    public MealdayAdapter(Context context, ArrayList<Mealday> mealdays, FragmentManager fm){
        this.context = context;
        this.mealdays = mealdays;
        this.fm = fm;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public int getGroupCount() {
        return mealdays.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mealdays.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition , int childPosition) {
        return mealdays.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        ParentHolder parentHolder = null;

        Mealday mealday = (Mealday) getGroup(groupPosition);

        if(convertView == null) {
            LayoutInflater userInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = userInflater.inflate(R.layout.mealplanner_parent, null);
            convertView.setHorizontalScrollBarEnabled(true);

            parentHolder = new ParentHolder();
            convertView.setTag(parentHolder);

        } else {
            parentHolder = (ParentHolder) convertView.getTag();
        }

        parentHolder.mealday = (TextView) convertView.findViewById(R.id.text_meal);
        parentHolder.mealday.setText(getDayStringOld(mealday.getDate(),new Locale("en")));

        parentHolder.indicator = (ImageView) convertView.findViewById(R.id.image_indicator);

        if(isExpanded) {
            parentHolder.indicator.setImageResource(R.drawable.ic_up_key);
        } else {
            parentHolder.indicator.setImageResource(R.drawable.ic_down_key);
        }

        return convertView;
    }

//    public static int getDayNumberOld(Date date) {
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(date);
//        return cal.get(Calendar.DAY_OF_WEEK);
//    }

    public static String getDayStringOld(Date date, Locale locale) {
        DateFormat formatter = new SimpleDateFormat("EEEE", locale);
        return formatter.format(date);
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ChildHolder childHolder = null;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.mealpplanner_groupitem, parent, false);
            childHolder = new ChildHolder();
            convertView.setTag(childHolder);
        }
        else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        childHolder.horizontalListView = (RecyclerView) convertView.findViewById(R.id.meals);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        childHolder.horizontalListView.setLayoutManager(layoutManager);

        MealPlannerAdapter horizontalListAdapter = new MealPlannerAdapter(context, mealdays.get(groupPosition), fm);
        childHolder.horizontalListView.setAdapter(horizontalListAdapter);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {

    }

    @Override
    public void onGroupCollapsed(int groupPosition) {

    }

    @Override
    public long getCombinedChildId(long groupId, long childId) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long groupId) {
        return 0;
    }


    private static class ChildHolder{
        static RecyclerView horizontalListView;
    }

    private static class ParentHolder{
        TextView mealday;
        ImageView indicator;
    }
}