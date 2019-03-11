package ta.pratiwi.onfish.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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

public class RegistrasiPenjualActivity extends AppCompatActivity {

    TextView txtNama, txtEmail, txtPassword, txtNomor_hp,txtAlamat, txtLokasi;
    Button btnReg, btnLokasi;
    private ProgressDialog pDialog;
    SessionManagerUser session;

    public String SERVER_POST = Config.URL+"reg_penjual.php";
    public String lat, lon;;

    private static final String TAG = RegistrasiPenjualActivity.class.getSimpleName();

    int PLACE_PICKER_REQUEST    =   1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi_penjual);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registrasi Penjual");

        session = new SessionManagerUser(getApplicationContext());

        txtNama = (TextView) findViewById(R.id.nama);
        txtEmail = (TextView) findViewById(R.id.email);
        txtPassword = (TextView) findViewById(R.id.password);
        txtNomor_hp = (TextView) findViewById(R.id.nomor_hp);
        txtAlamat = (TextView) findViewById(R.id.alamat);
        btnReg = (Button) findViewById(R.id.registrasi_button);

        txtLokasi = (TextView) findViewById(R.id.txt_lokasi);

        btnLokasi = (Button) findViewById(R.id.btn_lokasi);
        btnLokasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder   =   new PlacePicker.IntentBuilder();
                Intent intent;
                try {
                    intent  =   builder.build(RegistrasiPenjualActivity.this);
                    startActivityForResult(intent,PLACE_PICKER_REQUEST );
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtPassword.getText().length() < 6){
                    Toast.makeText(RegistrasiPenjualActivity.this, "Password minimal 6 karakter!", Toast.LENGTH_SHORT).show();
                }
                else{
                    //registrasikan user
                    String nama = txtNama.getText().toString();
                    String email = txtEmail.getText().toString();
                    String password = txtPassword.getText().toString();
                    String nomor_hp = txtNomor_hp.getText().toString();
                    String alamat = txtAlamat.getText().toString();

                    new postData(nama, email, password, nomor_hp, alamat, lat, lon).execute();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( requestCode == PLACE_PICKER_REQUEST) {
            if(resultCode == RESULT_OK)
            {
                Place place =   PlacePicker.getPlace(data, RegistrasiPenjualActivity.this);
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;
                lat = String.valueOf(latitude);
                lon = String.valueOf(longitude);
                String address = "Lat: "+String.valueOf(latitude)+"\nLon: "+String.valueOf(longitude);
                txtLokasi.setText(address);
            }
        }
    }

    private class postData extends AsyncTask<Void,Void,String> {
        private String nama;
        private String email;
        private String password;
        private String nomor_hp;
        private String alamat;
        private String lati;
        private String longi;

        public postData(String nama, String email, String password, String nomor_hp, String alamat, String lati, String longi){
            this.nama = nama;
            this.email = email;
            this.password = password;
            this.nomor_hp = nomor_hp;
            this.alamat = alamat;
            this.lati = lati;
            this.longi = longi;
        }

        private String scs = "";
        private String psn = "";
        private String nm = "";
        private String id_penjual = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(RegistrasiPenjualActivity.this);
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
                detail.put("lat", lati);
                detail.put("lon", longi);

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
                    id_penjual = ob.getString("id_penjual");

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
                session.createLoginSession(id_penjual, nama, email, nomor_hp, alamat, "penjual");

                Toast.makeText(getApplicationContext(), "Selamat datang "+nm,Toast.LENGTH_SHORT).show();

                //terus tutup activity ini
                finish();

                Intent intent = new Intent(RegistrasiPenjualActivity.this, MainPenjualActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
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
