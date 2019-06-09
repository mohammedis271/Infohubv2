package com.example.muaazjoosuf.infohubv2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class NewsSearchActivity extends AppCompatActivity {
    EditText editTextKeyword;
    EditText editTextDate;
    Switch locationSwitch;
    Spinner spinnerSearchType;
    Spinner spinnerLanguage;
    Spinner spinnerSortBy;
    Spinner spinnerCountryChoice;

    ArrayAdapter<CharSequence> arrayAdapterSearchType;
    ArrayAdapter<CharSequence> arrayAdapterLanguage;
    ArrayAdapter<CharSequence> arrayAdapterSortBy;
    ArrayAdapter<CharSequence> arrayAdapterCountryChoice;


    public String searchType = "";
    public String languageChoice = "";
    public String sortBy = "";
    public String countryChoice = "";
    public String keywords = "";
    public String datePublished = "";
    public boolean isSwitchChecked;


    public LocationManager locationManager;
    public LocationListener locationListener ;


    //completing request protocol
   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);

        }
    }*/


    //creation of menu search item
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.information, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //menu search icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {

            return true;
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        spinnerSearchType = (Spinner) findViewById(R.id.spinner_search_type);
        arrayAdapterSearchType = ArrayAdapter.createFromResource(this,
                R.array.types_of_search, android.R.layout.simple_spinner_item);
        arrayAdapterSearchType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSearchType.setAdapter(arrayAdapterSearchType);
        spinnerSearchType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchType = parent.getItemAtPosition(position).toString();
                Log.i("CustomTag", "SearchType = " + searchType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerLanguage = (Spinner) findViewById(R.id.spinner_language_choice);
        arrayAdapterLanguage = ArrayAdapter.createFromResource(this, R.array.types_of_languages_supported,
                android.R.layout.simple_spinner_item);
        arrayAdapterLanguage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(arrayAdapterLanguage);
        spinnerLanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                languageChoice = parent.getItemAtPosition(position).toString();
                Log.i("CustomTag", "LanguageChoice = " + languageChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerSortBy = (Spinner) findViewById(R.id.spinner_sort_by);
        arrayAdapterSortBy = ArrayAdapter.createFromResource(this, R.array.sort_by_supported,
                android.R.layout.simple_spinner_item);
        arrayAdapterSortBy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSortBy.setAdapter(arrayAdapterSortBy);
        spinnerSortBy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sortBy = parent.getItemAtPosition(position).toString();
                Log.i("CustomTag", "SortBy = " + sortBy);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView countryHeading = findViewById(R.id.set_country_heading);
        countryHeading.setVisibility(View.VISIBLE);
        spinnerCountryChoice = (Spinner) findViewById(R.id.spinner_country_choice);
        spinnerCountryChoice.setVisibility(View.VISIBLE);
        arrayAdapterCountryChoice = ArrayAdapter.createFromResource(NewsSearchActivity.this, R.array.country_choice_supported,
                android.R.layout.simple_spinner_item);
        arrayAdapterCountryChoice.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountryChoice.setAdapter(arrayAdapterCountryChoice);
        spinnerCountryChoice.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countryChoice = parent.getItemAtPosition(position).toString();
                Log.i("CustomTag", countryChoice);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        locationSwitch = (Switch) findViewById(R.id.switch_location);
        locationSwitch.setChecked(isSwitchChecked);
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    isSwitchChecked = isChecked;

                    TextView countryHeading = findViewById(R.id.set_country_heading);
                    countryHeading.setVisibility(View.VISIBLE);
                    spinnerCountryChoice = (Spinner) findViewById(R.id.spinner_country_choice);
                    spinnerCountryChoice.setVisibility(View.VISIBLE);

                } else {
                    countryChoice = "";
                    isSwitchChecked = isChecked;
                    Log.i("CustomTag", countryChoice);
                    TextView countryHeading = findViewById(R.id.set_country_heading);
                    countryHeading.setVisibility(View.GONE);
                    spinnerCountryChoice = (Spinner) findViewById(R.id.spinner_country_choice);
                    spinnerCountryChoice.setVisibility(View.GONE);
                }
            }
        });
        editTextKeyword = (EditText) findViewById(R.id.edit_text_keyword);
        editTextDate = (EditText) findViewById(R.id.edit_text_date_published);

        editTextKeyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                keywords = s.toString().trim();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopup(NewsSearchActivity.this);
                //Lines to hide soft input (keyboard)
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        });

        Button saveSearchButton = findViewById(R.id.save_search_btn);
        saveSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = NewsSearchActivity.this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("searchType", searchType).apply();
                sharedPreferences.edit().putString("languageChoice", languageChoice).apply();
                sharedPreferences.edit().putString("sortBy", sortBy).apply();
                sharedPreferences.edit().putString("countryChoice", countryChoice).apply();
                sharedPreferences.edit().putString("keywords", keywords).apply();
                sharedPreferences.edit().putString("datePublished", datePublished).apply();
                sharedPreferences.edit().putString("countryChoice",countryChoice).apply();
                sharedPreferences.edit().putBoolean("isSwitchChecked", isSwitchChecked).apply();
                Toast.makeText(NewsSearchActivity.this, "Preferences updated", Toast.LENGTH_SHORT).show();

                String transferData = searchType + "_" + languageChoice + "_" + sortBy + "_" + countryChoice + "_" + keywords + "_" + datePublished;
                Intent intent = new Intent(getApplicationContext(), NewsSectionActivity.class);
                intent.putExtra("transferData", transferData);
                startActivity(intent);
                finish();

            }
        });
        loadPreviousSettings();
        //end of menu setup


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(false);
    }

    //Button to make sure that when back button is pressed it exists intent
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showPopup(Activity context) {

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.calender_layout, null, false);
        // Creating the PopupWindow
        final PopupWindow popupWindow = new PopupWindow(
                layout, 1000, 1200);

        popupWindow.setContentView(layout);
        popupWindow.setOutsideTouchable(false);
        // Clear the default translucent background
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(layout, Gravity.CENTER, 60, 300);

        CalendarView cv = (CalendarView) layout.findViewById(R.id.calendarView);
        cv.setBackgroundColor(getResources().getColor(R.color.off_white));

        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                    int dayOfMonth) {
                Log.d("CustomTag", "date selected " + year + " " + (month + 1) + " " + dayOfMonth);
                String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                editTextDate.setText(selectedDate);
                datePublished = selectedDate;
                popupWindow.dismiss();
            }
        });
        RelativeLayout searchLayout = findViewById(R.id.search_layout);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
    }

    private void loadPreviousSettings() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        searchType = sharedPreferences.getString("searchType", "");
        languageChoice = sharedPreferences.getString("languageChoice", "");
        sortBy = sharedPreferences.getString("sortBy", "");
        countryChoice = sharedPreferences.getString("countryChoice", "");
        keywords = sharedPreferences.getString("keywords", "");
        datePublished = sharedPreferences.getString("datePublished", "");
        isSwitchChecked = sharedPreferences.getBoolean("isSwitchChecked", false);
        countryChoice = sharedPreferences.getString("countryChoice","");
        editTextDate.setText(datePublished);
        editTextKeyword.setText(keywords);
        locationSwitch.setChecked(isSwitchChecked);
        spinnerSearchType.setSelection(arrayAdapterSearchType.getPosition(searchType));
        spinnerLanguage.setSelection(arrayAdapterLanguage.getPosition(languageChoice));
        spinnerSortBy.setSelection(arrayAdapterSortBy.getPosition(sortBy));
        spinnerCountryChoice.setSelection(arrayAdapterCountryChoice.getPosition(countryChoice));
    }


}
