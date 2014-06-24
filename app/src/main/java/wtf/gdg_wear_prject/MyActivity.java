package wtf.gdg_wear_prject;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import static android.app.Notification.*;
import static android.util.Log.d;


public class MyActivity extends Activity {
    NotificationManagerCompat nManager;
    TwitterRequest twitterRequest;

    public static final int SIMPLE_NOT = 1;
    public static final int VOICE_ID = 2;

    public static final String VOICE_RETURN_KEY = "wear_gdg_voices";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        nManager = NotificationManagerCompat.from(this);
        twitterRequest = new TwitterRequest();
        twitterRequest.setListener(OnTwitterListener);

    }

    TwitterListener OnTwitterListener = new TwitterListener() {
        @Override
        public void onTwitterResponse(List<Status> tweets) {
            int count = 0;

            ArrayList<Notification> notificationList = new ArrayList<Notification>(5);

            for (Status s: tweets) {
                if (count > 5)
                    break;

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(s.getCreatedAt());
                int hours = calendar.get(Calendar.HOUR_OF_DAY);
                int minutes = calendar.get(Calendar.MINUTE);
                int seconds = calendar.get(Calendar.SECOND);

                Notification page = new NotificationCompat.Builder(MyActivity.this)
                        .setContentTitle(s.getUser().getName())
                        .setSubText(hours+":"+minutes+":"+seconds)
                        .setSmallIcon(R.drawable.icon_microphone)
                        .setContentText(s.getText())
                        .build();

                notificationList.add(page);


                d("[DEBUG]", "Author: "+s.getUser().getName()+"\nAt: "+s.getCreatedAt().toString()+"\nText: "+s.getText());
                count ++;
            }

            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(MyActivity.this)
                    .setContentTitle("Best tweets")
                    .setContentText("The best tweets at last hour")
                    .setSmallIcon(R.drawable.icon_gdg);

            Notification wearNotification = new WearableNotifications.Builder(nBuilder)
                    .addPages(notificationList)
                    .build();

            nManager.notify(4, wearNotification);
        }
    };

    public void send_simple_notification (View v) {
        Intent directoryEvent = new Intent(Intent.ACTION_VIEW);
        directoryEvent.setData(Uri.parse("https://developers.google.com/events/5826146929213440/"));

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, directoryEvent, 0);

        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setContentText("Acabo de llegar al evento de Android Wear")
                .setContentTitle("GDG Vigo")
                .addAction(R.drawable.icon_event, "Aqui voy", pendingIntent)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.back_gdg))
                .setSmallIcon(R.drawable.icon_gdg);

        nManager.notify(SIMPLE_NOT, nBuilder.build());
    }



    public void send_voice_notification (View v) {

        Intent replyIntent = new Intent(MyActivity.this, ReplyActivity.class);
        PendingIntent pendingreply = PendingIntent.getActivity(this, 0, replyIntent, 0);

        // Objeto para manejar el input por voz
        RemoteInput remoteInput = new RemoteInput.Builder(VOICE_RETURN_KEY)
                .setLabel("Cuentame")
                .setAllowFreeFormInput(false)
                .setChoices(getResources().getStringArray(R.array.voice_response))
                .build();

        // Acción (Boton grande en en wear) el cual llevara al Pending intent
        WearableNotifications.Action replyAction = new WearableNotifications.Action.Builder(
                R.drawable.icon_microphone, "Cuentame", pendingreply)
                .addRemoteInput(remoteInput).build();

        // Builder para construir la notificacion del wearable
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setContentText("Acabo de llegar al evento de Android Wear")
                .setContentTitle("GDG Vigo")
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.back_gdg))
                .setSmallIcon(R.drawable.icon_gdg);

        // Notificacion del wearable
         Notification wearNotification = new WearableNotifications.Builder(nBuilder)
                 .addAction(replyAction)
                 .build();

        nManager.notify(VOICE_ID, wearNotification);
    }


    public void send_pages_notification (View v) {
        String string1 = getResources().getString(R.string.big_text1);
        String string2 = getResources().getString(R.string.big_text2);
        String string3 = getResources().getString(R.string.big_text3);

        BigTextStyle bigText1 = new BigTextStyle()
                .bigText(string1)
                .setBigContentTitle("El GDG Mola");

        BigTextStyle bigText2 = new BigTextStyle()
                .bigText(string1)
                .setBigContentTitle("El GDG es Awesome");

        BigTextStyle bigText3 = new BigTextStyle()
                .bigText(string2)
                .setBigContentTitle("El GDG No regala nada...");

        Notification notification1 = new Builder(this)
                .setStyle(bigText1)
                .setSmallIcon(R.drawable.icon_gdg)
                .build();

        Notification notification2 = new Builder(this)
                .setStyle(bigText2)
                .setSmallIcon(R.drawable.icon_gdg)
                .build();

        Notification notification3 = new Builder(this)
                .setStyle(bigText3)
                .setSmallIcon(R.drawable.icon_gdg)
                .build();

        NotificationCompat.Builder roonotificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.back_gdg))
                .setContentText("El contenido que viene a continuación te mostrará la esencia del GDG")
                .setContentTitle("GDG Vigo")
                .setSmallIcon(R.drawable.icon_gdg);

        WearableNotifications.Action join = new WearableNotifications.Action
                .Builder(R.drawable.icon_gdg, "Unirse", null)
                .build();

        Notification wearableNotification = new WearableNotifications.Builder(roonotificationBuilder)
                .addPage(notification1)
                .addPage(notification2)
                .addPage(notification3)
                .addAction(join)
                .build();

        nManager.notify(3, wearableNotification);
    }


    public void twitter_test (View v) {
        twitterRequest.execute();
    }
}

interface TwitterListener {
    public void onTwitterResponse (List<Status> tweets);
}

class TwitterRequest extends AsyncTask<Void, Void, List<Status>> {
    ConfigurationBuilder cb;
    TwitterFactory tf;
    Twitter twitter;
    TwitterListener listener;

    public void setListener(TwitterListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onPreExecute() {
        cb = new ConfigurationBuilder();

        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("8CUqyFb3ToNkTAE3dG3eEIdtH")
                .setOAuthConsumerSecret("S16X7sEGgOzbNmcTisVgvSTW0gVgfzxKQK8rozUecTuqA75ZlR")
                .setOAuthAccessToken("583784686-fPQNzOmed97EROF0Xi5laBhfqkGnUWajDupeMJF5")
                .setOAuthAccessTokenSecret("asKSfrwg57bNpVVuwmdNp0v4IFdx2z31yiUZDemeoXBLJ");

        tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();


        super.onPreExecute();
    }

    @Override
    protected List<twitter4j.Status> doInBackground(Void... voids) {
        List<twitter4j.Status> statuses = null;

        try {
             statuses = twitter.getHomeTimeline();

        } catch (TwitterException e) {
            e.printStackTrace();
        }

        return statuses;
    }

    @Override
    protected void onPostExecute(List<twitter4j.Status> statuses) {
        super.onPostExecute(statuses);

        listener.onTwitterResponse(statuses);
    }
}
