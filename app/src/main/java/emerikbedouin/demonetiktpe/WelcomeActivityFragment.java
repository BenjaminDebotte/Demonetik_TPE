package emerikbedouin.demonetiktpe;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;

import java.io.IOException;
import java.io.InputStream;

/**
 * A placeholder fragment containing a simple view.
 */
public class WelcomeActivityFragment extends Fragment {


    private String ip;
    private String port;
    private String path;
    private String urlWebService;


    public WelcomeActivityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

        // On récupère les données des paramètres
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ip = sp.getString("webservice_address_ip", getString(R.string.preference_webservice_default_ip));
        port = sp.getString("webservice_address_port", getString(R.string.preference_webservice_default_port));
        path = sp.getString("webservice_address_path", getString(R.string.preference_webservice_default_path));
        urlWebService = "http://"+ip+":"+port+"/"+path;

        Button btnTransaction = (Button) rootView.findViewById(R.id.btnNewTransaction);

        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fonctionWebService = "resettransaction";
                HttpRequestParameters request = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService, "GET", "text/plain", null);
                new DataWebService().execute(request);

                Intent intent = new Intent(getActivity(), TPEActivity.class);
                intent.putExtra("url", urlWebService);
                startActivity(intent);

            }
        });


        return rootView;
    }

    private class DataWebService extends AsyncTask<HttpRequestParameters, Void, HttpRequestParameters> {

        protected HttpRequestParameters doInBackground(HttpRequestParameters... param) {
            try {

                InputStream is = ConnexionDataWebService.connexionToWebService(param[0]);
                if (is != null) {

                    // traitement du retour si échéant

                    return param[0];
                } else {

                    Log.e("Erreur WelcomeActivity", "Pas de réponse du webservice");
                    return param[0];
                }

            } catch (IOException e) {

                Log.e("Erreur WelcomeActivity", e.getMessage());
                return param[0];
            }
        }
    }
}
