package ca.yorku.eecs.mack.softkeyboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * SoftKeyboardSetup - a class that implements a setup dialog for experimental applications on Android. <p>
 *
 * @author Scott MacKenzie, 2014-2017
 */
@SuppressWarnings("unused")
public class SoftKeyboardSetup extends Activity implements TextWatcher
{
    final static String MYDEBUG = "MYDEBUG"; // for Log.i messages
    final static String TITLE = "SoftKeyboardSetup";

    /*
     * The following arrays are used to fill the spinners in the set up dialog. The first entries will be replaced by
     * corresponding values in the app's shared preferences, if any exist. In order for a value to exit as a shared
     * preference, the app must have been run at least once with the "Save" button tapped.
     */
    String[] participantCode = {"P99", "P01", "P02", "P03", "P04", "P05", "P06", "P07", "P08", "P09", "P10", "P11",
            "P12", "P13", "P14", "P15", "P16", "P17", "P18", "P19", "P20", "P21", "P22", "P23", "P24", "P25"};
    String[] sessionCode = {"S99", "S01", "S02", "S03", "S04", "S05", "S06", "S07", "S08", "S09", "S10", "S11", "S12",
            "S13", "S14", "S15", "S16", "S17", "S18", "S19", "S20", "S21", "S22", "S23", "S24", "S25"};
    String[] blockCode = {"(auto)"};
    String[] groupCode = {"G99", "G01", "G02", "G03", "G04", "G05", "G06", "G07", "G08", "G09", "G10", "G11", "G12",
            "G13", "G14", "G15", "G16", "G17", "G18", "G19", "G20", "G21", "G22", "G23", "G24", "G25"};
    String[] keyboardLayout = {"Qwerty", "Qwerty", "Opti", "Opti II", "Fitaly", "Lewis", "Metropolis"};
    String[] numberOfPhrases = {"5", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    String[] phrasesFileArray = {"phrases2", "phrases2", "quickbrownfox", "phrases100", "alphabet"};
    String[] ageArray = {"20-30","30-40","40-50"};
    String[] genderArray = {"Male","Female"};
    String[] testArray = {"With music","Without music"};

    // default values for EditText fields (may be different if shared preferences saved)
    String conditionCode = "Your Name";
    String keyboardScale = "1.0";
    String offsetFromBottom = "80";

    // defaults for booleans (may be different if shared preferences saved)
    boolean positionAtBottom = true;
    boolean showPopupKey = true;
    boolean lowercaseOnly = false;
    boolean showPresentedText = true;

    SharedPreferences sp;
    SharedPreferences.Editor spe;
    Button ok, save, exit;
    Vibrator vib;
    private Spinner spinParticipantCode,spinAge,spinGender,spinTestMode,spinBlockCode;
    private Spinner spinSessionCode, spinGroupCode, spinKeyboardLayout;
    private Spinner spinNumberOfPhrases, spinPhrasesFile;
    private EditText editConditionCode, editKeyboardScale, editOffsetFromBottom;
    private CheckBox checkPositionAtBottom;
    private CheckBox checkShowPopup;
    private CheckBox checkLowercaseOnly;
    // end set up parameters
    private CheckBox checkShowPresented;

    /**
     * Called when the activity is first created.
     *
     *
     */

    private Spinner spinRoutine(String key, String[] options, int resourceId)
    {
        options[0] = sp.getString(key,options[0]);
        Spinner spinner = (Spinner) findViewById(resourceId);

        // initialise spinner adapters
        ArrayAdapter<CharSequence> adapterTemp = new ArrayAdapter<CharSequence>(this, R.layout.spinnerstyle,
                options);
        spinner.setAdapter(adapterTemp);
        return spinner;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.setup);

        // get a reference to a SharedPreferences object (used to store, retrieve, and save setup parameters)
        sp = this.getPreferences(MODE_PRIVATE);
        spinParticipantCode=spinRoutine("participantcode",participantCode,R.id.spinParticipantCode);
        spinAge=spinRoutine("age",ageArray,R.id.spinAge);
        spinGender = spinRoutine("gender",genderArray,R.id.spinGender);
        spinTestMode=spinRoutine("testMode",testArray,R.id.spinTestmode);
        spinSessionCode=spinRoutine("sessionCode",sessionCode,R.id.spinSessionCode);
        spinGroupCode=spinRoutine("groupCode",groupCode,R.id.spinGroupCode);
        spinKeyboardLayout=spinRoutine("keyboardLayout",keyboardLayout,R.id.keyboardLayout);
        spinNumberOfPhrases=spinRoutine("numberOfPhrases",numberOfPhrases,R.id.numberOfPhrases);
        spinPhrasesFile=spinRoutine("phrasesFile",phrasesFileArray,R.id.phrasesFile);
        spinBlockCode=spinRoutine(" ",blockCode,R.id.spinBlockCode);



        // overwrite 1st entry from shared preferences, if corresponding value exits
        conditionCode = sp.getString("conditionCode", conditionCode);
        keyboardScale = sp.getString("keyboardScale", keyboardScale);
        offsetFromBottom = sp.getString("offsetFromBottom", offsetFromBottom);

        positionAtBottom = sp.getBoolean("positionAtBottom", positionAtBottom);
        showPopupKey = sp.getBoolean("showPopupKey", showPopupKey);
        lowercaseOnly = sp.getBoolean("lowercaseOnly", lowercaseOnly);
        showPresentedText = sp.getBoolean("showPresented", showPresentedText);

        editConditionCode = (EditText)findViewById(R.id.userName);
        editKeyboardScale = (EditText)findViewById(R.id.keyboardScale);
        editOffsetFromBottom = (EditText)findViewById(R.id.offsetFromBottom);

        checkShowPopup = (CheckBox)findViewById(R.id.showPopupKey);
        checkLowercaseOnly = (CheckBox)findViewById(R.id.lowercaseOnly);
        checkShowPresented = (CheckBox)findViewById(R.id.showPresentedText);

        // get references to OK, SAVE, and EXIT buttons
        ok = (Button)findViewById(R.id.ok);
        save = (Button)findViewById(R.id.save);
        exit = (Button)findViewById(R.id.exit);



        // initialize EditText setup items
        editConditionCode.setText(conditionCode);
        editKeyboardScale.setText(keyboardScale);
        editOffsetFromBottom.setText(offsetFromBottom);

        // initialize checkboxes
        checkShowPopup.setChecked(showPopupKey);
        checkLowercaseOnly.setChecked(lowercaseOnly);
        checkShowPresented.setChecked(showPresentedText);

        // prevent soft keyboard from popping up when activity launches
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        // listen for changes to the keyboard scale value (so it can be verified as parsable to float)
        editKeyboardScale.addTextChangedListener(this);

        // get a vibrator (used if keyboard scale entry is invalid
        vib = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void onClick(View v)
    {
        if (v == ok)
        {
            // first check the keyboard scale and offset from bottom values (don't proceed unless it's OK)
            if (!isFloat(editKeyboardScale.getText().toString()))
            {
                vib.vibrate(20);
                Toast.makeText(this, "NOTE: Invalid Keyboard scale!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isFloat(editOffsetFromBottom.getText().toString()))
            {
                vib.vibrate(20);
                Toast.makeText(this, "NOTE: Invalid offset from bottom!", Toast.LENGTH_SHORT).show();
                return;
            }

            // get user's choices
            String part = participantCode[spinParticipantCode.getSelectedItemPosition()];
            String sess = sessionCode[spinSessionCode.getSelectedItemPosition()];
            String block = blockCode[spinBlockCode.getSelectedItemPosition()];
            String group = groupCode[spinGroupCode.getSelectedItemPosition()];
            String cond = editConditionCode.getText().toString();
            String age = ageArray[spinAge.getSelectedItemPosition()];
            String testMode = testArray[spinTestMode.getSelectedItemPosition()];
            String gender = genderArray[spinGender.getSelectedItemPosition()];
            String keyLayout = keyboardLayout[spinKeyboardLayout.getSelectedItemPosition()];
            float keyScale = Float.parseFloat(editKeyboardScale.getText().toString());
            float offsetFromBottom = Float.parseFloat(editOffsetFromBottom.getText().toString());
            int num = Integer.parseInt(numberOfPhrases[spinNumberOfPhrases.getSelectedItemPosition()]);
            String phrasesFile = phrasesFileArray[spinPhrasesFile.getSelectedItemPosition()];
            boolean showPopup = checkShowPopup.isChecked();
            boolean lowercaseOnly = checkLowercaseOnly.isChecked();
            boolean showpre = checkShowPresented.isChecked();

            // package the user's choices in a bundle
            Bundle b = new Bundle();
            b.putString("participantCode", part);
            b.putString("sessionCode", sess);
             b.putString("blockCode", block);
            b.putString("age",age);
            b.putString("gender",gender);
            b.putString("testMode",testMode);
            b.putString("groupCode", group);
            b.putString("conditionCode", cond);
            b.putString("keyboardLayout", keyLayout);
            b.putFloat("keyboardScale", keyScale);
            b.putFloat("offsetFromBottom", offsetFromBottom);
            b.putInt("numberOfPhrases", num);
            b.putString("phrasesFile", phrasesFile);
            b.putBoolean("showPopupKey", showPopup);
            b.putBoolean("lowercaseOnly", lowercaseOnly);
            b.putBoolean("showPresented", showpre);

            // start experiment activity (sending the bundle with the user's choices)
            Intent i = new Intent(getApplicationContext(), SoftKeyboardActivity.class);
            i.putExtras(b);
            startActivity(i);
            //finish();

        } else if (v == save)
        {
            // first check the keyboard scale and offset from bottom values (don't proceed unless it's OK)
            if (!isFloat(editKeyboardScale.getText().toString()))
            {
                vib.vibrate(20);
                Toast.makeText(this, "NOTE: Invalid Keyboard scale!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isFloat(editOffsetFromBottom.getText().toString()))
            {
                vib.vibrate(20);
                Toast.makeText(this, "NOTE: Invalid offset from bottom!", Toast.LENGTH_SHORT).show();
                return;
            }

            spe = sp.edit();
            spe.putString("participantCode", participantCode[spinParticipantCode.getSelectedItemPosition()]);
            spe.putString("sessionCode", sessionCode[spinSessionCode.getSelectedItemPosition()]);
            spe.putString("groupCode", groupCode[spinGroupCode.getSelectedItemPosition()]);
            spe.putString("conditionCode", editConditionCode.getText().toString());
            spe.putString("keyboardLayout", keyboardLayout[spinKeyboardLayout.getSelectedItemPosition()]);
            spe.putString("keyboardScale", editKeyboardScale.getText().toString());
            spe.putString("offsetFromBottom", editOffsetFromBottom.getText().toString());
            spe.putString("numberOfPhrases", numberOfPhrases[spinNumberOfPhrases.getSelectedItemPosition()]);
            spe.putString("age",ageArray[spinAge.getSelectedItemPosition()]);
            spe.putString("gender",genderArray[spinGender.getSelectedItemPosition()]);
            spe.putString("testMode",testArray[spinTestMode.getSelectedItemPosition()]);
            spe.putString("phrasesFile", phrasesFileArray[spinPhrasesFile.getSelectedItemPosition()]);
            spe.putBoolean("showPopupKey", checkShowPopup.isChecked());
            spe.putBoolean("lowercaseOnly", checkLowercaseOnly.isChecked());
            spe.putBoolean("showPresented", checkShowPresented.isChecked());
            spe.apply();
            Toast.makeText(this, "Preferences saved!", Toast.LENGTH_SHORT).show();

        } else if (v == exit)
        {
            this.finish(); // terminate
        }
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        if (!isFloat(s.toString()))
            vib.vibrate(20);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
    }

    @Override
    public void afterTextChanged(Editable s)
    {
    }

    // returns true if the passed string is parsable to float, false otherwise
    private boolean isFloat(String floatString)
    {
        try
        {
            Float.parseFloat(floatString);
        } catch (NumberFormatException e)
        {
            return false;
        }
        return true;
    }
}
