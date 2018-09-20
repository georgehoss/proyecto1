package com.tenneco.tennecoapp.Dialogs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.tenneco.tennecoapp.Model.Line;
import com.tenneco.tennecoapp.Model.WorkHour;
import com.tenneco.tennecoapp.R;

/**
 * Created by ghoss on 15/09/2018.
 */
public class ActualDialog extends DialogFragment {
    private WorkHour workHour;
    private String lineName="";
    private String shift="";
    private String date="";
    private String actual="";
    private String target="";
    private String comments="";
    private int position=-1;


    public ActualDialog() {
    }

    public static ActualDialog newInstance(WorkHour workHour, String lineName, String shift, String date, int position) {

        Bundle args = new Bundle();
        ActualDialog fragment = new ActualDialog();
        args.putString("name",lineName);
        args.putString("shift",shift);
        args.putString("date",date);
        args.putString("actual",workHour.getActuals());
        args.putString("target",workHour.getTarget());
        args.putString("comments",workHour.getComments());
        args.putInt("position",position);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_actual, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if (getArguments()!=null){
            lineName = getArguments().getString("name");
            date = getArguments().getString("date");
            shift = getArguments().getString("shift");
            actual = getArguments().getString("actual");
            target = getArguments().getString("target");
            comments = getArguments().getString("comments");
            position = getArguments().getInt("position");
        }



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_actual, null));
        return builder.create();
    }

    public interface NoticeDialogListener {
        void onDialogPositiveClick(WorkHour workHour);
    }
}
