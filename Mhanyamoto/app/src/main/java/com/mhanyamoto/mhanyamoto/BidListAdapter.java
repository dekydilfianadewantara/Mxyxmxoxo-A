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
import android.graphics.BitmapShader;
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

import com.mhanyamoto.mhanyamoto.entity.Bid;
import com.mhanyamoto.mhanyamoto.service.Service;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by stark on 4/19/2015.
 */
public class BidListAdapter extends BaseAdapter {

    Service mService;
    List<Bid> bidList;
    Context context;
    String idRequest , price;
    private static LayoutInflater inflater=null;
    public BidListAdapter(Activity mainActivity, List<Bid> result) {
        // TODO Auto-generated constructor stub
        bidList=result;
        context=mainActivity;

    }


    private String[] fromArray = {"LONDON", "NEW YORK", "PAPUA"};
    private String[] toArray = {"MANCHESTER", "HAWAII", "BRUNEI"};
    private String[] timeArray = {"2 Hours", "60 Minutes", "2 Days"};
    private String[] budgetArray = {"1000", "500", "5000"};
    private String[] currentArray = {"600", "300", "4700"};
    private int[] thumbArray = {R.drawable.clothes, R.drawable.jersey, R.drawable.furniture};



    public BidListAdapter(Context context) {
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.bid_item, null);
        }
        TextView title = ViewHolder.get(convertView, R.id.item_name);
        TextView from = ViewHolder.get(convertView, R.id.item_from);
        TextView to = ViewHolder.get(convertView, R.id.item_to);
        TextView time = ViewHolder.get(convertView, R.id.item_time);
        TextView budget = ViewHolder.get(convertView, R.id.item_budget);
        TextView currentBid = ViewHolder.get(convertView, R.id.item_current_bid);
        ImageView thumbs = ViewHolder.get(convertView, R.id.item_thumb);
        final EditText bidValue = ViewHolder.get(convertView, R.id.item_bid_value);
        ImageButton bidBtn = ViewHolder.get(convertView, R.id.bidBtn);
        Picasso.with(context)
                .load(bidList.get(position).getImage())
                .resize(150, 150)
                .centerInside()
                .into(thumbs);
        title.setText(bidList.get(position).getTitle());
        from.setText(bidList.get(position).getDesc_from());
        to.setText(bidList.get(position).getDesc_to());
        time.setText(bidList.get(position).getDuration());
        budget.setText(bidList.get(position).getOpen_price());
        currentBid.setText(bidList.get(position).getCurrent_bid());

        //Dihapus Karena menggunakan Picasso
        /*try {
            Bitmap pict = convertToBitmap(context.getResources().getDrawable(thumbArray[position]), 150, 150);
            pict = getRoundedBitmap(pict);
            thumbs.setImageBitmap(pict);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        final int pos = position + 1;
        bidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Bid Confirmation");
                alertDialog.setMessage("Are you sure you want to bid this?");
                alertDialog.setPositiveButton("YES",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    price = bidValue.getText().toString();
                                    idRequest = bidList.get(position).getId();
                                    String mess = "#BID#$" + bidValue.getText().toString() + "#" + pos + "#id_user";
                                    sendSMS("+263775406288", mess);
                                } catch (Exception e) {
                                    // TODO: handle exception
                                    e.printStackTrace();
                                }
                            }
                        });
                alertDialog.setNegativeButton("NO",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                if (bidValue.getText().toString().isEmpty() || Integer.parseInt(bidValue.getText().toString()) == 0) {
                    Toast.makeText(context, "Bid value should more than 0", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.show();
                }
            }
        });
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

    private void sendSMS(String phoneNumber, String message) {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(context, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(context, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "BID SENT",
                                Toast.LENGTH_SHORT).show();

                        hawkInit();
                        String idUser = Hawk.get("idUser", "0");

                        initAPI();
                        mService.setBid(idUser, idRequest, price, new Callback<String>() {
                            @Override
                            public void success(String s, Response response) {

                            }

                            @Override
                            public void failure(RetrofitError error) {

                            }
                        });
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(context, "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(context, "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(context, "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(context, "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        context.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(context, "BID DELIVERED",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(context, "BID NOT DELIVERED",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                context.unregisterReceiver(this);
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
    }

    private void initAPI() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(context.getResources().getString(R.string.host))
                .build();

        mService = restAdapter.create(Service.class);
    }

    private void hawkInit() {
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.MEDIUM)
                .setStorage(HawkBuilder.newSqliteStorage(context))
                .setLogLevel(LogLevel.FULL)
                .build();
    }
}
