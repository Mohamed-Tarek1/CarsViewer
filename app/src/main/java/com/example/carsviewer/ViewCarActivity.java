package com.example.carsviewer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.textfield.TextInputEditText;

public class ViewCarActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQ_CODE = 1 ;
    public static final int ADD_CAR_RESULT_CODE = 2 ;
    public static final int EDIT_CAR_RESULT_CODE = 3 ;
    DatabaseAccess db;

    private Toolbar toolbar ;
    private TextInputEditText et_model , et_color, et_dpl, et_description;
    private ImageView iv;
    private int car_ID = -1;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_car);

        db =  DatabaseAccess.getInstance(this);

        // inflate the items
        toolbar  = findViewById(R.id.details_toolbar);
        setSupportActionBar(toolbar);

        et_model  = findViewById(R.id.et_details_model);
        et_color  = findViewById(R.id.et_details_color);
        iv  = findViewById(R.id.details_iv);
        et_dpl  = findViewById(R.id.et_details_dpl);
        et_description  = findViewById(R.id.et_details_description);

        Intent intent = getIntent();
        car_ID = intent.getIntExtra(MainActivity.CAR_KEY,-1);
        if(car_ID ==-1){
            //عملية اضافة
        fieldsState(true);
        clearData();

        }else{
            // عملية عرض
        
        fieldsState(false);
        db.open();
        Car c = db.getCar(car_ID);
        db.close();
        if(c!=null){
        fillCarToFields(c);
        }

        }
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE_REQ_CODE);
            }
        });
    }

    private void fillCarToFields(Car c){
        if(c.getImage() != null && !c.getImage().equals(""))
        iv.setImageURI(Uri.parse(c.getImage()));
        et_model.setText(c.getModel());
        et_color.setText(c.getColor());
        et_description.setText(c.getDecription());
        et_dpl.setText(c.getDpl()+"");
    }

    private void fieldsState(boolean state) {
        iv.setEnabled(state);
        et_model.setEnabled(state);
        et_dpl.setEnabled(state);
        et_description.setEnabled(state);
        et_color.setEnabled(state);
    }
    private void clearData(){
        iv.setImageURI(Uri.parse(""));
        et_model.setText("");
        et_color.setText("");
        et_description.setText("");
        et_dpl.setText("");
    }


    //working on details_menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.details_menu,menu);

            MenuItem save = menu.findItem(R.id.details_menu_save);
            MenuItem delete = menu.findItem(R.id.details_menu_delete);
            MenuItem edit = menu.findItem(R.id.details_menu_edit);

            if(car_ID ==-1){
            //عملية اضافة
            save.setVisible(true);
            delete.setVisible(false);
            edit.setVisible(false);

        }else{
            // عملية عرض
            save.setVisible(false);
            edit.setVisible(true);
            delete.setVisible(true);
        }
            return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId()){
            case R.id.details_menu_save:
                String model , color, desc , image="";
                Double dpl;
                model = et_model.getText().toString();
                color = et_color.getText().toString();
                desc = et_description.getText().toString();
                if(imageUri!=null)
                image = imageUri.toString();
                dpl = Double.parseDouble(et_dpl.getText().toString());
                Car c = new Car(car_ID,model,color,desc,image,dpl);
                boolean res;

                db.open();
                if(car_ID==-1){
                    res= db.insertCar(c);
                    if(res){
                        Toast.makeText(this,"Car Added Successfully",Toast.LENGTH_LONG).show();
                        setResult(ADD_CAR_RESULT_CODE,null);
                        finish();
                    }
                }else{
                    res = db.updateCar(c);
                    if(res){
                        Toast.makeText(this,"Car Edited Successfully",Toast.LENGTH_LONG).show();
                        setResult(EDIT_CAR_RESULT_CODE,null);
                        finish();
                    }
                }
                db.close();
                return true;
            case R.id.details_menu_edit:
                fieldsState(true);
                MenuItem save = toolbar.getMenu().findItem(R.id.details_menu_save);
                MenuItem delete = toolbar.getMenu().findItem(R.id.details_menu_delete);
                MenuItem edit = toolbar.getMenu().findItem(R.id.details_menu_edit);

                save.setVisible(true);
                delete.setVisible(false);
                edit.setVisible(false);

                return true;
            case R.id.details_menu_delete:
                db.open();
                c =  new Car(car_ID,null,null,null,null,0.0);
               res= db.deleteCar(c);
                if(res) {
                    Toast.makeText(this, "Car Deleted Successfully", Toast.LENGTH_LONG).show();
                    setResult(EDIT_CAR_RESULT_CODE, null);
                    finish();
                }
                db.close();
                return true;
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQ_CODE && resultCode==RESULT_OK){
            if(data != null){
                imageUri = data.getData();
                iv.setImageURI(imageUri);
            }
        }
    }
}