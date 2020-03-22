package psycho.developers.coronatracker.activity;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import psycho.developers.coronatracker.R;

public class ImportantAnnouncement extends AppCompatActivity {

    TextView showDataTextView;
   // Firebas
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_important_announcment);

        showDataTextView = findViewById(R.id.showDataTextView);
        firestore=FirebaseFirestore.getInstance();
        GetAnnouncement();
    }

    private void GetAnnouncement() {

        firestore.collection("Announcement")
                .document("important")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                         String announcement = task.getResult().getString("value");
                         showData(announcement);
                        }
                    }
                });
    }

    private void showData(String data) {

        if(data.equals("")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                showDataTextView.setText(Html.fromHtml("No Announcements Yet.", Html.FROM_HTML_MODE_COMPACT));
            } else {
                showDataTextView.setText(Html.fromHtml("No Announcements Yet."));
            }
        }else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                showDataTextView.setText(Html.fromHtml(data, Html.FROM_HTML_MODE_COMPACT));
            } else {
                showDataTextView.setText(Html.fromHtml(data));
            }
        }

    }
}
