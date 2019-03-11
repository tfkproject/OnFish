package ta.pratiwi.onfish.adapter;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.app.Config;
import ta.pratiwi.onfish.app.Request;
import ta.pratiwi.onfish.model.Invoice;
import ta.pratiwi.onfish.model.Orderan;

public class OrderanAdapter extends RecyclerView.Adapter<OrderanAdapter.MyViewHolder>  {

    public String SERVER_POST = Config.URL+"update_status_transaksi.php";
    private ProgressDialog pDialog;
    private Context mContext;
    private List<Orderan> itemList;
    private CardAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id_transaksi;
        public TextView nama;
        public TextView jenis;
        public TextView ket;
        public CardView cardView;

        public ImageButton btn_rincian;
        public ImageButton btn_cek;
        public ImageButton btn_wa;

        public MyViewHolder(View view) {
            super(view);
            id_transaksi = (TextView) view.findViewById(R.id.txt_id_transaksi);
            nama = (TextView) view.findViewById(R.id.txt_nama);
            jenis = (TextView) view.findViewById(R.id.txt_jenis);
            ket = (TextView) view.findViewById(R.id.txt_ket);
            cardView = (CardView) view.findViewById(R.id.card_view);

            btn_rincian = (ImageButton) view.findViewById(R.id.overflow);
            btn_cek = (ImageButton) view.findViewById(R.id.btn_cek);
            btn_wa = (ImageButton) view.findViewById(R.id.btn_wa);
        }
    }

    public OrderanAdapter(Context mContext, List<Orderan> itemList, CardAdapterListener listener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_orderan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Orderan item = itemList.get(position);

        holder.id_transaksi.setText("INV/"+item.getId_transaksi());
        holder.nama.setText(item.getNama_pelanggan());

        String status = "";
        final String jenis = item.getJenis();
        if(jenis.contains("A")){
            status = "Diantar";
            holder.jenis.setTextColor(Color.GRAY);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardSelected(position, item.getId_transaksi(), jenis);
                }
            });
        }
        if(jenis.contains("J")){
            status = "Dijemput";
            holder.jenis.setTextColor(Color.GRAY);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardSelected(position, item.getId_transaksi(), jenis);
                }
            });
        }

        holder.jenis.setText("Pilihan: "+status);
        String keterangan = item.getStatus();
        if(keterangan.contains("W")){
            keterangan = "Menunggu";
        }
        if(keterangan.contains("D")){
            keterangan = "Transaksi selesai";
            holder.btn_cek.setEnabled(false);
            holder.btn_cek.setImageResource(R.drawable.ic_action_check_disabled);
        }
        if(keterangan.contains("N")){
            keterangan = "Transaksi dibatalkan";
            holder.btn_cek.setEnabled(false);
            holder.btn_cek.setImageResource(R.drawable.ic_action_check_disabled);
        }
        holder.ket.setText("Ket: "+keterangan);

        holder.btn_rincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonSelected(position, item.getId_transaksi());
            }
        });

        holder.btn_cek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mContext);
                builder1.setMessage("Transaksi selesai?");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Ya",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                new updateData(item.getId_transaksi(), "D").execute();
                            }
                        });

                builder1.setNegativeButton(
                        "Tidak",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        });

        holder.btn_wa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Hubungi pembeli");
                builder.setMessage("WhatsApp atau telpon?");
                builder.setPositiveButton("WA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openWhatsappContact(item.getNomor_hp());
                    }
                });

                builder.setNegativeButton("Telpon", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:"+item.getNomor_hp()));
                        mContext.startActivity(intent);
                    }
                });
                AlertDialog alert = builder.create();
                alert.setCancelable(true);
                alert.setCanceledOnTouchOutside(true);
                alert.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface CardAdapterListener {
        void onCardSelected(int position, String id_transaksi, String status);
        void onButtonSelected(int position, String id_transaksi);
    }

    //for update status transaksi ke batal
    private class updateData extends AsyncTask<Void,Void,String> {
        private String id_transaksi;
        private String status;

        public updateData(String id_transaksi, String status){
            this.id_transaksi = id_transaksi;
            this.status = status;
        }

        private String scs = "";
        private String psn = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Loading..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                HashMap<String,String> detail = new HashMap<>();
                detail.put("id_transaksi", id_transaksi);
                detail.put("status", status);

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
                    Log.e("TAG", "ERROR  " + e);
                    Toast.makeText(mContext,"Maaf, terjadi error!",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(mContext, psn,Toast.LENGTH_SHORT).show();
            }
            if(scs.contains("0")){
                Toast.makeText(mContext, psn,Toast.LENGTH_SHORT).show();
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

    public void openWhatsappContact(String number) {
        PackageManager packageManager = mContext.getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone="+ number +"&text=" + URLEncoder.encode("", "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                mContext.startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
