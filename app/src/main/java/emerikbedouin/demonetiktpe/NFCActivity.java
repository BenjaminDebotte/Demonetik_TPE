package emerikbedouin.demonetiktpe;

import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.nfc.tech.NfcA;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static example.utils.StringUtils.convertByteArrayToHexString;
import static example.utils.StringUtils.convertHexStringToByteArray;
import static example.utils.StringUtils.removeSpaces;

/**
 * @link old version : http://www.java2s.com/Open-Source/Android_Free_Code/NFC/reader/org_docrj_smartcard_readerReaderActivity_java.htm
 * @link old version : http://www.java2s.com/Open-Source/Android_Free_Code/NFC/reader/org_docrj_smartcard_readerReaderXcvr_java.htm
 * @link last version : https://github.com/doc-rj/smartcard-reader
 */
public class NFCActivity extends AppCompatActivity {

    public static final String TAG = "CardReaderTest";

    private EditText editId;
    private EditText editSelect;
    private EditText editIncrement;

    private NfcAdapter adapter;
    //    private PendingIntent nfcintent;
    private IsoDep iso;

    private String[][] nfctechfilter = new String[][] { new String[] { NfcA.class.getName() } };

    // Parametre WebService
    String ipAddress = "192.168.43.233";
    String port = "8080";
    final String urlWebService = "http://"+ipAddress+":"+port+"/DemonetikWebService/demonetik/transaction/infoporteur";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        // VIEWS
        //editId = (EditText)findViewById(R.id.editId);
        //editSelect = (EditText)findViewById(R.id.editSelect);
        //editIncrement = (EditText)findViewById(R.id.editIncrement);

        adapter = NfcAdapter.getDefaultAdapter(this);
        if (adapter == null) {
            Toast.makeText(this, getString(R.string.nfc_unavailable), Toast.LENGTH_LONG).show();
            finish();
        }
//        nfcintent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!adapter.isEnabled()) {
            showMessage(getString(R.string.nfc_not_activated));
            return;
        }
        // register broadcast receiver
        IntentFilter filter = new IntentFilter(
                NfcAdapter.ACTION_ADAPTER_STATE_CHANGED);
        registerReceiver(mBroadcastReceiver, filter);

        // listen for type A tags/smartcards, skipping ndef check
        adapter.enableReaderMode(this, this, NfcAdapter.FLAG_READER_NFC_A
                | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);

//        adapter.enableForegroundDispatch(this, nfcintent, null, nfctechfilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        adapter.disableForegroundDispatch(this);

        unregisterReceiver(mBroadcastReceiver);
        adapter.disableReaderMode(this);
    }

//    protected void onNewIntent(Intent intent) {
////        super.onNewIntent(intent);
//        setIntent(intent);
//
//        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//        onTagDiscovered(tag);
//
//    }

    @Override
    public void showMessage(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NFCActivity.this, msg, Toast.LENGTH_LONG).show();
            }
        });
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
}