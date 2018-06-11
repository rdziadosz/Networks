package eu.dziadosz.networks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllAddressesActivity extends AppCompatActivity {
    @BindView(R.id.root_recycler_view)
    RecyclerView rootRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_adresses);
        ButterKnife.bind(this);
        String[] addresses = getIntent().getStringArrayExtra(Constants.ADDRESSES);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rootRecyclerView.setLayoutManager(mLayoutManager);
        rootRecyclerView.setItemAnimator(new DefaultItemAnimator());

        rootRecyclerView.setAdapter(new AddressesAdapter(addresses, addressClickListener));
    }
    private OnAddressClickListener addressClickListener = new OnAddressClickListener() {
        @Override
        public void onAddressClick(String address) {
            Intent intent = new Intent(AllAddressesActivity.this, IpActivity.class);
            intent.putExtra(Constants.ADDRESS, address);
            startActivity(intent);
        }
    };
}
