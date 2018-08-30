package ta.pratiwi.onfish.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

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
import ta.pratiwi.onfish.adapter.DaganganAdapter;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;
import ta.pratiwi.onfish.app.SessionManager;
import ta.pratiwi.onfish.model.Dagangan;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView rc;
    private DaganganAdapter adapter;
    private List<Dagangan> itemList;
    private ProgressDialog pDialog;

    public String SERVER = Config.URL+"pencarian.php";
    public String SERVER_POST = Config.URL+"tambah_ke_keranjang.php";
    public String SERVER_POST_SET = Config.URL+"set_ketersediaan.php";

    SessionManager session;

    private static final String TAG = SearchActivity.class.getSimpleName();

    ImageView img;
    TextView txt_desk;
    EditText text_jum_kg;
    TextView txt_harga_total, txt_jum_tersedia;
    Button btn_tambah_ke_keranjang;
    ImageButton btn_telp, btn_direk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        String q_pencarian = getIntent().getStringExtra("key_pencarian");

        getSupportActionBar().setTitle("Cari '"+q_pencarian+"'");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ///
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        ///

        rc = (RecyclerView) findViewById(R.id.recycler_view);

        itemList = new ArrayList<>();
        adapter = new DaganganAdapter(getApplicationContext(), itemList, new DaganganAdapter.CardAdapterListener() {
            @Override
            public void onButtonSelected(
                    int position,
                    String nama_penjual,
                    TextView id_dagangan,
                    TextView nama_ikan,
                    TextView nama_petani,
                    int harga_per_kg,
                    String img_url,
                    String deskripsi,
                    String kontak,
                    String sisa_kg,
                    String lat,
                    String lon
            ) {
                //kalau belum login
                if(!session.isLoggedIn()){
                    Toast.makeText(SearchActivity.this, "Anda harus login untuk dapat melakukan transaksi!", Toast.LENGTH_SHORT).show();
                }
                //kalau sudah login
                else{
                    HashMap<String, String> user = session.getUserDetails();
                    //input data ke keranjang
                    String id_pelanggan = user.get(SessionManager.KEY_ID_PELANGGAN);
                    String id_dgn = id_dagangan.getText().toString();

                    //munculkan dialog box, mau beli berapa Kg
                    formBerapaKg(id_dgn, nama_penjual, id_pelanggan, harga_per_kg, img_url, deskripsi, kontak, sisa_kg, lat, lon);
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(mLayoutManager);
        rc.setItemAnimator(new DefaultItemAnimator());
        rc.setAdapter(adapter);

        new getData(q_pencarian).execute();
    }

    int jum_kg;
    int harga_total;

    private void formBerapaKg(
            final String id_dagangan,
            final String nama_penjual,
            final String id_pelanggan,
            final int harga_per_kg,
            String img_url,
            String deskripsi,
            final String kontak,
            final String sisa_kg,
            final String lat,
            final String lon
    ){
        //panggil layout
        final Dialog dialog = new Dialog(SearchActivity.this);
        dialog.setContentView(R.layout.dialog_req_jum_kg);
        dialog.setTitle("Jumlah Pesanan");

        //set komponen layout
        img = (ImageView) dialog.findViewById(R.id.img_ikan);
        txt_desk = (TextView) dialog.findViewById(R.id.txt_desk);
        text_jum_kg = (EditText) dialog.findViewById(R.id.edit_jum_kg);
        txt_harga_total = (TextView) dialog.findViewById(R.id.txt_harga_total);
        btn_tambah_ke_keranjang = (Button) dialog.findViewById(R.id.btn_tambah_ke_keranjang);
        btn_telp = (ImageButton) dialog.findViewById(R.id.btn_telp);
        btn_direk = (ImageButton) dialog.findViewById(R.id.btn_direk);
        txt_jum_tersedia = (TextView) dialog.findViewById(R.id.txt_jum_tersedia);

        //default
        Picasso.with(SearchActivity.this).load(img_url).into(img);
        txt_desk.setText(deskripsi);
        text_jum_kg.setText("1");
        btn_telp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ini munculkan dialog box keranjang belanja
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
                builder.setTitle("Hubungi penyedia");
                builder.setMessage("WhatsApp atau telpon?");
                builder.setPositiveButton("WA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openWhatsappContact(kontak);
                    }
                });

                builder.setNegativeButton("Telpon", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+kontak));
                        startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.setCanceledOnTouchOutside(true);
                alert.show();
            }
        });

        btn_direk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchActivity.this, DirectionMap.class);
                intent.putExtra("key_nama_tujuan", nama_penjual);
                intent.putExtra("key_lat_tujuan", lat);
                intent.putExtra("key_long_tujuan", lon);
                startActivity(intent);
            }
        });

        jum_kg = Integer.valueOf(text_jum_kg.getText().toString());
        harga_total = jum_kg * harga_per_kg;
        txt_harga_total.setText("Rp. "+harga_total);


        text_jum_kg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0 || text_jum_kg.getText().length() <= Integer.valueOf(sisa_kg) ){

                    jum_kg = Integer.valueOf(charSequence.toString());
                    harga_total = jum_kg * harga_per_kg;
                    txt_harga_total.setText("Rp. "+harga_total);
                }
                else{
                    Toast.makeText(SearchActivity.this, "Minimal pesanan adalah 1 Kg dan tidak melebihi dari batas ketersediaan", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txt_jum_tersedia.setText(sisa_kg+" Kg");

        btn_tambah_ke_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_jum_kg.getText().length() <= 0){
                    Toast.makeText(SearchActivity.this, "Minimal pesanan adalah 1 Kg", Toast.LENGTH_SHORT).show();
                    text_jum_kg.setText("1");

                }
                else{
                    //masukkan ke keranjang
                    String id_dgn = id_dagangan;
                    String id_plg = id_pelanggan;
                    String jml_kg = String.valueOf(jum_kg);
                    String hrg_ttl = String.valueOf(harga_total);

                    //jumlah ketersediaan berkurang
                    int sisa = Integer.valueOf(sisa_kg) - jum_kg;
                    new setJumKetersediaan(id_dgn, String.valueOf(sisa)).execute();

                    new postData(id_dgn, id_plg, jml_kg, hrg_ttl).execute();
                    dialog.dismiss();
                }
            }
        });


        dialog.show();

    }

    public void openWhatsappContact(String number) {
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(i);
    }

    private class getData extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String q_pencarian;

        public getData(String q_pencarian){
            this.q_pencarian = q_pencarian;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {
            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("q_pencarian", q_pencarian);

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to saveImage.php file
                    String response = Request.post(SERVER,dataToSend);

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
                            String harga = c.getString("harga_per_kg");
                            String berat_kg = c.getString("berat_tersedia");
                            String foto = c.getString("foto");
                            String deskripsi = c.getString("deskripsi");
                            String no_hp = c.getString("no_hp");

                            Dagangan p = new Dagangan();
                            p.setId_dagangan(id_dagangan);
                            p.setId_petani(id_petani);
                            p.setNama_penjual(nama_penjual);
                            p.setId_kategori_ikan(id_kategori_ikan);
                            p.setNama_ikan(nama_ikan);
                            p.setBerat_tersedia(berat_kg);
                            p.setHarga_per_kg(harga);
                            p.setLink_foto(foto);
                            p.setDeskripsi(deskripsi);
                            p.setNohp(no_hp);

                            itemList.add(p);

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

        }

    }


    private class postData extends AsyncTask<Void,Void,String> {
        private String id_dagangan;
        private String id_pelanggan;
        private String jum_kg;
        private String harga_total;

        public postData(String id_dagangan, String id_pelanggan, String jum_kg, String harga_total){
            this.id_dagangan = id_dagangan;
            this.id_pelanggan = id_pelanggan;
            this.jum_kg = jum_kg;
            this.harga_total = harga_total;
        }

        private String scs = "";
        private String psn = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SearchActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //menganbil data-data yang akan dikirim

                //generate hashMap to store encodedImage and the name
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_dagangan", id_dagangan);
                detail.put("id_pelanggan", id_pelanggan);
                detail.put("jum_kg", jum_kg);
                detail.put("harga_total", harga_total);

                try{
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(SERVER_POST,dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getString("success");
                    psn = ob.getString("message");

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.e(TAG, "ERROR  " + e);
                    Toast.makeText(getApplicationContext(),"Maaf, terjadi error!",Toast.LENGTH_SHORT).show();
                    //return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            pDialog.dismiss();

            if(scs.contains("1")){
                dialogBox(psn);
            }
            if(scs.contains("0")){
                Toast.makeText(getApplicationContext(), psn,Toast.LENGTH_SHORT).show();
            }

        }

    }

    private class setJumKetersediaan extends AsyncTask<Void,Void,String> {
        private String id_dagangan;
        private String jum_kg;

        public setJumKetersediaan(String id_dagangan, String jum_kg){
            this.id_dagangan = id_dagangan;
            this.jum_kg = jum_kg;
        }

        private String scs = "";
        private String psn = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*pDialog = new ProgressDialog(DaganganActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //menganbil data-data yang akan dikirim

                //generate hashMap to store encodedImage and the name
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_dagangan", id_dagangan);
                detail.put("jum_kg", jum_kg);

                try{
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(SERVER_POST_SET,dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getString("success");
                    psn = ob.getString("message");

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.e(TAG, "ERROR  " + e);
                    Toast.makeText(getApplicationContext(),"Maaf, terjadi error!",Toast.LENGTH_SHORT).show();
                    //return null;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }



        @Override
        protected void onPostExecute(String s) {
            //pDialog.dismiss();
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

    private void dialogBox(String pesan){
        //ini munculkan dialog box keranjang belanja
        AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
        builder.setTitle("Lihat keranjang anda?");
        builder.setMessage(pesan);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //masuk ke keranjang belanja
                Intent intent = new Intent(SearchActivity.this, KeranjangActivity.class);
                startActivity(intent);
            }
        });

        builder.setNegativeButton("Nanti", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.setCancelable(false);
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}


