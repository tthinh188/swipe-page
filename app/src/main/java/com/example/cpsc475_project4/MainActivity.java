package com.example.cpsc475_project4;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;
import androidx.preference.PreferenceManager;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

public class MainActivity extends AppCompatActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String CNU_JSON_URL = "https://www.pcs.cnu.edu/~kperkins/pets/pets.json";
    private static final String TENTON_URL = "https://www.tentonsoftware.com/pets";
    private static final String ERROR = "404! ";
    private static final String CNU_KEY = "CNU - Defender";

    private String myURL = CNU_JSON_URL;
    private String JSonKey;

    JSONArray petList;
    ViewPager2 vp;
    ViewPager2_Adapter vpa;
    SharedPreferences.OnSharedPreferenceChangeListener listener = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences myPreference = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        myPreference.registerOnSharedPreferenceChangeListener(listener);

        getPrefVal(myPreference);

        if (JSonKey.equals(CNU_KEY)){  // process preference
            myURL = CNU_JSON_URL;
        }
        else{
            myURL = TENTON_URL;
        }

        DownloadTask_TP dt = new DownloadTask_TP(this);
        dt.execute(myURL);
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
        if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void getPrefVal(SharedPreferences settings){
        JSonKey = settings.getString("json", CNU_KEY);
    }

    protected void processJSon(String string) {
        if (string == null) {
            setContentView(R.layout.activity_main);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            TextView tv3 = findViewById(R.id.tv3);
            String error_mess = ERROR + myURL;
            tv3.setText(error_mess);
        } else {
            try {
                JSONObject jsonobject = new JSONObject(string);
                petList = jsonobject.getJSONArray("pets");

                setContentView(R.layout.swipe_activity);
                Toolbar toolbar = findViewById(R.id.toolbar);
                setSupportActionBar(toolbar);

                vp = findViewById(R.id.view_pager);
                vpa = new ViewPager2_Adapter(this, petList);
                vp.setAdapter(vpa);

            } catch (Exception e) {
                Toast.makeText(this, "JSONObject exception", Toast.LENGTH_SHORT).show();
            }
        }
    }

        @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Map<String, ?> all = sharedPreferences.getAll();
        Object value = all.get(key);
        if (String.valueOf(value).equals(CNU_KEY)){
            myURL = CNU_JSON_URL;
        }
        else{
            myURL = TENTON_URL;
        }
        DownloadTask_TP dt = new DownloadTask_TP(this);
        dt.execute(myURL);
    }
}
