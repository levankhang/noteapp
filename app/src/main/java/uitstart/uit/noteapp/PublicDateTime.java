package uitstart.uit.noteapp;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Khang on 2/19/2017.
 */

public class PublicDateTime {
    public static final SimpleDateFormat FORMAT_DATE=new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat FORMAT_TIME=new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat FORMAT_CALENDAR=new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static  Calendar calendar;

    private MainActivity mainActivity;
    private NoteActionActivity noteActionActivity;

    public PublicDateTime(MainActivity mainActivity) {
        this.mainActivity=mainActivity;
        calendar=Calendar.getInstance();
    }

    public PublicDateTime(NoteActionActivity noteActionActivity){
        this.noteActionActivity = noteActionActivity;
        calendar=Calendar.getInstance();
    }

    public void changedDateAction(final boolean isFromMainActivity, final boolean isFromNoteActionActivity){

        DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                if(isFromMainActivity)
                    mainActivity.adater.refreshData(FORMAT_DATE.format(calendar.getTime()));

                if(isFromNoteActionActivity)
                    noteActionActivity.btnDate.setText(FORMAT_DATE.format(calendar.getTime()));
            }
        };

        initCalendar(isFromMainActivity, isFromNoteActionActivity);

        DatePickerDialog datePickerDialog=new DatePickerDialog(isFromMainActivity ? mainActivity: noteActionActivity,onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void initCalendar(boolean isFromMainActivity, boolean isFromNoteActionActivity) {
        if(isFromNoteActionActivity)
            try {
                Date d=null;
                d=FORMAT_CALENDAR.parse(noteActionActivity.btnDate.getText().toString()+" "+noteActionActivity.btnTime.getText().toString());
                calendar.setTime(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        else calendar=Calendar.getInstance();
    }


    // But default is from NoteActionActivity
    public void changedTimeAction(final boolean isFromMainActivity, final boolean isFromNoteActionActivity){
        TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND,0);

                if(isFromNoteActionActivity)
                    noteActionActivity.btnTime.setText(FORMAT_TIME.format(calendar.getTime()));
            }
        };
        initCalendar(isFromMainActivity,isFromNoteActionActivity);

        TimePickerDialog timePickerDialog=new TimePickerDialog(noteActionActivity,onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }

}
