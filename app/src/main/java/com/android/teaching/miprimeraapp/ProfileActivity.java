package com.android.teaching.miprimeraapp;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteConstraintException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.teaching.miprimeraapp.basedatos.AppDatabase;
import com.android.teaching.miprimeraapp.basedatos.User;

import java.io.File;
import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    // Views
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText ageEditText;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }

        return false;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameEditText = findViewById(R.id.username);
        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);
        ageEditText = findViewById(R.id.age_edit_text);
        ageEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // MOSTRAR DatePickerDialog
                    new DatePickerDialog(ProfileActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view,
                                                      int year, int month, int dayOfMonth) {
                                    // Escribir la fecha en el edit text
                                    int anoActual = Calendar.getInstance().get(Calendar.YEAR);
                                    int edad = anoActual - year;
                                    ageEditText.setText(String.valueOf(edad));
                                }
                            }, 1980, 1, 1).show();
                }
            }
        });
        radioButtonMale = findViewById(R.id.radio_button_male);
        radioButtonFemale = findViewById(R.id.radio_button_female);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        Log.d("ListActivity", "¿Existe y puedo escribir?: "
                + isExternalStorageWritable());
        Log.d("ListActivity", "¿Existe y puedo leer?: "
                + isExternalStorageReadable());


        // para cargar imagen de perfil
        if (isExternalStorageReadable()) {
            File imgFile = new File(getExternalFilesDir(null), "halflife-lambda.jpg");
            if (imgFile.exists()) {
                ImageView myImage = findViewById(R.id.lambda);
                myImage.setImageURI(Uri.fromFile(imgFile));
            }
        }


    }


    @Override
    protected void onStart() {
        super.onStart();


        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.user_preferences),
                Context.MODE_PRIVATE);
        String usernameValue = sharedPreferences.getString("username_key", "");
        AppDatabase myDataBase = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")
                .allowMainThreadQueries()
                .build();
        User myUser = myDataBase.userDao().findByUsername(usernameValue);
        if (myUser != null) {
            usernameEditText.setText(myUser.getUsername());
            emailEditText.setText(myUser.getEmail());
            ageEditText.setText(myUser.getAge());
            passwordEditText.setText(myUser.getPassword());
            if (myUser.getGender().equals("H")) {
                radioButtonMale.setChecked(true);
            } else if (myUser.getGender().equals("M")) {
                radioButtonFemale.setChecked(true);
            }
        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        String username = usernameEditText.getText().toString();
        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.register_user_preferences),
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("username_register_key", username);


        String email = emailEditText.getText().toString();

        editor.putString("email_register_key", email);


        String age = ageEditText.getText().toString();

        editor.putString("age_register_key", age);

        if (radioButtonMale.isChecked()) {
            editor.putString("gender_key", "H");
        } else if (radioButtonFemale.isChecked()) {
            editor.putString("gender_key", "M");
        }

        editor.apply();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater myInflater = getMenuInflater();
        myInflater.inflate(R.menu.menu_profile_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // Guardar el perfil
                saveInternal();
                break;
            case R.id.action_delete:
                // Borrar el perfil
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void saveInternal() {
        // Edit Texts
        Log.d("ProfileActivity", "Username: " + usernameEditText.getText());
        Log.d("ProfileActivity", "Email: " + emailEditText.getText());
        Log.d("ProfileActivity", "Password: " + passwordEditText.getText());
        Log.d("ProfileActivity", "Age: " + ageEditText.getText());

        // Radio Buttons
        if (radioButtonMale.isChecked()) {
            // El usuario ha seleccionado "H"
            Log.d("ProfileActivity", "Gender: male");
        } else if (radioButtonFemale.isChecked()) {
            // El usuario ha seleccionado "M"
            Log.d("ProfileActivity", "Gender: female");
        }
        AppDatabase db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "database-name")
                .allowMainThreadQueries()
                .build();

        try {
            User user = new User();
            user.setAge(ageEditText.getText().toString());
            user.setEmail(emailEditText.getText().toString());
            user.setUsername(usernameEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            if (radioButtonMale.isChecked()) {
                // El usuario ha seleccionado "H"
                user.setGender("H");
            } else if (radioButtonFemale.isChecked()) {
                // El usuario ha seleccionado "M"
                user.setGender("M");
            }
            db.userDao().insert(user);
        } catch (SQLiteConstraintException ex) {

        }
    }

    public void guardarDatos(View view) {
        saveInternal();
        SharedPreferences sharePref =
                getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharePref.edit();
        editor.remove("username");
        editor.remove("email");
        editor.remove("age");
        editor.apply();


    }


    /**
     * Método que se ejecutará cuando el usuario pulse "Delete"
     *
     * @param view -
     */
    public void onDelete(View view) {


        // Mostrar un dialogo de confirmación
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(R.string.dialog_title);
        builder.setMessage(R.string.dialog_message);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha pulsado el botón "SÍ"
                Toast.makeText(ProfileActivity.this, "SI QUIERO!",
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha pulsado el botón "NO"
                Toast.makeText(ProfileActivity.this, "NO QUIERO!",
                        Toast.LENGTH_LONG).show();
            }
        });
        builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // El usuario ha pulsado el botón "CANCELAR"
                Toast.makeText(ProfileActivity.this, "Candelando...",
                        Toast.LENGTH_LONG).show();
            }
        });

        builder.create().show();
    }


}
