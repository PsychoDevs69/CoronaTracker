package psycho.developers.coronatracker.adapters;

import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.text.DecimalFormat;
import java.util.List;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.model.GlobalDataModel;

public class GLobalDataAdapter extends RecyclerView.Adapter<GLobalDataAdapter.ViewHolder> {

    private Context context;
    private List<GlobalDataModel> list;
    private GlobalDataModel indianData;
    private DecimalFormat decimalFormat = new DecimalFormat("#");
    private boolean expanded = false;

    public GLobalDataAdapter(Context context, List<GlobalDataModel> list, GlobalDataModel indianData) {
        this.context = context;
        this.list = list;
        this.indianData = indianData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.global_toll_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlobalDataModel model = list.get(position);


        if (model.getLocationName().equalsIgnoreCase("India") &&
                !indianData.getLocationName().equalsIgnoreCase("NA")){
            Log.e("REACHED", "onBindViewHolder: ");

            holder.location.setText(model.getLocationName());
            holder.confirmed.setText(decimalFormat.format(indianData.getConfirmed()));
            holder.deaths.setText(decimalFormat.format(indianData.getDeaths()));
            holder.recovered.setText(decimalFormat.format(indianData.getRecovered()));
            holder.activeCases.setText(decimalFormat.format(indianData.getActiveCases()));

            //holder.todayDeaths.setText(decimalFormat.format(indianData.getTodayDeaths()));
            //holder.todayCases.setText(decimalFormat.format(indianData.getTodayCases()));

            holder.todayDeaths.setText("NA");
            holder.todayCases.setText("NA");

        } else {
            holder.location.setText(model.getLocationName());
            holder.confirmed.setText(decimalFormat.format(model.getConfirmed()));
            holder.deaths.setText(decimalFormat.format(model.getDeaths()));
            holder.recovered.setText(decimalFormat.format(model.getRecovered()));
            holder.activeCases.setText(decimalFormat.format(model.getActiveCases()));
            holder.todayDeaths.setText(decimalFormat.format(model.getTodayDeaths()));
            holder.todayCases.setText(decimalFormat.format(model.getTodayCases()));
        }



        holder.criticalCases.setText(decimalFormat.format(model.getCriticalCases()));
        holder.casesPerMillion.setText(decimalFormat.format(model.getCasesPerMillion()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout moreDataLayout;
        MaterialCardView globalDataCard;
        RelativeLayout cardParentLayout;
        TextView location, deaths, recovered, confirmed,
                activeCases, todayDeaths, todayCases, criticalCases, casesPerMillion, viewMoreText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.locationNameTextView);
            deaths = itemView.findViewById(R.id.deathsTextView);
            recovered = itemView.findViewById(R.id.recoveredTextView);
            confirmed = itemView.findViewById(R.id.confirmedCaseTextView);

            activeCases = itemView.findViewById(R.id.activeCases_expandedCard);
            todayDeaths = itemView.findViewById(R.id.deathsToday_expandedCard);
            todayCases = itemView.findViewById(R.id.casesToday_expandedCard);
            criticalCases = itemView.findViewById(R.id.criticalCases_expandedCard);
            casesPerMillion = itemView.findViewById(R.id.casesPerMillion_expandedCard);
            globalDataCard = itemView.findViewById(R.id.globalDataCard);
            moreDataLayout = itemView.findViewById(R.id.moreDataLayout);
            cardParentLayout = itemView.findViewById(R.id.cardParentLayout);
            viewMoreText = itemView.findViewById(R.id.viewMoreText);

            globalDataCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expanded) {
                        TransitionManager.beginDelayedTransition(globalDataCard, new AutoTransition());
                        moreDataLayout.setVisibility(View.GONE);
                        viewMoreText.setVisibility(View.VISIBLE);
                        expanded = false;
                    } else {
                        TransitionManager.beginDelayedTransition(globalDataCard, new AutoTransition());
                        viewMoreText.setVisibility(View.GONE);
                        moreDataLayout.setVisibility(View.VISIBLE);
                        expanded = true;
                    }
                }
            });
        }
    }
}
