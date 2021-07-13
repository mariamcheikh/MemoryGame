package com.example.memorygame.Utils.Levels;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.memorygame.R;
import com.example.memorygame.Utils.Constants;
import com.example.memorygame.Utils.EasyLevelAdapter;
import com.example.memorygame.Utils.Result;
import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.ArrayList;


public class EasyLevel extends Fragment {

    private RecyclerView EasyLevelRecyclerView;
    public ArrayList<Integer> cards;
    public int CARDS[] = {
                R.drawable.card1,
                R.drawable.card2,
                R.drawable.card3,
                R.drawable.card4,
                R.drawable.card5,
                R.drawable.card6,

    };
    EasyFlipView flippedCard;
    public long RemainingTime;
    public boolean isPaused, isCancelled;
    Bundle b;
    private SharedPreferences pref;
    int pos, count, bestScore;


    public EasyLevel() {
        // Required empty public constructor
    }



    public void fragmentTransaction(Bundle b){
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        final Result r= new Result();
        r.setArguments(b);
        transaction.replace(R.id.layoutFragment, r);
        transaction.commit();
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_easy_level, container, false);

        EasyLevelRecyclerView = rootView.findViewById(R.id.easylevelview);
        b=new Bundle();
        b.putInt("level", Constants.LEVEL_EASY);
        pref = getActivity().getSharedPreferences(Constants.PREF_NAME,0);
        bestScore = pref.getInt(Constants.EASY_HIGH_KEY);

        ((TextView) rootView.findViewById(R.id.bestEasy)).append(bestScore+"");

        RecyclerView.LayoutManager lm = new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false);
        EasyLevelRecyclerView.setLayoutManager(lm);

        cards = new ArrayList<>();

        for (int card : CARDS){
            cards.add(card);
        }

        EasyLevelRecyclerView.setAdapter(new EasyLevelAdapter(cards));


        isCancelled = false;

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
                    isPaused = true;
                    AlertDialog.Builder pause = new AlertDialog.Builder(getContext());
                    pause.setTitle("Game paused");
                    pause.setMessage("Do you want to quit ?");
                    pause.setCancelable(false);
                    pause.setPositiveButton("Resume", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isPaused = false;
                    pause.setNegativeButton("Quit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isCancelled = true;
                            getFragmentManager().popBackStack();
                        }
                    });
                    pause.show();

                }


        });

        EasyLevelRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {

            @Override
            public boolean onInterceptTouchEvent(final RecyclerView rv, MotionEvent e) {
                final View child = rv.findChildViewUnder(e.getX(),e.getY());
                if (child != null){

                    final int position = rv.getChildAdapterPosition(child);

                    if (flippedCard == null){
                        flippedCard = (EasyFlipView) child;
                        pos = position;
                    }

                    else{
                        if (pos == position){
                            flippedCard=null;
                        }
                        else{
                            if (cards.get(pos).equals(cards.get(position))){
                                ((EasyFlipView) child).setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
                                    @Override
                                    public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                                        for (int i = 0; i < EasyLevelRecyclerView.getChildCount(); i++) {
                                            EasyFlipView child1 = (EasyFlipView) EasyLevelRecyclerView.getChildAt(i);
                                            child1.setEnabled(false);
                                        }
                                    }
                                });
                            }
                            else {
                                ((EasyFlipView) child).setOnFlipListener(new EasyFlipView.OnFlipAnimationListener() {
                                    @Override
                                    public void onViewFlipCompleted(EasyFlipView easyFlipView, EasyFlipView.FlipState newCurrentSide) {
                                        for (int i = 0; i < EasyLevelRecyclerView.getChildCount(); i++) {
                                            EasyFlipView child1 = (EasyFlipView) EasyLevelRecyclerView.getChildAt(i);
                                            child1.setEnabled(false);
                                        }
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                flippedCard.flipTheView();
                                                ((EasyFlipView) child).flipTheView();
                                                flippedCard = null;
                                                ((EasyFlipView) child).setOnFlipListener(null);

                                                for (int i = 0; i < EasyLevelRecyclerView.getChildCount(); i++) {
                                                    EasyFlipView child1 = (EasyFlipView) EasyLevelRecyclerView.getChildAt(i);
                                                    child1.setEnabled(true);
                                                }
                                            }
                                        }, 100);
                                    }
                                });
                            }
                        }

                    }
                }
                return false;
            }
            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {}
            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {}
        });
       // return rootView;
    }
 }


