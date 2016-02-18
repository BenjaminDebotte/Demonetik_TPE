package emerikbedouin.demonetiktpe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class RecapitulatifActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recapitulatif);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String pin = getIntent().getStringExtra("pin");
        String montant = getIntent().getStringExtra("montant");
        String nom = getIntent().getStringExtra("nom");
        String prenom = getIntent().getStringExtra("prenom");
        String numCarte = getIntent().getStringExtra("numCarte");

        EditText editNom = (EditText) findViewById(R.id.editNom);
        EditText editNum = (EditText) findViewById(R.id.editNumCarte);
        EditText editMontant = (EditText) findViewById(R.id.editMontant);
        EditText editPIN = (EditText) findViewById(R.id.editPIN);
        EditText editResTransaction = (EditText) findViewById(R.id.editResTransaction);

        editNom.setText(prenom + " " + nom);
        editNum.setText(numCarte);
        editMontant.setText(montant + "€");
        editPIN.setText(pin);
    }
}
