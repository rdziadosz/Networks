package eu.dziadosz.networks;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.apache.commons.net.util.SubnetUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FullSubnetsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FullSubnetsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FullSubnetsFragment extends Fragment {

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
    @BindView(R.id.show_all_addresses_btn)
    Button showAddressesBtn;

    private Snackbar incorrectAddressSnack;
    private SubnetUtils subnet;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FullSubnetsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FullSubnetsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FullSubnetsFragment newInstance(String param1, String param2) {
        FullSubnetsFragment fragment = new FullSubnetsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_subnets, container, false);
        ButterKnife.bind(this, view);
        incorrectAddressSnack = Snackbar.make(view, "Niepoprawne dane wejściowe!", Snackbar.LENGTH_INDEFINITE);
        ArrayList<SubnetUtils> podsieci=dzielNaPodsieci(39,24,"192.168.1.0");
        for (SubnetUtils p:podsieci) {
            Log.v("DUPA_DEBUG",p.getInfo().getCidrSignature());
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @OnClick(R.id.show_all_addresses_btn)
    protected void showAllAddresses() {

        Intent intent = new Intent(getActivity(), AllAddressesActivity.class);
        intent.putExtra(Constants.ADDRESSES, subnet.getInfo().getAllAddresses());
        startActivity(intent);
    }

    @OnEditorAction({
            R.id.ip1,
            R.id.ip2,
            R.id.ip3,
            R.id.ip4,
            R.id.mask_bits,
            R.id.hosts_number,
    })
    protected boolean ipChanged() {
        try {
            subnet = new SubnetUtils(ip1.getText().toString() + "." +
                    ip2.getText().toString() + "." + ip3.getText().toString() + "." + ip4.getText().toString() +
                    "/" + maskBits.getText().toString());

            if (subnet.getInfo().getAddressCountLong() > Integer.valueOf(hostsNumber.getText().toString())) {
                throw new IllegalArgumentException("Za dużo hostów!");
            }
            incorrectAddressSnack.dismiss();
            showAddressesBtn.setEnabled(true);

        } catch (IllegalArgumentException e) {
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
