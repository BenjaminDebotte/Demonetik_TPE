package emerikbedouin.demonetiktpe;

import android.content.Context;
import android.nfc.Tag;
import android.nfc.tech.IsoDep;
import android.util.Log;

import java.io.IOException;

import static example.utils.StringUtils.*;

/**
 * Created by Joan on 09/12/2015.
 */
public class NFCThread implements Runnable {

    public static final String TAG = "CardReaderTestThread";

    public interface UiCallback {
        void showMessage(String msg);
        void setEditText(int id, String txt);
    }

    private Context context;
    private Tag tag;
    private UiCallback cb;
    private int montant;

    public NFCThread(Context context, Tag tag, UiCallback cb, int montant) {
        this.context = context;
        this.tag = tag;
        this.cb = cb;
        this.montant = montant;
    }

    private IsoDep iso;

    @Override
    public void run() {
        String id = convertByteArrayToHexString(tag.getId());
        Log.d(TAG, "Tag detect " + id);
        cb.showMessage("Tag detect "+id);
        cb.setEditText(R.id.editId, id);

        iso = IsoDep.get(tag);
        if (iso == null) {
            cb.showMessage(context.getString(R.string.non_iso));
            return;
        }
        try {
            iso.connect();
        } catch (IOException e) {
            cb.showMessage(context.getString(R.string.iso_connect_error));
            Log.e(TAG, context.getString(R.string.iso_connect_error) + " : " + e.getMessage());
            return;
        }

        try {
            String ret = send_apdu("00 A4 04 00 09 F0 01 02 03 04 48 43 45 01 00");
            ret = convertHexToString(ret);
            String[] infosPorteur = ret.split(" ");
            String nomPorteur = infosPorteur[0] + " " + infosPorteur[1];
            String numCarte = infosPorteur[2] + " " + infosPorteur[3] + " " + infosPorteur[4] + " " + infosPorteur[5];
            cb.setEditText(R.id.nomPorteur, nomPorteur);
            cb.setEditText(R.id.numCarte, numCarte);

            ret = send_apdu("B0 40 00 00 01 " + Integer.toHexString(montant));
            cb.setEditText(R.id.editIncrement, ret);

            //ret = send_apdu("B0 20 00 00 04 " + Integer.toHexString(pin));

        } catch (IOException e) {
            cb.showMessage(context.getString(R.string.iso_read_error));
            Log.e(TAG, context.getString(R.string.iso_read_error) + " : " + e.getMessage());
        } finally {
            try {
                iso.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String send_apdu(String sapdu) throws IOException {
        Log.i(TAG, "SEND -> " + sapdu);
        final byte [] apdu = convertHexStringToByteArray(removeSpaces(sapdu));
        byte [] recv = iso.transceive(apdu);
        String ret = convertByteArrayToHexString(recv);
        Log.i(TAG, "RECV <- " + ret);
        return ret;
    }
}