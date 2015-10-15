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
import android.widget.ToggleButton;

import com.mhanyamoto.mhanyamoto.entity.Bid;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by stark on 4/19/2015.
 */
public class OrderListAdapter extends BaseAdapter {

    private String[] nameArray = {"CLOTHES", "JERSEY", "FURNITURE"};
    private Boolean[] statArray = {true, true, false};
    private String[] timeArray = {"2 Hours", "60 Minutes", "2 Days"};
    private String[] budgetArray = {"1000", "500", "5000"};

    private int[] thumbArray = {R.drawable.clothes, R.drawable.jersey, R.drawable.furniture};

    private Context context;

    List<Bid> bidList;

    public OrderListAdapter(Activity context,  List<Bid> result) {

        this.context = context;
        bidList=result;
    }

    @Override
    public int getCount() {
        return bidList.size();
    }

    @Override
    public Object getItem(int position) {
        return bidList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.order_item, null);
        }
        TextView title = ViewHolder.get(convertView, R.id.item_name);
        TextView time = ViewHolder.get(convertView, R.id.item_time);
        TextView budget = ViewHolder.get(convertView, R.id.item_budget);
        ImageView thumbs = ViewHolder.get(convertView, R.id.item_thumb);
        ToggleButton statBtn = ViewHolder.get(convertView, R.id.statBtn);

        title.setText(bidList.get(position).getTitle());
        time.setText(bidList.get(position).getDuration());
        budget.setText(bidList.get(position).getOpen_price());
        statBtn.setChecked(statArray[position]);

        Picasso.with(context)
                .load(bidList.get(position).getImage())
                .resize(150, 150)
                .centerInside()
                .into(thumbs);

        //Dihapus Karena menggunakan Picasso
        /*try {
            Bitmap pict = convertToBitmap(context.getResources().getDrawable(thumbArray[position]), 150, 150);
            pict = getRoundedBitmap(pict);
            thumbs.setImageBitmap(pict);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        return convertView;
    }

    public Bitmap convertToBitmap(Drawable drawable, int widthPixels, int heightPixels) {
        Bitmap mutableBitmap = Bitmap.createBitmap(widthPixels, heightPixels, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mutableBitmap);
        drawable.setBounds(0, 0, widthPixels, heightPixels);
        drawable.draw(canvas);

        return mutableBitmap;
    }

    public static Bitmap getRoundedBitmap(Bitmap bitmap) {
        final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(output);

        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawOval(rectF, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        bitmap.recycle();

        return output;
    }
}
