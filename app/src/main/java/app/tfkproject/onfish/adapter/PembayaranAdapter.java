package app.tfkproject.onfish.adapter;


import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import app.tfkproject.onfish.R;
import app.tfkproject.onfish.model.Invoice;

public class PembayaranAdapter extends RecyclerView.Adapter<PembayaranAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Invoice> itemList;
    private CardAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id_transaksi;
        public TextView total_bayar;
        public TextView waktu;
        public TextView jenis;
        public CardView cardView;

        public ImageButton btn_rincian;

        public MyViewHolder(View view) {
            super(view);
            id_transaksi = (TextView) view.findViewById(R.id.txt_id_transaksi);
            total_bayar = (TextView) view.findViewById(R.id.txt_total_bayar);
            waktu = (TextView) view.findViewById(R.id.txt_waktu);
            jenis = (TextView) view.findViewById(R.id.txt_jenis);
            cardView = (CardView) view.findViewById(R.id.card_view);

            btn_rincian = (ImageButton) view.findViewById(R.id.overflow);
        }
    }

    public PembayaranAdapter(Context mContext, List<Invoice> itemList, CardAdapterListener listener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pembayaran, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Invoice invoiceItem = itemList.get(position);

        holder.id_transaksi.setText("INV/"+invoiceItem.getId_transaksi());
        holder.total_bayar.setText("Total Bayar: Rp. "+invoiceItem.getTotal_bayar());
        holder.waktu.setText("Waktu: "+invoiceItem.getWaktu());

        String status = "";
        final String jenis = invoiceItem.getJenis();
        if(jenis.contains("A")){
            status = "Diantar";
            holder.jenis.setTextColor(Color.GRAY);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardSelected(position, invoiceItem.getId_transaksi(), jenis);
                }
            });
        }
        if(jenis.contains("J")){
            status = "Dijemput";
            holder.jenis.setTextColor(Color.GRAY);
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onCardSelected(position, invoiceItem.getId_transaksi(), jenis);
                }
            });
        }

        holder.jenis.setText("Pilihan: "+status);

        holder.btn_rincian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonSelected(position, invoiceItem.getId_transaksi());
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
}
