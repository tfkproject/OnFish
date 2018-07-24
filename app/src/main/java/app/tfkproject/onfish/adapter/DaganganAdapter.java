package app.tfkproject.onfish.adapter;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import app.tfkproject.onfish.R;
import app.tfkproject.onfish.model.Dagangan;

public class DaganganAdapter extends RecyclerView.Adapter<DaganganAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Dagangan> itemList;
    private CardAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id_dagangan;
        public TextView ikan;
        public TextView id_kat_ikan;
        public ImageView thumbnail;
        public TextView petani;
        public TextView id_petani;
        public TextView harga;
        public CardView cardView;

        public ImageButton btn_beli;

        public MyViewHolder(View view) {
            super(view);
            id_dagangan = (TextView) view.findViewById(R.id.id_dagangan);
            petani = (TextView) view.findViewById(R.id.nama_petani);
            id_petani = (TextView) view.findViewById(R.id.id_petani);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            ikan = (TextView) view.findViewById(R.id.nama_ikan);
            id_kat_ikan = (TextView) view.findViewById(R.id.id_kat_ikan);
            harga = (TextView) view.findViewById(R.id.harga);

            btn_beli = (ImageButton) view.findViewById(R.id.overflow);
        }
    }

    public DaganganAdapter(Context mContext, List<Dagangan> itemList, CardAdapterListener listener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dagangan, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Dagangan daganganItem = itemList.get(position);

        holder.id_dagangan.setText(daganganItem.getId_dagangan());
        holder.id_kat_ikan.setText(daganganItem.getId_kategori_ikan());
        holder.ikan.setText(daganganItem.getNama_ikan());
        holder.id_petani.setText(daganganItem.getId_petani());
        holder.petani.setText(daganganItem.getNama_petani());
        holder.harga.setText("Rp. "+daganganItem.getHarga_per_kg()+"/Kg");
        final int harga_per_kg = Integer.valueOf(daganganItem.getHarga_per_kg());
        // loading item cover using Picasso library
        Picasso.with(mContext).load(daganganItem.getLink_foto()).into(holder.thumbnail);

        holder.btn_beli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonSelected(position, holder.id_dagangan, holder.ikan, holder.petani, harga_per_kg, daganganItem.getLink_foto(), daganganItem.getDeskripsi(), daganganItem.getNohp());
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface CardAdapterListener {
        void onButtonSelected(int position, TextView id_dagangan, TextView nama_ikan, TextView nama_petani, int harga_per_kg, String img_url, String deskipsi, String kontak);
    }
}
