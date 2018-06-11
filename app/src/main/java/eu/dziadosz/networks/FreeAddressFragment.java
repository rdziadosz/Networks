package eu.dziadosz.networks;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.net.util.SubnetUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;


/**
 * A simple {@link Fragment} subclass.
 */
public class FreeAddressFragment extends Fragment {
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
    @BindView(R.id.hosts_number)
    EditText hostsNumber;
    @BindView(R.id.taken_count)
    EditText takenCount;
    @BindView(R.id.free_at_start)
    EditText freeAtStart;
    @BindView(R.id.free_count)
    TextView freeCount;
    @BindView(R.id.first_free)
    TextView firstFreeAddress;

    private int freeAddresses;
    private int takenAddresses;

    private Snackbar incorrectAddressSnack;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_free_address, container, false);
        incorrectAddressSnack = Snackbar.make(rootView.getRootView(), "Niepoprawne dane wejściowe!", Snackbar.LENGTH_INDEFINITE);
        ButterKnife.bind(this, rootView);
        return rootView;

    }
    @OnTextChanged({
            R.id.ip1,
            R.id.ip2,
            R.id.ip3,
            R.id.ip4,
            R.id.mask_bits,
            R.id.hosts_number,
            R.id.taken_count,
            R.id.free_at_start
    })
    protected void textChange() {
        ipChanged();
    }
    @OnEditorAction({
            R.id.ip1,
            R.id.ip2,
            R.id.ip3,
            R.id.ip4,
            R.id.mask_bits,
            R.id.hosts_number,
            R.id.taken_count,
            R.id.free_at_start
    })
    protected boolean editorialAction() {
        return ipChanged();
    }
    protected boolean ipChanged() {
        try {
            SubnetUtils subnet = new SubnetUtils(ip1.getText().toString() + "." +
                    ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString() +
                    "/" + maskBits.getText().toString());
            if (subnet.getInfo().getAddressCountLong() > Integer.valueOf(hostsNumber.getText().toString())) {
                throw new IllegalArgumentException("Za dużo hostów!");
            }
            long free = subnet.getInfo().getAddressCountLong() - takenAddresses;
            if(free > 0) {
                freeCount.setText(String.valueOf(free));
            } else {
                throw new IllegalArgumentException("Za dużo zajętych adresów");
            }
            incorrectAddressSnack.dismiss();

        } catch (IllegalArgumentException e) {
            incorrectAddressSnack.setText(e.getMessage());
            incorrectAddressSnack.show();
        }
        return true;
    }

    protected ArrayList<SubnetUtils> dzielNaPodsieci(int liczbaHostow, int maska, String ip){
        ArrayList<SubnetUtils> result = new ArrayList<>();
        SubnetUtils newsubnet;
        int n=maska;
        while(liczbaHostow>0){
            if((int)(Math.pow(2,32-n)-2)>liczbaHostow){
                n++;
            }
            else {
                if(liczbaHostow<=2)n=30;
                if(result.isEmpty())newsubnet = new SubnetUtils(ip+"/"+n);
                else newsubnet = new SubnetUtils(getNextIPV4Address(result.get(result.size()-1).getInfo().getBroadcastAddress())+"/"+n);
                result.add(newsubnet);
                liczbaHostow-=(Math.pow(2,32-n)-2);
            }
        }
        return result;
    }

    public static String getNextIPV4Address(String ip) {
        String[] nums = ip.split("\\.");
        int i = (Integer.parseInt(nums[0]) << 24 | Integer.parseInt(nums[2]) << 8
                |  Integer.parseInt(nums[1]) << 16 | Integer.parseInt(nums[3])) + 1;

        // If you wish to skip over .255 addresses.
        if ((byte) i == -1) i++;

        return String.format("%d.%d.%d.%d", i >>> 24 & 0xFF, i >> 16 & 0xFF,
                i >>   8 & 0xFF, i >>  0 & 0xFF);
    }
}
