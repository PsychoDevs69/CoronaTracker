package psycho.developers.coronatracker.adapters;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.List;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.model.StateWiseDataModel;

public class StateWiseListAdapter extends RecyclerView.Adapter<StateWiseListAdapter.ViewHolder> {

    Context context;
    List<StateWiseDataModel> list;
    boolean isExpanded = false;
    private DecimalFormat decimalFormat = new DecimalFormat("#");

    public StateWiseListAdapter(Context context, List<StateWiseDataModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_wise_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StateWiseDataModel model = list.get(position);

        holder.stateName.setText(model.getName());
        holder.confirmedIndian.setText(decimalFormat.format(model.getActive()));
        holder.confirmedForeigner.setText("covid19india.org");
        holder.deaths.setText(decimalFormat.format(model.getDeaths()));
        holder.recovered.setText(decimalFormat.format(model.getRecovered()));

        holder.confirmedTotal.setText(decimalFormat.format(model.getConfirmed()));

        holder.helpline.setText(model.getHelpline().replace(",", "\n"));

        cardAnimation(holder);

    }

    private void cardAnimation(final ViewHolder holder) {
        holder.parentCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isExpanded) {
                    TransitionManager.beginDelayedTransition(holder.parentCard, new AutoTransition());
                    holder.moreDataLayout.setVisibility(View.GONE);
                    holder.viewMore.setVisibility(View.VISIBLE);
                    isExpanded = false;
                } else {
                    TransitionManager.beginDelayedTransition(holder.parentCard, new AutoTransition());
                    holder.viewMore.setVisibility(View.GONE);
                    holder.moreDataLayout.setVisibility(View.VISIBLE);
                    isExpanded = true;
                }
            }
        });
    }

    /*private String getTotalValue(StateWiseDataModel model) {
        return decimalFormat.format(model.getConfirmedForeign() + model.getConfirmedIndian());
    }*/

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView stateName, recovered, confirmedTotal, confirmedIndian,
                confirmedForeigner, helpline, deaths, viewMore;

        LinearLayout moreDataLayout;
        MaterialCardView parentCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stateName = itemView.findViewById(R.id.locationNameTextView);
            recovered = itemView.findViewById(R.id.recoveredTextView);
            confirmedTotal = itemView.findViewById(R.id.confirmedCaseTextView);
            confirmedIndian = itemView.findViewById(R.id.indianCitizen_expandedCard);
            confirmedForeigner = itemView.findViewById(R.id.foreigner_expandedCard);
            helpline = itemView.findViewById(R.id.helpline_expandedCard);
            deaths = itemView.findViewById(R.id.deathsTextView);
            viewMore = itemView.findViewById(R.id.viewMoreText);
            moreDataLayout = itemView.findViewById(R.id.moreDataLayout);
            parentCard = itemView.findViewById(R.id.stateDataCard);


        }
    }
}
