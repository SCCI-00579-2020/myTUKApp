package com.example.tuktradecircle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tuktradecircle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class UpdateProfileActivity extends AppCompatActivity {
    private EditText editTextUpdateName, editTextUpdateCourse, editTextUpdateYear, editTextUpdatePhone;
    private String textFullName, textCourse, textYear, textPhone;
    private FirebaseAuth authProfile;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        getSupportActionBar().setTitle("Update Profile Details");
        progressBar = findViewById(R.id.progressBar);
        editTextUpdateName = findViewById(R.id.editText_update_name);
        editTextUpdateCourse = findViewById(R.id.editText_update_course);
        editTextUpdatePhone = findViewById(R.id.editText_update_phone);
        editTextUpdateYear = findViewById(R.id.editText_update_year);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        //show profile data
        showProfile(firebaseUser);

        //upload profile pic
        Button buttonUploadProfilePic = findViewById(R.id.button_update_profilePic);
        buttonUploadProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //update profile button
        Button buttonUploadProfile = findViewById(R.id.button_update_profile);
        buttonUploadProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile(firebaseUser);
            }
        });

    }

    private void updateProfile(FirebaseUser firebaseUser) {

        if (TextUtils.isEmpty(textFullName)) {
            Toast.makeText(UpdateProfileActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
            editTextUpdateName.setError("Full name is required");
            editTextUpdateName.requestFocus();

        } else if (TextUtils.isEmpty(textCourse)) {
            Toast.makeText(UpdateProfileActivity.this, "Please enter the course you're doing", Toast.LENGTH_SHORT).show();
            editTextUpdateCourse.setError("School ID is required");
            editTextUpdateCourse.requestFocus();
        } else if (TextUtils.isEmpty(textYear)) {
            Toast.makeText(UpdateProfileActivity.this, "Please enter your current year of study", Toast.LENGTH_SHORT).show();
            editTextUpdateYear.setError("School ID is required");
            editTextUpdateYear.requestFocus();
        } else if (TextUtils.isEmpty(textPhone)) {
            Toast.makeText(UpdateProfileActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
            editTextUpdatePhone.setError("Phone number is required");
            editTextUpdatePhone.requestFocus();
        } else if (textPhone.length() != 10) {
            Toast.makeText(UpdateProfileActivity.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
            editTextUpdatePhone.setError("Should be 10 digits");
            editTextUpdatePhone.requestFocus();

        } else {
            //obtain data entered by user
            textFullName = editTextUpdateName.getText().toString();

            textCourse = editTextUpdateCourse.getText().toString();
            textYear = editTextUpdateYear.getText().toString();
            textPhone = editTextUpdatePhone.getText().toString();
            //enter user data into the firebase realtime database. set up dependancies
            ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(textFullName, textCourse, textYear, textPhone);


            //obtain user reference from database for 'registered users'
            DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users ");
            String userID = firebaseUser.getUid();
            progressBar.setVisibility(View.VISIBLE);
            referenceProfile.child(userID).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        //setting new name
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(textFullName).build();
                        firebaseUser.updateProfile(profileUpdates);
                        Toast.makeText(UpdateProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
//stop user from returning to update profile
                        Intent intent = new Intent(UpdateProfileActivity.this, UserProfileActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            throw task.getException();
                        } catch (Exception e) {
                            Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    progressBar.setVisibility(View.GONE);

                }
            });

        }
    }

    //fetch data from firebase
    private void showProfile(FirebaseUser firebaseUser) {
        String userIDofRegistered = firebaseUser.getUid();
        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("Registered Users ");
        progressBar.setVisibility(View.VISIBLE);
        referenceProfile.child(userIDofRegistered).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails = snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails != null) {
                    textFullName = firebaseUser.getDisplayName();
                    textFullName = readUserDetails.fullName;
                    textCourse = readUserDetails.course;
                    textPhone = readUserDetails.phone;
                    textYear = readUserDetails.year;

                    editTextUpdateName.setText(textFullName);
                    editTextUpdateCourse.setText(textCourse);
                    editTextUpdatePhone.setText(textPhone);
                    editTextUpdateYear.setText(textYear);

                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UpdateProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
