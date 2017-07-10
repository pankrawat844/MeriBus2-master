package paztechnologies.com.meribus;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;

/**
 * Created by Admin on 3/24/2017.
 */

public class Ride_Detail extends Fragment implements OnMapReadyCallback, View.OnClickListener, DirectionCallback {
    String Server_Key = "AIzaSyCdUU47miOD97PDox1FwhGr3SHPkbG4A2s";
    private GoogleMap googleMap;
    private MapView mMapView;
    //private LatLng camera = new LatLng(28.6158851, 77.0406466);
    private LatLng curr ;
    private LatLng des ;
    private Button payment;
    private TextView start_time,end_time,select_route,pickup_point,drop_point,amount,distance_txt,time_txt;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_detail2, container, false);
        MapsInitializer.initialize(this.getActivity());
        init(view);
             sharedPreferences= getActivity().getSharedPreferences("app",0);
            curr= new LatLng(Double.valueOf(sharedPreferences.getString("pickup_lattiude","")),Double.valueOf(sharedPreferences.getString("pickup_longitude",""))) ;
        des= new LatLng(Double.valueOf(sharedPreferences.getString("drop_latitude","28.6158851")),Double.valueOf(sharedPreferences.getString("drop_longtitude","77.0406466"))) ;
            Toast.makeText(getActivity(),sharedPreferences.getString("drop_latitude","28.6158851")+sharedPreferences.getString("drop_longtitude","77.0406466"),3);
        start_time.setText(sharedPreferences.getString("start_time",""));
            end_time.setText(sharedPreferences.getString("end_time",""));
            select_route.setText(sharedPreferences.getString("route_name",""));
            pickup_point.setText( sharedPreferences.getString("pick_point",""));
            drop_point.setText( sharedPreferences.getString("drop_point",""));
            amount.setText("RS "+sharedPreferences.getString("amount",""));

            SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);

             mapFragment.getMapAsync(this);
             payment.setOnClickListener(this);

        //        if (google == null) {
//            FragmentManager fragmentManager = getFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//            google = SupportMapFragment.newInstance();
//            fragmentTransaction.replace(R.id.map, google).commit();
//        }
        requestDirection();

        return view;
    }

        private void init(View v){
            start_time=(TextView)v.findViewById(R.id.start_time);
            end_time=(TextView)v.findViewById(R.id.end_time);
            select_route=(TextView)v.findViewById(R.id.select_route);
            pickup_point=(TextView)v.findViewById(R.id.current_location);
            drop_point=(TextView)v.findViewById(R.id.des);
            amount=(TextView)v.findViewById(R.id.total);
            payment = (Button) v.findViewById(R.id.submit);
            distance_txt=(TextView)v.findViewById(R.id.distance);
            time_txt=(TextView)v.findViewById(R.id.time);
        }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:

                Payment payment = new Payment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, payment);
                ft.commit();
        }
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        //  Snackbar.make(getView(), "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
//        Toast.makeText(getActivity(), " " + direction.getStatus(), Toast.LENGTH_SHORT).show();

        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(curr).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_point)));
            googleMap.addMarker(new MarkerOptions().position(des).icon(BitmapDescriptorFactory.fromResource(R.mipmap.icon_point)));
            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.YELLOW));
            getdistancentime();
        }
    }

    @Override
    public void onDirectionFailure(Throwable t) {
        //   Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                        getActivity(), R.raw.maps_style));
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curr, 9));

//        LatLng latlong = new LatLng(28.6158851,77.0406466);
//        googleMap.addMarker(new MarkerOptions().position(latlong)
//                .title("india"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlong,10));
//        googleMap.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
    }

    public void requestDirection() {
        // Snackbar.make(getView(), "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(Server_Key)
                .from(curr)
                .to(des)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
     //   Toast.makeText(getActivity(), "Direction Requesting...", Toast.LENGTH_SHORT).show();
       //     getLocationInfo(sharedPreferences.getString("pick_point",""));
      //  getLocationInfo_des(sharedPreferences.getString("drop_point",""));

    }




    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    private  void getdistancentime(){
        RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
        String url= "https://maps.googleapis.com/maps/api/distancematrix/json?units=km&origins="+sharedPreferences.getString("pickup_lattiude","")+","+sharedPreferences.getString("pickup_longitude","")+"&destinations="+sharedPreferences.getString("drop_latitude","28.6158851")+","+sharedPreferences.getString("drop_longtitude","28.6158851")+"&key="+Server_Key;
        StringRequest request= new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject respons = new JSONObject(response);
                    JSONArray row= respons.getJSONArray("rows");
                    JSONObject row_obj= row.getJSONObject(0);
                    JSONArray row_arr= row_obj.getJSONArray("elements");
                    JSONObject elem_obj=row_arr.getJSONObject(0);
                    JSONObject distance= elem_obj.getJSONObject("distance");
                    distance_txt.setText(distance.getString("text"));
                    JSONObject time= elem_obj.getJSONObject("duration");
                    time_txt.setText(time.getString("text"));

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrorListner",error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map= new HashMap<>();
                map.put("units","km");
                map.put("origins",sharedPreferences.getString("pickup_lattiude","")+","+sharedPreferences.getString("pickup_longitude",""));

                map.put("destinations",sharedPreferences.getString("drop_latitude","28.6158851")+","+sharedPreferences.getString("drop_longtitude","28.6158851"));
                map.put("key",Server_Key);
                return map;
            }
        };
        requestQueue.add(request);
    }


}
