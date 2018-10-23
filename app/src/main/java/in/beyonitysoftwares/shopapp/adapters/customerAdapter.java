package in.beyonitysoftwares.shopapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.model.customer;

public class customerAdapter extends RecyclerView.Adapter<customerAdapter.ViewHolder> {

    List<customer> customerList;
    Context context;

    public customerAdapter(List<customer> customerList, Context context) {
        this.customerList = customerList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_cutomer, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(itemView);

        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        customer c = customerList.get(position);
        holder.name.setText(c.getName());
        holder.place.setText(c.getAddress());
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,place;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.customername);
            place = (TextView) itemView.findViewById(R.id.customerplace);
        }
    }
}
