package ta.pratiwi.onfish.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import ta.pratiwi.onfish.adapter.PembayaranAdapter;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;
import ta.pratiwi.onfish.app.SessionManagerUser;
import ta.pratiwi.onfish.model.Invoice;


public class PembayaranFragment extends Fragment  {

    private RecyclerView rc;
    private PembayaranAdapter adapter;
    private List<Invoice> itemList;
    private ProgressDialog pDialog;

    public String SERVER = Config.URL+"pembayaran.php";

    SessionManagerUser session;

    private static final String TAG = PembayaranFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_pembayaran_layout, container, false);

        TextView stsLogin = (TextView) rootView.findViewById(R.id.txt_status_login);
        ///
        session = new SessionManagerUser(getContext());
        session.checkLogin();
        //kalau belum login
        if(!session.isLoggedIn()){
            stsLogin.setVisibility(View.VISIBLE);
            //Toast.makeText(getContext(), "Anda harus login untuk dapat melakukan transaksi!", Toast.LENGTH_SHORT).show();
        }
        //kalau sudah login
        else{
            stsLogin.setVisibility(View.GONE);
            //ambil data user
            HashMap<String, String> user = session.getUserDetails();
            String id_pelanggan = user.get(SessionManagerUser.KEY_ID_PELANGGAN);

            rc = (RecyclerView) rootView.findViewById(R.id.recycler_view);

            itemList = new ArrayList<>();
            adapter = new PembayaranAdapter(getContext(), itemList, new PembayaranAdapter.CardAdapterListener() {
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

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            rc.setLayoutManager(mLayoutManager);
            rc.setItemAnimator(new DefaultItemAnimator());
            rc.setAdapter(adapter);

            new getData(id_pelanggan).execute();
        }
        ///

        return rootView;
    }

    private class getData extends AsyncTask<Void,Void,String> {

        //variabel untuk tangkap data
        private int scs = 0;
        private String id_pelanggan;

        public getData(String id_pelanggan){
            this.id_pelanggan = id_pelanggan;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getContext());
            pDialog.setMessage("Loading...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(Void... params) {
            try{
                //susun parameter
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_pelanggan", id_pelanggan);

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
                            String id_transaksi = c.getString("id_transaksi");
                            String id_keranjang = c.getString("id_keranjang");
                            String id_pelanggan = c.getString("id_pelanggan");
                            String total_bayar = c.getString("total_bayar");
                            String waktu = c.getString("waktu");
                            String status = c.getString("jenis");
                            String ket = c.getString("status");

                            Invoice p = new Invoice();
                            p.setId_transaksi(id_transaksi);
                            p.setId_keranjang(id_keranjang);
                            p.setId_pelanggan(id_pelanggan);
                            p.setTotal_bayar(total_bayar);
                            p.setWaktu(waktu);
                            p.setJenis(status);
                            p.setStatus(ket);

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
