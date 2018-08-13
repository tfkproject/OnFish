package ta.pratiwi.onfish.fragments;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.activity.CariSekitarActivity;


/**
 * Created by taufik on 17/04/18.
 */

public class SekitaranFragment extends Fragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;

    private static final String TAG = "Lokasi";

    Location lastLocation;
    Marker currLocationMarker;

    private LocationRequest locationRequest;
    private GoogleApiClient googleApiClient;

    LatLng lokasi_asal;
    LatLng lokasi_tujuan;

    Polyline line;
    String tipe_rute;
    private int PROXIMITY_RADIUS = 10000;

    String[] kategori_pencarian ={
            "ATM",
            "Bank",
            "Rumah makan",
            "Kampus",
            "Sekolah",
            "Taman",
            "Rumah Sakit",
            "SPBU"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_sekitaran, container, false);

        //cek apakah google play services terinstall/ada
        if (!isGooglePlayServicesAvailable()) {
            Toast.makeText(getActivity(), "Maaf, Googla Play Services tidak tersedia diperangkat anda", Toast.LENGTH_LONG).show();
        }
        else {
            Log.d("onCreate", "Google Play Services available. Continuing.");
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        final SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    private synchronized void buildGoogleApiClient() {
        Log.d(TAG, "mulai deteksi lokasi");
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /*String nama_objek = "ATM";//getIntent().getStringExtra("key_nama_tempat_maps");
        String lokasi_lat = "1.663787";//getIntent().getStringExtra("key_lokasi_LAT_maps");
        String lokasi_long = "101.447081";//getIntent().getStringExtra("key_lokasi_LONG_maps");

        double lok_lat = Double.valueOf(lokasi_lat);
        double lok_long= Double.valueOf(lokasi_long);

        // tambahkan marker dan aktifkan pindah kamera
        lokasi_tujuan = new LatLng(lok_lat, lok_long);
        //lokasi_tujuan = new LatLng(1.660664, 101.437487);
        mMap.addMarker(new MarkerOptions().position(lokasi_tujuan).title(nama_objek).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))).showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi_tujuan));*/
        //mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //Initialize Google Play Services
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleApiClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else {
            buildGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


    }

    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getActivity());
        if(result != ConnectionResult.SUCCESS) {
            if(googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        0).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "onConnected");

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000); // milliseconds
        locationRequest.setFastestInterval(1000); // the fastest rate in milliseconds at which your app can handle location updates
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Toast.makeText(getActivity(), "Maaf, koneksi terganggu.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }

        lastLocation = location;

        //ambil lokasi tengah (center map)
        //String lokasi_lat = "1.663787"; //getIntent().getStringExtra("key_lokasi_LAT_maps");
        //String lokasi_long = "101.447081"; //getIntent().getStringExtra("key_lokasi_LONG_maps");

        double lat1, lon1, lat2, lon2;
        lat1 = location.getLatitude(); //Double.valueOf(lokasi_lat);
        lon1 = location.getLongitude(); //Double.valueOf(lokasi_long);

        lat2 = location.getLatitude();
        lon2 = location.getLongitude();

        lokasi_asal = new LatLng(lat2, lon2);

        double dLon = Math.toRadians(lon2 - lon1);

        //convert to radians
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);

        double Bx = Math.cos(lat2) * Math.cos(dLon);
        double By = Math.cos(lat2) * Math.sin(dLon);
        double lat3 = Math.atan2(Math.sin(lat1) + Math.sin(lat2), Math.sqrt((Math.cos(lat1) + Bx) * (Math.cos(lat1) + Bx) + By * By));
        double lon3 = lon1 + Math.atan2(By, Math.cos(lat1) + Bx);

        LatLng lokasi_asal_tengah = new LatLng(Math.toDegrees(lat3), Math.toDegrees(lon3));


        //move map camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(lokasi_asal_tengah));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(17));

        //build_retrofit_and_get_response_nearby("restaurant");

        //stop location updates
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (googleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "onConnectionFailed");

        stopLocationUpdates();
    }



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_sekitaran, menu);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(false);

        /////
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(getActivity(), CariSekitarActivity.class);
                intent.putExtra("key_pencarian", query);
                startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                return true;

            }

        });
        /////
    }
}
