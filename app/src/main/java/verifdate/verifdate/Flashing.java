package verifdate.verifdate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class Flashing extends AppCompatActivity
{
    protected Connexion m_connexion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashing);
        Intent i = getIntent();
        m_connexion = (Connexion) i.getSerializableExtra("sampleObject");
        try{Thread.sleep(3000);}catch(Exception e){}
        StartQR();
    }

    protected void StartQR()
    {
        try
        {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");

            startActivityForResult(intent, 0);

        }
        catch (Exception e)
        {

            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                m_connexion.Envoyer(data.getStringExtra("SCAN_RESULT"));
            }
            if(resultCode == RESULT_CANCELED)
            {
                //handle cancel
            }
        }
        StartQR();
    }
}
