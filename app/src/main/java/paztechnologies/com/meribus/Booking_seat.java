package paztechnologies.com.meribus;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import paztechnologies.com.meribus.Adapter.Pay_Per_Day_Adapter;
import paztechnologies.com.meribus.model.Pay_Per_Day_Model;


/**
 * A simple {@link Fragment} subclass.
 */
public class Booking_seat extends Fragment {

        RecyclerView recyclerView;
        List<Pay_Per_Day_Model> list= new ArrayList<>();
        Pay_Per_Day_Adapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_booking_seat, container, false);
         recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        adapter= new Pay_Per_Day_Adapter(list);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        static_data();
        return view;
    }

    private void static_data(){


        for(int i=1;i<5;i++){
            Pay_Per_Day_Model model= new Pay_Per_Day_Model();
            model.setS_no(String.valueOf(i));
            list.add(model);
        }

        adapter.notifyDataSetChanged();
    }




}
