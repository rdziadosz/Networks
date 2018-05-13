package eu.dziadosz.networks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder> {
    String[] addressArray;

    public AddressesAdapter(String[] addressArray) {
        this.addressArray = addressArray;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_row, parent, false);
        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.address.setText(addressArray[position]);
    }

    @Override
    public int getItemCount() {
        return addressArray.length;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView address;

        public AddressViewHolder(View itemView) {
            super(itemView);
            address = itemView.findViewById(R.id.address_name);
        }
    }
}
