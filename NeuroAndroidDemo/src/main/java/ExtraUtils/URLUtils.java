package ExtraUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.net.MalformedURLException;
import java.util.ArrayList;

public class URLUtils {
	private String mURLString;
	public String protocol;
	public String url;
	public String port;
	public String topic;

	/**
	 * Series the URL from NeuroScal
	 * @param str
	 */
	public URLUtils(String str) {
		this.mURLString = str;
		String[] str1 = this.mURLString.split("://");
		this.protocol = str1[0];
		this.url = (str1[1].split("/"))[0];
		this.topic = "/" + (str1[1].split("/"))[1] + "/"
				+ (str1[1].split("/"))[2];
		this.port = (url.split(":"))[1];
	}

	/**
	 * Series the endpoints from the NeuroScal
	 * @param json
	 * @param model
	 * @return
	 * @throws MalformedURLException
	 */
	public static URLUtils GetEndpoints(JSONObject json, String model)
	{
		JSONObject endpoints = (JSONObject) (json.get("endpoints"));
		JSONArray data = (JSONArray) endpoints.get("data");
		if (data == null)
			return null;
		for (Object e : data) {
			String mode = (String) ((JSONObject) e).get("mode");
			if (mode.equals(model)) {
				String str = (String) ((JSONObject) e).get("url");
				URLUtils url = new URLUtils(str);
				return url;
			}
		}
		return null;
	}

	/**
	 * To retrieve the output node from neuro scale
	 * @param json
	 * @return
	 */
	public static ArrayList<String> GetOutputNodes(JSONObject json)
	{
		ArrayList<String> nodes_str = null;
		try {
			JSONObject data = (JSONObject) (json.get("metadata"));
			JSONObject nodes = (JSONObject)(data.get("nodes"));
			JSONArray out_node = (JSONArray)(nodes.get("out"));
			// to new a arrylist, and add all the nodes to our Configuration
			if (out_node.size() >0 ) {
				nodes_str = new ArrayList<>();
				for (Object node : out_node) {
					String name = (String) ((JSONObject) node).get("name"); // get the node name


					nodes_str.add(name);
				}
			}
		}
		catch (Exception e)
		{
			nodes_str = null;
		}
		return nodes_str;
	}
}
