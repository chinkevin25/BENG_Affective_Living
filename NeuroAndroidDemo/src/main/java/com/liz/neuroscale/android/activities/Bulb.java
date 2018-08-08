package com.liz.neuroscale.android.activities;
import android.os.Environment;
import android.util.Log;

import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import ExtraUtils.HTTP.CommHTTP;

public class Bulb
{
    private final String lifxToken = "cd93714d188bd179dbcad7ac943219ac2b3a6a9626e34410f0ad0e65446af5ab";
    private static final long TIME_OF_CALIBRATION=20000;
    private boolean firstRun = true;
    private boolean firstRunCalibration=true;
    long timeCal;
    private CommHTTP mHttp = new CommHTTP();
    private String lightColor = "white";
    private long baseLineTime;

    File dir; //directory to save the data files in external storage
    File dataFile;
    File colorFile;
    Map<Double, Double> data = new LinkedHashMap<Double, Double>();//(time, output) from NS
    Map<Double, String> colorMap = new LinkedHashMap<Double, String>(); //time, color


    public Bulb()
    {
        setPower("on");
        setColor("white");
        if (isExternalStorageWritable()) {
            Log.v("MyApp", "External storage possible");
        }
        dir = new File(Environment.getExternalStoragePublicDirectory
                (Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/data/");
        dir.mkdir();
        dataFile = new File(dir, "data.txt");
        colorFile = new File(dir, "color.txt");
    }
    private boolean isExternalStorageWritable()
    {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /**
     * Disconnects the bulb and writes out
     */
    public void disconnect()
    {
        try
        {
            FileOutputStream os = new FileOutputStream(dataFile);
            ObjectOutputStream oos = new ObjectOutputStream(os);
            FileOutputStream os2 = new FileOutputStream(colorFile);
            ObjectOutputStream oos2 = new ObjectOutputStream(os2);

            StringBuilder builderDataMap = new StringBuilder();
            StringBuilder builderColorMap = new StringBuilder();

            for (Map.Entry<Double,Double> entry : data.entrySet()) //rewrites the map in a way that can easily be loaded into excel
            {
                builderDataMap.append("\n"); // Or whatever break you want
                builderDataMap.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue());
            }
            for (Map.Entry<Double,String> entry : colorMap.entrySet())
            {
                builderColorMap.append("\n"); // Or whatever break you want
                builderColorMap.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue());
            }

            oos.writeObject(builderDataMap.toString());
            oos2.writeObject(builderColorMap.toString());

            os.close();
            oos.close();
            os2.close();
            oos2.close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /**
     * The first 20seconds of the muse are calibration and thus nothing will be done by system.
     */
    private void isCalibrated()
    {
        if (firstRunCalibration)
        {
            timeCal=System.currentTimeMillis();
            firstRunCalibration= false;
        }
        else
        {
            if (System.currentTimeMillis()-timeCal>TIME_OF_CALIBRATION)
            {
                baseLineTime =System.currentTimeMillis();
                calibrated=true;
            }
        }
    }
    private boolean calibrated=false;
    /**
     * Receiving the input from NeuroScale, the data will be added to the objects internal data
     * structure for analysis
     * @param d
     */
    private static double samplingTime;
    public void addData(double d)
    {
        if (calibrated)
        {
            double t = (double)System.currentTimeMillis();
            if (firstRun)
            {
                samplingTime = t;
                data.put(t, d);
                colorMap.put(t, lightColor);
            }
            if (t-samplingTime>3000)//only samples every 3 seconds
            {
                data.put(t, d);
                colorMap.put(t, lightColor);
                samplingTime=t;
            }

            order();
        }
        else
        {
            isCalibrated();
        }
    }
    private static double testTime = 90000;
    private static double rsaTime = 10000;
    private static double TIME_OF_BASELINE=240000;
    private static double STRESS_SLIDER_1 = TIME_OF_BASELINE+rsaTime;
    private static double RED_COLOR_1=STRESS_SLIDER_1+testTime;
    private static double STRESS_SLIDER_2 = RED_COLOR_1+rsaTime;
    private static double GREEN_COLOR_1=STRESS_SLIDER_2+testTime;
    private static double STRESS_SLIDER_3=GREEN_COLOR_1+rsaTime;
    private static double RED_COLOR_2=STRESS_SLIDER_3+testTime;
    private static double STRESS_SLIDER_4 = RED_COLOR_2+rsaTime;
    private static double GREEN_COLOR_2=STRESS_SLIDER_4+testTime;
    private static double STRESS_SLIDER_5 = GREEN_COLOR_2+rsaTime;
    private static double RED_COLOR_3=STRESS_SLIDER_5+testTime;
    private static double STRESS_SLIDER_6 = RED_COLOR_3+rsaTime;
    private static double GREEN_COLOR_3=STRESS_SLIDER_6+testTime;
    private static double STRESS_SLIDER_7 = GREEN_COLOR_3+rsaTime;
    private static double RED_COLOR_4=STRESS_SLIDER_7+testTime;
    private static double STRESS_SLIDER_8 = RED_COLOR_4+rsaTime;
    private static double GREEN_COLOR_4=STRESS_SLIDER_8+testTime;
    private static double STRESS_SLIDER_9 = GREEN_COLOR_4+rsaTime;
    private static double RED_COLOR_5=STRESS_SLIDER_9+testTime;
    private static double STRESS_SLIDER_10 = RED_COLOR_5+rsaTime;
    private static double GREEN_COLOR_5=STRESS_SLIDER_10+testTime;
    private static String colorA = "red";
    private static String colorB = "blue";
    private boolean RSATest1=true;
    private boolean RSATest2=true;
    private boolean RSATest3=true;
    private boolean RSATest4=true;
    private boolean RSATest5=true;
    private boolean RSATest6=true;
    private boolean RSATest7=true;
    private boolean RSATest8=true;
    private boolean RSATest9=true;
    private boolean RSATest10=true;


    /**
     * Iterates through the order of the test...
     */
    private void order()
    {
        double currentTime = System.currentTimeMillis()- baseLineTime;
        if (currentTime < TIME_OF_BASELINE) //5min baseline neutral color
        {
            setBrightness(.7);
            setColor("white");
            lightColor="baseline white";
        }
        else if (currentTime>TIME_OF_BASELINE && currentTime<STRESS_SLIDER_1) //10seconds for RSA
        {
            if (RSATest1)
            {
                setPower("off");
                RSATest1=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            setBrightness(1);
            lightColor="testing white";

        }
        else if (currentTime>STRESS_SLIDER_1 && currentTime<RED_COLOR_1) //red test1
        {
            setColor(colorA);
            lightColor=colorA+1;

        }
        else if (currentTime>RED_COLOR_1 && currentTime<STRESS_SLIDER_2)//10sec RSA
        {
            if (RSATest2)
            {
                setPower("off");
                RSATest2=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white1";

        }
        else if (currentTime>STRESS_SLIDER_2 && currentTime<GREEN_COLOR_1) //red test1
        {
            setColor(colorB);
            lightColor=colorB+1;
        }
        else if (currentTime>GREEN_COLOR_1 && currentTime<STRESS_SLIDER_3)//10sec RSA
        {
            if (RSATest3)
            {
                setPower("off");
                RSATest3=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white2";

        }
        //1
        else if (currentTime>STRESS_SLIDER_3 && currentTime<RED_COLOR_2) //red test1
        {
            setColor(colorA);
            lightColor=colorA+2;
        }
        else if (currentTime>RED_COLOR_2 && currentTime<STRESS_SLIDER_4)//10sec RSA
        {
            if (RSATest4)
            {
                setPower("off");
                RSATest4=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white3";

        }
        else if (currentTime>STRESS_SLIDER_4 && currentTime<GREEN_COLOR_2) //red test1
        {
            setColor(colorB);
            lightColor=colorB+2;

        }
        else if (currentTime>GREEN_COLOR_2 && currentTime<STRESS_SLIDER_5)//10sec RSA
        {
            if (RSATest5)
            {
                setPower("off");
                RSATest5=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white4";

        }
        //2
        else if (currentTime>STRESS_SLIDER_5 && currentTime<RED_COLOR_3) //red test1
        {
            setColor(colorA);
            lightColor=colorA+3;

        }
        else if (currentTime>RED_COLOR_3 && currentTime<STRESS_SLIDER_6)//10sec RSA
        {
            if (RSATest6)
            {
                setPower("off");
                RSATest6=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white5";

        }
        else if (currentTime>STRESS_SLIDER_6 && currentTime<GREEN_COLOR_3) //red test1
        {
            setColor(colorB);
            lightColor=colorB+3;

        }
        else if (currentTime>GREEN_COLOR_3 && currentTime<STRESS_SLIDER_7)//10sec RSA
        {
            if (RSATest7)
            {
                setPower("off");
                RSATest7=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white6";

        }
        //3
        else if (currentTime>STRESS_SLIDER_7 && currentTime<RED_COLOR_4) //red test1
        {
            setColor(colorA);
            lightColor=colorA + 4;

        }
        else if (currentTime>RED_COLOR_4 && currentTime<STRESS_SLIDER_8)//10sec RSA
        {
            if (RSATest8)
            {
                setPower("off");
                RSATest8=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white7";

        }
        else if (currentTime>STRESS_SLIDER_8 && currentTime<GREEN_COLOR_4) //red test1
        {
            setColor(colorB);
            lightColor=colorB + 4;

        }
        else if (currentTime>GREEN_COLOR_4 && currentTime<STRESS_SLIDER_9)//10sec RSA
        {
            if (RSATest9)
            {
                setPower("off");
                RSATest9=false;
            }
            else
            {
                setPower("on");
                setColor("white"); //maybe make it blink a few times...
            }
            lightColor="white8";
        }
        else if(currentTime>STRESS_SLIDER_9)
        {
            setColor("green");
            lightColor = "green, end of test";
        }

//        //4
//        else if (currentTime>STRESS_SLIDER_9 && currentTime<RED_COLOR_5) //red test1
//        {
//            setColor("red");//r1
//            lightColor="red5";
//
//        }
//        else if (currentTime>RED_COLOR_5 && currentTime<STRESS_SLIDER_10)//10sec RSA
//        {
//            setColor("white");
//            lightColor="white9";
//
//        }
//        else if (currentTime>STRESS_SLIDER_10 && currentTime<GREEN_COLOR_5) //red test1
//        {
//            setColor("blue");//b1
//            lightColor="blue5";
//        }
//        else if (currentTime>GREEN_COLOR_5 )//10sec RSA
//        {
//            setColor("orange");
//            lightColor = "orange, end of test";
//        }
        else //something went wrong
        {
            setBrightness(0);
        }
        //5


    }

    private int setBrightness(double b)
    {
        String url = "https://api.lifx.com/v1/lights/all/state";
        Map<String, String> stateMap = new HashMap<>();
        stateMap.put("brightness", b+"");
        /**convert to json object*/
        JSONObject stateJson = new JSONObject(stateMap);
        /**put data to server and return response code*/
        return this.mHttp.AsynPUT(url, lifxToken, stateJson);
    }
    private int setColor(String color)
    {
        String url = "https://api.lifx.com/v1/lights/all/state";
        Map<String, String> stateMap = new HashMap<>();
        stateMap.put("color", color);
        JSONObject stateJson = new JSONObject(stateMap);
        return this.mHttp.AsynPUT(url, lifxToken, stateJson);
    }
    private int setPower(String power)
    {
        String url = "https://api.lifx.com/v1/lights/all/state";
        Map<String, String> stateMap = new HashMap<>();
        stateMap.put("power", power);
        JSONObject stateJson = new JSONObject(stateMap);
        return this.mHttp.AsynPUT(url, lifxToken, stateJson);
    }

}
