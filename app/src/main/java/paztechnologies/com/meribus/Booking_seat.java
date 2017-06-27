package paztechnologies.com.meribus;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
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
    String pickup_response,_routeId,StartShiftTime,EndShiftTime,Seattype,per_seat_price;
    String[] temp=new String[]{};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_booking_seat, container, false);
        _routeId=getArguments().getString("_routeId");
        StartShiftTime=getArguments().getString("StartShiftTime");
        EndShiftTime=getArguments().getString("EndShiftTime");
        Seattype=getArguments().getString("Seattype");
        per_seat_price=getArguments().getString("perseat_price");
         recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);
        adapter= new Pay_Per_Day_Adapter(list,getActivity(),temp);
        RecyclerView.LayoutManager layoutManager= new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        new Seat_Count().execute();
        return view;
    }




    private class Seat_Count extends AsyncTask<String[], Void, String> {

        ProgressDialog dialog;
        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading....");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String[]... params) {
            setpickup_point();
            return pickup_response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            dialog.dismiss();

            try {
                List<String> time = new ArrayList<>();

                //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray = new JSONArray(s);
                JSONArray arr = jsonArray.getJSONArray(0);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    Pay_Per_Day_Model model= new Pay_Per_Day_Model("",jsonObject.getString("S.No"),jsonObject.getString("Date"),jsonObject.getString("Total Seat"),"","","",per_seat_price);
                    list.add(model);
                }
                 adapter.notifyDataSetChanged();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }



    private void setpickup_point() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "PPS_BindGrid_For_Seats_when_RadioButton");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_routeId");
            Orderid.setValue(_routeId);
            request.addProperty(Orderid);


            PropertyInfo start = new PropertyInfo();
            start.setType(android.R.string.class);
            start.setName("StartShiftTime");
            start.setValue(StartShiftTime);
            request.addProperty(start);

            PropertyInfo end = new PropertyInfo();
            end.setType(android.R.string.class);
            end.setName("EndShiftTime");
            end.setValue(EndShiftTime);
            request.addProperty(end);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("Seattype");
            aa123.setValue(Seattype);
            request.addProperty(aa123);
            //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/PPS_BindGrid_For_Seats_when_RadioButton", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            pickup_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + pickup_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }
}

