package emerikbedouin.demonetiktpe;

import android.os.AsyncTask;
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

    public WelcomeActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_welcome, container, false);

       Button btnTransaction = (Button) rootView.findViewById(R.id.btnNewTransaction);

        btnTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Parametre WebService
                String ipAddress = "192.168.43.233";
                String port = "8080";
                final String urlWebService = "http://"+ipAddress+":"+port+"/DemonetikWebService/demonetik/transaction/";

                String fonctionWebService = "resettransaction";
                HttpRequestParameters request = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService, "GET", "text/plain", null);
                new DataWebService().execute(request);

                Intent intent = new Intent(getActivity(), TPEActivity.class);
                //Lancement de l'activité client
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

        // onPostExecute displays the results of the AsyncTask
        protected void onPostExecute(String result) {

            System.out.println("on Post time !");
        }
    }
}
