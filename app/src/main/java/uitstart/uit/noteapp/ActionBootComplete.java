package uitstart.uit.noteapp;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Khang on 2/24/2017.
 */
public class ActionBootComplete extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AlarmManager alarmManager= (AlarmManager) context.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        MainActivity.noteDataBase=new NoteDataBase(context.getApplicationContext());
        for(Note i: MainActivity.noteDataBase.getAllNote())
            alarmManager.set(AlarmManager.RTC_WAKEUP,createCalendar(i.getDate(),i.getTime()).getTimeInMillis(),i.createPendingIntent(context.getApplicationContext()));
    }

    public Calendar createCalendar(String date, String time){
        Calendar calendar=Calendar.getInstance();
        Date d=null;
        try {
            d=PublicDateTime.FORMAT_CALENDAR.parse(date+" "+time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.setTime(d);
        calendar.set(Calendar.SECOND,0);
        return calendar;
    }
}
