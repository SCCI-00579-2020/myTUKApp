package com.example.tuktradecircle.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tm.R;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfileActivity extends AppCompatActivity {
    private TextView textViewWelcome, textViewFullName, textViewEmail, textViewCourse, textViewID, textViewYear, textViewPhone;
    private ProgressBar progressBar;
    private String fullName, email, course, ID, year, phone;
    private ImageView imageView;
    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getSupportActionBar().setTitle("Account");

        textViewWelcome =findViewById(R.id.textView_show_welcome);
        textViewFullName= findViewById(R.id.textView_show_full_name);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewCourse= findViewById(R.id.textView_show_course);
        textViewID= findViewById(R.id.textView_show_id);
        textViewYear=findViewById(R.id.textView_show_year);
        textViewPhone= findViewById(R.id.textView_show_phone);
        progressBar= findViewById(R.id.progressBar);

        //set onclick listener on the ImageView to open UploadProfilePic Activity
        imageView= findViewById(R.id.imageView_profile_dp);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, UploadProfilePicActivity.class);
                startActivity(intent);

            }
        });
        authProfile= FirebaseAuth.getInstance();
        FirebaseUser firebaseUser= authProfile.getCurrentUser();
        if (firebaseUser== null){
            Toast.makeText(this, "User details are not available at the moment ", Toast.LENGTH_SHORT).show();
        }else{
            checkIfEmailVerified(firebaseUser);
            progressBar.setVisibility(View.VISIBLE);
            showUserProfile(firebaseUser);
        }

    }
    //users coming to UserProfileActivity after successful registration

    private void checkIfEmailVerified(FirebaseUser firebaseUser) {
        if (!firebaseUser.isEmailVerified()){
            showAlertDialog();
        }
    }

    private void showAlertDialog() {
        //set up alert builder
        AlertDialog.Builder builder= new AlertDialog.Builder(UserProfileActivity.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email, otherwise you cannot login in");
        //open email app if user clicks on continue
        builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent= new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //EMAIL APP open as a new window not in the app
                startActivity(intent);
            }
        });
        //create alert dialog box
        AlertDialog alertDialog= builder.create();
        //show the alertDialog
        alertDialog.show();
    }

    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID= firebaseUser.getUid();

        //extracting user reference from database for 'registered users'
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails= snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails !=null){
                    fullName= firebaseUser.getDisplayName();
                    email= firebaseUser.getEmail();
                    course= readUserDetails.course;
                    ID=readUserDetails.schoolID;
                    year= readUserDetails.year;
                    phone= readUserDetails.phone;

                    textViewWelcome.setText("Welcome"+ fullName +"!");
                    textViewFullName.setText(fullName);
                    textViewCourse.setText(course);
                    textViewID.setText(ID);
                    textViewEmail.setText(email);
                    textViewPhone.setText(phone);
                    textViewYear.setText(year);

                    //set profile pic after user has uploaded
                    Uri uri= firebaseUser.getPhotoUrl();

                    //imageviewer setImageURI() should not be used with regular URIs. were using picasso
                    Picasso.get().load(uri).into(imageView);
                }else{
                    Toast.makeText(UserProfileActivity.this, "something went wrong", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);

            }
        });
    }
    //creating ActionBar menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate menu items
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //when any menu item is selected
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.menu_refresh){
            //refresh activity
            startActivity(getIntent());
            finish();
            overridePendingTransition(0,0);
        }else if (id== R.id.menu_update_profile){
            Intent intent = new Intent(UserProfileActivity.this, UpdateProfileActivity.class);
            startActivity(intent);
        }else if(id==R.id.menu_home){
            Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
            startActivity(intent);
       /*// }else if(id==R.id.menu_update_email){
            Intent intent = new Intent(UserProfileActivity.this, UpdateEmailActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_settings) {
            Toast.makeText(UserProfileActivity.this, "settings", Toast.LENGTH_SHORT).show();

        } else if (id==R.id.menu_change_password) {
            Intent intent = new Intent(UserProfileActivity.this, ChangePasswordActivity.class);
            startActivity(intent);
        } else if (id==R.id.menu_logout) {
            authProfile.signOut();
            Toast.makeText(UserProfileActivity.this, "Logged Out", Toast.LENGTH_SHORT).show();*/
            //Intent intent = new Intent(UserProfileActivity.this, UserProfileActivity.class);

            //clear stack to prevent user coming back to user profile activity on presssing back buton after logging out
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            finish(); //close UserProfileActivity

        }else{
            Toast.makeText(this, " Something went wrong", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


}