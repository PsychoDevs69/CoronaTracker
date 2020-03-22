package psycho.developers.coronatracker.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import psycho.developers.coronatracker.R;
import psycho.developers.coronatracker.Utils.PrecautionContent;
import psycho.developers.coronatracker.adapters.PrecautionAdapter;
import psycho.developers.coronatracker.model.PrecautionsModel;

public class Precaution extends Fragment {

    View view;
    private RecyclerView precRecycler;
    private List<PrecautionsModel> precList=new ArrayList<>();
    private String[] titles;
    private String[] contents;
    private String[] whyContents;
    public Precaution() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_precaution, container, false);
        init();

        return view;
    }

    private void init() {
        initElements();
    }

    private void initElements() {
        precRecycler=view.findViewById(R.id.precRecycler);
        titles= PrecautionContent.getTitles();
        contents= PrecautionContent.getContents();
        whyContents= PrecautionContent.getWhyContents();

        for(int i=0;i<titles.length;i++){
            precList.add(new PrecautionsModel(titles[i],contents[i],whyContents[i]));
        }
        precRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        precRecycler.setAdapter(new PrecautionAdapter(getActivity(),precList));
    }

}
