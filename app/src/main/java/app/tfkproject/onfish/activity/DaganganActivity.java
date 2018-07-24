package app.tfkproject.onfish.activity;

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

import app.tfkproject.onfish.R;
import app.tfkproject.onfish.adapter.DaganganAdapter;
import app.tfkproject.onfish.app.Config;
import app.tfkproject.onfish.app.Request;
import app.tfkproject.onfish.app.SessionManager;
import app.tfkproject.onfish.model.Dagangan;

public class DaganganActivity extends AppCompatActivity {

    private RecyclerView rc;
    private DaganganAdapter adapter;
    private List<Dagangan> itemList;
    private ProgressDialog pDialog;

    public String SERVER = Config.URL+"dagangan.php";
    public String SERVER_POST = Config.URL+"tambah_ke_keranjang.php";

    SessionManager session;

    private static final String TAG = DaganganActivity.class.getSimpleName();

    ImageView img;
    TextView txt_desk;
    EditText text_jum_kg;
    TextView txt_harga_total;
    Button btn_tambah_ke_keranjang;
    ImageButton btn_telp;
    TextView txt_telp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagangan);

        ///
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        ///

        rc = (RecyclerView) findViewById(R.id.recycler_view);

        itemList = new ArrayList<>();
        adapter = new DaganganAdapter(getApplicationContext(), itemList, new DaganganAdapter.CardAdapterListener() {
            @Override
            public void onButtonSelected(int position, TextView id_dagangan, TextView nama_ikan, TextView nama_petani, int harga_per_kg, String img_url, String deskripsi, String kontak) {
                //kalau belum login
                if(!session.isLoggedIn()){
                    Toast.makeText(DaganganActivity.this, "Anda harus login untuk dapat melakukan transaksi!", Toast.LENGTH_SHORT).show();
                }
                //kalau sudah login
                else{
                    HashMap<String, String> user = session.getUserDetails();
                    //input data ke keranjang
                    String id_pelanggan = user.get(SessionManager.KEY_ID_PELANGGAN);
                    String id_dgn = id_dagangan.getText().toString();

                    //munculkan dialog box, mau beli berapa Kg
                    formBerapaKg(id_dgn, id_pelanggan, harga_per_kg, img_url, deskripsi, kontak);
                }
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc.setLayoutManager(mLayoutManager);
        rc.setItemAnimator(new DefaultItemAnimator());
        rc.setAdapter(adapter);

        String id_kat_ikan = getIntent().getStringExtra("id_kat_ikan_key");
        String nama_ikan = getIntent().getStringExtra("nama_ikan_key");

        getSupportActionBar().setTitle("Kategori");
        getSupportActionBar().setSubtitle(nama_ikan);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new getData(id_kat_ikan).execute();
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
            pDialog = new ProgressDialog(DaganganActivity.this);
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

    private void dialogBox(String pesan){
        //ini munculkan dialog box keranjang belanja
        AlertDialog.Builder builder = new AlertDialog.Builder(DaganganActivity.this);
        builder.setTitle("Lihat keranjang anda?");
        builder.setMessage(pesan);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //masuk ke keranjang belanja
                Intent intent = new Intent(DaganganActivity.this, KeranjangActivity.class);
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

    int jum_kg;
    int harga_total;

    private void formBerapaKg(final String id_dagangan, final String id_pelanggan, final int harga_per_kg, String img_url, String deskripsi, final String kontak){
        //panggil layout
        final Dialog dialog = new Dialog(DaganganActivity.this);
        dialog.setContentView(R.layout.dialog_req_jum_kg);
        dialog.setTitle("Jumlah Pesanan");

        //set komponen layout
        img = (ImageView) dialog.findViewById(R.id.img_ikan);
        txt_desk = (TextView) dialog.findViewById(R.id.txt_desk);
        text_jum_kg = (EditText) dialog.findViewById(R.id.edit_jum_kg);
        txt_harga_total = (TextView) dialog.findViewById(R.id.txt_harga_total);
        btn_tambah_ke_keranjang = (Button) dialog.findViewById(R.id.btn_tambah_ke_keranjang);
        btn_telp = (ImageButton) dialog.findViewById(R.id.btn_telp);
        txt_telp = (TextView) dialog.findViewById(R.id.txt_telp);

        //default
        Picasso.with(DaganganActivity.this).load(img_url).into(img);
        txt_desk.setText(deskripsi);
        text_jum_kg.setText("1");
        txt_telp.setText(kontak);
        btn_telp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ini munculkan dialog box keranjang belanja
                AlertDialog.Builder builder = new AlertDialog.Builder(DaganganActivity.this);
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
                alert.setCancelable(false);
                alert.setCanceledOnTouchOutside(false);
                alert.show();
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
                if(charSequence.length() != 0 || text_jum_kg.getText().length() > 0 ){

                    jum_kg = Integer.valueOf(charSequence.toString());
                    harga_total = jum_kg * harga_per_kg;
                    txt_harga_total.setText("Rp. "+harga_total);
                }
                else{
                    Toast.makeText(DaganganActivity.this, "Minimal pesanan adalah 1 Kg", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_tambah_ke_keranjang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(text_jum_kg.getText().length() <= 0){
                    Toast.makeText(DaganganActivity.this, "Minimal pesanan adalah 1 Kg", Toast.LENGTH_SHORT).show();
                    text_jum_kg.setText("1");

                }
                else{
                    //masukkan ke keranjang
                    String id_dgn = id_dagangan;
                    String id_plg = id_pelanggan;
                    String jml_kg = String.valueOf(jum_kg);
                    String hrg_ttl = String.valueOf(harga_total);

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

    /////////
    private class getData extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String id_kat_ikan;

        public getData(String id_kat_ikan){
            this.id_kat_ikan = id_kat_ikan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DaganganActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {
            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_kategori_ikan", id_kat_ikan);

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
                            String id_petani = c.getString("id_petani");
                            String nama_petani = c.getString("nama_petani");
                            String id_kategori_ikan = c.getString("id_kategori_ikan");
                            String nama_ikan = c.getString("nama_ikan");
                            String harga = c.getString("harga_per_kg");
                            String foto = c.getString("foto");
                            String deskripsi = c.getString("deskripsi");
                            String kontak = c.getString("nohp");

                            Dagangan p = new Dagangan();
                            p.setId_dagangan(id_dagangan);
                            p.setId_petani(id_petani);
                            p.setNama_petani(nama_petani);
                            p.setId_kategori_ikan(id_kategori_ikan);
                            p.setNama_ikan(nama_ikan);
                            p.setHarga_per_kg(harga);
                            p.setLink_foto(foto);
                            p.setDeskripsi(deskripsi);
                            p.setNohp(kontak);

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
    /////////

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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


}
