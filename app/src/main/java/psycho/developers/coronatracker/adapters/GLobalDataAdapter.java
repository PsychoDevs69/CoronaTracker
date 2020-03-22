package psycho.developers.coronatracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.model.GlobalDataModel;

public class GLobalDataAdapter extends RecyclerView.Adapter<GLobalDataAdapter.ViewHolder> {

    Context context;
    List<GlobalDataModel> list;

    public GLobalDataAdapter(Context context, List<GlobalDataModel> list) {
        this.context = context;
        this.list = list;
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

        holder.location.setText(model.getLocationName());
        holder.confirmed.setText(new DecimalFormat("#").format(model.getConfirmed()));
        holder.deaths.setText(new DecimalFormat("#").format(model.getDeaths()));
        holder.recovered.setText(new DecimalFormat("#").format(model.getRecovered()));


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView location, deaths, recovered, confirmed;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            location = itemView.findViewById(R.id.locationNameTextView);
            deaths = itemView.findViewById(R.id.deathsTextView);
            recovered = itemView.findViewById(R.id.recoveredTextView);
            confirmed = itemView.findViewById(R.id.confirmedCaseTextView);
        }
    }
}
