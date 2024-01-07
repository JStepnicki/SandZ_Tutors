package SandZ.Tutors.activites;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import java.util.ArrayList;

import SandZ.Tutors.R;
import SandZ.Tutors.data.classes.Meeting;
import SandZ.Tutors.database_handlers.FirebaseManager;

public class MeetingsView extends AppCompatActivity {

    private FirebaseManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meetings);
        manager = new FirebaseManager(this);
        manager.getMeetingsForCurrentUser(successListener, failureListener);
    }


    private final OnSuccessListener<ArrayList<Meeting>> successListener = new OnSuccessListener<ArrayList<Meeting>>() {
        @Override
        public void onSuccess(ArrayList<Meeting> meetings) {
            if (meetings.isEmpty()) {
                Toast.makeText(MeetingsView.this, "Brak spotkań", Toast.LENGTH_SHORT).show();
            } else {
                ArrayList<String> meetingLinks = new ArrayList<>();
                for (Meeting meeting : meetings) {
                    String link = meeting.getLink();
                    if (link != null && !link.isEmpty()) {
                        meetingLinks.add(link);
                    }
                }

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(MeetingsView.this, android.R.layout.simple_list_item_1, meetingLinks);
                ListView meetingView = findViewById(R.id.meetingView);
                meetingView.setAdapter(arrayAdapter);
            }
        }
    };

    private final OnFailureListener failureListener = new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {
            Toast.makeText(MeetingsView.this, "Błąd: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    };
}
