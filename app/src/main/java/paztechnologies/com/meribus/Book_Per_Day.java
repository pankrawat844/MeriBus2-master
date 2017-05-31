package paztechnologies.com.meribus;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class Book_Per_Day extends Fragment {


    Spinner current_location, des_location, select_route, select_end_time;
    android.widget.Spinner select_start_time;
    Button place_ride;

    String startshiftresponse, endshiftresponse, route_response, pickup_response, drop_response,rate_response ,seat_type = "Both Pickup and Drop";
    List<String> route_id = new ArrayList<>();
    ProgressDialog progressDialog;
    RadioGroup radio_grp;
    RadioButton both, drop, pickup;
    DatePickerDialog start_date_picker, end_date_picker;
    SimpleDateFormat dateFormatter;
    TextView start_date,amount;
    List<String> pickupid_array;
    SharedPreferences sp;
    int total_amount;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_per_day, container, false);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

        progressDialog = new ProgressDialog(getActivity());
        init(view);
        new  Start_Shift_Time().execute();
        place_ride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getActivity().getSharedPreferences("app", 0);
                SharedPreferences.Editor editor = sp.edit();
//                if(seat_type.equals("Both Pickup and Drop")) {
//                    if (select_start_time.getSelectedItem() != null && select_end_time.getSelectedItem() != null && select_route.getSelectedItem() != null && current_location.getSelectedItem() != null && des_location.getSelectedItem() != null && start_date.getText().length() != 0) {
//
//                        editor.putString("start_time", select_start_time.getSelectedItem().toString());
//                        editor.putString("end_time", select_end_time.getSelectedItem().toString());
//
//                        editor.putString("route_name", select_route.getSelectedItem().toString());
//                        editor.putString("route_id", route_id.get(select_route.getSelectedItemPosition()));
//                        editor.putString("pick_point", current_location.getSelectedItem().toString());
//                        editor.putString("drop_point", des_location.getSelectedItem().toString());
//                        editor.putString("date", start_date.getText().toString());
//                        editor.putString("amount",amount.getText().toString());
//                        editor.commit();
//                        Ride_Detail ride_detail = new Ride_Detail();
//                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.container, ride_detail);
//                        fragmentTransaction.commit();
//                    } else {
//
//                        Toast.makeText(getActivity(), "All Fields are mandatory,Please Fill All Details", 3).show();
//                    }
//                }else if(seat_type.equals("Drop")){
//                    if ( select_end_time.getSelectedItem() != null && select_route.getSelectedItem()!= null && current_location.getSelectedItem() != null && des_location.getSelectedItem() != null && start_date.getText().length() != 0) {
//                        editor.putString("end_time", select_end_time.getSelectedItem().toString());
//                        editor.putString("route_name",select_route.getSelectedItem().toString());
//                        editor.putString("route_id", route_id.get(select_route.getSelectedItemPosition()));
//                        editor.putString("pick_point",current_location.getSelectedItem().toString());
//                        editor.putString("drop_point",des_location.getSelectedItem().toString());
//                        editor.putString("date", start_date.getText().toString());
//                        editor.putString("amount",amount.getText().toString());
//                        editor.commit();
//                        Ride_Detail ride_detail = new Ride_Detail();
//                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.container, ride_detail).addToBackStack(null);
//                        fragmentTransaction.commit();
//                    }else {
//
//                        Toast.makeText(getActivity(), "All Fields are mandatory,Please Fill All Details", 3).show();
//                    }
//                }else if(seat_type.equals("Pick Up")){
//                    if (select_start_time.getSelectedItem() != null  && select_route.getSelectedItem() != null && current_location.getSelectedItem() != null && des_location.getSelectedItem() != null && start_date.getText().length() != 0) {
//
//
//                        editor.putString("start_time", select_start_time.getSelectedItem().toString());
//
//                        editor.putString("route_name", select_route.getSelectedItem().toString());
//                        editor.putString("route_id", route_id.get(select_route.getSelectedItemPosition()));
//                        editor.putString("pick_point", current_location.getSelectedItem().toString());
//                        editor.putString("drop_point", des_location.getSelectedItem().toString());
//                        editor.putString("date", start_date.getText().toString());
//                        editor.putString("amount",amount.getText().toString());
//                        editor.commit();
//                        Ride_Detail ride_detail = new Ride_Detail();
//                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
//                        fragmentTransaction.replace(R.id.container, ride_detail).addToBackStack("");
//                        fragmentTransaction.commit();
//                    } else {
//
//                        Toast.makeText(getActivity(), "All Fields are mandatory,Please Fill All Details", 3).show();
//                    }
               // }
                Booking_seat ride_detail = new Booking_seat();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, ride_detail);
                fragmentTransaction.commit();
            }
        });

        radio_grp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                if (both.isChecked()) {
                    seat_type = "Both Pickup and Drop";
                    select_start_time.setVisibility(View.VISIBLE);
                    select_end_time.setVisibility(View.VISIBLE);
                    new  Pickup_Point().execute();
                } else if (drop.isChecked()) {
                    select_start_time.setVisibility(View.INVISIBLE);
                    select_end_time.setVisibility(View.VISIBLE);


                    seat_type = "Drop";
                    if (select_route.getSelectedItem() != null) {
                        new  Pickup_Point_When_Drop().execute();
                    }
                } else if (pickup.isChecked()) {
                    seat_type = "Pick Up";
                    select_start_time.setVisibility(View.VISIBLE);

                    select_end_time.setVisibility(View.INVISIBLE);
                    if (select_route.getSelectedItem() != null) {
                        new  Pickup_Point_When_Picup().execute();
                    }
                }
            }
        });
