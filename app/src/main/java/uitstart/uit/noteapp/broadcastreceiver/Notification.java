package uitstart.uit.noteapp.broadcastreceiver;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import uitstart.uit.noteapp.R;
import uitstart.uit.noteapp.activity.MainActivity;
import uitstart.uit.noteapp.activity.ShowInfoActivity;
import uitstart.uit.noteapp.database.SettingDataBase;
import uitstart.uit.noteapp.model.MySetting;
import uitstart.uit.noteapp.model.Note;


/**
 * Created by Khang on 2/24/2017.
 */
public class Notification extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        MainActivity.settingDataBase=new SettingDataBase(context.getApplicationContext());
        MySetting mySetting=MainActivity.settingDataBase.getMySetting();

        if(mySetting.getIs_notification()==1){{
            makeNotification(context,intent);
        }}

    }

    private void makeNotification(Context context, Intent intent) {

        Note n= (Note) intent.getSerializableExtra("note");

        Intent showInfoIntent=new Intent(context,ShowInfoActivity.class);
        showInfoIntent.putExtra("note",intent.getSerializableExtra("note"));
        PendingIntent pendingIntentShowInfo=PendingIntent.getActivity(context,n.getId(),showInfoIntent,PendingIntent.FLAG_ONE_SHOT);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //Uri uri=Uri.parse("android.resource://"+context.getPackageName()+"/"+R.raw.sound);

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context);
        builder.setContentTitle(context.getResources().getString(R.string.notification));
        builder.setContentText(context.getResources().getString(R.string.notification_detail));
        builder.setSmallIcon(R.drawable.ic_notification);
        builder.setContentIntent(pendingIntentShowInfo);
        builder.setSound(uri);
        builder.setAutoCancel(true);

        Bitmap bmp=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_notification),64,64,true);
        builder.setLargeIcon(bmp);

        NotificationManager notificationManager= (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(n.getId(),builder.build());
    }
}
