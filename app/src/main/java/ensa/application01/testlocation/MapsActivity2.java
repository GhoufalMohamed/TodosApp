package ensa.application01.testlocation;

import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Bundle bandle;
    private double lat;
    private double longg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        bandle=getIntent().getExtras();

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
      //  Toast.makeText(this,"Map is ready",Toast.LENGTH_SHORT).show();
        mMap = googleMap;
        if(bandle != null){
            this.lat=Double.parseDouble(bandle.getString("Latt"));

            this.longg=Double.parseDouble(bandle.getString("longg"));
            Log.d("LatLnggggg", String.valueOf(lat)+String.valueOf(longg));
        }

        // Add a marker in Sydney and move the camera
        LatLng CurrentPossition = new LatLng(this.lat, this.longg);
        mMap.addMarker(new MarkerOptions().position(CurrentPossition).title("Marker in Tanger"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(CurrentPossition,14));
    }
}