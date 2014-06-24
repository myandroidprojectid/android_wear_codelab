package wtf.gdg_wear_prject;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.preview.support.wearable.notifications.RemoteInput;
import android.preview.support.wearable.notifications.WearableNotifications;
import android.support.v4.app.NotificationCompat;
import android.view.View;


public class MyActivity extends Activity {
    NotificationManagerCompat nManager;

    public static final int SIMPLE_NOT = 1;
    public static final String VOICE_RETURN_KEY = "wear_gdg_voices";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        nManager = NotificationManagerCompat.from(this);

    }

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


    public static final int VOICE_ID = 2;
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

}
