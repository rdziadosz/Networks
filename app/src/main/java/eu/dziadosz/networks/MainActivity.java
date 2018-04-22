package eu.dziadosz.networks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.apache.commons.net.util.SubnetUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SubnetUtils subnet = new SubnetUtils("192.168.0.3/31");
        
    }
}
