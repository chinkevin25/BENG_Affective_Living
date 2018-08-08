package ExtraUtils;

import org.json.simple.JSONArray;
import org.msgpack.util.json.JSON;

import java.util.Arrays;

public class ArrayUtils {
	/**
	 * 1D matrix to String
	 * @param a
	 * @return
	 */
	public static String toString(float[] a)
	{
		return Arrays.toString(a);
	}
	public static String toString(Double[] a)
	{
		return Arrays.toString(a);
	}
	
	/**
	 * 2D matrix to String
	 * @param a
	 * @param marker
	 * @return
	 */
	public static String toString(float[][] a, String marker)
	{
		String str="[";
		int i =0;
		for(i=0; i<a.length-1; i++)
			str += Arrays.toString(a[i])+ marker;
		
		str += Arrays.toString(a[i]);
		return str+"]";
	}
	public static String toString(Double[][] a, String marker)
	{
		String str="[";
		int i =0;
		for(i=0; i<a.length-1; i++)
			str += Arrays.toString(a[i])+ marker;

		str += Arrays.toString(a[i]);
		return str+"]";
	}

	/**
	 * retrun 2D array
	 * @param a
	 * @return
	 */
	public static JSONArray toJsonArray(Double[][] a)
	{
		JSONArray array = new JSONArray();

		for(int i=0; i<a.length; i++)
			array.add(toJsonArray(a[i]));

		return array;
	}

	/**
	 * return json array converted from data array
	 * @param a
	 * @return
	 */
	public static JSONArray toJsonArray(Double[] a)
	{
		JSONArray array1 = new JSONArray();
		for (Double item : a){
			array1.add(item);
		}
		return array1;
	}

	/**
	 * return json array converted from data array
	 * @param a
	 * @return
	 */
	public static JSONArray toJsonArray(float[] a)
	{
		JSONArray array1 = new JSONArray();
		for (float item : a){
			array1.add(item);
		}
		return array1;
	}
	public static String toString(String[][] a, String marker)
	{
		String str="[";
		int i =0;
		for(i=0; i<a.length-1; i++)
			str += Arrays.toString(a[i])+ marker;
		
		str += Arrays.toString(a[i]);
		return str+"]";
	}
	/**
	 * Print all the bluetooth_message
	 * @param str
	 */
	public static void Log(String message)
	{
		System.out.println(message);
	}
}
