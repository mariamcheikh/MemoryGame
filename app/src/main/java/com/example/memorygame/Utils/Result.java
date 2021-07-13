package com.example.memorygame.Utils;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.memorygame.R;

import static com.example.memorygame.Utils.Constants.EASY_HIGH_KEY;


public class Result extends Fragment {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private int bestEasyScore , bestHardScore;

    public Result() {

    }


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_result, container, false);


        pref = getActivity().getSharedPreferences("HighScore",0);
        editor= pref.edit();

        bestEasyScore = pref.getInt(EASY_HIGH_KEY,22);


        Bundle b=getArguments();
        if (b.getString("Data").equals("win")){
            if (Integer.valueOf(b.get("level").toString()) == Constants.LEVEL_EASY){
                if (Integer.valueOf(b.get("Time").toString()) < bestEasyScore){
                    editor.putInt(EASY_HIGH_KEY);
                    editor.apply();
                    ((TextView) rootView.findViewById(R.id.newhigh)).setText("New High Score!");
                }
            }

            ((TextView) rootView.findViewById(R.id.desc1)).setText("You won!");
        }
        else{
             ((TextView) rootView.findViewById(R.id.desc1)).setText("Nice  try, but you lost.");
        }
        return rootView;
    }

}
