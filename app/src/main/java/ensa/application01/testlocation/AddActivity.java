package ensa.application01.testlocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class AddActivity extends AppCompatActivity {
    private EditText name;
    private DatePicker datePicker;
    private EditText designation;
    private EditText latitude;
    private EditText longitude;
    private Button saveButton;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private ImageView myImg;
    private Button btn;
    private OutputStream outputStream;
    private String FileName;
    private Bitmap mybitmap;
    private static final int PICK_IMAGES_CODE = 50;
    private LocationManager locationManager;
    private LocationListener locationlistener;
    private double latt;
    private double longg;
    private String dd;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy HH:mm:ss");
    private Uri uri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initDatePicker();
        dateButton = findViewById(R.id.dateT);
        name = findViewById(R.id.Name);
        dateButton.setText(getTodayDate());
        dd = simpleDateFormat.format(new Date());
        designation = findViewById(R.id.designation);
        latitude = findViewById(R.id.lat);
        longitude = findViewById(R.id.longg);
        saveButton = findViewById(R.id.save);
        myImg = findViewById(R.id.myImage);
        btn = findViewById(R.id.mybtn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {
                    pickImage();
                }

            }
        });

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        } else {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationlistener);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria, true);
            locationManager.requestLocationUpdates(bestProvider, 0, 0, locationlistener);
            Location location = locationManager.getLastKnownLocation(bestProvider);
            latt = location.getLatitude();
            longg = location.getLongitude();
            latitude.setText(String.valueOf(latt));
            longitude.setText(String.valueOf(longg));
            Toast.makeText(this, "You hav your current Lattitude and longitude", Toast.LENGTH_LONG).show();
            Log.d("LatLng", String.valueOf(latt) + String.valueOf(longg));
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DataBaseHelper myDB = new DataBaseHelper(AddActivity.this);
                String newPath = saveImage(uri);
                //Todo:rember
                myDB.addObject(name.getText().toString().trim(),
                        dd,
                        designation.getText().toString().trim(),
                        Double.valueOf(latitude.getText().toString().trim()),
                        Double.valueOf(longitude.getText().toString().trim()),
                        newPath);
            }
        });


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationlistener);
                Criteria criteria = new Criteria();
                String bestProvider = locationManager.getBestProvider(criteria, true);
                locationManager.requestLocationUpdates(bestProvider, 0, 0, locationlistener);
                Location location = locationManager.getLastKnownLocation(bestProvider);
                latt = location.getLatitude();
                longg = location.getLongitude();
                latitude.setText(String.valueOf(latt));
                longitude.setText(String.valueOf(longg));
                Toast.makeText(this, "You hav your current Lattitude and longitude", Toast.LENGTH_LONG).show();
                Log.d("LatLng", String.valueOf(latt) + String.valueOf(longg));
            } else {
                Toast.makeText(this, "Enable to acess Location", Toast.LENGTH_LONG).show();
            }

            pickImage();
        }
    }

    private void AddImage(Bitmap myBitmap) {
        Log.d("MyBit2", String.valueOf(myBitmap));
        FileName = System.currentTimeMillis() + ".jpg";
        File filepath = Environment.getExternalStorageDirectory();
        File dir = new File(filepath.getAbsolutePath() + "/Pics/");
        dir.mkdir();
        File file = new File(dir, FileName);
        try {
            outputStream = new FileOutputStream((file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (myBitmap != null) {
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            Toast.makeText(AddActivity.this, "Sucessfuly Saved", Toast.LENGTH_LONG);
        }
        try {
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            String path;
            Uri imageUri = data.getData();
            uri = imageUri;
            Log.d("imageURI", String.valueOf(imageUri));
//            FileName = String.valueOf(imageUri);
            myImg.setImageURI(imageUri);
        } else {
            Toast.makeText(this, "Sorry it's not working", Toast.LENGTH_LONG);
        }


    }


    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = makeDateString(dayOfMonth, month, year);
                dd = date;
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int dayOfMonth, int month, int year) {
        return getMonthFormat(month) + " " + dayOfMonth + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1) {
            return "JAN";
        }
        if (month == 2) {
            return "FEB";
        }
        if (month == 3) {
            return "MAR";
        }
        if (month == 4) {
            return "APR";
        }
        if (month == 5) {
            return "MAY";
        }
        if (month == 6) {
            return "JUN";
        }
        if (month == 7) {
            return "JUL";
        }
        if (month == 8) {
            return "AUG";
        }
        if (month == 9) {
            return "SEP";
        }
        if (month == 10) {
            return "OCT";
        }
        if (month == 11) {
            return "NOV";
        }
        if (month == 12) {
            return "DEC";
        }
        return "JAN";
    }

    public void pickImage() {
        Intent intent5 = new Intent();
        intent5.setType("image/*");
        intent5.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent5.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent5, "Select Image"), 100);

    }


    public void onClickDatePicker(View view) {
        datePickerDialog.show();
    }


    public String send(String nn) {
        return nn;
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public String uniqueID() {
        return UUID.randomUUID().toString();
    }

    public String saveImage(Uri uri) {
        if (uri != null) {
            File file = new File(uri.getPath());
            FileOutputStream fos = null;
            try {
                String pathName = uniqueID() + ".png";
                fos = openFileOutput(pathName, MODE_PRIVATE);
                InputStream iStream = getContentResolver().openInputStream(uri);
                byte[] inputData = getBytes(iStream);
                fos.write(inputData);
                Toast.makeText(this, "Image saved successfully", Toast.LENGTH_SHORT).show();
                return pathName;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else
            return null;
    }
}