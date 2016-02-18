package emerikbedouin.demonetiktpe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static example.utils.StringUtils.convertByteArrayToHexString;

public class PINActivity extends AppCompatActivity { //implements NfcAdapter.ReaderCallback, NFCThread.UiCallback {

    public static final String TAG = "CardReaderTest";

    private NfcAdapter adapter;
    //    private PendingIntent nfcintent;
    private IsoDep iso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.entrer_pin);
        setSupportActionBar(toolbar);

        /*adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) {
            Toast.makeText(this, getString(R.string.nfc_unavailable), Toast.LENGTH_LONG).show();
            finish();
        }*/
    }
/*
    @Override
    protected void onPause() {
        super.onPause();
//        adapter.disableForegroundDispatch(this);

        unregisterReceiver(mBroadcastReceiver);
        adapter.disableReaderMode(this);
    }

    @Override
    public void onTagDiscovered(Tag tag) {

        // Parametre WebService
        String ipAddress = "192.168.43.233";
        String port = "8080";
        final String urlWebService = "http://"+ipAddress+":"+port+"/DemonetikWebService/demonetik/transaction/";

        //int montant = getIntent().getIntExtra("montant", 0);
        //Runnable nfcr = new NFCThread(this, tag, this, montant, 1234);
        //new Thread(nfcr).start();

        // Check de la connexion
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {


            String id = convertByteArrayToHexString(tag.getId());

            HashMap<String, String> params = new HashMap<String, String>();
            params.put("nom", "Bedouin");
            params.put("prenom", "Emerik");
            params.put("numCarte", id);
            HttpRequestParameters requestParam = new HttpRequestParameters(urlWebService, "POST", "text/plain", params);
            Log.i("PORTEUR", "Informations porteur envoyées");
            new DataWebService().execute(requestParam);

        } else {
            // display error connexion ko
        }
    }

    @Override
    public void setEditText(final int id, final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EditText edit = (EditText) findViewById(id);
                edit.setText(txt);
            }
        });
    }

    @Override
    public void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(PINActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    public class DataWebService extends AsyncTask<HttpRequestParameters, Void, HttpRequestParameters> {

        protected HttpRequestParameters doInBackground(HttpRequestParameters... param) {
            try {

                InputStream is = ConnexionDataWebService.connexionToWebService(param[0]);
                if (is != null) {

                    // traitement du retour si échéant

                    return param[0];
                } else {


                    return param[0];
                }

            } catch (IOException e) {
                System.out.println(e.getMessage());
                return param[0];
            }
        }

        // onPostExecute displays the results of the AsyncTask
        protected void onPostExecute(String result) {

            System.out.println("on Post time !");
        }
    }

    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        //        @SuppressWarnings("deprecation")
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action == null)
                return;
            if (action.equals(NfcAdapter.ACTION_ADAPTER_STATE_CHANGED)) {
                int state = intent.getIntExtra(NfcAdapter.EXTRA_ADAPTER_STATE,
                        NfcAdapter.STATE_ON);
                if (state == NfcAdapter.STATE_ON
                        || state == NfcAdapter.STATE_TURNING_ON) {
                    Log.d(TAG, "state: " + state);
                    if (state == NfcAdapter.STATE_ON) {
                        //Bundle extras = new Bundle();
                        //extras.putBoolean("bit_transparent_mode", true);
                        adapter
                                .enableReaderMode(
                                        PINActivity.this,
                                        PINActivity.this,
                                        NfcAdapter.FLAG_READER_NFC_A
                                                //NfcAdapter.FLAG_READER_NFC_B
                                                //| NfcAdapter.FLAG_READER_NFC_F
                                                //| NfcAdapter.FLAG_READER_NFC_V
                                                //| NfcAdapter.FLAG_READER_NFC_BARCODE
                                                //| NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS
                                                | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,
                                        null);
                        //extras);
                    }
                } else {
                    showMessage(getString(R.string.nfc_not_activated));
                }
            }
        }
    };*/
}