//        select_start_time.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(Spinner parent, View view, int position, long id) {
//            parent.setSelection(position);
//            }
//        });
//        current_location.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // findPlace(v);
//            }
//        });

        select_start_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (seat_type.equals("Both Pickup and Drop")) {
                    new  Select_Route().execute();
                } else if (seat_type.equals("Pick Up")) {
                    new  Select_Route_When_Pick().execute();

                } else if (seat_type.equalsIgnoreCase("Drop")) {
                    new  Select_Route_When_Drop().execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        select_end_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (seat_type.equals("Both Pickup and Drop")) {
                    new Select_Route().execute();
                } else if (seat_type.equals("Pick Up")) {
                    new Select_Route_When_Pick().execute();

                } else if (seat_type.equalsIgnoreCase("Drop")) {
                    new  Select_Route_When_Drop().execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        select_route.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                new  Pickup_Point().execute();
                if (seat_type.equals("Both Pickup and Drop")) {
                    new  Pickup_Point().execute();
                } else if (seat_type.equals("Pick Up")) {

                    new  Pickup_Point_When_Picup().execute();

                } else if (seat_type.equalsIgnoreCase("Drop")) {

                    new  Pickup_Point_When_Drop().execute();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        current_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new  Drop_Point().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        des_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(current_location.getSelectedItem()!=null) {
                    new  Rate().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start_date_picker.show();
            }
        });
        Calendar newCalendar = Calendar.getInstance();
        start_date_picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth);
                int total_days=0;
                start_date.setText(dateFormatter.format(calendar.getTime()));
                if(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)==30) {


                    for(int i=calendar.get(Calendar.DAY_OF_MONTH);i==30;i++){
                        calendar.set(Calendar.DAY_OF_MONTH,i);
                        if(calendar.get(Calendar.DAY_OF_MONTH)==Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_MONTH)==Calendar.SATURDAY){

                        }
                        else{
                            total_days+=1;
                        }

                    }
                    int per_day_total=total_amount/22;
                    amount.setText(String.valueOf(per_day_total * total_days));
                }
                else if(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)==29)
                {
                    for(int i=calendar.get(Calendar.DAY_OF_MONTH);i==29;i++){
                        calendar.set(Calendar.DAY_OF_MONTH,i);
                        if(calendar.get(Calendar.DAY_OF_MONTH)==Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_MONTH)==Calendar.SATURDAY){

                        }
                        else{
                            total_days+=1;
                        }

                    }
                    int per_day_total=total_amount/21;
                    amount.setText(String.valueOf(per_day_total * total_days));
                }
                else
                {
                    for(int i=calendar.get(Calendar.DAY_OF_MONTH);i==31;i++){
                        calendar.set(Calendar.DAY_OF_MONTH,i);
                        if(calendar.get(Calendar.DAY_OF_MONTH)==Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_MONTH)==Calendar.SATURDAY){

                        }
                        else{
                            total_days+=1;
                        }

                    }
                    int per_day_total=total_amount/23;
                    amount.setText(String.valueOf(per_day_total * total_days));
                }
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        return view;
    }


    void init(View v) {
        current_location = (Spinner) v.findViewById(R.id.current_location);
        des_location = (Spinner) v.findViewById(R.id.des_location);
        select_route = (Spinner) v.findViewById(R.id.select_route);
        select_start_time = (android.widget.Spinner) v.findViewById(R.id.start_time);

        select_end_time = (Spinner) v.findViewById(R.id.end_time);
        radio_grp = (RadioGroup) v.findViewById(R.id.radio_grp);
        both = (RadioButton) v.findViewById(R.id.both);
        pickup = (RadioButton) v.findViewById(R.id.pickup);
        drop = (RadioButton) v.findViewById(R.id.drop);
        place_ride = (Button) v.findViewById(R.id.submit);
        start_date = (TextView) v.findViewById(R.id.start_date);
        amount=(TextView)v.findViewById(R.id.amount);
        ArrayAdapter<CharSequence> current_adapter = ArrayAdapter.createFromResource(getActivity(), R.array.arr, R.layout.support_simple_spinner_dropdown_item);
        // current_location.setAdapter(new NothingSelectedSpinnerAdapter(current_adapter, R.layout.nothing_selected_pickup, getActivity()));
        // des_location.setAdapter(new NothingSelectedSpinnerAdapter(current_adapter, R.layout.nothing_selected_drop_point, getActivity()));
    }

    private void startshifttime() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getShiftTime");


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getShiftTime", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            startshiftresponse = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + startshiftresponse);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }

    private void endshifttime() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getEndTime");


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getEndTime", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            endshiftresponse = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + endshiftresponse);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }

    private void setSelect_route() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "getrouteList");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("shifttime");
            Orderid.setValue(select_start_time.getSelectedItem().toString());
            request.addProperty(Orderid);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("EndTime");
            aa123.setValue(select_end_time.getSelectedItem().toString());
            request.addProperty(aa123);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/getrouteList", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            route_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + route_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }

    private void setSelect_route_pick() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getRoute_Start_ShiftWiseMonthly_when_radio_button_pickup");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("shifttime");
            Orderid.setValue(select_start_time.getSelectedItem().toString());
            request.addProperty(Orderid);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getRoute_Start_ShiftWiseMonthly_when_radio_button_pickup", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            route_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + route_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }

    private void setSelect_route_drop() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getRoute_EndShiftTime_WiseMonthly_when_RadioButton_is_Drop");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("shifttime");
            Orderid.setValue("");
            request.addProperty(Orderid);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("EndTime");
            aa123.setValue(select_end_time.getSelectedItem().toString());
            request.addProperty(aa123);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getRoute_EndShiftTime_WiseMonthly_when_RadioButton_is_Drop", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            route_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + route_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }
    }

    private void setpickup_point() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "getPickUpList");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_routeId");
            Orderid.setValue(route_id.get(select_route.getSelectedItemPosition() - 1));
            request.addProperty(Orderid);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("Seattype");
            aa123.setValue(seat_type);
            request.addProperty(aa123);
            //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/getPickUpList", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            pickup_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + pickup_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }

    }

    private void setdrop_point() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "getDropList");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_routeId");
            Orderid.setValue(route_id.get(select_route.getSelectedItemPosition() - 1));
            request.addProperty(Orderid);

            PropertyInfo aa123 = new PropertyInfo();
            aa123.setType(android.R.string.class);
            aa123.setName("Seattype");
            aa123.setValue(seat_type);
            request.addProperty(aa123);
            //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/getDropList", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            drop_response = soapPrimitive.toString();
            Log.e("TAG", "Soap primitive1" + drop_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap Exception" + e.toString());
        }

    }

    private class Start_Shift_Time extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading....");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String[]... params) {
            startshifttime();
            return startshiftresponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            progressDialog.dismiss();

            try {
                List<String> time = new ArrayList<>();
                //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray = new JSONArray(s);
                JSONArray arr = jsonArray.getJSONArray(0);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    time.add(jsonObject.getString("StartShiftTime"));
                }
                ArrayAdapter<String> start_adapter = new ArrayAdapter<String>(getActivity(), R.layout.nothing_selected_drop_point, time);
                start_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                select_start_time.setAdapter(new NothingSelectedSpinnerAdapter(start_adapter, R.layout.nothing_selected_shift_start, getContext()));
                new  End_Shift_Time().execute();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class End_Shift_Time extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String[]... params) {
            endshifttime();
            return endshiftresponse;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            //   progressDialog.dismiss();

            try {
                List<String> time = new ArrayList<>();
                //  Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray = new JSONArray(s);
                JSONArray arr = jsonArray.getJSONArray(0);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    time.add(jsonObject.getString("EndShiftTime"));
                }
                ArrayAdapter<String> end_adapter = new ArrayAdapter<String>(getActivity(), R.layout.select_end_time, time);
                end_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                select_end_time.setAdapter(new NothingSelectedSpinnerAdapter(end_adapter, R.layout.nothing_selected_shift_end_time, getActivity()));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class Select_Route extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String[]... params) {
            setSelect_route();
            return route_response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            progressDialog.dismiss();

            try {
                List<String> time = new ArrayList<>();
                HashMap<String, String> hash = new HashMap<>();
                //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray = new JSONArray(s);
                JSONArray arr = jsonArray.getJSONArray(0);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    time.add(jsonObject.getString("RouteName"));
                    route_id.add(jsonObject.getString("routeId"));
                }
                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(getActivity(),R.layout.select_route_list, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route_adapter.notifyDataSetChanged();
                select_route.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter, R.layout.nothing_selected_select_route, getContext()));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class Select_Route_When_Pick extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String[]... params) {
            setSelect_route_pick();
            return route_response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            progressDialog.dismiss();

            try {
                List<String> time = new ArrayList<>();
                HashMap<String, String> hash = new HashMap<>();
                //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray = new JSONArray(s);
                JSONArray arr = jsonArray.getJSONArray(0);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    time.add(jsonObject.getString("RouteName"));
                    route_id.add(jsonObject.getString("routeId"));
                }

                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(getActivity(), R.layout.select_route_list, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route_adapter.notifyDataSetChanged();
                select_route.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter, R.layout.nothing_selected_select_route, getContext()));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class Select_Route_When_Drop extends AsyncTask<String[], Void, String> {

        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String[]... params) {
            setSelect_route_drop();
            return route_response;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.e("resulttttt",s);
            progressDialog.dismiss();

            try {
                List<String> time = new ArrayList<>();
                //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray = new JSONArray(s);
                JSONArray arr = jsonArray.getJSONArray(0);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    time.add(jsonObject.getString("RouteName"));
                    route_id.add(jsonObject.getString("routeId"));
                }
                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(getActivity(), R.layout.select_route_list, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route_adapter.notifyDataSetChanged();
                select_route.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter, R.layout.nothing_selected_select_route, getContext()));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    private class Pickup_Point extends AsyncTask<String[], Void, String> {

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
                pickupid_array= new ArrayList<>();
                //   Toast.makeText(getActivity(),s,3).show();
                JSONArray jsonArray = new JSONArray(s);
                JSONArray arr = jsonArray.getJSONArray(0);
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject = arr.getJSONObject(i);
                    time.add(jsonObject.getString("PickUpName"));
                    pickupid_array.add(jsonObject.getString("PickUpId"));
                }
                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(getActivity(), R.layout.selected_pickup_location, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route_adapter.notifyDataSetChanged();
                current_location.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter, R.layout.nothing_selected_pickup, getContext()));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }

    class Pickup_Point_When_Drop extends AsyncTask<String[], Void, String> {

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
            setpickup_point_drop();
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
                    time.add(jsonObject.getString("PickUpName"));
                }
                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(getActivity(), R.layout.selected_pickup_location, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route_adapter.notifyDataSetChanged();
                current_location.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter, R.layout.nothing_selected_pickup, getContext()));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }


        private void setpickup_point_drop() {
            try {
                SoapObject request = new SoapObject("http://tempuri.org/", "_getPickup_Point_When_RadioButton_Drop");

                PropertyInfo Orderid = new PropertyInfo();
                Orderid.setType(android.R.string.class);
                Orderid.setName("_routeId");
                Orderid.setValue(route_id.get(select_route.getSelectedItemPosition() - 1));
                request.addProperty(Orderid);

                PropertyInfo aa123 = new PropertyInfo();
                aa123.setType(android.R.string.class);
                aa123.setName("Seattype");
                aa123.setValue(seat_type);
                request.addProperty(aa123);
                //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport =
                        new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call("http://tempuri.org/IService1/_getPickup_Point_When_RadioButton_Drop", envelope);
                SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

                pickup_response = soapPrimitive.toString();
                Log.e("TAG", "Soap primitive1" + pickup_response);
            } catch (SocketTimeoutException e) {

            } catch (Exception e) {
                Log.e("TAG", "Soap Exception" + e.toString());
            }
        }
    }

    class Pickup_Point_When_Picup extends AsyncTask<String[], Void, String> {

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
            setpickup_point_when_pickup();
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
                    time.add(jsonObject.getString("PickUpName"));
                }
                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(getActivity(), R.layout.selected_pickup_location, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route_adapter.notifyDataSetChanged();
                current_location.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter, R.layout.nothing_selected_pickup, getContext()));
            } catch (Exception e) {

                e.printStackTrace();
            }
        }


        private void setpickup_point_when_pickup() {
            try {
                SoapObject request = new SoapObject("http://tempuri.org/", "_getPickup_Point_When_RadioButton_Drop");

                PropertyInfo Orderid = new PropertyInfo();
                Orderid.setType(android.R.string.class);
                Orderid.setName("_routeId");
                Orderid.setValue(route_id.get(select_route.getSelectedItemPosition() - 1));
                request.addProperty(Orderid);

                PropertyInfo aa123 = new PropertyInfo();
                aa123.setType(android.R.string.class);
                aa123.setName("Seattype");
                aa123.setValue(seat_type);
                request.addProperty(aa123);
                //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.dotNet = true;
                envelope.setOutputSoapObject(request);
                HttpTransportSE androidHttpTransport =
                        new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
                androidHttpTransport.debug = true;
                androidHttpTransport.call("http://tempuri.org/IService1/_getPickup_Point_When_RadioButton_Drop", envelope);
                SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

                pickup_response = soapPrimitive.toString();
                Log.e("TAG", "Soap primitive1" + pickup_response);
            } catch (SocketTimeoutException e) {

            } catch (Exception e) {
                Log.e("TAG", "Soap Exception" + e.toString());
            }
        }
    }

    private class Drop_Point extends AsyncTask<String[], Void, String> {

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
            setdrop_point();
            return drop_response;
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
                    time.add(jsonObject.getString("PickUpName"));
                }
                ArrayAdapter<String> route_adapter = new ArrayAdapter<String>(getActivity(), R.layout.selected_pickup_location, time);
                route_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                route_adapter.notifyDataSetChanged();
                des_location.setAdapter(new NothingSelectedSpinnerAdapter(route_adapter, R.layout.nothing_selected_drop_point, getContext()));

            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }






    private class Rate extends AsyncTask<String[], Void, String> {

        ProgressDialog dialog;
        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;
        JSONObject jsonObject;
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
            setrate();
            return rate_response;
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
                    jsonObject = arr.getJSONObject(0);
                    time.add(jsonObject.getString("GrandTotal"));
                    time.add(jsonObject.getString("OnWay"));
                }
                if(seat_type.equals("Both Pickup and Drop")){
                    amount.setText(jsonObject.getString("GrandTotal"));
                    total_amount=Integer.valueOf(jsonObject.getString("GrandTotal"));
                }else{
                    amount.setText(jsonObject.getString("OnWay"));
                    total_amount=Integer.valueOf(jsonObject.getString("OnWay"));
                }


            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    private void setrate() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getZoneRate_When_RadioButton_Both_Pickup_nd_Drop_and_Also_Pickup_use_PickupPoint_DropDown");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_pickUpId");
            Orderid.setValue(pickupid_array.get(des_location.getSelectedItemPosition() - 1));
            request.addProperty(Orderid);
            Log.e("pickup id",pickupid_array.get(des_location.getSelectedItemPosition() - 1));

            //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getZoneRate_When_RadioButton_Both_Pickup_nd_Drop_and_Also_Pickup_use_PickupPoint_DropDown", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            rate_response = soapPrimitive.toString();
            Log.e("TAG", "Soap rate" + rate_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap rate2" + e.toString());
        }

    }


    private class Rate_Drop extends AsyncTask<String[], Void, String> {

        ProgressDialog dialog;
        private Login activity;
        private String soapAction;
        private String methodName;
        private String paramsName;
        JSONObject jsonObject;
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
            setrate();
            return rate_response;
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
                    jsonObject = arr.getJSONObject(0);
                    time.add(jsonObject.getString("GrandTotal"));
                    time.add(jsonObject.getString("OnWay"));
                    amount.setText(jsonObject.getString("OnWay"));
                    total_amount=Integer.valueOf(jsonObject.getString("OnWay"));
                }





            } catch (Exception e) {

                e.printStackTrace();
            }
        }
    }


    private void setrate_drop() {
        try {
            SoapObject request = new SoapObject("http://tempuri.org/", "_getZoneRate_When_RadioButton_Drop_Use_DROPPoint_DropDown");

            PropertyInfo Orderid = new PropertyInfo();
            Orderid.setType(android.R.string.class);
            Orderid.setName("_pickUpId");
            Orderid.setValue(pickupid_array.get(des_location.getSelectedItemPosition() - 1));
            request.addProperty(Orderid);
            Log.e("pickup id",pickupid_array.get(des_location.getSelectedItemPosition() - 1));

            //Toast.makeText(getActivity(),route_id.get(select_route.getSelectedItemPosition())+" "+seat_type,3).show();

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.dotNet = true;
            envelope.setOutputSoapObject(request);
            HttpTransportSE androidHttpTransport =
                    new HttpTransportSE("http://sales.meribus.com/Service1.svc", 5000);
            androidHttpTransport.debug = true;
            androidHttpTransport.call("http://tempuri.org/IService1/_getZoneRate_When_RadioButton_Drop_Use_DROPPoint_DropDown", envelope);
            SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();

            rate_response = soapPrimitive.toString();
            Log.e("TAG", "Soap rate" + rate_response);
        } catch (SocketTimeoutException e) {

        } catch (Exception e) {
            Log.e("TAG", "Soap rate2" + e.toString());
        }

    }
}
