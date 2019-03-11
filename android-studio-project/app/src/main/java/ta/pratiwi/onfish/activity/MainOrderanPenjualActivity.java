package ta.pratiwi.onfish.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
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
import ta.pratiwi.onfish.adapter.OrderanAdapter;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;
import ta.pratiwi.onfish.app.SessionManagerUser;
import ta.pratiwi.onfish.model.Dagangan;
import ta.pratiwi.onfish.model.Orderan;

public class MainOrderanPenjualActivity extends AppCompatActivity {

    private RecyclerView rc;

    private String url = Config.URL+"orderan.php";

    TextView txtNotif;

    private ProgressDialog pDialog;
    private OrderanAdapter adapter;
    private List<Orderan> items;
    private SessionManagerUser session;

    boolean log_in;

    public static final int MY_PERMISSIONS_REQUEST = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orderan_penjual);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("List Orderan Anda");

        //cek permission di android M
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermission();
        }

        session = new SessionManagerUser(getApplicationContext());
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
            final String id_pelanggan = user.get(SessionManagerUser.KEY_ID_PELANGGAN);
            String nm_pelanggan = user.get(SessionManagerUser.KEY_NM_PELANGGAN);
            String email_pelanggan = user.get(SessionManagerUser.KEY_MAIL_PELANGGAN);
            String nohp_pelanggan = user.get(SessionManagerUser.KEY_NOHP_PELANGGAN);
            String alamat_pelanggan = user.get(SessionManagerUser.KEY_ALAMAT_PELANGGAN);

            txtNotif = (TextView) findViewById(R.id.txt_notif);

            rc = (RecyclerView) findViewById(R.id.recycler_view);

            items = new ArrayList<>();

            ////new getProduk(id_umkm).execute();

            adapter = new OrderanAdapter(MainOrderanPenjualActivity.this, items, new OrderanAdapter.CardAdapterListener() {
                @Override
                public void onCardSelected(int position, String id_transaksi, String status) {
                    /*if(status.contains("N") || status.contains("W")){
                        //masuk ke konfirmasi pembayaran
                        Intent intent = new Intent(getActivity(), KonfirmasiBayarActivity.class);
                        intent.putExtra("key_id_transaksi", id_transaksi);
                        startActivity(intent);
                    }
                    if(status.contains("S") || status.contains("X")){
                        //masuk ke konfirmasi barang
                        Intent intent = new Intent(getActivity(), KonfirmasiBarangActivity.class);
                        intent.putExtra("key_id_transaksi", id_transaksi);
                        startActivity(intent);
                    }*/
                }

                @Override
                public void onButtonSelected(int position, String id_transaksi) {
                    //buka printed invoice
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.URL+"print/?id_transaksi="+id_transaksi));
                    startActivity(browserIntent);
                }
            });

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainOrderanPenjualActivity.this);
            rc.setLayoutManager(mLayoutManager);
            rc.setAdapter(adapter);

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
            pDialog = new ProgressDialog(MainOrderanPenjualActivity.this);
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
                            String id_transaksi = c.getString("id_transaksi");
                            String id_keranjang = c.getString("id_keranjang");
                            String id_pelanggan = c.getString("id_pelanggan");
                            String nama_pelanggan = c.getString("nama_pelanggan");
                            String nomor_hp = c.getString("nomor_hp");
                            String alamat = c.getString("alamat");
                            String jenis = c.getString("jenis");
                            String status = c.getString("status");

                            Orderan p = new Orderan();
                            p.setId_transaksi(id_transaksi);
                            p.setId_keranjang(id_keranjang);
                            p.setId_pelanggan(id_pelanggan);
                            p.setNama_pelanggan(nama_pelanggan);
                            p.setNomor_hp(nomor_hp);
                            p.setAlamat(alamat);
                            p.setJenis(jenis);
                            p.setStatus(status);

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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_penjual, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
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
        String id_pelanggan = user.get(SessionManagerUser.KEY_ID_PELANGGAN);

        items.clear();
        new getProduk(id_pelanggan).execute();
    }
}
