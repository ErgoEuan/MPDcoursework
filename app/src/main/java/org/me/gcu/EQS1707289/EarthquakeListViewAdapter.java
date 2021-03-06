package org.me.gcu.EQS1707289;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class EarthquakeListViewAdapter extends ArrayAdapter<Earthquake> {

    private Context context;
    private int resource;

    static class ViewHolder{
        TextView location;
        TextView magnitude;
    }

    public EarthquakeListViewAdapter(Context context, int resource, List<Earthquake>objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        String location = "";
        Double magnitude = 0.0;

        if (getItem(position) != null) {

            location = getItem(position).getLocation();
            magnitude = getItem(position).getMagnitude();

        }

        ViewHolder holder = new ViewHolder();

        if(convertView ==null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(resource, parent, false);
            holder.location = convertView.findViewById(R.id.locationLabel);
            holder.magnitude = convertView.findViewById(R.id.magnitudeLabel);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.location.setText(location);
        holder.magnitude.setText(String.valueOf(magnitude));
        holder.magnitude.setTextColor(Color.parseColor(ColourCoder.getColourCode(magnitude)));

        return convertView;
    }

}
