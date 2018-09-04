package ta.pratiwi.onfish.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.adapter.DaganganPenjualAdapter;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;
import ta.pratiwi.onfish.app.SessionManager;
import ta.pratiwi.onfish.model.Dagangan;

public class MainPenjualActivity extends AppCompatActivity {

    private RecyclerView rc;

    private String url = Config.URL+"dagangan_penjual.php";

    TextView txtNotif;

    private ProgressDialog pDialog;
    private DaganganPenjualAdapter adapter;
    private List<Dagangan> items;
    private SessionManager session;

    boolean log_in;

    public static final int MY_PERMISSIONS_REQUEST = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_penjual);

        getSupportActionBar().setTitle("List Produk Anda");

        //cek permission di android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        //kalau belum login
        if(!session.isLoggedIn()){
            log_in = false;
            finish();
        }
        //kalau sudah login
        else {
            log_in = true;
            //ambil data user
            HashMap<String, String> user = session.getUserDetails(); //bukan pelanggan, tapi user sbnarnya ni
            final String id_pelanggan = user.get(SessionManager.KEY_ID_PELANGGAN);
            String nm_pelanggan = user.get(SessionManager.KEY_NM_PELANGGAN);
            String email_pelanggan = user.get(SessionManager.KEY_MAIL_PELANGGAN);
            String nohp_pelanggan = user.get(SessionManager.KEY_NOHP_PELANGGAN);
            String alamat_pelanggan = user.get(SessionManager.KEY_ALAMAT_PELANGGAN);

            txtNotif = (TextView) findViewById(R.id.txt_notif);

            rc = (RecyclerView) findViewById(R.id.recycler_view);

            items = new ArrayList<>();

            ////new getProduk(id_umkm).execute();

            adapter = new DaganganPenjualAdapter(MainPenjualActivity.this, items, new DaganganPenjualAdapter.AdapterListener() {
                @Override
                public void onSelected(
                        int position,
                        String id_dagangan,
                        String id_jenis_ikan,
                        String jenis_ikan,
                        String url_gambar,
                        String berat_tersedia,
                        String harga,
                        String desk
                ) {
                    Intent intent = new Intent(MainPenjualActivity.this, DaganganPenjualDetailActivity.class);
                    intent.putExtra("key_id_penjual", id_pelanggan);
                    intent.putExtra("key_id_dagangan", id_dagangan);
                    intent.putExtra("key_url_foto", url_gambar);
                    intent.putExtra("key_id_jenis_ikan", id_jenis_ikan);
                    intent.putExtra("key_jenis_ikan", jenis_ikan);
                    intent.putExtra("key_berat", berat_tersedia);
                    intent.putExtra("key_harga", harga);
                    intent.putExtra("key_desk", desk);
                    startActivity(intent);
                }
            });

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainPenjualActivity.this);
            rc.setLayoutManager(mLayoutManager);
            rc.setAdapter(adapter);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainPenjualActivity.this, TambahDaganganActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    private class getProduk extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String id_penjual;

        public getProduk(String id_penjual){
            this.id_penjual = id_penjual;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainPenjualActivity.this);
            pDialog.setMessage("Memuat data...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_penjual", id_penjual);

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url,dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        JSONArray products = ob.getJSONArray("daftar");

                        for (int i = 0; i < products.length(); i++) {
                            JSONObject c = products.getJSONObject(i);

                            // Storing each json item in variable
                            String id_dagangan = c.getString("id_dagangan");
                            String id_petani = c.getString("id_penjual");
                            String nama_penjual = c.getString("nama_penjual");
                            String id_kategori_ikan = c.getString("id_jenis_ikan");
                            String nama_ikan = c.getString("nama_ikan");
                            String berat_kg = c.getString("berat_tersedia");
                            String harga = c.getString("harga_per_kg");
                            String foto = c.getString("foto");
                            String deskripsi = c.getString("deskripsi");
                            String no_hp = c.getString("no_hp");

                            Dagangan p = new Dagangan();
                            p.setId_dagangan(id_dagangan);
                            p.setId_petani(id_petani);
                            p.setNama_penjual(nama_penjual);
                            p.setId_jenis_ikan(id_kategori_ikan);
                            p.setNama_ikan(nama_ikan);
                            p.setBerat_tersedia(berat_kg);
                            p.setHarga_per_kg(harga);
                            p.setLink_foto(foto);
                            p.setDeskripsi(deskripsi);
                            p.setNohp(no_hp);

                            items.add(p);

                        }
                    } else {
                        // no data found

                    }

                } catch (JSONException e){
                    e.printStackTrace();
                }

            } catch (Exception e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            adapter.notifyDataSetChanged();
            pDialog.dismiss();
            if (scs == 1) {
                txtNotif.setVisibility(View.GONE);
            } else {
                txtNotif.setVisibility(View.VISIBLE);
            }
        }

    }

    private String hashMapToUrl(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_penjual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            session.logoutUser();
            Intent intent = new Intent(MainPenjualActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);

            //terus tutup activity ini
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean checkPermission(){
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST);
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
            case MY_PERMISSIONS_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
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

    @Override
    protected void onResume() {
        super.onResume();
        HashMap<String, String> user = session.getUserDetails(); //bukan pelanggan, tapi user sbnarnya ni
        String id_pelanggan = user.get(SessionManager.KEY_ID_PELANGGAN);

        items.clear();
        new getProduk(id_pelanggan).execute();
    }
}
