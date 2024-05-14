package com.example.tuktradecircle.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tm.R;
import com.example.tuktradecircle.fragments.HomeFragment;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.cart) {
                   /* Intent intent = new Intent(HomeActivity.this, ShoppingCartActivity.class);
                    startActivity(intent);*/
                    Toast.makeText(HomeActivity.this, "Shopping Cart Selected", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.account) {
                    Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                    startActivity(intent);
                    Toast.makeText(HomeActivity.this, "Messages", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.chats) {
                    Intent intent = new Intent(HomeActivity.this, ChatActivity.class);
                    startActivity(intent);
                    Toast.makeText(HomeActivity.this, "Account Selected", Toast.LENGTH_SHORT).show();
                } else if (itemId == R.id.Post) {
                    Intent intent = new Intent(HomeActivity.this, PostActivity.class);
                    startActivity(intent);
                    Toast.makeText(HomeActivity.this, "Add post or item", Toast.LENGTH_SHORT).show();
                }else if (itemId == R.id.TandC) {
                    Intent intent = new Intent(HomeActivity.this, ConditionsActivity.class);
                    startActivity(intent);
                    Toast.makeText(HomeActivity.this, "Review Rules,Terms and conditions", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
