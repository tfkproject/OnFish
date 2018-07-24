package app.tfkproject.onfish.app;

import android.os.StrictMode;

public class Config {
    //link server
    public static final String URL = "http://203.153.21.11/app/onfish/api/";

    public void izinNetworkPolicy(){
        //dapatkan  izin untuk melakukan thread policy (proses Background AsycnTask)
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
}
