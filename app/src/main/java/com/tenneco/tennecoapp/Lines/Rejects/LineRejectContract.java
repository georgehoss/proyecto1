package com.tenneco.tennecoapp.Lines.Rejects;

import android.content.Context;

/**
 * Created by ghoss on 07/12/2018.
 */
public interface LineRejectContract {
    interface View{
        void initAdapters();
        void updateLine();
        void showAddEventDialog(Context context, final int reason);
    }
}
