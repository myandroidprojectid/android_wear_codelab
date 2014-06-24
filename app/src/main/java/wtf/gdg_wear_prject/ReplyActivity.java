package wtf.gdg_wear_prject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class ReplyActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reply_layout);

        TextView replyTextView = (TextView) findViewById(R.id.response_text);

        // Aqui obtenemos el texto recibido por la notificacion
        String voiceString = getIntent().getStringExtra(MyActivity.VOICE_RETURN_KEY);

        if (voiceString != null && !voiceString.equals("")) {
            replyTextView.setText(voiceString);
        }
    }
}
