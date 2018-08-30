package ta.pratiwi.onfish.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class TambahDaganganActivity extends AppCompatActivity {

    private RecyclerView rc;
    private DaganganAdapter adapter;
    private List<Dagangan> itemList;
    private ProgressDialog pDialog;

    public String SERVER = Config.URL+"dagangan.php";
    public String SERVER_POST = Config.URL+"tambah_ke_keranjang.php";

    SessionManager session;

    private static final String TAG = TambahDaganganActivity.class.getSimpleName();

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
        setContentView(R.layout.activity_tambah_dagangan);


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
            pDialog = new ProgressDialog(TambahDaganganActivity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(TambahDaganganActivity.this);
        builder.setTitle("Lihat keranjang anda?");
        builder.setMessage(pesan);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //masuk ke keranjang belanja
                Intent intent = new Intent(TambahDaganganActivity.this, KeranjangActivity.class);
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
        private String id_jenis_ikan;

        public getData(String id_jenis_ikan){
            this.id_jenis_ikan = id_jenis_ikan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(TambahDaganganActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {
            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_jenis_ikan", id_jenis_ikan);

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
