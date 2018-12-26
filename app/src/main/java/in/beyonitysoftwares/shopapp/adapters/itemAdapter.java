package in.beyonitysoftwares.shopapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.model.item;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.ViewHolder> {

    List<item> itemList;
    Context context;

    public itemAdapter(List<item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_list, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(itemView);

        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        item i = itemList.get(position);
        holder.name.setText(i.getName());
        holder.qty.setText(i.getQty());
        holder.price.setText(i.getPrice());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,qty,price;
        ImageButton delete;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.proname);
            qty = (TextView) itemView.findViewById(R.id.proqty);
            price = (TextView) itemView.findViewById(R.id.proprice);
            delete = (ImageButton) itemView.findViewById(R.id.prodelete);

        }
    }
}
