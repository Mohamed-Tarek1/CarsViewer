package com.example.carsviewer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseAccess {

    private SQLiteDatabase database;
    private final SQLiteOpenHelper openHelper;
    private static DatabaseAccess instance;

    private  DatabaseAccess(Context context) {
        this.openHelper = new MyDatabase(context);
    }


    public static DatabaseAccess getInstance(Context context){
    if (instance == null)
    {
        instance = new DatabaseAccess(context);
    }
    return instance;
    }

    public void open(){
        this.database= this.openHelper.getWritableDatabase();

    }
    public void close(){
        if(this.database!=null){
            this.database.close();
        }
    }

    // دالة إضافة
    public boolean insertCar(Car car){

        ContentValues values = new ContentValues();
        values.put(MyDatabase.CAR_CLN_MODEL , car.getModel());
        values.put(MyDatabase.CAR_CLN_COLOR , car.getColor());
        values.put(MyDatabase.CAR_CLN_DPL , car.getDpl());
        values.put(MyDatabase.CAR_CLN_IMAGE , car.getImage());
        values.put(MyDatabase.CAR_CLN_DESCRIPTION , car.getDecription());

        long result = database.insert(MyDatabase.CAR_TB_NAME ,null,values);
        return result != -1;
    }

    // دالة التعديل
    public boolean updateCar(Car car){
        ContentValues values = new ContentValues();
        values.put(MyDatabase.CAR_CLN_MODEL , car.getModel());
        values.put(MyDatabase.CAR_CLN_COLOR , car.getColor());
        values.put(MyDatabase.CAR_CLN_DPL , car.getDpl());
        values.put(MyDatabase.CAR_CLN_IMAGE , car.getImage());
        values.put(MyDatabase.CAR_CLN_DESCRIPTION , car.getDecription());

        String[] args = {String.valueOf(car.getId())};
        int result = database.update(com.example.carsviewer.MyDatabase.CAR_TB_NAME ,values , "id=?",args);
        return result > 0;
    }

    public long getCarsCount(){
        return DatabaseUtils.queryNumEntries(database, com.example.carsviewer.MyDatabase.CAR_TB_NAME);

    }

    public boolean deleteCar(Car car){
        String[] args = {String.valueOf(car.getId())};
        int result = database.delete(com.example.carsviewer.MyDatabase.CAR_TB_NAME , "id=?",args);
        return result > 0;
    }

    public ArrayList<Car> getAllCars(){
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+ com.example.carsviewer.MyDatabase.CAR_TB_NAME , null);

        if(cursor != null && cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_COLOR));
                double dpl = cursor.getDouble(cursor.getColumnIndex(MyDatabase.CAR_CLN_DPL));
                String image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_DESCRIPTION));

                Car c = new Car(id,model,color,description,image,dpl);
                cars.add(c);
            } while (cursor.moveToNext());
                cursor.close();
        }
        return  cars;

    }

    public Car getCar( int car_id){

        Cursor cursor = database.rawQuery("SELECT * FROM "+
                MyDatabase.CAR_TB_NAME+" WHERE "+MyDatabase.CAR_CLN_ID+"=?" , new String[]{String.valueOf(car_id)});

        if(cursor != null && cursor.moveToFirst()){

                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_COLOR));
                double dpl = cursor.getDouble(cursor.getColumnIndex(MyDatabase.CAR_CLN_DPL));
                String image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_DESCRIPTION));

                Car c = new Car(id,model,color,description,image,dpl);
            cursor.close();
            return  c;
        }
        return  null;

    }





    // search method

    public ArrayList<Car> getCars(String modelSearch){
        ArrayList<Car> cars = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM "+ com.example.carsviewer.MyDatabase.CAR_TB_NAME+" WHERE "+  com.example.carsviewer.MyDatabase.CAR_CLN_MODEL+" LIKE ?" ,
                new String [] {modelSearch+"%"});

        // convert cursor to a list of car
        // get if the cursor is empty or not

        if(cursor != null && cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex(MyDatabase.CAR_CLN_ID));
                String model = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_MODEL));
                String color = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_COLOR));
                double dpl = cursor.getDouble(cursor.getColumnIndex(MyDatabase.CAR_CLN_DPL));
                String image = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_IMAGE));
                String description = cursor.getString(cursor.getColumnIndex(MyDatabase.CAR_CLN_DESCRIPTION));

                Car c = new Car(id,model,color,description,image,dpl);
                cars.add(c);
            } while (cursor.moveToNext());
                cursor.close();
            }
            return cars;
        }

}
