package in.beyonitysoftwares.shopapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import in.beyonitysoftwares.shopapp.R;
import in.beyonitysoftwares.shopapp.model.Invoice;
import in.beyonitysoftwares.shopapp.model.customer;
import in.beyonitysoftwares.shopapp.utils.Helper;

import static android.support.constraint.Constraints.TAG;

public class invoiceAdapter extends RecyclerView.Adapter<invoiceAdapter.ViewHolder> {

    List<Invoice> invoiceList;
    Context context;

    public invoiceAdapter(List<Invoice> invoiceList, Context context) {
        this.invoiceList = invoiceList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_invoice, parent, false);
        RecyclerView.ViewHolder holder = new ViewHolder(itemView);

        return (ViewHolder) holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Invoice i = invoiceList.get(position);

        String name = "";
        for(customer c : Helper.getCustomerList()){
            Log.d(TAG, "onBindViewHolder: "+c.getId()+" "+i.getCustomerid1()+" "+String.valueOf(c.getId()).equals(i.getCustomerid1()));
            if(String.valueOf(c.getId()).equals(i.getCustomerid1())){
                name = c.getName();
                break;
            }
        }
        holder.name.setText(name);

        holder.invoiceNo.setText(i.getInvoiceNo());
    }

    @Override
    public int getItemCount() {
        return invoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,invoiceNo;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.customername);
            invoiceNo = (TextView) itemView.findViewById(R.id.invoicelistno);
        }
    }


}
