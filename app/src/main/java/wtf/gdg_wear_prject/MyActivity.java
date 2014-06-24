package wtf.gdg_wear_prject;

import android.app.Activity;
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
        NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(this)
                .setContentText("Acabo de llegar al evento de Android Wear")
                .setContentTitle("GDG Vigo")
                .setSmallIcon(R.drawable.icon_gdg);

        nManager.notify(SIMPLE_NOT, nBuilder.build());
    }

}
