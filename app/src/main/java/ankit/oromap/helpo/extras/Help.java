package ankit.oromap.helpo.extras;

import android.net.Uri;
import android.os.AsyncTask;

import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Help extends AsyncTask {
    private String username;
    private String lat;
    private String longi;
    public feeling_low ob;
    private String password;
    private String description;
    private Boolean b = false;
    public Help(String user,String lat,String longi,String password,String description)
    {
        this.username=user;
        this.lat = lat;
        this.longi = longi;
        this.password = password;
        this.description = description;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        try
        {
            Uri.Builder build = new Uri.Builder().appendQueryParameter("username",username).appendQueryParameter("description",description).appendQueryParameter("password",password).appendQueryParameter("lat",lat).appendQueryParameter("long",longi);
            String query = build.build().getEncodedQuery();
            URL u = new URL("http://helpo.oromap.in/addhelp.php");
            HttpURLConnection con = (HttpURLConnection)u.openConnection();
            con.setRequestMethod("POST");
            con.connect();
            OutputStream os = con.getOutputStream();
            os.write(query.getBytes("UTF-8"));
            os.flush();
            os.close();
            DataInputStream ir = new DataInputStream(con.getInputStream());
            String res = ir.readUTF();
            if(res.equals("200"))
            {
                b = true;
            }
            else
            {
                b = false;
            }
        }
        catch (Exception e){
            b=false;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        ob.feeling_low(b);
    }
}
