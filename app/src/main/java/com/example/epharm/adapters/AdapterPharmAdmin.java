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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.epharm.MainActivity;
import com.example.epharm.MainAdminActivity;
import com.example.epharm.R;
import com.example.epharm.database.DbHelper;
import com.example.epharm.models.PharmacistModel;

import java.util.ArrayList;

/* Custom Adapter class for recyclerView
 * we inflate user records with items from our model*/
public class AdapterPharmAdmin extends RecyclerView.Adapter<AdapterPharmAdmin.ItemHolderRecord> {

    //variables
    private Context context;
    private ArrayList<PharmacistModel> pharmacistsList;

    //db HElper
    DbHelper dbHelper;

    //constructor
    public AdapterPharmAdmin(Context context, ArrayList<PharmacistModel> pharmacistsList){
        this.context = context;
        this.pharmacistsList = pharmacistsList;
        dbHelper = new DbHelper(context);
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public ItemHolderRecord onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate layout
        View view = LayoutInflater.from(context).inflate(R.layout.single_user, parent,false);
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
        //handle more button click listener (show options like edit, delete etc)
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delete single user
                deleteUser(position, id);

            }
        });
        Log.d("ImagePath", "onBindViewHolder: "+profileImage);
    }

    private void deleteUser(final int position, final int id) {
        //options to display in dialog
        String[] options = {"Cancel", "Delete"};
        //dialog
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //add items to dialog
        builder.setTitle("You are about to delete this user")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //handle item clicks
                        if (which==0){
                            ((MainAdminActivity)context).onResume();
                        }
                        else if(which==1){
                            //delete is clicked
                            dbHelper.deletePharm(id);
                            //refresh record by calling activity's onResume method
                            pharmacistsList.remove(position);
                            //refresh record by calling activity's onResume method
                            notifyDataSetChanged();
                            ((MainAdminActivity)context).onResume();
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
        ImageButton deleteBtn;

        public ItemHolderRecord(@NonNull View pharmacistView){
            super(pharmacistView);

            //init views
            imageIvv = pharmacistView.findViewById(R.id.imageIv);
            userEmailTv = pharmacistView.findViewById(R.id.userEmailTv);
            deleteBtn = pharmacistView.findViewById(R.id.deleteBtn);
            //userNameTv = pharmacistView.findViewById(R.id.userNameTv);
        }
        //init views

    }
}
