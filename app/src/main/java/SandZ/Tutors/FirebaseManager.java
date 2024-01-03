package SandZ.Tutors;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.auth.AuthResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FirebaseManager {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    private FirebaseUser user;

    public FirebaseManager(Context context) {
        this.context = context;
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
    }



    public void signOut() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return user;
    }

    public void getUserData(String fieldName, OnDataRetrievedListener listener) {
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            DocumentReference userRef = db.collection("users").document(user.getUid());

            // Fetch the document
            userRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            String data = document.getString(fieldName);
                            listener.onDataRetrieved(data != null ? data : "");
                        } else {
                            listener.onDataRetrieved(""); // Document does not exist
                        }
                    } else {
                        listener.onDataRetrieved(""); // Failed to fetch data
                    }
                }
            });
        } else {
            listener.onDataRetrieved(""); // User not logged in
        }
    }
    public void registerUser(String email, String password, String name, String surname, boolean isTeacher) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // Create a user object with additional details
                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", email);
                                userData.put("name", name);
                                userData.put("surname", surname);
                                userData.put("userType", isTeacher ? "teacher" : "student");

                                // Store the user in Firestore
                                db.collection("users").document(user.getUid())
                                        .set(userData, SetOptions.merge())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Create a "meetings" collection for the user
                                                    db.collection("users").document(user.getUid())
                                                            .collection("meetings").document("placeholder")
                                                            .set(new HashMap<>()); // Placeholder document

                                                    if (isTeacher) {
                                                        // Additional fields for teachers
                                                        Map<String, Object> teacherData = new HashMap<>();
                                                        teacherData.put("link", ""); // Initialize with an empty link
                                                        teacherData.put("subjects", new ArrayList<>());
                                                        teacherData.put("grades", new ArrayList<>());

                                                        // Update the teacher information in Firestore
                                                        db.collection("users").document(user.getUid())
                                                                .set(teacherData, SetOptions.merge())
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            // Create a "terms" collection for teachers
                                                                            db.collection("users").document(user.getUid())
                                                                                    .collection("terms").document("placeholder")
                                                                                    .set(new HashMap<>()); // Placeholder document for terms

                                                                            Toast.makeText(context, "Account created and user information added to Firestore.", Toast.LENGTH_SHORT).show();
                                                                            Intent intent = new Intent(context, Login.class);
                                                                            context.startActivity(intent);
                                                                            ((Register) context).finish();
                                                                        } else {
                                                                            Toast.makeText(context, "Account created, but failed to add teacher information to Firestore.", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                    } else {
                                                        // Student registration
                                                        Toast.makeText(context, "Account created and user information added to Firestore.", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(context, Login.class);
                                                        context.startActivity(intent);
                                                        ((Register) context).finish();
                                                    }
                                                } else {
                                                    Toast.makeText(context, "Account created, but failed to add user information to Firestore.", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            } else {
                                Toast.makeText(context, "User is null.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

