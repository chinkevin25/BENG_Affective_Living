package com.liz.neuroscale.android.activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.liz.neuroscale.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ExtraUtils.Configuration;

/**
 * activity for access token configuration
 */
public class ConfigureActivity extends Activity {
//    private View mView
    private Button btnSaveAccesstoken;
    private EditText etUrl;
    private EditText etAccess;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_configure);
        this.etAccess = (EditText) findViewById(R.id.editPassword);
        this.etUrl = (EditText) findViewById(R.id.editURL);

        ConfigurePackageFormat();
        /**read configure*/
        if (readConfigure())/**successed*/
        {
            this.etUrl.setText(Configuration.api_url);
            this.etAccess.setText(Configuration.access_token);
            this.spinnerPackFormat.setSelection(Configuration.pack_json_msgpack? 0:1);
        }

        /**Set button save */
        btnSaveAccesstoken = (Button) findViewById(R.id.buttonSaveConfigure);
        btnSaveAccesstoken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Configuration.api_url = etUrl.getText().toString();
                Configuration.access_token = etAccess.getText().toString();
                //Configuration.pack_json_msgpack = packageFormat.get(spinnerPackFormat.getSelectedItemPosition());
                Map<String, String> map = new HashMap<String, String>();
                map.put("URL", Configuration.api_url);
                map.put("Access", Configuration.access_token);
                map.put("Format", Configuration.pack_json_msgpack + "");
                JSONObject saveOBJ = new JSONObject(map);
                writeConfigure(saveOBJ);
                //Toast.makeText(ConfigureFragment.this, "URI and access token saved", Toast.LENGTH_SHORT).show();
            }
        });
    }

    List<String> packageFormat = new ArrayList<String>();
    Spinner spinnerPackFormat;
    private void ConfigurePackageFormat()
    {
        spinnerPackFormat = (Spinner)findViewById(R.id.spnner_pack);
        this.packageFormat.add("JSON");
        this.packageFormat.add("MESSAGE_PACK");

        ArrayAdapter<String> adapterArray = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, packageFormat);

        spinnerPackFormat.setAdapter(adapterArray);

        spinnerPackFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Configuration.pack_json_msgpack = (packageFormat.get(position) == "JSON");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        //outState = result.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    /**
     * Read configuration from a private file
     * @return
     */
    private boolean readConfigure() {
        try {
//            File traceFile = new File(getActivity().getExternalFilesDir(null), "NeuroAndroidPrivate");
            File traceFile = new File(this.getFilesDir().getPath().toString(), "NeuroAndroidPrivate");
//            File traceFile = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOCUMENTS
//            ), "NeuroAndroidDemoPrivate");
            if (traceFile.exists()) {
                BufferedReader reader = new BufferedReader(new FileReader(traceFile));
                String line;
                StringBuilder stringBuilder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();
                try {
                    JSONObject confugre = new JSONObject(stringBuilder.toString());
                    if (confugre != null) {
                        Configuration.access_token = confugre.getString("Access");
                        Configuration.api_url = confugre.getString("URL");
                        Configuration.pack_json_msgpack = confugre.getBoolean("Format");
                        return true;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * write configuraton to file
     * @param SON
     */
    private void writeConfigure(JSONObject SON)
    {
        try {
//            File traceFile = new File(getActivity().getExternalFilesDir(null), "NeuroAndroidPrivate");
            File traceFile = new File(this.getFilesDir().getPath().toString(), "NeuroAndroidPrivate");
//            //File traceFile = new File(Environment.getExternalStoragePublicDirectory(
//                    Environment.DIRECTORY_DOCUMENTS
//            ), "NeuroAndroidDemoPrivate");
            if (!traceFile.exists())
                traceFile.createNewFile();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(traceFile, false));
            bufferedWriter.write(SON.toString());
            bufferedWriter.close();
            System.out.println("The file was successfully updated");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("The file was failed to write");
            //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
        }
    }
}
