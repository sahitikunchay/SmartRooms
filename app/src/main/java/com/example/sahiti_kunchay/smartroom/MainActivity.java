package com.example.sahiti_kunchay.smartroom;

import android.Manifest;
import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    ArrayList<IBeacon> mIBeaconArrayList = new ArrayList<IBeacon>();
    public static int sClear = 0;
    TextView mTextView;
    ImageView mImageView;
    TextView mScanDetails;
    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        if (Build.VERSION.SDK_INT >= 23) {
            // Android M Permission check 
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                //final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final AlertDialog.Builder b = new AlertDialog.Builder(this);
                b.setTitle("This app needs location access");
                b.setMessage("Please grant location access so this app can detect beacons.");
                b.setPositiveButton(android.R.string.ok, null);
                b.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
                    }
                });
                b.show();
            }
        }

        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        mTextView = (TextView) findViewById(R.id.textView);
        mImageView = (ImageView) findViewById(R.id.imageView3);
        mScanDetails = (TextView) findViewById(R.id.textView3);

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scan();
            }
        });

    }

    public static String bytesToHex(byte[] pBytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[pBytes.length * 2];
        for ( int j = 0; j < pBytes.length; j++ ) {
            int v = pBytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void scan(){
        final BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
                Log.d("bleyay", device.getAddress());
                int startByte = 2;
                boolean patternFound = false;
                while (startByte <= 5) {
                    if (    ((int) scanRecord[startByte + 2] & 0xff) == 0x02 && //Identifies an IBeacon
                            ((int) scanRecord[startByte + 3] & 0xff) == 0x15) { //Identifies correct data length
                        patternFound = true;
                        break;
                    }
                    startByte++;
                }

                if (patternFound) {
                    //Convert to hex String
                    byte[] uuidBytes = new byte[16];
                    System.arraycopy(scanRecord, startByte+4, uuidBytes, 0, 16);
                    String hexString = bytesToHex(uuidBytes);

                    //Here is your UUID
                    String uuid =  hexString.substring(0,8) + "-" +
                            hexString.substring(8,12) + "-" +
                            hexString.substring(12,16) + "-" +
                            hexString.substring(16,20) + "-" +
                            hexString.substring(20,32);

                    //Here is your Major value
                    int major = (scanRecord[startByte+20] & 0xff) * 0x100 + (scanRecord[startByte+21] & 0xff);

                    //Here is your Minor value
                    int minor = (scanRecord[startByte+22] & 0xff) * 0x100 + (scanRecord[startByte+23] & 0xff);
                    byte txPower = scanRecord[startByte+24];


                    if(sClear ==1){
                        mIBeaconArrayList.clear();
                        sClear =0;
                    }

                    IBeacon iBeacon = new IBeacon(device.getAddress(),uuid,device.getName(),major,minor,rssi);
                    if (!mIBeaconArrayList.contains(iBeacon)) {
                        mIBeaconArrayList.add(iBeacon);
                        if(iBeacon.getMacAddress().equals("CF:DF:23:23:84:65")){
                            double distance = getDistance(rssi, txPower);
                            mScanDetails.setText("Distance from room: " + getDistance(rssi, txPower) +"m");
                            if(distance<3.0){
                                final AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                                b.setTitle("You've Reached the conference Room!");
                                b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent mIntent = new Intent(MainActivity.this, ControlActivity.class);
                                        startActivity(mIntent);
                                    }
                                });
                                b.show();
                            }
                        }
                    }


                }
            }
        };

        mBluetoothAdapter.startLeScan(leScanCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_LOCATION: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("smartroomapp", "coarse location permission granted");
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }
                    });
                    builder.show();
                }
                return;
            }
        }
    }

    double getDistance(int rssi, int txPower) {
        return Math.pow(10d, ((double) txPower - rssi) / (10 * 2));
    }
}
