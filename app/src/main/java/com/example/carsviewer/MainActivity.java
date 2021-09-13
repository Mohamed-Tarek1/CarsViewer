package com.example.carsviewer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQ_CODE = 5 ;
    private RecyclerView rv;
    private FloatingActionButton fab;
    private Toolbar toolbar;
    private DatabaseAccess db;
    private CarRVAdabter adabter;

    private static final int ADD_CAR_REQ_CODE =1 ;
    private static final int EDIT_CAR_REQ_CODE =1 ;
    public static final String CAR_KEY ="car_key" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // to take the permission of writing in external storage
        if(ContextCompat.checkSelfPermission(this, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) != getPackageManager().PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQ_CODE);

        }


        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        rv = findViewById(R.id.main_rv);
        fab = findViewById(R.id.main_fb);
    db= DatabaseAccess.getInstance(this);
    db.open();
    ArrayList<Car> cars = db.getAllCars();
    db.close();

    adabter = new CarRVAdabter(cars, new OnRecyclerViewItemClickListener() {
        @Override
        public void onItemClick(int carId) {
            Intent i = new Intent(getBaseContext(),ViewCarActivity.class);
            i.putExtra(CAR_KEY,carId);
            startActivityForResult(i,EDIT_CAR_REQ_CODE);
        }
    });
    rv.setAdapter(adabter);
    RecyclerView.LayoutManager lm = new GridLayoutManager(this,2);
    rv.setLayoutManager(lm);
    rv.setHasFixedSize(true);

    //the action of FAButton to add a new car
    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
    Intent intent = new Intent(getApplicationContext(),ViewCarActivity.class);
    startActivityForResult(intent , ADD_CAR_REQ_CODE);
        }
    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.main_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                db.open();
                ArrayList<Car> c = db.getCars(query);
                db.close();
                adabter.setCars(c);
                adabter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                db.open();
                ArrayList<Car> c = db.getCars(newText);
                db.close();
                adabter.setCars(c);
                adabter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {

                db.open();
                ArrayList<Car> c = db.getAllCars();
                db.close();
                adabter.setCars(c);
                adabter.notifyDataSetChanged();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_CAR_REQ_CODE ){
            db.open();
            ArrayList<Car> cars = db.getAllCars();
            db.close();
            adabter.setCars(cars);
            adabter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch(requestCode){
            case PERMISSION_REQ_CODE:
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                    // permission accepted
                }

        }


    }
}