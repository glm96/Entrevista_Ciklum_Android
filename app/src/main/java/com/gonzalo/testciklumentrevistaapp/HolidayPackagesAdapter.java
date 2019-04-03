package com.gonzalo.testciklumentrevistaapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class HolidayPackagesAdapter extends RecyclerView.Adapter<HolidayPackagesAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(HolidayPackage item);
    }

    private List<HolidayPackage> hplist;
    private final OnItemClickListener listener;

    public HolidayPackagesAdapter(List<HolidayPackage> userModelList, @NonNull OnItemClickListener listener) {
        this.hplist = userModelList;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(hplist.get(position),listener);
    }

    @Override
    public int getItemCount() {
        return hplist.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name, departure, destination, depdate, destdate;

        public ViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.hotelname);
            departure = (TextView) v.findViewById(R.id.dep);
            destination = (TextView) v.findViewById(R.id.dest);
            depdate = (TextView) v.findViewById(R.id.depdate);
            destdate = (TextView) v.findViewById(R.id.destdate);
        }

        public void bind(final HolidayPackage item, final OnItemClickListener listener) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy HH:mm");
            HolidayPackage hp = item;
            String s = "Hotel: " + hp.getLodging().getName()+"  " +Short.toString(hp.getLodging().getStarRating())+"/5";
            name.setText(s);
            s = hp.getOutbound().getDepartureCode();
            departure.setText(s);
            s = sdf.format(hp.getOutbound().getDepartureDate());
            depdate.setText(s);
            s = hp.getOutbound().getArrivalCode();
            destination.setText(s);
            s = sdf.format(hp.getOutbound().getArrivalDate());
            destdate.setText(s);

            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    listener.onItemClick(item);
                }
            });
        }
    }
}
