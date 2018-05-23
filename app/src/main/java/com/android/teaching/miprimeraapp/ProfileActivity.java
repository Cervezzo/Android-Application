package com.android.teaching.miprimeraapp;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
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
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.Calendar;

public class ProfileActivity extends AppCompatActivity {

    // Views
    private EditText usernameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText ageEditText;
    private RadioButton radioButtonMale;
    private RadioButton radioButtonFemale;

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
                    new DatePickerDialog(ProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
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
        } else if(radioButtonFemale.isChecked()) {
            // El usuario ha seleccionado "M"
            Log.d("ProfileActivity", "Gender: female");
        }
    }

    public void guardarDatos(View view) {
        saveInternal();
    }

    /**
     * Método que se ejecutará cuando el usuario pulse "Delete"
     *
     * @param view  -
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
