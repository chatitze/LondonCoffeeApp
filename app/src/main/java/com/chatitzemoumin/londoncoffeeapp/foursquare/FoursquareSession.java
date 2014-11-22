package com.chatitzemoumin.londoncoffeeapp.foursquare;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.Context;

/**
 * Manage access token and user name. Uses shared preferences to store access token
 * and user name.
 * 
 * @author Lorensius W. L T <lorenz@londatiga.net>
 *
 */
public class FoursquareSession {
	private SharedPreferences sharedPref;
	private Editor editor;
	
	private static final String SHARED = "Foursquare_Preferences";
	private static final String FSQ_USERNAME = "username";
	private static final String FSQ_ACCESS_TOKEN = "access_token";
	private static final String FSQ_USER_PROFILE_PIC_URL = "userprofilepicurl";
	
	public FoursquareSession(Context context) {
		sharedPref 	  = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
		
		editor 		  = sharedPref.edit();
	}
	
	/**
	 * Save access token, user name and user profile picture URL
	 * 
	 * @param accessToken Access token
	 * @param username User name
	 * @param userprofilepicurl User profile picture URL
	 */
	public void storeAccessToken(String accessToken, String username, String userprofilepicurl) {
		editor.putString(FSQ_ACCESS_TOKEN, accessToken);
		editor.putString(FSQ_USERNAME, username);
		editor.putString(FSQ_USER_PROFILE_PIC_URL, userprofilepicurl);
		
		editor.commit();
	}
	
	/**
	 * Reset access token and user name
	 */
	public void resetAccessToken() {
		editor.putString(FSQ_ACCESS_TOKEN, null);
		editor.putString(FSQ_USERNAME, null);
		editor.putString(FSQ_USER_PROFILE_PIC_URL, null);
		
		editor.commit();
	}
	
	/**
	 * Get user name
	 * 
	 * @return User name
	 */
	public String getUsername() {
		return sharedPref.getString(FSQ_USERNAME, null);
	}
	
	/**
	 * Get access token
	 * 
	 * @return Access token
	 */
	public String getAccessToken() {
		return sharedPref.getString(FSQ_ACCESS_TOKEN, null);
	}
	
	/**
	 * Get user profile picture URL
	 * 
	 * @return User profile picture URL
	 */
	public String getUserProfilePicURL() {
		return sharedPref.getString(FSQ_USER_PROFILE_PIC_URL, null);
	}
}