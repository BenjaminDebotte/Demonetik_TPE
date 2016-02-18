package emerikbedouin.demonetiktpe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.InputStream;

public class RecapitulatifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapitulatif);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String urlWebService = getIntent().getStringExtra("url");

        Button nouvelleTransaction = (Button) findViewById(R.id.nouvelleTransaction);
        nouvelleTransaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                startActivity(intent);
            }
        });

        String pin = getIntent().getStringExtra("pin");
        String montant = getIntent().getStringExtra("montant");
        String nom = getIntent().getStringExtra("nom");
        String prenom = getIntent().getStringExtra("prenom");
        String numCarte = getIntent().getStringExtra("numCarte");
        String resTransaction = getIntent().getStringExtra("resTransaction");

        EditText editNom = (EditText) findViewById(R.id.editNom);
        EditText editNum = (EditText) findViewById(R.id.editNumCarte);
        EditText editMontant = (EditText) findViewById(R.id.editMontant);
        EditText editPIN = (EditText) findViewById(R.id.editPIN);
        EditText editResTransaction = (EditText) findViewById(R.id.editResTransaction);

        editNom.setText(prenom + " " + nom);
        editNum.setText(numCarte);
        editMontant.setText(montant + "€");
        editPIN.setText(pin);
        editResTransaction.setText(resTransaction);

        // Fin de la transaction
        String fonctionWebService = "endtransaction";
        HttpRequestParameters request = ClientWebService.getClientWebService(this, urlWebService + fonctionWebService, "GET", "text/plain", null);
        new DataWebService().execute(request);
    }

    private class DataWebService extends AsyncTask<HttpRequestParameters, Void, HttpRequestParameters> {

        protected HttpRequestParameters doInBackground(HttpRequestParameters... param) {
            try {

                InputStream is = ConnexionDataWebService.connexionToWebService(param[0]);
                if (is != null) {

                    // traitement du retour si échéant

                    return param[0];
                } else {

                    Log.e("Erreur Recapitulatif", "Pas de réponse du webservice");
                    return param[0];
                }

            } catch (IOException e) {

                Log.e("Erreur Recapitulatif", e.getMessage());
                return param[0];
            }
        }
    }
}
