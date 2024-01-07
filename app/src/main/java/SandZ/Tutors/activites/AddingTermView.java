package SandZ.Tutors.activites;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.Timestamp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import SandZ.Tutors.R;
import SandZ.Tutors.database_handlers.FirebaseManager;

public class AddingTermView extends AppCompatActivity {
    private FirebaseManager manager;
    private EditText dateEditText, timeEditText, linkEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_term);
        manager = new FirebaseManager(this);
        CalendarView calendarView = findViewById(R.id.calendarView);
        dateEditText = findViewById(R.id.dateEditText);
        timeEditText = findViewById(R.id.timeEditText);
        linkEditText = findViewById(R.id.linkEditText);
        Button addButton = findViewById(R.id.addTermButton);
        calendarView.setOnDateChangeListener((view, year, month, day) -> {
            String selectedDate = year + "-" + (month + 1) + "-" + day;
            dateEditText.setText(selectedDate);
        });

        addButton.setOnClickListener(v -> {
            String selectedDate = dateEditText.getText().toString();
            String selectedTime = timeEditText.getText().toString();
            String link = linkEditText.getText().toString();
            Timestamp timestamp = prepareTimestamp(selectedDate, selectedTime);

            if (timestamp != null) {
                manager.addTermToFirebase(manager.getCurrentUser().getUid(), timestamp,false,link);
            } else {
                Toast.makeText(AddingTermView.this, "Invalid date or time format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private Timestamp prepareTimestamp(String date, String time) {
        try {
            String dateTimeStr = date + " " + time;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Calendar calendar = Calendar.getInstance();
            Date parsedDate = sdf.parse(dateTimeStr);
            if (parsedDate != null) {
                calendar.setTime(parsedDate);
                calendar.add(Calendar.HOUR_OF_DAY, -1);
                parsedDate = calendar.getTime();
            }
            return new Timestamp(parsedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}