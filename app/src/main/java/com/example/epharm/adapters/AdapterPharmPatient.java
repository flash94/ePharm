package com.example.epharm.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epharm.MainActivity;
import com.example.epharm.MainAdminActivity;
import com.example.epharm.PatientDetailActivity;
import com.example.epharm.PatientMainActivity;
import com.example.epharm.PharmDetailActivity;
import com.example.epharm.R;
import com.example.epharm.database.DbHelper;
import com.example.epharm.models.PatientModel;

import java.util.ArrayList;

/* Custom Adapter class for recyclerView
 * we inflate user records with items from our model*/
public class AdapterPharmPatient extends RecyclerView.Adapter<AdapterPharmPatient.ItemHolderRecord> {

    //variables
    private Context context;
    private ArrayList<PatientModel> patientList;

    //db HElper
    DbHelper dbHelper;

    //constructor
    public AdapterPharmPatient(Context context, ArrayList<PatientModel> patientList){
        this.context = context;
        this.patientList = patientList;
        dbHelper = new DbHelper(context);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemHolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.single_patient, parent,false);
        return new ItemHolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolderRecord holder, final int position) {


        //get data, set data, handle view clicks in this method
        PatientModel patientModel = patientList.get(position);
        final int id = patientModel.getId();
        final String email = patientModel.getEmail();
        final String addedTimeStamp = patientModel.getAddedTimeStamp();
        final String firstName = patientModel.getFirstName();
        final String lastName = patientModel.getLastName();
        final String pharmEmail = patientModel.getPharmacistEmail();
        final String phoneNumber = patientModel.getPhoneNumber();
        final String profileImage = patientModel.getProfileImage();
        final String updatedTimestamp = patientModel.getUpdatedTimestamp();

        //set data to views
        holder.userEmailTv.setText(email);

        //if item has no image
        if (profileImage == null){
            //no image in record set default
            holder.imageIvv.setImageResource(R.drawable.ic_user_icon);
        }
        else{
            //have image in record
            holder.imageIvv.setImageURI(Uri.parse(profileImage));
        }

        //handle onclick (go to detail record activity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PatientDetailActivity.class);
                intent.putExtra("PATIENT_ID", id);
                context.startActivity(intent);
                //will later implement

            }
        });

        holder.subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete single user
                Intent intent = new Intent(context, PatientDetailActivity.class);
                intent.putExtra("PATIENT_ID", id);
                context.startActivity(intent);
            }
        });
        Log.d("ImagePath", "onBindViewHolder: "+profileImage);
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }


    class ItemHolderRecord extends RecyclerView.ViewHolder{
        //variables
        private Context context;
        private ArrayList<PatientModel>patientList;

        //views
        ImageView imageIvv;
        TextView userEmailTv, userNameTv;
        ImageButton subscribeBtn;

        public ItemHolderRecord(@NonNull View pharmacistView){
            super(pharmacistView);

            //init views
            imageIvv = pharmacistView.findViewById(R.id.imageIv);
            userEmailTv = pharmacistView.findViewById(R.id.userEmailTv);
            subscribeBtn = pharmacistView.findViewById(R.id.subscribeBtn);
            //userNameTv = pharmacistView.findViewById(R.id.userNameTv);
        }
        //init views

    }
}
