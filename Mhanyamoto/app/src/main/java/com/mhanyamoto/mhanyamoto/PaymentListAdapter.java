package com.mhanyamoto.mhanyamoto;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.telephony.gsm.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by stark on 4/19/2015.
 */
public class PaymentListAdapter extends BaseAdapter {

    private String[] dateArray = {"20/10/2014", "25/11/2015", "30/12/2015"};
    private String[] amountArray = {"50", "10", "100"};
    private String[] statArray = {"Paid", "Paid", "Pending"};

    private int[] thumbArray = {R.drawable.clothes, R.drawable.jersey, R.drawable.furniture};

    private Context context;

    public PaymentListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return dateArray.length;
    }

    @Override
    public Object getItem(int position) {
        return dateArray[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.payment_item, null);
        }
        TextView date = ViewHolder.get(convertView, R.id.dateTxt);
        TextView pay = ViewHolder.get(convertView, R.id.payTxt);
        TextView amount = ViewHolder.get(convertView, R.id.amountTxt);
        TextView stat = ViewHolder.get(convertView, R.id.statTxt);
        date.setText(dateArray[position]);
        pay.setText("Payment Mhanyamoto");
        amount.setText(amountArray[position]);
        stat.setText(statArray[position]);

        return convertView;
    }

}
