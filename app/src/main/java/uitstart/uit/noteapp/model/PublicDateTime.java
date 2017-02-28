package uitstart.uit.noteapp.model;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import uitstart.uit.noteapp.activity.MainActivity;
import uitstart.uit.noteapp.activity.NoteActionActivity;

/**
 * Created by Khang on 2/19/2017.
 */

public class PublicDateTime {
    public static final SimpleDateFormat FORMAT_DATE=new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat FORMAT_TIME=new SimpleDateFormat("HH:mm");
    public static final SimpleDateFormat FORMAT_CALENDAR=new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public static  Calendar calendar;

    public PublicDateTime() {
        calendar=Calendar.getInstance();
    }

    public void changeDate(final NoteActionActivity noteActionActivity){

        DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                noteActionActivity.btnDate.setText(FORMAT_DATE.format(calendar.getTime()));
            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(noteActionActivity,onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }


    public void changedTime(final NoteActionActivity noteActionActivity){
        TimePickerDialog.OnTimeSetListener onTimeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                calendar.set(Calendar.SECOND,0);
                noteActionActivity.btnTime.setText(FORMAT_TIME.format(calendar.getTime()));
            }
        };

        TimePickerDialog timePickerDialog=new TimePickerDialog(noteActionActivity,onTimeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true);
        timePickerDialog.show();
    }


    public void viewNoteOfDay(final MainActivity mainActivity) {
        DatePickerDialog.OnDateSetListener onDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                    mainActivity.adater.refreshData(FORMAT_DATE.format(calendar.getTime()));
            }
        };

        DatePickerDialog datePickerDialog=new DatePickerDialog(mainActivity,onDateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
