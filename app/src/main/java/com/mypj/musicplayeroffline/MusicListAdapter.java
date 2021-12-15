package com.mypj.musicplayeroffline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MusicListAdapter extends ArrayAdapter<String> {
    String[] arrNew;
    TextView textView;

    public MusicListAdapter(@NonNull Context context, int resource, @NonNull String[] arrayOfArrayAdapter) {
        super(context, resource, arrayOfArrayAdapter);
        this.arrNew = arrayOfArrayAdapter;
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return arrNew[position];
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.single_view, parent, false);
        textView = convertView.findViewById(R.id.textView);
        textView.setText(getItem(position));
        return convertView;
    }
}
