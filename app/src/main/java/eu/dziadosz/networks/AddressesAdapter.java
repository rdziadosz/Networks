package eu.dziadosz.networks;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AddressesAdapter extends RecyclerView.Adapter<AddressesAdapter.AddressViewHolder> {
    String[] addressArray;
    private OnAddressClickListener onClickListener;

    public AddressesAdapter(String[] addressArray, OnAddressClickListener listener) {
        this.addressArray = addressArray;
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public AddressViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.address_row, parent, false);
        return new AddressViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressViewHolder holder, int position) {
        holder.addressTV.setText(addressArray[position]);
        holder.address = addressArray[position];
    }

    @Override
    public int getItemCount() {
        return addressArray.length;
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        public TextView addressTV;
        public String address;
        public AddressViewHolder(View itemView) {
            super(itemView);
            addressTV = itemView.findViewById(R.id.address_name);
            addressTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickListener.onAddressClick(address);
                }
            });
        }
    }
}
