package emerikbedouin.demonetiktpe;

import android.os.AsyncTask;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by guillaume on 12/02/16.
 */
public final class ConnexionDataWebService {

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
            if (params.getParameters() != null) {
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));

                // Get a set of the entries
                Set set = params.getParameters().entrySet();

                // Get an iterator
                Iterator i = set.iterator();

                while (i.hasNext()) {
                    Map.Entry me = (Map.Entry) i.next();
                    System.out.print(me.getKey() + ": ");
                    System.out.println(me.getValue());
                    writer.write(me.getKey() + "=" + me.getValue());
                }

                writer.flush();
                writer.close();
                os.close();
            }

            // Connexion à l'URL
            urlConnection.connect();

            System.out.println("Retour requete Http : " + urlConnection.getResponseCode());
            params.setResult("" + urlConnection.getResponseCode());

            // Si le serveur nous répond avec un code OK
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return urlConnection.getInputStream();
            }
        } catch (Exception ex) {
            System.out.println("Erreur d'execution : " + ex.getMessage());
        }
        return null;

    }
}
