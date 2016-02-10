package emerikbedouin.demonetiktpe;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * A placeholder fragment containing a simple view.
 */
public class TPEActivityFragment extends Fragment {

    private boolean consoleEditable;
    protected TextView textViewConsole;
    protected  boolean operationStart;
    protected Button btnPinpadV, btnPinpadC, btnPinpadA;


    public TPEActivityFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_tpe, container, false);

        // Parametre WebService
        String ipAddress = "10.214.11.249";
        String port = "13588";
        final String urlWebService = "http://"+ipAddress+":"+port+"/DemonetikWebService/demonetik/transaction/";


        // initialisationd e la transaction : Requete Webservice
        String fonctionWebService = "inittransaction";
        getClientWebService( urlWebService+fonctionWebService, "GET", "text/plain", null);


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
                getClientWebService( urlWebService+fonctionWebService, "GET", "text/plain", null);

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

                    String fonctionWebService = "montant";
                    LinkedList<String> params = new LinkedList<String>();
                    params.add(textViewConsole.getText().toString());
                    getClientWebService(urlWebService + fonctionWebService, "POST", "text/plain", params);
                    textViewConsole.setText("Approchez carte sans contact");
                    consoleEditable = false;
                }
                catch (Exception ex) {
                    System.out.println("Erreur validation du montant : " + ex.getMessage());
                }
            }
        });





        return rootView;
    }




    // Partie connexion webService
    public void getClientWebService(String urlWebService, String requestType, String dataReturnType,LinkedList<String> parameters){
        // Check de la connexion
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            HttpRequestParameters params = new HttpRequestParameters(urlWebService, requestType, dataReturnType, parameters);
            new DataWebService().execute(params);

        } else {
            // display error connexion ko
        }

    }

    public InputStream connexionToWebService(HttpRequestParameters params) throws IOException {
        try {

            URL urlObject = new URL(params.getUrl());

            // Ouverture de la connexion
            HttpURLConnection urlConnection = (HttpURLConnection) urlObject.openConnection();
            urlConnection.setReadTimeout(15000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod(params.getRequestType());
            urlConnection.setRequestProperty("Accept", params.getDataReturnType());


            //Parametres eventuels
            if(params.getParameters() != null){
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write("montant="+params.getParameters().getFirst());

                writer.flush();
                writer.close();
                os.close();
            }

            // Connexion à l'URL
            urlConnection.connect();

            System.out.println("Retour requete Http : "+ urlConnection.getResponseCode());
            params.setResult(""+urlConnection.getResponseCode());

            // Si le serveur nous répond avec un code OK
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return urlConnection.getInputStream();
            }
        } catch (Exception ex) {
            System.out.println("Erreur d'execution : "+ex.getMessage());
        }
        return null;

    }

    private class DataWebService extends AsyncTask<HttpRequestParameters, Void, HttpRequestParameters> {

        protected HttpRequestParameters doInBackground(HttpRequestParameters... param) {
            try {

                InputStream is = connexionToWebService(param[0]);
                if( is!=null ){

                    // traitement du retour si échéant

                    return param[0];
                }
                else{


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


}
