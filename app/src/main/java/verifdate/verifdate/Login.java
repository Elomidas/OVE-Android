package verifdate.verifdate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.FileOutputStream;

public class Login extends AppCompatActivity
{
    protected Connexion m_connexion;
    protected FileOutputStream m_file;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        m_connexion = null;
        Button b_valider = (Button) findViewById(R.id.b_valider);
        b_valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(Connexion())
                {
                    /*
                    Intent intent = new Intent(Login.this, Flashing.class);
                    intent.putExtra("Connexion", m_connexion);
                    startActivity(intent);
                    */
                    StartQR();
                }
                else
                {
                    TextView tv = ((TextView)findViewById(R.id.tv_info));
                    tv.setText(m_connexion.Message());
                }
            }
        });
    }

    //Test de connexion
    protected boolean Connexion()
    {
        String str_ip = ((TextView)findViewById(R.id.input_ip)).getText().toString();
        int input_port = Integer.parseInt(((TextView)findViewById(R.id.input_port)).getText().toString());
        m_connexion = new Connexion(str_ip, input_port);
        return (m_connexion.Message().length() == 0);
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
        String str = "";
        if (requestCode == 0)
        {
            if (resultCode == RESULT_OK)
            {
                m_connexion.Envoyer(str = data.getStringExtra("SCAN_RESULT"));
            }
            if(resultCode == RESULT_CANCELED)
            {
                //handle cancel
            }
        }
        System.err.println(str);

        try {
            m_file = new FileOutputStream("/sdcard/Download/LogDate.txt");
            m_file.write(str.getBytes());
            m_file.close();
        }catch(Exception e){}
        StartQR();
    }
}
