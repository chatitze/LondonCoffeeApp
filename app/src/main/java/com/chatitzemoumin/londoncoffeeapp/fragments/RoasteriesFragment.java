package com.chatitzemoumin.londoncoffeeapp.fragments;

import com.chatitzemoumin.londoncoffeeapp.MainActivity;
import com.chatitzemoumin.londoncoffeeapp.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RoasteriesFragment extends Fragment {

	private static final String ARG_SECTION_NUMBER = "section_number";
	private static RoasteriesFragment roasteriesInstance;
	
	public static RoasteriesFragment getRoasteriesInstance(){
		if(roasteriesInstance==null){
			roasteriesInstance = new RoasteriesFragment();
		}
		return roasteriesInstance;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		return rootView;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
}
