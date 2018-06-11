package eu.dziadosz.networks;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.commons.net.util.SubnetUtils;

import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnLongClick;
import butterknife.OnTextChanged;

public class IpActivity extends AppCompatActivity {

    @BindView(R.id.network)
    TextView network;
    @BindView(R.id.netmask)
    TextView netmask;
    @BindView(R.id.address)
    TextView address;
    @BindView(R.id.broadcast)
    TextView broadcast;
    @BindView(R.id.lowest_adr)
    TextView lowestAdr;
    @BindView(R.id.highest_adr)
    TextView highestAdr;
    @BindView(R.id.address_count)
    TextView addressCount;
    @BindView(R.id.ip1)
    EditText ip1;
    @BindView(R.id.ip2)
    EditText ip2;
    @BindView(R.id.ip3)
    EditText ip3;
    @BindView(R.id.ip4)
    EditText ip4;
    @BindView(R.id.mask_bits)
    EditText maskBits;
    @BindView(R.id.network_binary)
    TextView networkBinary;
    @BindView(R.id.netmask_binary)
    TextView netmaskBinary;
    @BindView(R.id.address_binary)
    TextView addressBinary;
    @BindView(R.id.broadcast_binary)
    TextView broadcastBinary;
    @BindView(R.id.lowest_adr_binary)
    TextView lowestAdrBinary;
    @BindView(R.id.highest_adr_binary)
    TextView highestAdrBinary;

    private Snackbar incorrectAddressSnack;
    private SubnetUtils subnet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        incorrectAddressSnack = Snackbar.make(network, "Niepoprawny adres IP", Snackbar.LENGTH_INDEFINITE);
        String address = getIntent().getStringExtra(Constants.ADDRESS);
        String[] addressParts = address.split("\\.");
        String lastPart[] = addressParts[3].split("/");
        addressParts[3] = lastPart[0];
        String mask = lastPart[1];
        ip1.setText(addressParts[0]);
        ip2.setText(addressParts[1]);
        ip3.setText(addressParts[2]);
        ip4.setText(addressParts[3]);
        maskBits.setText(mask);
    }
    @OnTextChanged({
            R.id.ip1,
            R.id.ip2,
            R.id.ip3,
            R.id.ip4,
            R.id.mask_bits,
    })
    protected void ipTextChanged() {
        ipChanged();
    }
    @OnLongClick({
            R.id.network_root,
            R.id.netmask_root,
            R.id.address_root,
            R.id.broadcast_root,
            R.id.lowest_adr_root,
            R.id.highest_adr_root
    })
    protected boolean onRootLongClick(View view) {
        String textToConvert = null;
        TextView display = null;
        switch (view.getId()) {
            case R.id.network_root:
                textToConvert = network.getText().toString();
                display = networkBinary;
                break;
            case R.id.netmask_root:
                textToConvert = netmask.getText().toString();
                display = netmaskBinary;
                break;
            case R.id.address_root:
                textToConvert = address.getText().toString();
                display = addressBinary;
                break;
            case R.id.broadcast_root:
                textToConvert = broadcast.getText().toString();
                display = broadcastBinary;
                break;
            case R.id.lowest_adr_root:
                textToConvert = lowestAdr.getText().toString();
                display = lowestAdrBinary;
                break;
            case R.id.highest_adr_root:
                textToConvert = highestAdr.getText().toString();
                display = highestAdrBinary;
                break;
        }
        if(textToConvert != null && display != null) {
            int visibility = display.getVisibility();
            if(visibility == View.GONE) {
                try {
                    byte[] bytes = InetAddress.getByName(textToConvert).getAddress();
                    display.setText(new BigInteger(1, bytes).toString(2));
                    display.setVisibility(View.VISIBLE);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            } else {
                display.setVisibility(View.GONE);
            }
        }
        return true;
    }
    @OnEditorAction({
            R.id.ip1,
            R.id.ip2,
            R.id.ip3,
            R.id.ip4,
            R.id.mask_bits,
    })
    protected boolean ipChanged() {
        try {
            subnet = new SubnetUtils(ip1.getText().toString() + "." +
                    ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString() +
                    "/" + maskBits.getText().toString());

            address.setText(subnet.getInfo().getAddress());
            netmask.setText(subnet.getInfo().getNetmask());
            broadcast.setText(subnet.getInfo().getBroadcastAddress());
            network.setText(subnet.getInfo().getNetworkAddress());
            lowestAdr.setText(subnet.getInfo().getLowAddress());
            highestAdr.setText(subnet.getInfo().getHighAddress());
            addressCount.setText(String.valueOf(subnet.getInfo().getAddressCountLong()));
            incorrectAddressSnack.dismiss();

        } catch (IllegalArgumentException e) {
            incorrectAddressSnack.show();
        }
        return true;
    }
}
