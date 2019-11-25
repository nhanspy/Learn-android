package com.example.sqlitedemo2;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    String DB_PATH_SUFFIX = "/databases/";
    public static SQLiteDatabase database = null;
    public static String DATABASE_NAME = "qlsach.db";
    //khai bao de dung listview
    ArrayList<String> myList;
    ArrayAdapter<String> myAdapter;
    ListView lv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        processCopy();//chay file CSDL
        database = openOrCreateDatabase("qlsach.db", MODE_PRIVATE, null);
        // su dung listview
        lv1 = (ListView) findViewById(R.id.lv1);
        myList = new ArrayList<String>();
        myAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, myList);
        lv1.setAdapter(myAdapter);
        Cursor c = database.query("tbSach", null, null, null, null, null, null);
        c.moveToFirst();
        String msg = "";

        while (c.isAfterLast() == false){
            msg = c.getString(0) + " " + c.getString(1) + " " +  c.getString(2) + " " + c.getString(4) ;
            myList.add(msg);
            myAdapter.notifyDataSetChanged();
            c.moveToNext();
        }
        c.close();
    }

    private void processCopy() {
        //private app
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
                Toast.makeText(this, "Copying success from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {
        // TODO Auto-generated method stub
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            // Path to the just created empty db
            String outFileName = getDatabasePath();
            //if the path doesn't exist first, create it
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0)
            {
                myOutput.write(buffer, 0, length);
            }
            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            //TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
