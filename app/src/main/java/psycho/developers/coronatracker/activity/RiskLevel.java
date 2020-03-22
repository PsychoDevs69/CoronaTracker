package psycho.developers.coronatracker.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.Utils.PrecautionContent;

public class RiskLevel extends AppCompatActivity {

    TextView actions, level,toolbarTitle;
    MaterialCardView upper, lower;
    String[] actionsArr = {
            PrecautionContent.actions1,
            PrecautionContent.actions2,
            PrecautionContent.actions4,
            PrecautionContent.actions5,
            PrecautionContent.actions6,

    };
    String levels[] = {"Low", "Medium", "High"};

    NestedScrollView mainQuiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_risk_level);

        actions = findViewById(R.id.actions);
        level = findViewById(R.id.level);
        toolbarTitle=findViewById(R.id.toolbarTitle);

        upper = findViewById(R.id.upperCard);
        lower = findViewById(R.id.lowerCard);

        mainQuiz = findViewById(R.id.mainQuiz);
    }

    public void ResultBtn(View view) {
        mainQuiz.setVisibility(View.GONE);
        toolbarTitle.setText("Results");
        if (view == findViewById(R.id.low1)) {
            actions.setText(actionsArr[0]);
            level.setText(levels[0]);
        }
        if (view == findViewById(R.id.low2)) {
            actions.setText(actionsArr[1]);
            level.setText(levels[0]);

        }
        if (view == findViewById(R.id.low3)) {
            actions.setText(actionsArr[2]);
            level.setText(levels[0]);

        }
        if (view == findViewById(R.id.low4)) {
            actions.setText(actionsArr[3]);
            level.setText(levels[1]);

        }
        if (view == findViewById(R.id.low5)) {
            actions.setText(actionsArr[4]);
            level.setText(levels[2]);

        }
        upper.setVisibility(View.VISIBLE);
        lower.setVisibility(View.VISIBLE);

    }
}
