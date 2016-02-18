package emerikbedouin.demonetiktpe;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Created by guillaume on 12/02/16.
 */
public class ClientWebService {

    public ClientWebService() {

    }

    // Partie connexion webService
    public static HttpRequestParameters getClientWebService(Activity activity, String urlWebService, String requestType, String dataReturnType, HashMap<String, String> parameters){
        // Check de la connexion
        ConnectivityManager connMgr = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            HttpRequestParameters params = new HttpRequestParameters(urlWebService, requestType, dataReturnType, parameters);
            return params;

        } else {
            // display error connexion ko
            return null;
        }
    }
}
