package com.vakilapp.lawbooks.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vakilapp.lawbooks.R;


/**
 * Created by root on 2/1/16.
 */
public class Snackbarer {

    // Toast Functions
    public static void showError(Context _ctx) {
        showMsg(_ctx, _ctx.getResources().getString(R.string.generic_error));
    }

    public static void showNetworkError(Context _ctx) {
        showMsg(_ctx, _ctx.getResources().getString(R.string.network_error));
    }

    public static void showMsg(Context _ctx, String _msg) {
        Toast.makeText(_ctx, _msg, Toast.LENGTH_LONG).show();
    }

    public static void showMsg(Context _ctx, int _msg) {
        Toast.makeText(_ctx, _msg, Toast.LENGTH_LONG).show();
    }

    // SnackBar Functions

    public static void showMsg(ViewGroup root, int id) {
        Snackbar.make(root, id, Snackbar.LENGTH_LONG).show();
    }

    public static void showError(ViewGroup root) {
        Snackbar.make(root, R.string.generic_error, Snackbar.LENGTH_LONG).show();
    }

    public static void showNetworkError(ViewGroup root) {
        Snackbar.make(root, R.string.network_error, Snackbar.LENGTH_LONG).show();
    }


    public static void showMsg(ViewGroup root, String id) {
        Snackbar.make(root, id, Snackbar.LENGTH_LONG).show();
    }

    public static void showMsg(ViewGroup root, String id, int actionString, final SnackbarerInterface action) {


        Snackbar snackbar = Snackbar
                .make(root, id, Snackbar.LENGTH_LONG)
                .setAction(actionString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        action.onActionOccured();
                    }
                });
        snackbar.show();
    }

    public static void showMsg(ViewGroup root, int id, int actionString, final SnackbarerInterface action, int MESSAGE_LENGTH) {


        Snackbar snackbar = Snackbar
                .make(root, id, MESSAGE_LENGTH)
                .setAction(actionString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        action.onActionOccured();
                    }
                });
        snackbar.show();
    }


    public static void showMsg(ViewGroup root, int id, int actionString, final SnackbarerInterface action) {


        Snackbar snackbar = Snackbar
                .make(root, id, Snackbar.LENGTH_LONG)
                .setAction(actionString, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        action.onActionOccured();
                    }
                });
        snackbar.show();
    }

    /**
     * Created by root on 1/1/16.
     */
    public static interface SnackbarerInterface {
        void onActionOccured();
    }
}
