package verifdate.verifdate;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Activite_principale extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try
        {
            Thread.sleep(1000);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //On passe à la vue suivante
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
}
