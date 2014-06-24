package wtf.gdg_wear_prject;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preview.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat;
import android.view.View;


public class MyActivity extends Activity {
    NotificationManagerCompat nManager;

    public static final int SIMPLE_NOT = 1;

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

}
