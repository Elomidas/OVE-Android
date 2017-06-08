package verifdate.verifdate;

import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

/**
 * Created by GAMING on 08/06/2017.
 */

public class Connexion implements Serializable
{
    protected DatagramSocket m_socket;
    protected InetAddress m_adresse;
    protected String m_message;
    protected int m_port;
    protected int m_num;

    public Connexion(String adresse, int port)
    {
        m_message = "";
        m_port = port;
        m_num = 1;
        if(Adresse(adresse) && Socket());
    }

    public String Message()
    {
        return m_message;
    }

    public boolean Envoyer(String data)
    {
        String[] str = data.split("-");
        String strTot = str[0] + "-" + str[1] + "-" + str[2] + "-" + str[3] + "-" + str[4] + "\n"; //AAAA-MM-JJ-RRRRRR-QQQQ\n
        ByteBuffer b = ByteBuffer.allocate(128)
                        .put(Conv(m_num))
                        .put(strTot.getBytes())
                        .slice();
        boolean res = sendBytes(b.array(), m_num);
        m_num++;
        return res;
    }

    protected boolean Adresse(String adresse)
    {
        try
        {
            m_adresse = InetAddress.getByName(adresse);
        }
        catch(Exception e)
        {
            m_message = "Impossible de resoudre l'adressse";
            return false;
        }
        return true;
    }

    protected boolean Socket()
    {
        try
        {
            m_socket = new DatagramSocket();
        }
        catch(Exception e)
        {
            m_message = "Impossible d'ouvrir une nouvelle socket : \n" + e.getMessage();
            return false;
        }
        return true;
    }

    protected static byte[] Conv(int i)
    {
        byte[] res = new byte[2];
        res[1] = (byte) (i & 0x000000FF);
        res[0] = (byte) ((i & 0x0000FF00) / 0x00000100);
        return res;
    }

    protected static int Conv(byte[] b)
    {
        int res = (b[0] << 8) | b[1];
        return res;
    }

    //Envoi d'un DTG jusqu'à 3 fois, test de réception du ACK
    protected boolean sendBytes(byte[] tab, int numDTG)
    {
        DatagramPacket dp = new DatagramPacket(tab, tab.length, m_adresse, m_port);
        int i;
        //On essaye d'envoyer le DTG jusqu'à 3 fois si on ne reçoit aucun ACK
        for (i = 0; i < 3; i = (ACK(numDTG) ? 5 : (i + 1)))
        {
            try
            {
                m_socket.send(dp);
            }
            catch (Exception e)
            {
                return false;
            }
        }
        if (i == 3)
            return false;
        return true;
    }

    //Retourne "true" à la la réception du ACK correspondant au numéro du DTG,
    //		   "false" si on passe par un Timeout
    protected boolean ACK(int numBloc)
    {
        return true;
        /*
        DatagramPacket packet;
        boolean ack = false;
        try
        {
            m_socket.setSoTimeout(3000);
            //On reçoit jusqu'à recevoir un ACK pour le DTG voulu ou avoir un Timeout
            while (!ack)
            {
                packet = new DatagramPacket(new byte[512], 512);
                m_socket.receive(packet);
                //On récupère l'adresse et le port de l'emetteur
                m_adresse = packet.getAddress();
                m_port = packet.getPort();
                //On regarde si on a reçu un ACK
                byte[] buffer = packet.getData();
                int LSB = buffer[3];
                int MSB = buffer[2];
                if (LSB < 0)
                    LSB = 256 + LSB;
                if (MSB < 0)
                    MSB = 256 + MSB;
                int opCode = (buffer[0] * 256) + buffer[1];
                int no = (MSB * 256) + LSB;
                if (no < 0)
                    no *= -1;
                //Si on a un ACK pour le bon DTG, on retourne "true"
                ack = ((opCode == 4) && (no == numBloc));
            }
        }
        catch (Exception e)
        {
            //Timeout : on retourne "false"
            return false;
        }
        return ack;
        */
    }
}
