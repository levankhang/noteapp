package uitstart.uit.noteapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;

import com.akexorcist.localizationactivity.LocalizationActivity;

import java.util.ArrayList;

import uitstart.uit.noteapp.R;
import uitstart.uit.noteapp.model.MySetting;

public class SettingActivity extends LocalizationActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private ImageView imgback;
    private Switch swNotification;
    private Spinner spLanguage;
    private ArrayAdapter adapterSpinnerLanguage;
    private ArrayList list_spinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        addControls();
        addEvents();
        loadSetting();
    }


    private void loadSetting() {
        if(MainActivity.mySetting.getIs_notification()==1)
            swNotification.setChecked(true);
        else
            swNotification.setChecked(false);

        if(MainActivity.mySetting.getLanguage_code().equals("en"))
            spLanguage.setSelection(0);
        else
            spLanguage.setSelection(1);


    }

    private void addEvents() {
        imgback.setOnClickListener(this);

        spLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    MainActivity.mySetting.setLanguage_code("en");
                    setLanguage(MainActivity.mySetting.getLanguage_code());
                }

                if(position==1){
                    MainActivity.mySetting.setLanguage_code("vi");
                    setLanguage(MainActivity.mySetting.getLanguage_code());
                }

                MainActivity.settingDataBase.updateSetting(MainActivity.mySetting);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        swNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    MainActivity.mySetting.setIs_notification(1);
                else
                    MainActivity.mySetting.setIs_notification(0);

                MainActivity.settingDataBase.updateSetting(MainActivity.mySetting);
            }
        });
    }

    private void addControls() {
        toolbar= (Toolbar) findViewById(R.id.toolbar_setting);
        imgback= (ImageView) findViewById(R.id.imgback);
        swNotification= (Switch) findViewById(R.id.swNotification);
        spLanguage= (Spinner) findViewById(R.id.spLanguage);

        list_spinner=new ArrayList();
        list_spinner.add("English");
        list_spinner.add("Việt Nam");

        adapterSpinnerLanguage=new ArrayAdapter(this,android.R.layout.simple_spinner_item,list_spinner);
        adapterSpinnerLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spLanguage.setAdapter(adapterSpinnerLanguage);

    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        switch (id){
            case R.id.imgback: finish(); break;
        }
    }
}
