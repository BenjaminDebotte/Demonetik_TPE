package emerikbedouin.demonetiktpe;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class NFCThread extends Fragment implements NfcAdapter.ReaderCallback{


    private NfcAdapter adapterNfc;

    public NFCThread() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_nfc, container, false);


        adapterNfc = NfcAdapter.getDefaultAdapter(this.getActivity());

        if (!adapterNfc.isEnabled()) {

            ///Afficher message
            showMessage("NFC not activated");
            System.out.println("NFC not activated");
        }

        return rootView;
    }

    @Override
    public void onTagDiscovered(Tag tag) {

    }



    public void showMessage(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }
}
