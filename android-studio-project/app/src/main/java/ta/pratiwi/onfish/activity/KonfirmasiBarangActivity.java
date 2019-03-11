package ta.pratiwi.onfish.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;

public class KonfirmasiBarangActivity extends AppCompatActivity {

    TextView txtId_transaksi;
    Button btnSudah, btnBelum;
    private ProgressDialog pDialog;

    public String SERVER = Config.URL+"update_status_transaksi.php";
    private static final String TAG = KonfirmasiBarangActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konfirmasi_barang);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Konfirmasi Barang");

        final String id_transaksi = getIntent().getStringExtra("key_id_transaksi");
        txtId_transaksi = (TextView) findViewById(R.id.txt_id_transaksi);
        txtId_transaksi.setText("INV/"+id_transaksi);

        btnSudah = (Button) findViewById(R.id.btn_sudah);
        btnSudah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updateStatus(id_transaksi, "D").execute();
            }
        });

        btnBelum = (Button) findViewById(R.id.btn_belum);
        btnBelum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new updateStatus(id_transaksi, "X").execute();
            }
        });
    }

    private class updateStatus extends AsyncTask<Void,Void,String> {
        private String id_transaksi;
        private String status;

        public updateStatus(String id_transaksi, String status){
            this.id_transaksi = id_transaksi;
            this.status = status;
        }

        private String scs = "";
        private String psn = "";
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(KonfirmasiBarangActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                //generate hashMap to store encodedImage and the name
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_transaksi", id_transaksi);
                detail.put("jenis", status);

                try{
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(SERVER,dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getString("success");
                    psn = ob.getString("message");

                }catch (JSONException e){
                    e.printStackTrace();
                    Log.e(TAG, "ERROR  " + e);
                    Toast.makeText(getApplicationContext(),"Maaf, terjadi kesalahan",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), psn,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(KonfirmasiBarangActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);

                //terus tutup activity ini
                finish();
            }
            if(scs.contains("0")){
                Toast.makeText(getApplicationContext(), psn,Toast.LENGTH_SHORT).show();
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
