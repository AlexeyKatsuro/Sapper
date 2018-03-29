package com.dedalexey.sapper;

import android.graphics.Point;
import android.util.Log;
import android.widget.Button;

/**
 * Created by Alexey on 07.10.2017.
 */

public class Cell {
    private int mPosition;
    private int mCountBombs;
    private boolean isShown;
    private boolean isMarked;
    private boolean isBomb;


    private static class StateText {
        public static final String MARK = "\uD83D\uDEA9";
        public static final String BOMB = "\uD83D\uDCA3";
    }
    public Cell() {
    }

    public Cell(int position) {
        mPosition = position;
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public boolean isShown() {
        return isShown;
    }

    public void setShown(boolean shown) {
        isShown = shown;
    }

    public boolean isMarked() {
        return isMarked;
    }

    public void setMarked(boolean marked) {
        isMarked = marked;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public void increaseCountBombs(){
        mCountBombs++;
    }

    public int getCountBombs() {
        return mCountBombs;
    }

    public String getText() {
       if(isShown){
           if(isBomb){
               return StateText.BOMB;
           } else {
               return String.valueOf(mCountBombs);
           }
       } else if(isMarked){
           return StateText.MARK;
       } else {
           return "";
       }
    }

}
