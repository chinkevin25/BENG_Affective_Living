package com.liz.neuroscale.android.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.Spinner;
import com.liz.neuroscale.android.R;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ExtraUtils.HTTP.CommHTTP;

import ExtraUtils.LIFX.LIFXBulb;
import ExtraUtils.LIFX.BulbColor;

/**
 * Created by Kevin Chin on 4/23/2016.
 */
public class ParameterActivity extends Activity {

    // LIFX token for API access, change this if the bulb is reset. Each bulb requires the same token id
    private final String lifxToken = "c2ccc9cf5eeb451117755f977d491e705b46f584d7b658d8e0c7a78d6945be32";

    private CommHTTP mHttp;
    private ToggleButton onButton;
    public ArrayList<LIFXBulb> mBulbs;
    private Spinner spBulbs;
    private SeekBar brightnessSlider, saturationSlider, hueSlider, kelvinSlider;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_parameter);

       //  Intialize the GUI
       Initialize();

        onButton = (ToggleButton)findViewById(R.id.onButton);
        onButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onButton.isChecked()) { /** turn on the light*/
                    if (mBulbs.size() > 0)
                        SetState(mBulbs.get(spBulbs.getSelectedItemPosition()).id, "on");
                } else {
                    if (mBulbs.size() > 0)
                        SetState(mBulbs.get(spBulbs.getSelectedItemPosition()).id, "off");
                }
            }
        });
    }

    private int mHueColor;
    private float mBrightness;
    private float mSaturation;
    private int mKelvin;

    private final int MIN_KELVIN = 2500;
    private final int MAX_KELVIN = 9000;
    private void Initialize()
    {
        this.mHttp = new CommHTTP();

        this.mBulbs = new ArrayList<>();
        try
        {
            String json = mHttp.AsynGet("https://api.lifx.com/v1/lights/all", lifxToken);

            JSONArray lightJSONs = (JSONArray) JSONValue.parse(json);

            ArrayList<String> buld_str = new ArrayList<>();
            for (Object lightJSON : lightJSONs)
            {
                JSONObject Jobject = (JSONObject) lightJSON;
                LIFXBulb bulb = new LIFXBulb(Jobject);

                this.mBulbs.add(bulb);
                buld_str.add(bulb.id);
            }

            spBulbs = (Spinner) findViewById(R.id.lifxSpinner);

            ArrayAdapter<String> adapterArray = new ArrayAdapter<String>(ParameterActivity.this,android.R.layout.simple_spinner_dropdown_item, buld_str);
            spBulbs.setAdapter(adapterArray);
        }
        catch (Exception e)
        {
            Log.i("Exception", e.toString());
            return;
        }

        brightnessSlider = (SeekBar)findViewById(R.id.brightnessSlider);//[0.0-1.0]
        brightnessSlider.setMax(100);
        brightnessSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mBrightness = progress / 100.0f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBulbs.size() > 0)
                    SetBrightness(mBulbs.get(spBulbs.getSelectedItemPosition()).id, mBrightness);
            }
        });
        saturationSlider =(SeekBar)findViewById(R.id.saturationSlider); //[0.0-1.0]
        saturationSlider.setMax(100);
        saturationSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSaturation = progress/100.0f;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBulbs.size() > 0)
                    SetSaturation(mBulbs.get(spBulbs.getSelectedItemPosition()).id, mSaturation);
            }
        });
        kelvinSlider=(SeekBar)findViewById(R.id.kelvinSlider);//kelvin:[2500-9000]
        kelvinSlider.setMax(MAX_KELVIN - MIN_KELVIN);
        kelvinSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mKelvin = progress+MIN_KELVIN;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBulbs.size() > 0)
                    SetKelvin(mBulbs.get(spBulbs.getSelectedItemPosition()).id, mKelvin);
            }
        });

        hueSlider=(SeekBar)findViewById(R.id.hueSlider);      //[0-360]
        hueSlider.setMax(360);
        hueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHueColor = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (mBulbs.size() > 0)
                    SetHue(mBulbs.get(spBulbs.getSelectedItemPosition()).id, mHueColor);
            }
        });
    }

    public int SetState(String id, String state)
    {
        String url = "https://api.lifx.com/v1/lights/id:"+ id + "/state";
        Map<String, String> stateMap = new HashMap<>();
        stateMap.put("power", state);
        /**convert to json object*/
        JSONObject stateJson = new JSONObject(stateMap);
        /**put data to server and return response code*/
        return this.mHttp.AsynPUT(url, lifxToken, stateJson);
    }

    public int SetHue(String id, int hue)
    {
        String url = "https://api.lifx.com/v1/lights/id:"+ id + "/state";
        Map<String, String> stateMap = new HashMap<>();
        stateMap.put("color", "hue:"+ hue);
        /**convert to json object*/
        JSONObject stateJson = new JSONObject(stateMap);
        /**put data to server and return response code*/
        return this.mHttp.AsynPUT(url, lifxToken, stateJson);
    }

    public int SetBrightness(String id, float brightness)
    {
        String url = "https://api.lifx.com/v1/lights/id:"+ id + "/state";
        Map<String,String> stateMap = new HashMap<>();
        stateMap.put("brightness", brightness + "");
        JSONObject stateJson = new JSONObject(stateMap);
        return this.mHttp.AsynPUT(url, lifxToken, stateJson);
    }

    public boolean SetKelvin(String id, int kelvin)
    {
        if (kelvin<= MAX_KELVIN && kelvin>= MIN_KELVIN) {
            String url = "https://api.lifx.com/v1/lights/id:" + id + "/state";
            Map<String, String> stateMap = new HashMap<>();
            stateMap.put("color", "kelvin:"+ kelvin);
            /**convert to json object*/
            JSONObject stateJson = new JSONObject(stateMap);
            /**put data to server and return response code*/
            this.mHttp.AsynPUT(url, lifxToken, stateJson);
            return true;
        }else {
            return false;
        }
    }
    public int SetSaturation(String id, float saturation)
    {
        String url = "https://api.lifx.com/v1/lights/id:"+ id + "/state";
        Map<String, String> stateMap = new HashMap<>();
        stateMap.put("color", "saturation:"+ saturation);
        /**convert to json object*/
        JSONObject stateJson = new JSONObject(stateMap);
        /**put data to server and return response code*/
        return this.mHttp.AsynPUT(url, lifxToken, stateJson);
    }
}
