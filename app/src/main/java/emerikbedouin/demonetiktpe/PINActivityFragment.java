package emerikbedouin.demonetiktpe;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SyncStatusObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
 * Created by guillaume on 15/02/16.
 */
public class PINActivityFragment extends Fragment {

	private final int CIPHER_KEY = 0xFA6C7D87;
    private boolean consoleEditable;
    protected TextView textViewConsole;
    protected  boolean operationStart;


    public PINActivityFragment() {


    }

    private int encipher_PIN(int plain_pin) {
    	return plain_pin ^ CIPHER_KEY;
    }
    private int decipher_PIN(int cipher_pin) {
    	return cipher_pin ^ CIPHER_KEY;
    }
    
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tpe, container, false);

        // Parametre WebService
        String ipAddress = "192.168.43.233";
        String port = "8080";
        final String urlWebService = "http://"+ipAddress+":"+port+"/DemonetikWebService/demonetik/transaction/";


        consoleEditable = true;
        operationStart = false;

        textViewConsole = (TextView) rootView.findViewById(R.id.textViewConsole);
        textViewConsole.setText("Entrez le code PIN");

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
                new DataWebService().execute(request);

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

                // Envoi des infos porteur
                String fonctionWebService = "infoporteur";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("nom", getActivity().getIntent().getStringExtra("nomPorteur"));
                params.put("prenom", getActivity().getIntent().getStringExtra("prenomPorteur"));
                params.put("numcarte", getActivity().getIntent().getStringExtra("numCarte"));
                HttpRequestParameters request = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService, "POST", "text/plain", params);
                new DataWebService().execute(request);
                consoleEditable = false;

                // Demande auto
                String fonctionWebService2 = "demandeauto";
                HashMap<String, String> params2 = new HashMap<String, String>();
                params.put("pin", textViewConsole.getText().toString());
                HttpRequestParameters request2 = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService2, "POST", "text/plain", params);
                new DataWebService().execute(request2);

                Log.e("demande auto", "done");

                //wait(3000);


                String fonctionWebService3 = "demandeautoresultat";
                HttpRequestParameters request3 = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService3, "GET", "text/plain", null);
                new DataWebService().execute(request3);

                Log.e("demande auto resultat", "done");

                Intent intent = new Intent(getActivity().getApplicationContext(), RecapitulatifActivity.class);
                intent.putExtra("pin", Integer.parseInt(params.get("pin")));
                startActivity(intent);
            }
            catch (Exception ex) {
                Log.e("Erreur PIN", "Erreur validation du code PIN : " + ex.getMessage());
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

                    Log.e("coucoucoucoucou", is.toString());
                    return param[0];
                } else {

                    Log.e("Erreur PINActivity", "Pas de réponse du webservice");
                    return param[0];
                }

            } catch (IOException e) {

                Log.e("Erreur PINActivity", e.getMessage());
                return param[0];
            }
        }

        // onPostExecute displays the results of the AsyncTask
        protected void onPostExecute(String result) {

            System.out.println("on Post time !");
        }
    }
    
    
    
    
}