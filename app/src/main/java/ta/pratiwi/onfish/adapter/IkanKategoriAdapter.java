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
import ta.pratiwi.onfish.model.IkanKategori;

public class IkanKategoriAdapter extends RecyclerView.Adapter<IkanKategoriAdapter.MyViewHolder>  {

    private Context mContext;
    private List<IkanKategori> itemList;
    private CardAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView ikan;
        public TextView id_kat_ikan;
        public ImageView thumbnail;
        public CardView cardView;
        public MyViewHolder(View view) {
            super(view);
            ikan = (TextView) view.findViewById(R.id.nama_ikan);
            id_kat_ikan = (TextView) view.findViewById(R.id.id_kat_ikan);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            cardView = (CardView) view.findViewById(R.id.card_view);
        }
    }

    public IkanKategoriAdapter(Context mContext, List<IkanKategori> itemList, CardAdapterListener listener) {
        this.mContext = mContext;
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        IkanKategori ikanKategoriItem = itemList.get(position);

        holder.ikan.setText(ikanKategoriItem.getNama_ikan());
        holder.id_kat_ikan.setText(ikanKategoriItem.getId_kategori_ikan());
        // loading item cover using Picasso library
        Picasso.with(mContext).load(ikanKategoriItem.getLink_foto()).into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onCardSelected(position, holder.id_kat_ikan, holder.ikan, holder.thumbnail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public interface CardAdapterListener {
        void onCardSelected(int position, TextView id_kategori_ikan, TextView nama_ikan, ImageView image);
    }
}
