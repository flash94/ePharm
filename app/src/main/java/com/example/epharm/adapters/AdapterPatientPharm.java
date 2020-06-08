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
import com.example.epharm.PatientMainActivity;
import com.example.epharm.PharmDetailActivity;
import com.example.epharm.R;
import com.example.epharm.database.DbHelper;
import com.example.epharm.models.PharmacistModel;
import com.flutterwave.raveandroid.RaveUiManager;

import java.util.ArrayList;

import static com.example.epharm.database.Constants.subscribedPharmacistEmail;

/* Custom Adapter class for recyclerView
 * we inflate user records with items from our model*/
public class AdapterPatientPharm extends RecyclerView.Adapter<AdapterPatientPharm.ItemHolderRecord> {

    //variables
    private Context context;
    private ArrayList<PharmacistModel> pharmacistsList;

    //db HElper
    DbHelper dbHelper;

    //constructor
    public AdapterPatientPharm(Context context, ArrayList<PharmacistModel> pharmacistsList){
        this.context = context;
        this.pharmacistsList = pharmacistsList;
        dbHelper = new DbHelper(context);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemHolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.single_pharmacist, parent,false);
        return new ItemHolderRecord(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolderRecord holder, final int position) {


        //get data, set data, handle view clicks in this method
        PharmacistModel pharmacistModel = pharmacistsList.get(position);
        final int id = pharmacistModel.getId();
        final String email = pharmacistModel.getEmail();
        final String addedTimeStamp = pharmacistModel.getAddedTimeStamp();
        final String firstName = pharmacistModel.getFirstName();
        final String lastName = pharmacistModel.getLastName();
        final String consult = pharmacistModel.getConsultationFee();
        final String exp = pharmacistModel.getYearOfExperience();
        final String phoneNumber = pharmacistModel.getPhoneNumber();
        final String profileImage = pharmacistModel.getProfileImage();
        final String updatedTimestamp = pharmacistModel.getUpdatedTimestamp();

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

        String data = subscribedPharmacistEmail;
        if(data.equals(email)){
            holder.subscribeBtn.setImageResource(R.drawable.ic_subscribed);
        }

        //handle onclick (go to detail record activity)
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PharmDetailActivity.class);
                intent.putExtra("PHARMACIST_ID", id);
                context.startActivity(intent);
                //will later implement

            }
        });

        holder.subscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete single user
                subscribeTo(position, id, email, consult);
            }
        });
        Log.d("ImagePath", "onBindViewHolder: "+profileImage);
    }

    private void subscribeTo(final int position, final int id, final String email, final String fee) {
        //options to display in dialog
        String[] options = {"Cancel", "Subscribe"};
        //dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //add items to dialog
        builder.setTitle("Subscribe to this Pharmacist. Consultation fee is "+ fee)
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item clicks
                        if (which==0){
                            ((PatientMainActivity)context).onResume();
                        }
                        else if(which==1){
                            //save update image
                            if(!dbHelper.checkSubscription()){
                                ((PatientMainActivity)context).onResume();
                                Toast.makeText(context,"You are already subscribed to a pharmacist", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Intent intent = new Intent(context, PharmDetailActivity.class);
                                intent.putExtra("PHARMACIST_ID", id);
                                context.startActivity(intent);
                            }
                        }
                    }
                });
        //show dialog
        builder.create().show();
    }

    @Override
    public int getItemCount() {
        return pharmacistsList.size();
    }


    class ItemHolderRecord extends RecyclerView.ViewHolder{
        //variables
        private Context context;
        private ArrayList<PharmacistModel>pharmacistsList;

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
