package ta.pratiwi.onfish.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;

public class DaganganPenjualDetailActivity extends AppCompatActivity {

    private TextView txtJenis, txtBerat, txtHarga, txtDesk;
    private Button btnHapus, btnEdit;
    private ImageView imgDagangan;
    private ProgressDialog pDialog;

    private static String url = Config.URL+"dagangan_hapus.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagangan_penjual_detail);

        getSupportActionBar().setTitle("Rincian Dagangan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final String id_penjual = getIntent().getStringExtra("key_id_penjual");
        final String id_dagangan = getIntent().getStringExtra("key_id_dagangan");
        final String id_jenis_ikan = getIntent().getStringExtra("key_id_jenis_ikan");
        final String jenis_ikan = getIntent().getStringExtra("key_jenis_ikan");
        final String url_foto = getIntent().getStringExtra("key_url_foto");
        final String berat = getIntent().getStringExtra("key_berat");
        final String harga = getIntent().getStringExtra("key_harga");
        final String desk = getIntent().getStringExtra("key_desk");

        imgDagangan = (ImageView) findViewById(R.id.img_dagangan);
        txtJenis = (TextView) findViewById(R.id.txt_jenis);
        txtBerat = (TextView) findViewById(R.id.txt_berat);
        txtHarga = (TextView) findViewById(R.id.txt_harga);
        txtDesk = (TextView) findViewById(R.id.txt_desk);

        btnHapus = (Button) findViewById(R.id.btn_hapus);
        btnEdit = (Button) findViewById(R.id.btn_edit);

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DaganganPenjualDetailActivity.this);

                builder.setTitle("Konfirmasi");
                builder.setMessage("Yakin ingin menghapus data?");

                builder.setPositiveButton("YA", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        new prosesHapus(id_dagangan).execute();
                    }
                });

                builder.setNegativeButton("TIDAK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DaganganPenjualDetailActivity.this, EditDaganganActivity.class);
                intent.putExtra("key_id_penjual", id_penjual);
                intent.putExtra("key_id_dagangan", id_dagangan);
                intent.putExtra("key_url_foto", url_foto);
                intent.putExtra("key_id_jenis_ikan", id_jenis_ikan);
                intent.putExtra("key_jenis_ikan", jenis_ikan);
                intent.putExtra("key_berat", berat);
                intent.putExtra("key_harga", harga);
                intent.putExtra("key_desk", desk);
                startActivity(intent);

                finish();
            }
        });

        Picasso.with(DaganganPenjualDetailActivity.this).load(url_foto).into(imgDagangan);
        txtJenis.setText(jenis_ikan);
        txtBerat.setText(berat+" Kg");
        txtHarga.setText("Rp. "+harga);
        txtDesk.setText(desk);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id_menu = item.getItemId();
        if(id_menu == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private class prosesHapus extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String psn;
        private String id_dagangan;

        public prosesHapus(String id_dagangan){
            this.id_dagangan = id_dagangan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DaganganPenjualDetailActivity.this);
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {

            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_dagangan", id_dagangan);

                try {
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(url, dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getInt("success");

                    if (scs == 1) {
                        psn = ob.getString("message");
                    } else {
                        // no data found
                        psn = ob.getString("message");
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
            pDialog.dismiss();
            if(scs == 1){
                Toast.makeText(DaganganPenjualDetailActivity.this, psn, Toast.LENGTH_SHORT).show();
                finish();
            }
            else{
                Toast.makeText(DaganganPenjualDetailActivity.this, psn, Toast.LENGTH_SHORT).show();
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

}
