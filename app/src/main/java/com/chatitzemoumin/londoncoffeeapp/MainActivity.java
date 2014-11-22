package com.chatitzemoumin.londoncoffeeapp;


import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.chatitzemoumin.londoncoffeeapp.fragments.NavigationDrawerFragment;
import com.chatitzemoumin.londoncoffeeapp.slidinguppanel.SlidingUpPanelLayout;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends ActionBarActivity implements
		NavigationDrawerFragment.NavigationDrawerCallbacks, SlidingUpPanelLayout.PanelSlideListener {

	// ------------------------------------------------- Navigation Drawer -------------------------------------------------
	/**
	 * Fragment managing the behaviors, interactions and presentation of the
	 * navigation drawer.
	 */
	private NavigationDrawerFragment mNavigationDrawerFragment;

	/**
	 * Used to store the last screen title. For use in
	 * {@link #restoreActionBar()}.
	 */
	private CharSequence mTitle;

	// ------------------------------------------------- Sliding Up Panel -------------------------------------------------
	
	private ListView mListView;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;

    private View mTransparentHeaderView;
    private View mTransparentView;
    private View mSpaceView;

    private MapFragment mMapFragment;

    private GoogleMap mMap;
    
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// ------------------------------------------------- Navigation Drawer -------------------------------------------------
		mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager()
				.findFragmentById(R.id.navigation_drawer);
		mTitle = getTitle();

		// Set up the drawer.
		mNavigationDrawerFragment.setUp(R.id.navigation_drawer,
				(DrawerLayout) findViewById(R.id.drawer_layout));
		
		// ------------------------------------------------- Sliding Up Panel -------------------------------------------------
		mListView = (ListView) findViewById(R.id.list);
	    mListView.setOverScrollMode(ListView.OVER_SCROLL_NEVER);

	    mSlidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.slidingLayout);
	    mSlidingUpPanelLayout.setEnableDragViewTouchEvents(true);

	    int mapHeight = getResources().getDimensionPixelSize(R.dimen.map_height);
	    mSlidingUpPanelLayout.setPanelHeight(mapHeight); // you can use different height here
	    mSlidingUpPanelLayout.setScrollableView(mListView, mapHeight);

	    mSlidingUpPanelLayout.setPanelSlideListener(this);

	    // transparent view at the top of ListView
	    mTransparentView = findViewById(R.id.transparentView);

	    // init header view for ListView
	    mTransparentHeaderView = LayoutInflater.from(this).inflate(R.layout.transparent_header_view, null, false);
	    mSpaceView = mTransparentHeaderView.findViewById(R.id.space);

	    ArrayList<String> testData = new ArrayList<String>(100);
	    for (int i = 0; i < 100; i++) {
	         testData.add("Item " + i);
	    }
	    mListView.addHeaderView(mTransparentHeaderView);
	    mListView.setAdapter(new ArrayAdapter<String>(this, R.layout.simple_list_item, testData));
	    mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	        @Override
	        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	            mSlidingUpPanelLayout.collapsePane();
	        }
	    });
	    collapseMap();

	     mMapFragment = MapFragment.newInstance();
	     FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
	     fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");
	     fragmentTransaction.commit();

	     setUpMapIfNeeded();
	     
	     
	}

    
	// ------------------------------------------------- Navigation Drawer -------------------------------------------------
	@Override
	public void onNavigationDrawerItemSelected(int position) {
		// update the main content by replacing fragments
		/*
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,
						PlaceholderFragment.newInstance(position + 1)).commit();
		*/
		switch(position){
			case 0:
				
				break;
			case 1:
				
				break;
				
			default:
				break;
		}
		/*
		FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager
				.beginTransaction()
				.replace(R.id.container,new SlidingUpPanelFragment()).commit();
		*/
	}

	public void onSectionAttached(int number) {
		switch (number) {
		case 1:
			mTitle = getString(R.string.title_section1);
			break;
		case 2:
			mTitle = getString(R.string.title_section2);
			break;
		case 3:
			mTitle = getString(R.string.title_section3);
			break;
		case 4:
			mTitle = getString(R.string.title_section4);
			break;
		case 5:
			mTitle = getString(R.string.title_section5);
			break;
		}
	}

	public void restoreActionBar() {
		ActionBar actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setTitle(mTitle);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		if (!mNavigationDrawerFragment.isDrawerOpen()) {
			// Only show items in the action bar relevant to this screen
			// if the drawer is not showing. Otherwise, let the drawer
			// decide what to show in the action bar.
			getMenuInflater().inflate(R.menu.main, menu);
			restoreActionBar();
			return true;
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		private static final String ARG_SECTION_NUMBER = "section_number";

		/**
		 * Returns a new instance of this fragment for the given section number.
		 */
		public static PlaceholderFragment newInstance(int sectionNumber) {
			PlaceholderFragment fragment = new PlaceholderFragment();
			Bundle args = new Bundle();
			args.putInt(ARG_SECTION_NUMBER, sectionNumber);
			fragment.setArguments(args);
			return fragment;
		}

		public PlaceholderFragment() {
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

	// ------------------------------------------------- Sliding Up Panel -------------------------------------------------

	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = mMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setCompassEnabled(false);
                mMap.getUiSettings().setZoomControlsEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                CameraUpdate update = getLastKnownLocation();
                if (update != null) {
                    mMap.moveCamera(update);
                }
            }
        }
    }
	
    @Override
    protected void onResume() {
        super.onResume();
        // In case Google Play services has since become available.
        setUpMapIfNeeded();
    }

    private CameraUpdate getLastKnownLocation() {
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_LOW);
        String provider = lm.getBestProvider(criteria, true);
        if (provider == null) {
            return null;
        }
        Location loc = lm.getLastKnownLocation(provider);
        if (loc != null) {
            return CameraUpdateFactory.newCameraPosition(CameraPosition.fromLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), 14.0f));
        }
        return null;
    }

    private void collapseMap() {
        mSpaceView.setVisibility(View.VISIBLE);
        mTransparentView.setVisibility(View.GONE);
    }

    private void expandMap() {
        mSpaceView.setVisibility(View.GONE);
        mTransparentView.setVisibility(View.INVISIBLE);
    }

	//-------------- SlidingUpPanelLayout.PanelSlideListener -------------------------
	
	@Override
	public void onPanelSlide(View panel, float slideOffset) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPanelCollapsed(View panel) {
		expandMap();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(14f), 1000, null);
		
	}

	@Override
	public void onPanelExpanded(View panel) {
		collapseMap();
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11f), 1000, null);
		
	}

	@Override
	public void onPanelAnchored(View panel) {
		// TODO Auto-generated method stub
		
	}

}
