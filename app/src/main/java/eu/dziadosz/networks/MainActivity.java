package eu.dziadosz.networks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.net.util.SubnetUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.network) TextView network;
    @BindView(R.id.netmask) TextView netmask;
    @BindView(R.id.address) TextView address;
    @BindView(R.id.broadcast) TextView broadcast;
    @BindView(R.id.ip1) EditText ip1;
    @BindView(R.id.ip2) EditText ip2;
    @BindView(R.id.ip3) EditText ip3;
    @BindView(R.id.ip4) EditText ip4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        SubnetUtils subnet = new SubnetUtils(ip1.getText().toString()+"."+ip2.getText().toString()+"."+ip3.getText().toString()+"."+ip4.getText().toString()+"/24");
        address.setText(subnet.getInfo().getAddress());
        netmask.setText(subnet.getInfo().getNetmask());
        broadcast.setText(subnet.getInfo().getBroadcastAddress());
        network.setText(subnet.getInfo().getNetworkAddress());

    }
}
