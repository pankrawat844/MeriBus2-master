package paztechnologies.com.meribus.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

import paztechnologies.com.meribus.R;
import paztechnologies.com.meribus.model.Pay_Per_Day_Model;

/**
 * Created by Admin on 5/31/2017.
 */

public class Pay_Per_Day_Adapter extends RecyclerView.Adapter<Pay_Per_Day_Adapter.viewholder> {
        List<Pay_Per_Day_Model> list;

        public Pay_Per_Day_Adapter(List<Pay_Per_Day_Model> list){
            this.list=list;
        }
    @Override
    public viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payper_layout,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(viewholder holder, int position) {
        Pay_Per_Day_Model model= list.get(position);
        holder.srno.setText(model.getS_no());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder{
        Spinner trip_type;
        CheckBox select_check;
        TextView srno,date,total_seat,seat;
        public viewholder(View itemView) {
            super(itemView);
            trip_type=(Spinner)itemView.findViewById(R.id.trip_type);
            srno=(TextView)itemView.findViewById(R.id.srno);
        }
    }
}
