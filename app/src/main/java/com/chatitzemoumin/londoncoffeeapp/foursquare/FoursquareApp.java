package com.chatitzemoumin.londoncoffeeapp.foursquare;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chatitzemoumin.londoncoffeeapp.foursquare.FoursquareDialog.FsqDialogListener;
import com.chatitzemoumin.londoncoffeeapp.utils.Utils;

/**
 * 
 * @author Lorensius W. L. T <lorenz@londatiga.net>
 *
 */
public class FoursquareApp {
	private FoursquareSession mSession;
	private FoursquareDialog mDialog;
	private FsqAuthListener mListener;
	private ProgressDialog mProgress;
	private String mTokenUrl;
	private String mAccessToken;
	
	private String mUserProfilePhotoUrl;
	private Bitmap mUserProfilePhotoBitmap;
	
	/**
	 * Callback url, as set in 'Manage OAuth Costumers' page (https://developer.foursquare.com/)
	 */
	public static final String CALLBACK_URL = "londoncoffeeapp://connect";
	private static final String AUTH_URL = "https://foursquare.com/oauth2/authenticate?response_type=code";
	private static final String TOKEN_URL = "https://foursquare.com/oauth2/access_token?grant_type=authorization_code";	
	private static final String API_URL = "https://api.foursquare.com/v2";
	
	private static final String TAG = "FoursquareApi";
	

	public static final String CLIENT_ID = "SLGVKW2EF21XTSUBA0JZ5HSYRTMJTTIIFRNS02K3EB4BM2U0";
	public static final String CLIENT_SECRET = "WOQMDNRO2BYQBRD4FEUSCOPF2DYMW0ZBBAFANUOWXNA3I14Z";
	
	public FoursquareApp(Context context) {
		mSession		= new FoursquareSession(context);
		
		mAccessToken	= mSession.getAccessToken();
		
		mTokenUrl		= TOKEN_URL + "&client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET
						+ "&redirect_uri=" + CALLBACK_URL;

		mUserProfilePhotoUrl = mSession.getUserProfilePicURL();
		
		//1
		String url		= AUTH_URL + "&client_id=" + CLIENT_ID + "&redirect_uri=" + CALLBACK_URL;
		
		//url = "fsq+slgvkw2ef21xtsuba0jz5hsyrtmjttiifrns02k3eb4bm2u0+authorize://?fsqCallback=CALLBACK_URL";

		FsqDialogListener listener = new FsqDialogListener() {
			@Override
			public void onComplete(String code) {
				getAccessToken(code);
			}
			
			@Override
			public void onError(String error) {
				mListener.onFail("Authorization failed");
			}
		};
		
		mDialog			= new FoursquareDialog(context, url, listener);
		mProgress		= new ProgressDialog(context);
		
		mProgress.setCancelable(false);
	}
	
