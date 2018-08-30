package ta.pratiwi.onfish.adapter;


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

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.model.Keranjang;

public class KeranjangAdapter extends RecyclerView.Adapter<KeranjangAdapter.MyViewHolder>  {

    private Context mContext;
    private List<Keranjang> itemList;
    private CardAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView id_item_beli;
        public TextView id_dagangan;
        public TextView ikan;
        public TextView id_kat_ikan;
        public ImageView thumbnail;
        public TextView petani;
        public TextView id_petani;
        public TextView jum_kg;
        public TextView harga_total;
        public CardView cardView;

        public ImageButton btn_remove;

        public MyViewHolder(View view) {
            super(view);
            id_item_beli = (TextView) view.findViewById(R.id.id_item_beli);
            id_dagangan = (TextView) view.findViewById(R.id.id_dagangan);
            petani = (TextView) view.findViewById(R.id.nama_petani);
            id_petani = (TextView) view.findViewById(R.id.id_petani);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
            ikan = (TextView) view.findViewById(R.id.nama_ikan);
            id_kat_ikan = (TextView) view.findViewById(R.id.id_kat_ikan);
            jum_kg = (TextView) view.findViewById(R.id.jum_kg);
            harga_total = (TextView) view.findViewById(R.id.harga);

            btn_remove = (ImageButton) view.findViewById(R.id.overflow);
        }
    }

    public KeranjangAdapter(Context mContext, List<Keranjang> itemList, CardAdapterListener listener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_keranjang, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Keranjang keranjangItem = itemList.get(position);

        holder.id_item_beli.setText(keranjangItem.getId_item_beli());
        holder.id_dagangan.setText(keranjangItem.getId_dagangan());
        holder.id_kat_ikan.setText(keranjangItem.getId_kategori_ikan());
        holder.ikan.setText(keranjangItem.getNama_ikan());
        holder.id_petani.setText(keranjangItem.getId_petani());
        holder.petani.setText(keranjangItem.getNama_petani());
        holder.jum_kg.setText("Jumlah Pesan: "+keranjangItem.getJum_kg()+" Kg");
        holder.harga_total.setText("Harga Total: Rp. "+keranjangItem.getHarga_total());
        // loading item cover using Picasso library
        Picasso.with(mContext).load(keranjangItem.getLink_foto()).into(holder.thumbnail);

        holder.btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onButtonSelected(
                        position,
                        holder.id_item_beli,
                        holder.ikan,
                        holder.petani,
                        keranjangItem.getId_dagangan(),
                        keranjangItem.getBerat_tersedia(),
                        keranjangItem.getJum_kg()
                );
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface CardAdapterListener {
        void onButtonSelected(
                int position,
                TextView id_record,
                TextView nama_ikan,
                TextView nama_petani,
                String id_dagangan,
                String berat_tersedia,
                String jum_kg
        );
    }
}
