package com.rest.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;


public class Coordinates {

	// everything in degrees
	private String lat;
	private String lon;
	double decimalLat=0;
	double decimalLon=0;
	public Coordinates(String lat, String lon) {
		this.lat = lat;
		this.lon = lon;
	}
	public Coordinates() {
		
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public void convertToDecimal(){
		decimalLat =Float.parseFloat(lat.substring(0, lat.length()-1));
		decimalLon =Float.parseFloat(lon.substring(0, lon.length()-1));
		if(lat.charAt(lat.length()-1) == 'S'){
			decimalLat= -1*decimalLat;
		}
		if(lon.charAt(lon.length()-1) == 'W'){
			decimalLon= -1*decimalLon;
		}
	}
	
	private static double deg2rad(double deg) {
		return (deg * Math.PI / 180.0);
	}
	
	private static double rad2deg(double rad) {
		return (rad * 180 / Math.PI);
	}
	
	// returns distance in the desired unit default is miles
	public double findDist(Coordinates venuePoint, String unit) {
		
		convertToDecimal();
		venuePoint.convertToDecimal();
		double theta = this.decimalLon-venuePoint.decimalLon;
		
		double dist = Math.sin(deg2rad(this.decimalLat)) * Math.sin(deg2rad(venuePoint.decimalLat)) + Math.cos(deg2rad(this.decimalLat)) * Math.cos(deg2rad(venuePoint.decimalLat)) * Math.cos(deg2rad(theta));
		dist = Math.acos(dist);
		dist = rad2deg(dist);
		dist = dist * 60 * 1.1515;
		if (unit == "K") {
			dist = dist * 1.609344;
		} else if (unit == "N") {
			dist = dist * 0.8684;
		}
		return dist;
	}
	
	public double findDistUsingGoogle(Coordinates venuePoint, String unit) {
		convertToDecimal();
		venuePoint.convertToDecimal();
		
		
		double dis=0.0;
		String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&";
		try {
			String originLat= this.decimalLat+"";
			String originLon= this.decimalLon+"";
			String destinationLat= venuePoint.decimalLat+"";
			String destinationLon= venuePoint.decimalLon+"";
			url=url+"origins=" + originLat+"," +originLon+"&";
			url=url+"destinations=" +  destinationLat+"," +destinationLon + "&";
			url=url+"key=" + "AIzaSyDC8WPm48gtQi9A07ifJzvlV5rdmHGEqXI";
			URL obj = new URL(url);
			HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			BufferedReader in = new BufferedReader(
			        new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			JSONObject jsonObj = new JSONObject(response.toString());
			JSONArray rows = (JSONArray)jsonObj.get("rows");
			Iterator rowsIt = rows.iterator();
			JSONObject rowChild = (JSONObject)rowsIt.next();
			JSONArray elements = (JSONArray)rowChild.get("elements");
			Iterator elementsIt = elements.iterator();
			JSONObject elementChild = (JSONObject)elementsIt.next();
			JSONObject distance = elementChild.getJSONObject("distance");
			String disStringInKms = distance.getString("text"); 
			disStringInKms = disStringInKms.substring(0,disStringInKms.length()-3);
			dis = Double.parseDouble(disStringInKms);
			return dis;
			
		} catch (Exception e) {
			System.out.println(dis);
		}
		return dis;
	}
	
	
}
