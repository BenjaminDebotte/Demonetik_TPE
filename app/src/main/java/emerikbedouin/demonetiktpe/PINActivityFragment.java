package emerikbedouin.demonetiktpe;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

import example.utils.StringUtils;

/**
 * Created by guillaume on 15/02/16.
 */
public class PINActivityFragment extends Fragment {

	private final int CIPHER_KEY = 0xFA6C7D87;
    private boolean consoleEditable;
    protected TextView textViewConsole;
    protected  boolean operationStart;

    private String resTransaction;

    public PINActivityFragment() {

    }

    // Méthodes de chiffrement du code PIN
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

        final String urlWebService = getActivity().getIntent().getStringExtra("url");

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

                int pin = Integer.parseInt(textViewConsole.getText().toString());
                int pinChiffre = encipher_PIN(pin);

                // Demande auto
                String fonctionWebService = "demandeauto";
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("pin", String.valueOf(pinChiffre));
                HttpRequestParameters request = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService, "POST", "text/plain", params);
                new DataWebService().execute(request).get();

                Toast.makeText(getActivity().getApplicationContext(), R.string.attente_demande_auto, Toast.LENGTH_LONG);
                // On attend 5 secondes
                Thread.sleep(5000);

                String fonctionWebService2 = "demandeautoresultat";
                HttpRequestParameters request2 = ClientWebService.getClientWebService(getActivity(), urlWebService + fonctionWebService2, "GET", "text/plain", null);
                new DataWebServiceReponse().execute(request2).get();

                Intent intent = new Intent(getActivity(), RecapitulatifActivity.class);
                intent.putExtra("url", urlWebService);
                intent.putExtra("pin", String.valueOf(pin));
                intent.putExtra("montant", getActivity().getIntent().getStringExtra("montant"));
                intent.putExtra("nom", getActivity().getIntent().getStringExtra("nom"));
                intent.putExtra("prenom", getActivity().getIntent().getStringExtra("prenom"));
                intent.putExtra("numCarte", getActivity().getIntent().getStringExtra("numCarte"));
                intent.putExtra("resTransaction", resTransaction);
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
                Log.e("PINActivity", ex.getMessage());
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

                    Log.e("Erreur PINActivity", "Pas de réponse du webservice");
                    return param[0];
                }

            } catch (IOException e) {

                Log.e("Erreur PINActivity", e.getMessage());
                return param[0];
            }
        }
    }

    // Fait la même chose que DataWebService mais lit la réponse envoyée par le webservice
    private class DataWebServiceReponse extends AsyncTask<HttpRequestParameters, Void, String> {

        protected String doInBackground(HttpRequestParameters... param) {
            try {


                InputStream is = ConnexionDataWebService.connexionToWebService(param[0]);
                if (is != null) {

                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    StringBuilder out = new StringBuilder();
                    String newLine = System.getProperty("line.separator");
                    String line;
                    while ((line = reader.readLine()) != null) {
                        out.append(line);
                        out.append(newLine);
                    }

                    resTransaction = out.toString();
                } else {

                    Log.e("Erreur PINActivity", "Pas de réponse du webservice");
                    resTransaction = "Réponse vide";
                }

            } catch (IOException e) {

                Log.e("Erreur PINActivity", e.getMessage());
                resTransaction = "Pas de réponse";
            }

            // Résultat de la transaction
            if(resTransaction.contains("1"))
                resTransaction = "Transaction autorisée";
            else if (resTransaction.contains("0"))
                resTransaction = "Transaction refusée";

            return resTransaction;
        }
    }
}