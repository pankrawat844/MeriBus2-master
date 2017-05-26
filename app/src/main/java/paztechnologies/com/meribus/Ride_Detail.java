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
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.barcode.Barcode;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
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
    private LatLng curr =new LatLng(28.6168968, 77.0459028);
    private LatLng des = new LatLng(28.6158851, 77.0406466);
    private Button payment;
    private TextView start_time,end_time,select_route,pickup_point,drop_point,amount;
    SharedPreferences sharedPreferences;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ride_detail2, container, false);
        MapsInitializer.initialize(this.getActivity());
        init(view);
             sharedPreferences= getActivity().getSharedPreferences("app",0);
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
        Toast.makeText(getActivity(), " " + direction.getStatus(), Toast.LENGTH_SHORT).show();

        if (direction.isOK()) {
            googleMap.addMarker(new MarkerOptions().position(curr));
            googleMap.addMarker(new MarkerOptions().position(des));
            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            googleMap.addPolyline(DirectionConverter.createPolyline(getActivity(), directionPositionList, 5, Color.RED));

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
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curr, 15));

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


    public  void getLocationInfo(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ","%20");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" + address + "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        }
        catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            curr=getLatLong(jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public  LatLng getLatLong(JSONObject jsonObject) {

        try {

            double longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            curr= new LatLng(latitude,longitude);

        } catch (JSONException e) {
            return curr;

        }

        return curr;
    }



    public  void getLocationInfo_des(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        try {

            address = address.replaceAll(" ","%20");

            HttpPost httppost = new HttpPost("http://maps.google.com/maps/api/geocode/json?address=" +address+ "&sensor=false");
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            stringBuilder = new StringBuilder();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            response = client.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        }
        catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
            des=getLatLong_des(jsonObject);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }
    public  LatLng getLatLong_des(JSONObject jsonObject) {

        try {

            double longitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lng");

            double latitude = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                    .getJSONObject("geometry").getJSONObject("location")
                    .getDouble("lat");
            des= new LatLng(latitude,longitude);
            GoogleDirection.withServerKey(Server_Key)
                    .from(curr)
                    .to(des)
                    .transportMode(TransportMode.DRIVING)
                    .execute(this);
        } catch (JSONException e) {
            return null;

        }

        return des;
    }



}
