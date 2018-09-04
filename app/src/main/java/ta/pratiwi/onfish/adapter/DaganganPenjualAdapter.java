package ta.pratiwi.onfish.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import ta.pratiwi.onfish.R;
import ta.pratiwi.onfish.model.Dagangan;

/**
 * Created by user on 29/08/18.
 */

public class DaganganPenjualAdapter extends RecyclerView.Adapter<DaganganPenjualAdapter.ViewHolder> {

    List<Dagangan> items;
    Context context;
    private AdapterListener listener;

    public DaganganPenjualAdapter(Context context, List<Dagangan> items, AdapterListener listener) {
        this.context = context;
        this.items = items;
        this.listener = listener;
    }

    @Override
    public DaganganPenjualAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dagangan_penjual, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onSelected(
                        position,
                        items.get(position).getId_dagangan(),
                        items.get(position).getId_jenis_ikan(),
                        items.get(position).getNama_ikan(),
                        items.get(position).getLink_foto(),
                        items.get(position).getBerat_tersedia(),
                        items.get(position).getHarga_per_kg(),
                        items.get(position).getDeskripsi()
                );
            }
        });
        Picasso.with(context).load(items.get(position).getLink_foto()).into(holder.fotoProduk);
        holder.txtNama.setText(items.get(position).getNama_ikan());
        holder.txtBerat.setText("Tersedia: "+items.get(position).getBerat_tersedia()+" Kg");
        holder.txtHarga.setText("Rp. "+items.get(position).getHarga_per_kg());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        ImageView fotoProduk;
        TextView txtNama, txtBerat, txtHarga;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView) itemView.findViewById(R.id.card_view);
            fotoProduk = (ImageView) itemView.findViewById(R.id.foto_produk);
            txtNama = (TextView) itemView.findViewById(R.id.nama_produk);
            txtBerat = (TextView) itemView.findViewById(R.id.txt_berat);
            txtHarga = (TextView) itemView.findViewById(R.id.txt_harga);
        }
    }

    public interface AdapterListener {
        void onSelected(
                int position,
                String id_dagangan,
                String id_jenis_ikan,
                String jenis_ikan,
                String url_gambar,
                String berat_tersedia,
                String harga,
                String desk
        );
    }
}