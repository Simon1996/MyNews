package simonbabikyan96gmail.com.mynews;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.ServiceConfigurationError;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Admin on 26.12.2016.
 */
public class Notification extends Service {
    String datanews;
    String titlenotif;
    String destnotif;
    MyAsynk asynk;
    final String SAVED_TEXT = "saved_text";
    String checker;
    SharedPreferences sPref;
    @Override
    public void onCreate() {
        super.onCreate();
        Timer timer = new Timer();
        timer.schedule(new UpdateTimeTask(), 0, 18000000); //тикаем каждые 30 мinute без задержки 1800000
    }
    //задача для таймера
    //Проверяем на новую запись.
    class UpdateTimeTask extends TimerTask {
        public void run() {
            sPref = getSharedPreferences("MyPref",MODE_PRIVATE);
            String savedText = sPref.getString(SAVED_TEXT, "");
            checker = sPref.getString(savedText, "0");

            if(datanews != checker){
                asynk = new MyAsynk();
                asynk.showNotification = true;
                asynk.execute();
            } else {
                asynk = new MyAsynk();
                asynk.showNotification = false;
                asynk.execute();
            }
//            createNotification(getApplicationContext());//пушим уведомление
//            asynk = new MyAsynk();
//            asynk.execute();
        }
        }
    class MyAsynk extends AsyncTask<Void,Void,StringBuilder> {
        public boolean showNotification;
        @Override
        //работа в бекграунде
        protected StringBuilder doInBackground(Void... voids) {
            StringBuilder stringBuilder = new StringBuilder();
            String key = "0aa2713d5a1a4aad9a914c9294f6a22b";
            try {
                URL url = new URL("https://newsapi.org/v1/articles?source=the-next-web&sortBy=latest&apiKey=" + key);
                URLConnection uc = url.openConnection();
                uc.connect();
                BufferedInputStream in = new BufferedInputStream(uc.getInputStream());
                int ch;
                while ((ch = in.read()) != -1) {
                    stringBuilder.append((char) ch);
                }
            } catch (Exception e) {
            }
            return stringBuilder;
        }
        @Override
        protected void onPostExecute(StringBuilder stringBuilder) {
            try {
                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                JSONArray array = jsonObject.getJSONArray("articles");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    String title = object.getString("title");
                    String desc = object.getString("description");
                    String newsdata = object.getString("publishedAt");
                    datanews = newsdata;
                    titlenotif = title;
                    destnotif = desc;
                }
                // Create notification here on demand
                if(showNotification == true) {
                    createNotification(getApplicationContext());
                }
            }
            catch (Exception e){e.printStackTrace();}
        }
    }
    private void createNotification(Context context) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder ncBuilder = new NotificationCompat.Builder(context);
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
        ncBuilder.setVibrate(new long[]{500});
        ncBuilder.setLights(Color.WHITE, 3000, 3000);
        ncBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        ncBuilder.setContentIntent(pIntent);
        ncBuilder.setContentTitle(titlenotif + "");
        ncBuilder.setContentText(destnotif + "");
        ncBuilder.setTicker("You have news!");
        ncBuilder.setSmallIcon(R.drawable.news_icon);
        ncBuilder.setAutoCancel(true);

        manager.notify((int)System.currentTimeMillis(),ncBuilder.build());
    }
    public IBinder onBind(Intent arg0) {
        return null;
    }
}