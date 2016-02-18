package emerikbedouin.demonetiktpe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * A placeholder fragment containing a simple view.
 */
public class TPEActivityFragment extends Fragment {

    private boolean consoleEditable;
    protected TextView textViewConsole;
    protected  boolean operationStart;


    public TPEActivityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tpe, container, false);

        // Parametre WebService
        String ipAddress = "192.168.43.233";
        String port = "8080";
        final String urlWebService = "http://"+ipAddress+":"+port+"/DemonetikWebService/demonetik/transaction/";


        // initialisation de la transaction : Requete Webservice
        String fonctionWebService = "inittransaction";
        HttpRequestParameters request = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService, "GET", "text/plain", null);
        new DataWebService().execute(request);


        consoleEditable = true;
        operationStart = false;

        textViewConsole = (TextView) rootView.findViewById(R.id.textViewConsole);
        textViewConsole.setText("Entrez le montant de la transaction");

        //Ajout des listener aux boutons
        rootView.findViewById(R.id.button0).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button1).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button2).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button3).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button4).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button5).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button6).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button7).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button8).setOnClickListener(new btnPinpadOnClickListener());
        rootView.findViewById(R.id.button9).setOnClickListener(new btnPinpadOnClickListener());

        rootView.findViewById(R.id.buttonA).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String fonctionWebService = "resettransaction";
                HttpRequestParameters request = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService, "GET", "text/plain", null);
                new DataWebService().equals(request);

                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
            }
        });

        rootView.findViewById(R.id.buttonC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(consoleEditable){
                    if(textViewConsole.getText().length() > 0) {
                        String str = textViewConsole.getText().toString().substring(0, textViewConsole.getText().toString().length()-1);
                        textViewConsole.setText(str);
                    }
                }
            }
        });

        rootView.findViewById(R.id.buttonV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    // On envoie le montant au webservice
                    String fonctionWebService = "montant";
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("montant", textViewConsole.getText().toString());
                    HttpRequestParameters request = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService, "POST", "text/plain", params);
                    new DataWebService().execute(request);
                    //textViewConsole.setText("Approchez carte sans contact");
                    consoleEditable = false;

                    Intent intent = new Intent(getContext(), NFCActivity.class);
                    intent.putExtra("montant", textViewConsole.getText().toString());
                    startActivity(intent);
                }
                catch (Exception ex) {
                    System.out.println("Erreur validation du montant : " + ex.getMessage());
                }
            }
        });

        return rootView;
    }







    /**
     * OnClickListener pour les boutons du pinpad
     */
    public class btnPinpadOnClickListener implements View.OnClickListener {

        public void onClick(View v) {

            try{
                String txte = ((Button) v).getText().toString();
                int chiffre = Integer.parseInt(((Button) v).getText().toString());

                if(consoleEditable){
                    String txt= "";
                    if(operationStart == true){
                        txt = textViewConsole.getText().toString();

                    }
                    textViewConsole.setText(txt+chiffre);

                    operationStart = true;
                }
            }
            catch(Exception ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    private class DataWebService extends AsyncTask<HttpRequestParameters, Void, HttpRequestParameters> {

        protected HttpRequestParameters doInBackground(HttpRequestParameters... param) {
            try {

                InputStream is = ConnexionDataWebService.connexionToWebService(param[0]);
                if (is != null) {

                    // traitement du retour si échéant

                    return param[0];
                } else {

                    Log.e("Erreur TPEActivity", "Pas de réponse du webservice");
                    return param[0];
                }

            } catch (IOException e) {

                Log.e("Erreur TPEActivity", e.getMessage());
                return param[0];
            }
        }

        // onPostExecute displays the results of the AsyncTask
        protected void onPostExecute(String result) {

            System.out.println("on Post time !");
        }
    }
}
