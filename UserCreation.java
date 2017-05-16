package com.gap22.community.apartment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gap22.community.apartment.Database.Member;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static com.gap22.community.apartment.R.id.unit;

public class UserCreation extends AppCompatActivity {
    private EditText email, pwd, firstname, lastname, title, occupation, noofAdults, noofChild, noofInfants, unit;
    private TextView upload;

    private Spinner residencestatus;
    Button Create;
    private DatabaseReference mDatabase;
    private FirebaseAuth fireauth;
    private ProgressDialog progress;
    String[] status = new String[]{"Owner", "Tenant"

    };
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    ProgressDialog pd;

    //creating reference to firebase storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReferenceFromUrl("gs://communify-4b71c.appspot.com");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_creation);
        progress = new ProgressDialog(this);
        fireauth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("member");
        email = (EditText) findViewById(R.id.Email);
        pwd = (EditText) findViewById(R.id.password);
        title = (EditText) findViewById(R.id.Title);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.Lastnmae);
        occupation = (EditText) findViewById(R.id.Occupation);
        upload = (TextView) findViewById(R.id.UploadImage);
        residencestatus = (Spinner) findViewById(R.id.ResidenceStatus);
        residencestatus.setPrompt("Enter Residence Status");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.customspinner, status);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.customspinner);

        residencestatus.setAdapter(spinnerArrayAdapter);
        unit = (EditText) findViewById(R.id.Unit);
        noofAdults = (EditText) findViewById(R.id.NumAdults);
        noofChild = (EditText) findViewById(R.id.NumChildren);
        noofInfants = (EditText) findViewById(R.id.NumInfants);

        Create = (Button) findViewById(R.id.create);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
            }
        });
        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Member m = new Member();
                m.setEmail(email.getText().toString());
                m.setTitle(title.getText().toString());
                m.setFirstname(firstname.getText().toString());
                m.setLastname(lastname.getText().toString());
                m.setOccupation(occupation.getText().toString());
                m.setResidencestatus(residencestatus.getSelectedItem().toString());
                m.setUnit(unit.getText().toString());
                m.setNumAdults(Integer.parseInt(noofAdults.getText().toString()));
                m.setNumChildren(Integer.parseInt(noofChild.getText().toString()));
                m.setNumInfants(Integer.parseInt(noofInfants.getText().toString()));
                m.setStatus(1);
                if (validate(m)) {
                    progress.setMessage("Creating User  Please Wait");
                    progress.show();

                    String password = pwd.getText().toString().trim();
                    fireauth.createUserWithEmailAndPassword(m.getEmail().trim(), password).addOnCompleteListener(UserCreation.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            progress.dismiss();
                            if (task.isSuccessful()) {

                                String userId = task.getResult().getUser().getUid();

                                mDatabase.child(userId).setValue(m);


                                if(filePath != null) {
                                   String filename = userId+".jpg";

                                    StorageReference childRef = storageRef.child(filename);

                                    //uploading the image
                                    UploadTask uploadTask = childRef.putFile(filePath);

                                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                            Toast.makeText(UserCreation.this, "Upload successful", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                            Toast.makeText(UserCreation.this, "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    });
                                }
                                else {
                                    Toast.makeText(UserCreation.this, "Select an image", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Intent menu = new Intent(UserCreation.this, MainActivity.class);

                                finish();
                                startActivity(menu);

                                return;


                            } else {

                                Toast.makeText(UserCreation.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    });
                }


            }
        });

    }

    public boolean validate(Member m) {
        if (TextUtils.isEmpty(m.getEmail())) {
            Toast.makeText(UserCreation.this, "Please Enter Email", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(pwd.getText().toString())) {
            Toast.makeText(UserCreation.this, "Please Enter Password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(m.getTitle())) {
            Toast.makeText(UserCreation.this, "Please Enter Title", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(m.getFirstname())) {
            Toast.makeText(UserCreation.this, "Please Enter FirstName", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (TextUtils.isEmpty(m.getLastname())) {
            Toast.makeText(UserCreation.this, "Please Enter LastName", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(m.getOccupation())) {
            Toast.makeText(UserCreation.this, "Please Enter Occupation", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(m.getResidencestatus())) {
            Toast.makeText(UserCreation.this, "Please Enter Residence Status", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(m.getUnit())) {
            Toast.makeText(UserCreation.this, "Please Enter Units", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(filePath == null)
        {
            Toast.makeText(UserCreation.this, "Please Upload File", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}