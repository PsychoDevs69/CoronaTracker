package psycho.developers.coronatracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.model.PrecautionsModel;

public class PrecautionAdapter extends RecyclerView.Adapter<PrecautionAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView title,content,whyContent,why;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.precTitle);
            content=itemView.findViewById(R.id.precContent);
            whyContent=itemView.findViewById(R.id.precWhyContent);
            why=itemView.findViewById(R.id.precWhy);
        }
    }

    private Context context;
    private List<PrecautionsModel> precList;

    public PrecautionAdapter(Context context, List<PrecautionsModel> precList) {
        this.context = context;
        this.precList = precList;
    }

    @NonNull
    @Override
    public PrecautionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.precaution_cardview,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PrecautionAdapter.ViewHolder holder, int position) {
        PrecautionsModel precModel=precList.get(position);
        holder.title.setText(precModel.title);
        if(precModel.content.equals(""))
            holder.content.setVisibility(View.GONE);
        if(precModel.whyContent.equals("")){
            holder.whyContent.setVisibility(View.GONE);
            holder.why.setVisibility(View.GONE);
        }
        holder.content.setText(precModel.content);
        holder.whyContent.setText(precModel.whyContent);

    }

    @Override
    public int getItemCount() {
        return precList.size();
    }
}
