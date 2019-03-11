package ta.pratiwi.onfish.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import ta.pratiwi.onfish.app.SessionManagerUser;

public class BayarActivity extends AppCompatActivity {

    SessionManagerUser session;
    private ProgressDialog pDialog;
    public String SERVER_POST_DATA = Config.URL+"create_invoice.php";
    private static final String TAG = BayarActivity.class.getSimpleName();
    TextView txtJumBayar;
    Button btnInvoice;
    private RadioGroup rg;
    private RadioButton rbA, rbB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayar);

        getSupportActionBar().setTitle("Bayar");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtJumBayar = (TextView) findViewById(R.id.txt_jum_bayar);
        btnInvoice = (Button) findViewById(R.id.btn_buat_inv);


        ///
        session = new SessionManagerUser(getApplicationContext());
        session.checkLogin();
        ///
        HashMap<String, String> user = session.getUserDetails();
        final String total_bayar = getIntent().getStringExtra("key_total_bayar");
        final String id_pelanggan = user.get(SessionManagerUser.KEY_ID_PELANGGAN);
        final String id_keranjang = getIntent().getStringExtra("key_id_keranjang");

        rg = (RadioGroup) findViewById(R.id.rg_jenis);
        rbA = (RadioButton) findViewById(R.id.rb_pil_a);
        rbB = (RadioButton) findViewById(R.id.rb_pil_b);

        txtJumBayar.setText("Rp. "+total_bayar);
        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jenis = "";
                if(rbA.isChecked()){
                    jenis = "A";
                }
                if(rbB.isChecked()) {
                    jenis = "J";
                }
                new postData(total_bayar, id_pelanggan, id_keranjang, jenis).execute();
            }
        });

    }

    private class postData extends AsyncTask<Void,Void,String> {
        private String total_bayar;
        private String id_pelanggan;
        private String id_keranjang;
        private String jenis;

        public postData(String total_bayar, String id_pelanggan, String id_keranjang, String jenis){
            this.total_bayar = total_bayar;
            this.id_pelanggan = id_pelanggan;
            this.id_keranjang = id_keranjang;
            this.jenis = jenis;
        }

        private String scs = "";
        private String psn = "";
        private String id_transaksi = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(BayarActivity.this);
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
                detail.put("total_bayar", total_bayar);
                detail.put("id_pelanggan", id_pelanggan);
                detail.put("id_keranjang", id_keranjang);
                detail.put("jenis", jenis);

                try{
                    //convert this HashMap to encodedUrl to send to php file
                    String dataToSend = hashMapToUrl(detail);
                    //make a Http request and send data to php file
                    String response = Request.post(SERVER_POST_DATA,dataToSend);

                    //dapatkan respon
                    Log.e("Respon", response);

                    JSONObject ob = new JSONObject(response);
                    scs = ob.getString("success");
                    psn = ob.getString("message");
                    id_transaksi = ob.getString("id_transaksi");

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
                Toast.makeText(BayarActivity.this, psn, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(BayarActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                //buka printed invoice
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Config.URL+"print/?id_transaksi="+id_transaksi));
                startActivity(browserIntent);
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
