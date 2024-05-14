                                      package com.example.tuktradecircle.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

                                      public class PostActivity extends AppCompatActivity {

                                          private static final int PICK_IMAGE_REQUEST = 1;

                                          private FirebaseFirestore db;
                                          private Uri imageUri;
                                          private ImageView postImageView;

                                          @Override
                                          protected void onCreate(Bundle savedInstanceState) {
                                              super.onCreate(savedInstanceState);
                                              setContentView(R.layout.activity_post);

                                              db = FirebaseFirestore.getInstance();
                                              postImageView = findViewById(R.id.postImageView);

                                              Button chooseImageButton = findViewById(R.id.chooseImageButton);
                                              chooseImageButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      openFileChooser();
                                                  }
                                              });

                                              Button postButton = findViewById(R.id.postButton);
                                              postButton.setOnClickListener(new View.OnClickListener() {
                                                  @Override
                                                  public void onClick(View v) {
                                                      String postName = ((EditText) findViewById(R.id.postNameEditText)).getText().toString();
                                                      String postCategory = ((EditText) findViewById(R.id.postCategoryEditText)).getText().toString();
                                                      String postContent = ((EditText) findViewById(R.id.postContentEditText)).getText().toString();
                                                      uploadPostToFirestore(postName, postCategory, postContent);
                                                  }
                                              });
                                          }

                                          private void openFileChooser() {
                                              Intent intent = new Intent();
                                              intent.setType("image/*");
                                              intent.setAction(Intent.ACTION_GET_CONTENT);
                                              startActivityForResult(intent, PICK_IMAGE_REQUEST);
                                          }

                                          @Override
                                          protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
                                              super.onActivityResult(requestCode, resultCode, data);
                                              if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                                                      && data != null && data.getData() != null) {
                                                  imageUri = data.getData();
                                                  postImageView.setImageURI(imageUri);
                                                  postImageView.setVisibility(View.VISIBLE);
                                              }
                                          }

                                          private void uploadPostToFirestore(String postName, String postCategory, String postContent) {
                                              // Upload image first (if selected)
                                              if (imageUri != null) {
                                                  StorageReference fileReference = FirebaseStorage.getInstance().getReference("uploads").child(System.currentTimeMillis() + ".jpg");

                                                  fileReference.putFile(imageUri)
                                                          .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                              @Override
                                                              public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                  fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                                      @Override
                                                                      public void onSuccess(Uri uri) {
                                                                          String imageUrl = uri.toString();
                                                                          savePostToFirestore(postName, postCategory, postContent, imageUrl);
                                                                      }
                                                                  });
                                                              }
                                                          })
                                                          .addOnFailureListener(new OnFailureListener() {
                                                              @Override
                                                              public void onFailure(@NonNull Exception e) {
                                                                  Toast.makeText(PostActivity.this, "Error uploading image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                              }
                                                          });
                                              } else {
                                                  savePostToFirestore(postName, postCategory, postContent, null);
                                              }
                                          }

                                          private void savePostToFirestore(String postName, String postCategory, String postContent, String imageUrl) {
                                              Map<String, Object> post = new HashMap<>();
                                              post.put("postName", postName);
                                              post.put("postCategory", postCategory);
                                              post.put("postContent", postContent);
                                              post.put("imageUrl", imageUrl); // Can be null if no image was uploaded
                                              post.put("userId", getCurrentUserId()); // Assuming you have a method to get the current user's ID

                                              db.collection("Posts")
                                                      .add(post)
                                                      .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                          @Override
                                                          public void onSuccess(DocumentReference documentReference) {
                                                              Toast.makeText(PostActivity.this, "Post uploaded successfully", Toast.LENGTH_SHORT).show();
                                                              finish(); // Close the activity after posting
                                                          }
                                                      })
                                                      .addOnFailureListener(new OnFailureListener() {
                                                          @Override
                                                          public void onFailure(@NonNull Exception e) {
                                                              Toast.makeText(PostActivity.this, "Error uploading post: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                          }
                                                      });
                                          }

                                          private String getCurrentUserId() {
                                              // Implement your logic to get the current user's ID
                                              return "user_id_placeholder";
                                          }
                                      }


