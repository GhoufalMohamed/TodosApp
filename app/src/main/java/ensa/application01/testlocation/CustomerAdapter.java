package ensa.application01.testlocation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.MyViewHolder> {
   private Context context;
   private  ArrayList name, id, time, designation, latitude, longitude,imageUri;
   private int position;


    CustomerAdapter(Context context, ArrayList name,ArrayList time,ArrayList designation,ArrayList latitude,ArrayList longitude,ArrayList image,ArrayList id){
        this.context=context;
        this.name=name;
        this.time= time;
        this.designation=designation;
        this.latitude= latitude;
        this.longitude= longitude;
        this.imageUri=image;
        this.id=id;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater=LayoutInflater.from(context);
       View view = inflater.inflate(R.layout.mu_layout,parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        this.position= position;
holder.name_text.setText(String.valueOf(name.get(position)));
holder.time_text.setText(String.valueOf(time.get(position)));
        File toLoad=new File(context.getFilesDir(),imageUri.get(position).toString());
        Glide.with(context).load(toLoad).into(holder.image);


holder.mainLayout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, Details.class);
        intent.putExtra("name",String.valueOf(name.get(position)));
        intent.putExtra("Time",String.valueOf(time.get(position)));
        intent.putExtra("id",String.valueOf(id.get(position)));
        intent.putExtra("designation",String.valueOf(designation.get(position)));
        intent.putExtra("latitude",String.valueOf(latitude.get(position)));
        intent.putExtra("longitude",String.valueOf(longitude.get(position)));
        intent.putExtra("ImgUrl",String.valueOf(imageUri.get(position)));
        context.startActivity(intent);
    }
});

    }

    @Override
    public int getItemCount() {

        return name.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
private TextView name_text, time_text,ImageUri;
private ImageView myImg;
private LinearLayout mainLayout;
private ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name_text =itemView.findViewById(R.id.Name);
            time_text =itemView.findViewById(R.id.time);
            mainLayout= itemView.findViewById(R.id.mainLayout);
            image=itemView.findViewById(R.id.thumbnail);

        }
    }
}
