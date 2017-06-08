package com.example.a97557.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        CloseActivityClass.activityList.add(this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        sp = getSharedPreferences("SPShared", MODE_PRIVATE);
        String name = sp.getString("USERNAME", "张三");
        View headView = navigationView.getHeaderView(0);
        TextView username = (TextView) headView.findViewById(R.id.username);
        username.setText(name);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {
            sp = getSharedPreferences("SPShared", MODE_PRIVATE);
            final SharedPreferences.Editor editor = sp.edit();
            editor.putBoolean("REMEMBER_USER", false);
            editor.commit();
            Intent in = new Intent(HomeActivity.this, MainActivity.class);
            startActivity(in);
            CloseActivityClass.exitClient(HomeActivity.this);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home_page) {
            // Handle the camera action

        } else if (id == R.id.edit_studio) {
            Intent in = new Intent(HomeActivity.this,StudioActivity.class);
            startActivity(in);
        } else if (id == R.id.edit_schedule) {
            Intent in = new Intent(HomeActivity.this,ScheduleActivity.class);
            startActivity(in);
        } else if (id == R.id.edit_play) {
            Intent in = new Intent(HomeActivity.this,PlayActivity.class);
            startActivity(in);
        } else if(id == R.id.edit_employee) {
            Intent in = new Intent(HomeActivity.this,EmployeeActivity.class);
            startActivity(in);
        } else if (id == R.id.person) {
            Intent in = new Intent(HomeActivity.this,PersonActivity.class);
            startActivity(in);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeView(View view) {
        int id = view.getId();
        Intent in;
        switch (id) {
            case R.id.studio_relative:
                in = new Intent(HomeActivity.this, StudioActivity.class);
                startActivity(in);
                return;
            case R.id.play_relative:
                in = new Intent(this, PlayActivity.class);
                startActivity(in);
                return;
            case R.id.schedule_relative:
                in = new Intent(this, ScheduleActivity.class);
                startActivity(in);
                return;
            case R.id.employee_relative:
                in = new Intent(this, EmployeeActivity.class);
                startActivity(in);
                return;
            case R.id.person_relative:
                in = new Intent(this, PersonActivity.class);
                startActivity(in);
                return;
        }

        return;
    }
}
