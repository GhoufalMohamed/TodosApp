package ensa.application01.testlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

public class Details extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationlistener;
    private Button myButton;

    private TextView title;
    private TextView designation;
    private TextView latitude;
    private TextView longitude;
    private TextView time;
    private ImageView myImm;

    String name, temps, desig, lati, longi, imgUrl;

    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        title = findViewById(R.id.title);
        designation = findViewById(R.id.designation);
        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.Longitude);
        time = findViewById(R.id.timeid);
        myImm = findViewById(R.id.myImage2);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getIntentData();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
        }

        myButton = findViewById(R.id.GetPosition);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        intent2 = new Intent(this, MapsActivity2.class);
        locationlistener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                Log.d("Location", location.toString());
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {

            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }
        };


        myButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent2.putExtra("Latt", String.valueOf(lati));
                intent2.putExtra("longg", String.valueOf(longi));
                startActivity(intent2);


            }


        });
    }


        void getIntentData() {
            if (getIntent().hasExtra("name") && getIntent().hasExtra("Time") && getIntent().hasExtra("designation") &&
                    getIntent().hasExtra("latitude") &&
                    getIntent().hasExtra("longitude")) {
                //Getting Data
                name = getIntent().getStringExtra("name");
                temps = getIntent().getStringExtra("Time");
                desig = getIntent().getStringExtra("designation");
                lati = getIntent().getStringExtra("latitude");
                longi = getIntent().getStringExtra("longitude");
                imgUrl = getIntent().getStringExtra("ImgUrl");
                //setting data
                title.setText(name);
                designation.setText(desig);
                time.setText(temps);
                latitude.setText(lati);
                longitude.setText(longi);
                File loadTo=new File(getFilesDir(),imgUrl);
                Glide.with(this).load(loadTo).into(myImm);
            } else {
                Toast.makeText(this, "no data", Toast.LENGTH_SHORT).show();
            }
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.checkSelfPermission(Details.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getIntentData();

            }
        }
    }

}