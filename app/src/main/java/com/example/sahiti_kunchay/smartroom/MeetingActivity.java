package com.example.sahiti_kunchay.smartroom;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Calendar;

public class MeetingActivity extends AppCompatActivity {
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    GridView gridView;
    ArrayList<Item> gridArray = new ArrayList<Item>();
    CustomGridViewAdapter customGridAdapter;


    ImageView datePick;
    TextView timePick;
    ImageView timePicker;
    int hourz, minutez;
    TextView some;

    /*Integer[] imageIDs = {
            R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp,
            R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp,
            R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp,
            R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp,
            R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp,
            R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp, R.drawable.ic_desktop_mac_black_24dp,
    };

    String[] roomNames = {
            "Superset", "Einstein", "Binomial",
            "Superset", "Einstein", "Binomial",
            "Superset", "Einstein", "Binomial",
            "Superset", "Einstein", "Binomial",
            "Superset", "Einstein", "Binomial",
            "Superset", "Einstein", "Binomial"
    };*/

    GridView androidGridView;
    Button viewrooms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meeting);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        dateView = (TextView) findViewById(R.id.dateview);
        showDate(year, month+1, day);
        some = (TextView) findViewById(R.id.textView12);

        datePick = (ImageView) findViewById(R.id.imageView);
        timePick = (TextView) findViewById(R.id.textView12);
        timePicker = (ImageView) findViewById(R.id.imageView2);
        viewrooms = (Button) findViewById(R.id.viewrooms);

        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(MeetingActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        timePick.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
                timePickerDialog.show();
            }
        });
        datePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDate();
            }
        });

        /*androidGridView = (GridView) findViewById(R.id.gridview_android_example);
        androidGridView.setAdapter(new ImageAdapterGridView(this));

        androidGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent,
                                    View v, int position, long id) {
                Toast.makeText(getBaseContext(), "Grid Item " + (position + 1) + " Selected", Toast.LENGTH_LONG).show();
            }
        });*/

        viewrooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gridView.setVisibility(View.VISIBLE);
            }
        });
        Bitmap availableIcon = BitmapFactory.decodeResource(MeetingActivity.this.getResources(), R.drawable.ic_desktop_mac_black_24dp);
        //Bitmap availableIcon = drawableToBitmap(R.drawable.ic_date_range_black_24dp);

        //Drawable drawable = getResources().getDrawable(R.drawable.ic_desktop_mac_black_24dp);
        //Bitmap availableIcon = ((BitmapDrawable) drawable).getBitmap();
        Bitmap busyIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_desktop_mac_blac_24dp);
        Item k = new Item(availableIcon, "Superset");

        gridArray.add(k);
        gridArray.add(new Item(availableIcon,"Einstien"));
        gridArray.add(new Item(availableIcon,"Vertex"));
        gridArray.add(new Item(availableIcon,"Tangent"));
        gridArray.add(new Item(availableIcon,"Binomial"));
        gridArray.add(new Item(availableIcon,"Agni"));
        gridArray.add(new Item(availableIcon,"Subset"));
        gridArray.add(new Item(availableIcon,"Badami"));
        gridArray.add(new Item(busyIcon,"CV Raman"));
        gridArray.add(new Item(busyIcon,"Alpha"));
        gridArray.add(new Item(busyIcon,"Beta"));
        gridArray.add(new Item(busyIcon,"Gamma"));

        gridView = (GridView) findViewById(R.id.gridview_android_example);
        customGridAdapter = new CustomGridViewAdapter(this, R.layout.row_grid, gridArray);
        gridView.setAdapter(customGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String name = gridArray.get(position).getTitle();
                final AlertDialog.Builder b = new AlertDialog.Builder(MeetingActivity.this);
                b.setTitle(name);
                if(name.equals("Superset") || name.equals("Alpha")){
                    b.setMessage("The room is booked for the timeslot. You have been added to the waitlist. You shall be notified " +
                            "when the room is free");
                    b.setPositiveButton("OK", null);
                }
                else {
                    b.setMessage("Capacity: 30 \nProjecter: YES \nVC: YES");
                    b.setPositiveButton("BOOK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent mIntent = new Intent(MeetingActivity.this, MeetingIdActivity.class);
                            startActivity(mIntent);
                        }
                    });
                }
                /*b.setMessage("The room is booked for the timeslot. You have been added to the waitlist. You shall be notified " +
                        "when the room is free");
                b.setPositiveButton("OK", null);*/
                
                b.show();
            }
        });
    }

    @SuppressWarnings("deprecation")
    public void setDate() {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
