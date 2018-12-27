package in.beyonitysoftwares.shopapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;

import java.util.List;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.activities.new_invoice;
import in.beyonitysoftwares.shopapp.model.customer;

import static android.support.constraint.Constraints.TAG;

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
        holder.menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked menu");
                PopupMenu menu = new PopupMenu(context,holder.menu);
                menu.inflate(R.menu.customer_menu);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.bill:
                                Intent i  = new Intent(context,new_invoice.class);
                                i.putExtra("cid",String.valueOf(c.getId()));
                                context.startActivity(i);
                                return true;
                            case R.id.edit:
                                //handle menu2 click
                                return true;

                            default:
                                return false;
                        }
                    }
                });

                menu.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,place;
        ImageButton menu;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.customername);
            place = (TextView) itemView.findViewById(R.id.customerplace);
            menu = (ImageButton) itemView.findViewById(R.id.menu);
        }
    }
}
