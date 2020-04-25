package com.jcodder.pr4ctice.ui.view;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.jcodder.pr4ctice.model.Measure;

import java.util.ArrayList;

public class MeasureAdapter implements ListAdapter {
    ArrayList<Measure> measures;
    Context context;

    public MeasureAdapter(Context context, ArrayList<Measure> measures) {
        this.measures=measures;
        this.context=context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return measures.size();
    }

    @Override
    public Object getItem(int i) {
        return measures.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Measure measure = measures.get(i);
        //TODO
        return null;
    }

    @Override
    public int getItemViewType(int i) {
        return i;
    }

    @Override
    public int getViewTypeCount() {
        return measures.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