	private void getAccessToken(final String code) {
		mProgress.setMessage("Getting access token ...");
		mProgress.show();
		
		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Getting access token");
				
				int what = 0;
				
				try {
					URL url = new URL(mTokenUrl + "&code=" + code);
					
					Log.i(TAG, "Opening URL " + url.toString());
					
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					//urlConnection.setDoOutput(true);
					
					urlConnection.connect();
					
					JSONObject jsonObj  = (JSONObject) new JSONTokener(streamToString(urlConnection.getInputStream())).nextValue();
		        	mAccessToken 		= jsonObj.getString("access_token");
		        	
		        	Log.i(TAG, "Got access token: " + mAccessToken);
				} catch (Exception ex) {
					what = 1;
					
					ex.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
			}
		}.start();
	}
	
	private void fetchUserName() {
		mProgress.setMessage("Finalizing ...");
		
		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user name");
				int what = 0;
		
				try {
					String v	= timeMilisToString(System.currentTimeMillis()); 
					URL url 	= new URL(API_URL + "/users/self?oauth_token=" + mAccessToken + "&v=" + v);
					
					Log.d(TAG, "Opening URL " + url.toString());
					
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					//urlConnection.setDoOutput(true);
					
					urlConnection.connect();
					
					String response		= streamToString(urlConnection.getInputStream());
					JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();
		       
					JSONObject resp		= (JSONObject) jsonObj.get("response");
					JSONObject user		= (JSONObject) resp.get("user");
					JSONObject photo	= (JSONObject) user.get("photo");
					
					String firstName 	= user.getString("firstName");
		        	String lastName		= user.getString("lastName");
		        	mUserProfilePhotoUrl= photo.getString("prefix") + "original" + photo.getString("suffix");
		        	
		        	Log.i(TAG, "Got user name: " + firstName + " " + lastName);
		        	
		        	mSession.storeAccessToken(mAccessToken, firstName + " " + lastName, mUserProfilePhotoUrl);
				} catch (Exception ex) {
					what = 1;
					
					ex.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 2, 0));
			}
		}.start();
	}
	
	public void fetchUserProfilePhoto() {
		
		new Thread() {
			@Override
			public void run() {
				Log.i(TAG, "Fetching user profile photo"); 
				int what = 0;
		
				try {
					URL url 	= new URL(mUserProfilePhotoUrl);
					
					Log.d(TAG, "Opening URL " + url.toString());
					
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					
					urlConnection.setRequestMethod("GET");
					urlConnection.setDoInput(true);
					urlConnection.connect();
					
					InputStream is = urlConnection.getInputStream();
					mUserProfilePhotoBitmap = Utils.getCircularBitmap(BitmapFactory.decodeStream(is));	 
		        	urlConnection.disconnect();
					
		        	
				} catch (Exception ex) {
					what = 1;
					
					ex.printStackTrace();
				}
				
				mHandler.sendMessage(mHandler.obtainMessage(what, 3, 0));
			}
		}.start();
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.arg1 == 1) {
				if (msg.what == 0) {
					fetchUserName();					
				}else {
					mProgress.dismiss();
					
					mListener.onFail("Failed to get access token");
				}
			} else if(msg.arg1 == 2){
				if (msg.what == 0) {
					fetchUserProfilePhoto();				
				}else {
					mProgress.dismiss();
					
					mListener.onFail("Failed to get user name");
				}
				
			}else {
				mProgress.dismiss();
				
				mListener.onSuccess();
			}
		}
	};
	
	public boolean hasAccessToken() {
		return (mAccessToken == null) ? false : true;
	}
	
	public void setListener(FsqAuthListener listener) {
		mListener = listener;
	}
	
	public String getUserName() {
		return mSession.getUsername();
	}
	
	public Bitmap getUserProfilePhoto() {
		return mUserProfilePhotoBitmap;
	}
	
	public void authorize() {
		mDialog.show();
	}
	
	public ArrayList<FsqVenue> getNearby(double latitude, double longitude) throws Exception {
		ArrayList<FsqVenue> venueList = new ArrayList<FsqVenue>();
		
		Log.d(TAG, "Chatitze 1 ");
		
		try {
			String v	= timeMilisToString(System.currentTimeMillis()); 
			String ll 	= String.valueOf(latitude) + "," + String.valueOf(longitude);
			//URL url 	= new URL(API_URL + "/venues/search?ll=" + ll + "&oauth_token=" + mAccessToken + "&v=" + v);
			
			// chatitze 08.2014
			ll="51.5072,0.1275";
			String categoryId="4bf58dd8d48988d1e0931735,4bf58dd8d48988d16d941735";
					
			Log.i("Chatitze: ", ll);

			//URL url 	= new URL(API_URL + "/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=" + v + "&ll=" + ll + "&radius=1000" + "&query=coffee"); 
			URL url 	= new URL(API_URL + "/venues/search?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + "&v=" + v + "&ll=" + ll + "&radius=70000" + "&categoryId="+categoryId); 
				  
			Log.d(TAG, "Opening URL " + url.toString());
			
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			//urlConnection.setDoOutput(true);
			
			urlConnection.connect();
			
			Log.d(TAG, "Chatitze 2 ");
			
			String response		= streamToString(urlConnection.getInputStream());
			JSONObject jsonObj 	= (JSONObject) new JSONTokener(response).nextValue();
			
			Log.d(TAG, "Chatitze 3 RESPONSE: " + response);
			
			//JSONArray groups	= (JSONArray) jsonObj.getJSONObject("response").getJSONArray("groups");
			JSONArray venues	= (JSONArray) jsonObj.getJSONObject("response").getJSONArray("venues");
			//jsonObj.getJSONObject("response").getJSONArray("venues").get(0);
			int length			= venues.length();
			
			Log.d(TAG, "Chatitze 4 Legth: " + length);
			
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					
					//JSONObject group 	= (JSONObject) venues.get(i);
					JSONObject group 	= venues.getJSONObject(i);
					
					FsqVenue venue 	= new FsqVenue();
					
					venue.id 		= group.getString("id");
					venue.name		= group.getString("name");
					
					JSONObject location = (JSONObject) group.getJSONObject("location");
						
					Location loc 	= new Location(LocationManager.GPS_PROVIDER);
						
					loc.setLatitude(Double.valueOf(location.getString("lat")));
					loc.setLongitude(Double.valueOf(location.getString("lng")));
						
					venue.location	= loc;
					
					if(location.has("address")){
						venue.address	= location.getString("address");
					}else{
						venue.address	= "address London UK";
					}
					
					venue.distance	= location.getInt("distance");
					venue.herenow	= group.getJSONObject("hereNow").getInt("count");
					//venue.type		= group.getString("type");
					venue.type		= "trending";
						
					venueList.add(venue);
				
				}
			}
			
		} catch (Exception ex) {
			throw ex;
		}
		
		return venueList;
	}
	
	private String streamToString(InputStream is) throws IOException {
		String str  = "";
		
		if (is != null) {
			StringBuilder sb = new StringBuilder();
			String line;
			
			try {
				BufferedReader reader 	= new BufferedReader(new InputStreamReader(is));
				
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				
				reader.close();
			} finally {
				is.close();
			}
			
			str = sb.toString();
		}
		
		return str;
	}
	
	private String timeMilisToString(long milis) {
		SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
		Calendar calendar   = Calendar.getInstance();
		
		calendar.setTimeInMillis(milis);
		
		return sd.format(calendar.getTime());
	}
	public interface FsqAuthListener {
		public abstract void onSuccess();
		public abstract void onFail(String error);
	}
}