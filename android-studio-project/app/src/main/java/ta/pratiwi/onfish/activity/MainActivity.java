package ta.pratiwi.onfish.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.app.SessionManagerUser;
import ta.pratiwi.onfish.fragments.HomeFragment;
import ta.pratiwi.onfish.fragments.PembayaranFragment;
import ta.pratiwi.onfish.fragments.SekitaranFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    SessionManagerUser session;

    boolean log_in;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //cek permission di android M
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //masuk ke keranjang belanja
                Intent intent = new Intent(MainActivity.this, KeranjangActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        TextView name = (TextView)header.findViewById(R.id.nama_user);
        TextView email = (TextView)header.findViewById(R.id.email_user);
        TextView nomor_hp = (TextView)header.findViewById(R.id.nohp_user);
        TextView alamat = (TextView)header.findViewById(R.id.alamat_user);

        ImageView plg = (ImageView)header.findViewById(R.id.imageView);

        session = new SessionManagerUser(getApplicationContext());
        session.checkLogin();

        //set fragmentnya
        setFragment(HomeFragment.class);

        //kalau belum login
        if(!session.isLoggedIn()){
            log_in = false;
            String nm_pelanggan = "Anda belum login";
            name.setText(nm_pelanggan);
        }
        //kalau sudah login
        else{
            log_in = true;
            //ambil data user
            HashMap<String, String> user = session.getUserDetails();
            String id_pelanggan = user.get(SessionManagerUser.KEY_ID_PELANGGAN);
            final String nm_pelanggan = user.get(SessionManagerUser.KEY_NM_PELANGGAN);
            String email_pelanggan = user.get(SessionManagerUser.KEY_MAIL_PELANGGAN);
            String nohp_pelanggan = user.get(SessionManagerUser.KEY_NOHP_PELANGGAN);
            String alamat_pelanggan = user.get(SessionManagerUser.KEY_ALAMAT_PELANGGAN);
            String jenis_login = user.get(SessionManagerUser.KEY_JENIS_LOGIN);

            if(jenis_login.equals("penjual")){
                finish();
                //masuk ke halaman utama penjual
                Intent intent = new Intent(MainActivity.this, MainPenjualActivity.class);
                startActivity(intent);
            }

            name.setText(nm_pelanggan);
            email.setText(email_pelanggan);
            nomor_hp.setText(nohp_pelanggan);
            alamat.setText(alamat_pelanggan);

            plg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(MainActivity.this, ""+nm_pelanggan, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void setFragment(Class<? extends Fragment> fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.main_fragment_container, fragment.newInstance());
            fragmentTransaction.commit();
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false);

        /////
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
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

        MenuItem menu_login = menu.findItem(R.id.action_login);
        MenuItem menu_logout = menu.findItem(R.id.action_logout);

        if(log_in)
        {
            menu_login.setVisible(false);
            menu_logout.setVisible(true);
        }else{
            menu_login.setVisible(true);
            menu_logout.setVisible(false);
        }
        return true;
    }
    */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        /*//noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }*/
        if (id == R.id.action_login) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        if (id == R.id.action_logout) {
            session.logoutUser();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

            //terus tutup activity ini
            finish();
            return true;
        }
        if (id == R.id.action_exit) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
            setFragment(HomeFragment.class);
        } else if (id == R.id.nav_payment) {
            getSupportActionBar().setTitle("Pembayaran");
            setFragment(PembayaranFragment.class);
        } else if (id == R.id.nav_map) {
            getSupportActionBar().setTitle("Cari sekitar");
            setFragment(SekitaranFragment.class);
        } else if (id == R.id.nav_about) {
            tentangApp();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void tentangApp() {
        //panggil layout
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_about_app);
        dialog.setTitle("Tentang");
        dialog.show();
    }

    public boolean checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        /*if (googleApiClient == null) {
                            buildGoogleApiClient();
                        }
                        mMap.setMyLocationEnabled(true);*/
                    }

                } else {

                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other permissions this app might request.
            // You can add here other case statements according to your requirement.
        }
    }

}
