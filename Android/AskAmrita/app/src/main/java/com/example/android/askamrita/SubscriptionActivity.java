package com.example.android.askamrita;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class SubscriptionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mDrawerlayout = (DrawerLayout) findViewById(R.id.sidebar5);
        mToggle = new ActionBarDrawerToggle(SubscriptionActivity.this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent i = new Intent(SubscriptionActivity.this, activity_home.class);
            startActivity(i);
        } else if (id == R.id.forum) {
            Intent i = new Intent(SubscriptionActivity.this, ForumActivity.class);
            startActivity(i);

        } else if (id == R.id.latest) {
            Intent i = new Intent(SubscriptionActivity.this, LatestActivity.class);
            startActivity(i);

        } else if (id == R.id.subscriptions) {
            Intent i = new Intent(SubscriptionActivity.this, SubscriptionActivity.class);
            startActivity(i);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.sidebar5);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
