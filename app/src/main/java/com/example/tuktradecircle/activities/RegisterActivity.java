package com.example.tuktradecircle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class RegisterActivity extends AppCompatActivity {
    private EditText editTextRegisterFullName, editTextRegisterEmail, editTextRegisterSchoolID, editTextRegisterCourse,
            editTextRegisterYear, editTextRegisterPhone, editTextRegisterPwd, editTextRegisterConfirm;
    private ProgressBar progressBar;
    private static final String TAG= "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("Register");
        Toast.makeText(RegisterActivity.this, "Go on and register :)", Toast.LENGTH_LONG).show();

        editTextRegisterFullName= findViewById(R.id.editText_register_full_name);
        editTextRegisterEmail= findViewById(R.id.editText_register_email);
        editTextRegisterSchoolID= findViewById(R.id.editText_register_school);
        editTextRegisterCourse= findViewById(R.id.editText_register_course);
        editTextRegisterYear = findViewById(R.id.editText_register_year);
        editTextRegisterPhone = findViewById(R.id.editText_register_phone);
        editTextRegisterPwd = findViewById(R.id.editText_register_password);
        editTextRegisterConfirm = findViewById(R.id.editText_register_confirm);

        progressBar = findViewById(R.id.progressBar);

        Button buttonRegister = findViewById(R.id.button_register);
        buttonRegister.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //obtain the entered data//

                String textFullName = editTextRegisterFullName.getText().toString();
                String textEmail= editTextRegisterEmail.getText().toString();
                String textSchoolID= editTextRegisterSchoolID.getText().toString();
                String textCourse= editTextRegisterCourse.getText().toString();
                String textYear= editTextRegisterYear.getText().toString();
                String textPhone= editTextRegisterPhone.getText().toString();
                String textPwd= editTextRegisterPwd.getText().toString();
                String textConfirm = editTextRegisterConfirm.getText().toString();

                //validate mobile number using matcher and pattern, regular expression



                if (TextUtils.isEmpty(textFullName)){
                    Toast.makeText(RegisterActivity.this, "Please enter your full name", Toast.LENGTH_SHORT).show();
                    editTextRegisterFullName.setError("Full name is required");
                    editTextRegisterFullName.requestFocus();
                }else if (TextUtils.isEmpty(textEmail)){
                    Toast.makeText(RegisterActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError("E-mail is required");
                    editTextRegisterEmail.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(textEmail).matches()){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your email", Toast.LENGTH_SHORT).show();
                    editTextRegisterEmail.setError(" valid E-mail is required");
                    editTextRegisterEmail.requestFocus();
                }else if (TextUtils.isEmpty(textSchoolID)){
                    Toast.makeText(RegisterActivity.this, "Please enter your school ID", Toast.LENGTH_SHORT).show();
                    editTextRegisterSchoolID.setError("School ID is required");
                    editTextRegisterSchoolID.requestFocus();
                }else if (TextUtils.isEmpty(textCourse)){
                    Toast.makeText(RegisterActivity.this, "Please enter the course you're doing", Toast.LENGTH_SHORT).show();
                    editTextRegisterCourse.setError("School ID is required");
                    editTextRegisterCourse.requestFocus();
                }else if (TextUtils.isEmpty(textYear)){
                    Toast.makeText(RegisterActivity.this, "Please enter your current year of study", Toast.LENGTH_SHORT).show();
                    editTextRegisterYear.setError("School ID is required");
                    editTextRegisterYear.requestFocus();
                } else if (TextUtils.isEmpty(textPhone)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterPhone.setError("Phone number is required");
                    editTextRegisterPhone.requestFocus();
                }else if (textPhone.length()!=10){
                    Toast.makeText(RegisterActivity.this, "Please re-enter your mobile number", Toast.LENGTH_SHORT).show();
                    editTextRegisterPhone.setError("Should be 10 digits");
                    editTextRegisterPhone.requestFocus();
                } else if (TextUtils.isEmpty(textPwd)) {
                    Toast.makeText(RegisterActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterPwd.setError("password is required");
                    editTextRegisterPwd.requestFocus();
                } else if (TextUtils.isEmpty(textConfirm)) {
                    Toast.makeText(RegisterActivity.this, "Please confirm your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirm.setError("confirmation is required");
                    editTextRegisterConfirm.requestFocus();
                }else if (!textPwd.equals(textConfirm)){
                    Toast.makeText(RegisterActivity.this, "check your password", Toast.LENGTH_SHORT).show();
                    editTextRegisterConfirm.setError("Check your password");
                    editTextRegisterConfirm.requestFocus();
                    //clear the entered password//
                    editTextRegisterPwd.clearComposingText();
                    editTextRegisterConfirm.clearComposingText();
                }else{
                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(textFullName,textEmail,textSchoolID,textCourse,textYear,textPhone,textPwd,textConfirm);
                }


            }
        }));


    }
    //register user using the credentials given//
    private void registerUser(String textFullName, String textEmail, String textSchoolID, String textCourse, String textYear, String textPhone, String textPwd, String textConfirm) {
        FirebaseAuth auth= FirebaseAuth.getInstance();

        //define action code settings
        ActionCodeSettings actionCodeSettings = ActionCodeSettings.newBuilder()
                .setUrl("https://example.com") //
                .setHandleCodeInApp(true)
                .setAndroidPackageName(
                        "com.example.tm", // Replace with your Android package name
                        true, /* installIfNotAvailable */
                        "12" /* minimumVersion */)
                .build();


        //create user profile
        auth.createUserWithEmailAndPassword(textEmail, textPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    //enter user data into the firebase Realtme Database
                    ReadWriteUserDetails writeUserDetails= new ReadWriteUserDetails(textFullName, textCourse,textSchoolID,textYear,textPhone);
                    //extracting user reference from database  for registered user.
                    DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference( "Registered Users");

                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {   //when values in the database have been saved that is when verification will be done

                            if (task.isSuccessful()){
                                //send verification email//
                                firebaseUser.sendEmailVerification();
                                Toast.makeText(RegisterActivity.this, "Register successful :) Please verify your email", Toast.LENGTH_SHORT).show();
                                //opening user profile//
                                Intent intent= new Intent(RegisterActivity.this, UserProfileActivity.class);
                                //when user presses back button they cant go back to registering again
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | intent.FLAG_ACTIVITY_CLEAR_TASK | intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();//close register activity

                            }else{
                                Toast.makeText(RegisterActivity.this, "registration failed, please try again", Toast.LENGTH_SHORT).show();

                            }
                            //hide progress bar
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }else {
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthUserCollisionException e){
                        editTextRegisterPwd.setError("E-mail is already registered by a user");
                        editTextRegisterPwd.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this,e.getMessage(), Toast.LENGTH_SHORT).show();
                    }finally {
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }
        });
    }
}