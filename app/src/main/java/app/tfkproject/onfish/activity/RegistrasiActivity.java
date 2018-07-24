package app.tfkproject.onfish.activity;

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
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import app.tfkproject.onfish.R;
import app.tfkproject.onfish.app.Config;
import app.tfkproject.onfish.app.Request;
import app.tfkproject.onfish.app.SessionManager;

public class RegistrasiActivity extends AppCompatActivity {

    TextView txtNama, txtEmail, txtPassword, txtNomor_hp,txtAlamat;
    Button btnReg;
    private ProgressDialog pDialog;
    SessionManager session;

    public String SERVER_POST = Config.URL+"registrasi.php";

    private static final String TAG = RegistrasiActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registrasi");

        session = new SessionManager(getApplicationContext());

        txtNama = (TextView) findViewById(R.id.nama);
        txtEmail = (TextView) findViewById(R.id.email);
        txtPassword = (TextView) findViewById(R.id.password);
        txtNomor_hp = (TextView) findViewById(R.id.nomor_hp);
        txtAlamat = (TextView) findViewById(R.id.alamat);
        btnReg = (Button) findViewById(R.id.registrasi_button);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPassword.getText().length() < 6){
                    Toast.makeText(RegistrasiActivity.this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //registrasikan user
                    String nama = txtNama.getText().toString();
                    String email = txtEmail.getText().toString();
                    String password = txtPassword.getText().toString();
                    String nomor_hp = txtNomor_hp.getText().toString();
                    String alamat = txtAlamat.getText().toString();

                    new postData(nama, email, password, nomor_hp, alamat).execute();
                }
            }
        });

    }

    private class postData extends AsyncTask<Void,Void,String> {
        private String nama;
        private String email;
        private String password;
        private String nomor_hp;
        private String alamat;

        public postData(String nama, String email, String password, String nomor_hp, String alamat){
            this.nama = nama;
            this.email = email;
            this.password = password;
            this.nomor_hp = nomor_hp;
            this.alamat = alamat;
        }

        private String scs = "";
        private String psn = "";
        private String nm = "";
        private String id_pelanggan = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistrasiActivity.this);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                HashMap<String,String> detail = new HashMap<>();
                detail.put("nama", nama);
                detail.put("email", email);
                detail.put("password", password);
                detail.put("nomor_hp", nomor_hp);
                detail.put("alamat", alamat);

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
                    nm = ob.getString("nama");
                    id_pelanggan = ob.getString("id_pelanggan");

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
                //buat sesi login
                session.createLoginSession(id_pelanggan, nama, email, nomor_hp, alamat);

                Toast.makeText(getApplicationContext(), "Selamat datang "+nm,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(RegistrasiActivity.this, MainActivity.class);
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
