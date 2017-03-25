package com.example.jiayi.intercept;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;

import java.util.List;
import java.math.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.jiayi.intercept.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    private Button myWIFI = null;
    private WifiManager localWifiManager =null;
    private TextView nameWIFI = null;
    private AudioManager myAudio = null;
    List<ScanResult> list;
    private WifiManager wifiManager;
    public static final int RINGER_MODE_SILENT = 0;
    public static final int RINGER_MODE_VIBRATE = 1;
    public static final int RINGER_MODE_NORMAL = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this,"wifiname");
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        myWIFI = (Button)findViewById(R.id.create);
//        myWIFI.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this,"wifiname");
//                SQLiteDatabase db = dbHelper.getReadableDatabase();
//            }
//        });
//        myWIFI = (Button)findViewById(R.id.myWIFI);
//        myWIFI.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//            }
//        });
        init();

        Thread t = new Thread(r);
        t.start();


    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(10000);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                localWifiManager = (WifiManager) MainActivity.this.getSystemService(Context.WIFI_SERVICE);
                if (!localWifiManager.isWifiEnabled()) {
                    localWifiManager.setWifiEnabled(true);
                }
                WifiInfo wifiInfo = localWifiManager.getConnectionInfo();
                String name = wifiInfo.getSSID().replaceAll("\"", "");
                System.out.println(name);
                DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this, "wifiname");
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                Cursor cursor;
                cursor = db.query("wifiname", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);
                if (cursor.getCount() != 0) {
                    myAudio = (AudioManager) MainActivity.this.getSystemService(Context.AUDIO_SERVICE);
                    int myMode = myAudio.getRingerMode();
                    if(myMode!=0) {
                        myAudio.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                        System.out.println("ok");
                    }
                } else
                    System.out.println("no");
            }
        }
    };


    private void init() {


        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        openWifi();
        list = wifiManager.getScanResults();
        ListView listView = (ListView) findViewById(R.id.myListView);
        if (list == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        }else {
            listView.setAdapter(new MyAdapter(this,list));
        }

    }

    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public class MyAdapter extends BaseAdapter {
        LayoutInflater inflater;
        List<ScanResult> list;

        public MyAdapter(Context context, List<ScanResult> list) {
            // TODO Auto-generated constructor stub
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = null;
            view = inflater.inflate(R.layout.user, null);
            ScanResult scanResult = list.get(position);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(scanResult.SSID);
            TextView signalStrenth;
            signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
            signalStrenth.setText(String.valueOf(Math.abs(scanResult.level)));
            Button add = (Button)view.findViewById(R.id.myCheck);
            Button delete = (Button)view.findViewById(R.id.delete);

            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(position);
                    ScanResult scanResult1 = list.get(position);
//                    System.out.println(scanResult1.SSID);
                    String name;
                    name = scanResult1.SSID;

                    DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this,"wifiname");
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor;
                    cursor = db.query("wifiname", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);
                    System.out.println(name);
                    if(cursor.getCount()==0 ) {
                        ContentValues values = new ContentValues();
                        values.put("name",name);
                        db.insert("wifiname", null, values);
                        Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_LONG).show();
                    }
                    else {
                        System.out.println("exist");
                        Toast.makeText(MainActivity.this, "This WIFI Name Already Exists in DB.", Toast.LENGTH_LONG).show();
                    }

//                    String name1 = "";
//                    String name2 = "";
//                    Cursor cursor = db.query("wifiname", new String[]{"name"}, null, null, null, null, null);
//                    while (cursor.moveToNext()) {
//                        name2 = cursor.getString(cursor.getColumnIndex("name"));
//                        name1 += name2;
//                    }
//                    System.out.println(name1);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println(position);
                    ScanResult scanResult1 = list.get(position);
//                    System.out.println(scanResult1.SSID);
                    String name;
                    name = scanResult1.SSID;

                    DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this,"wifiname");
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    Cursor cursor;
                    cursor = db.query("wifiname", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);
                    System.out.println(name);
                    if(cursor.getCount()!=0 ) {
                        db.delete("wifiname", "name=?", new String[]{name});
                        Toast.makeText(MainActivity.this,"Successful",Toast.LENGTH_LONG).show();
                    }
                    else {
                        System.out.println("No Found");
                        Toast.makeText(MainActivity.this, "This WIFI Name Doesn't Exists in DB.", Toast.LENGTH_LONG).show();
                    }
                }
            });
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
