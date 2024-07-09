package androidsamples.java.journalapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EntryDetailsFragment # newInstance} factory method to
 * create an instance of this fragment.
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class EntryDetailsFragment extends Fragment{

  Boolean correctDate = false;
  Boolean orderedTime = false;
  Boolean start = false;
  Boolean end = false;
  String sTime="";
  String eTime="";

  Button datePick;
  Button startTime;
  Button endTime;
  EditText title;
  Button save;

  NavBackStackEntry navBackStackEntry_VM;
  NavBackStackEntry navBackStackEntry_DF;
  NavController navController;
  CommonViewModel vm;
  EntryDetailsViewModel model;
  int dec;
  int index;
  JournalData j;

  private static final HashMap<String, Integer> monthMap = new HashMap<>();
  static{
    monthMap.put("January",1);
    monthMap.put("February",2);
    monthMap.put("March",3);
    monthMap.put("April",4);
    monthMap.put("May",5);
    monthMap.put("June",6);
    monthMap.put("July",7);
    monthMap.put("August",8);
    monthMap.put("September",9);
    monthMap.put("October",10);
    monthMap.put("November",11);
    monthMap.put("December",12);
  }

  // Create our observer and add it to the NavBackStackEntry's lifecycle
  final LifecycleEventObserver obsTime = (source, event) -> {
    if (event.equals(Lifecycle.Event.ON_RESUME)) {
      if (navBackStackEntry_DF.getSavedStateHandle().contains(getString(R.string.TYPE_START))) {
        sTime = navBackStackEntry_DF.getSavedStateHandle().get(getString(R.string.TYPE_START));
        model.start = sTime;
        start = true;
        Log.d("Click_START_TIME", sTime);
      }
      if(navBackStackEntry_DF.getSavedStateHandle().contains(getString(R.string.TYPE_END))){
        eTime = navBackStackEntry_DF.getSavedStateHandle().get(getString(R.string.TYPE_END));
        model.end = eTime;
        end = true;
        Log.d("Click_END_TIME", eTime);
      }
      updateUI();
      if (!model.start.equals("Start Time") && !model.end.equals("End Time") && !isValidTimeRange()) {
        orderedTime = false;
        Toast.makeText(getContext(), "Start should be before End", Toast.LENGTH_SHORT).show();
      } else if(model.start.equals("Start Time") || model.end.equals("End Time")){
        orderedTime = false;
      }else {
        orderedTime = true;
      }
    }
  };
  final LifecycleEventObserver obsDate = (source, event) -> {
    if (event.equals(Lifecycle.Event.ON_RESUME)
            && navBackStackEntry_DF.getSavedStateHandle().contains(getString(R.string.DATE_KEY))) {
      String result = navBackStackEntry_DF.getSavedStateHandle().get(getString(R.string.DATE_KEY));
      Log.d("ENTRY_DETAILS_DATE",result);
      if (!validDate(result)) {
        correctDate = false;
        Toast.makeText(getContext(), "Enter correct date", Toast.LENGTH_SHORT).show();
      }else{
        correctDate = true;
        model.date = result;
        updateUI();
      }
    }
  };

  final LifecycleEventObserver obsCancel = (source, event) -> {
    if (event.equals(Lifecycle.Event.ON_RESUME)
            && navBackStackEntry_DF.getSavedStateHandle().contains(getString(R.string.cancel))) {
      Boolean result = navBackStackEntry_DF.getSavedStateHandle().get(getString(R.string.cancel));
      if(result == null){
        result = false;
      }
      Log.d("CANCEL INPUT",result.toString());
      if (dec != 1 && result) {
        // DELETE
        Toast.makeText(getContext(), "Deleting..", Toast.LENGTH_SHORT).show();
        Journal journal = new Journal(model.title, model.date, model.start, model.end);
        journal.setUid(UUID.fromString(j.uid));
        Log.d("DELETE",journal.toString());
        model.deleteEntries(Journal.fromJournalData(j));
      }
      navController.popBackStack();
    }
  };

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @SuppressLint("LongLogTag")
  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_entry_details, container, false);
    datePick = view.findViewById(R.id.btn_entry_date);
    startTime= view.findViewById(R.id.btn_start_time);
    endTime = view.findViewById(R.id.btn_end_time);
    save = view.findViewById(R.id.btn_save);

    title = view.findViewById(R.id.edit_title);

    Toolbar toolbar = view.findViewById(R.id.entry_toolbar);
    model = new ViewModelProvider(this).get(EntryDetailsViewModel.class);

    MenuProvider menuProvider = new MenuProvider() {
      @Override
      public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.entry_menu, menu);
      }

      @Override
      public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.deleteOption){
          deleteOption();
        }else if(id == R.id.shareOption){
          shareOption();
        }
        return false;
      }
    };
    toolbar.addMenuProvider(menuProvider);

    datePick.setOnClickListener(v -> {
      NavDirections action = EntryDetailsFragmentDirections.datePickerDialog();
      navController.navigate(action);
    });

    startTime.setOnClickListener(v -> {
      EntryDetailsFragmentDirections.TimePickerDialog action =
              EntryDetailsFragmentDirections.timePickerDialog(getString(R.string.TYPE_START));
      navController.navigate(action);
    });

    endTime.setOnClickListener(v -> {
        EntryDetailsFragmentDirections.TimePickerDialog action =
                EntryDetailsFragmentDirections.timePickerDialog(getString(R.string.TYPE_END));
        navController.navigate(action);
    });

    save.setOnClickListener(v -> {
      if(correctDate && orderedTime && !(title.getText().toString().equals(""))){
        start = false;
        end = false;
        orderedTime = false;
        correctDate = false;
        model.title = title.getText().toString();
        Toast.makeText(getContext(), "DONE...", Toast.LENGTH_SHORT).show();
        updateUI();
        saveIt();
      }else{
        if(title.getText().toString().equals("")){
          Toast.makeText(getContext(), "Enter title", Toast.LENGTH_SHORT).show();
        }
        if(!start){
          Toast.makeText(getContext(), "Enter start time", Toast.LENGTH_SHORT).show();
        }
        if(!end){
          Toast.makeText(getContext(), "Enter end time", Toast.LENGTH_SHORT).show();
        }
        if(!correctDate){
          Toast.makeText(getContext(), "Enter correct date", Toast.LENGTH_SHORT).show();
        }
        if(start && end && !orderedTime){
          Toast.makeText(getContext(), "Start should be before End", Toast.LENGTH_SHORT).show();
        }
      }
    });

    return view;
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);
    navBackStackEntry_VM = navController.getBackStackEntry(R.id.nav_graph);
    vm = new ViewModelProvider(navBackStackEntry_VM).get(CommonViewModel.class);

    navBackStackEntry_DF = navController.getBackStackEntry(R.id.entryDetailsFragment);

    EntryDetailsFragmentArgs args = EntryDetailsFragmentArgs.fromBundle(getArguments());
    dec = args.getOpCreateUpdateDelete();
    if (dec != 1 && args.getJEntry()) {
      // Update or delete
      j = vm.journalData;
      if(j == null){
        navController.popBackStack();
      }
      model.date = j.date;
      model.end = j.end;
      model.start = j.start;
      model.title = j.title;
      start = true;
      end = true;
      orderedTime = true;
      correctDate = true;
      index = args.getOptionalIndex();
      updateUI();
    }
    navBackStackEntry_DF.getLifecycle().addObserver(obsTime);
    navBackStackEntry_DF.getLifecycle().addObserver(obsDate);
    navBackStackEntry_DF.getLifecycle().addObserver(obsCancel);

      // As addObserver() does not automatically remove the observer, we
      // call removeObserver() manually when the view lifecycle is destroyed
    getViewLifecycleOwner().getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
      if (event.equals(Lifecycle.Event.ON_DESTROY)) {
        navBackStackEntry_DF.getLifecycle().removeObserver(obsDate);
        navBackStackEntry_DF.getLifecycle().removeObserver(obsTime);
        navBackStackEntry_DF.getLifecycle().removeObserver(obsCancel);
      }
    });
  }

  void updateUI(){
    if(!title.getText().toString().equals("")){
      model.title = title.getText().toString();
    }
    datePick.setText(model.date);
    startTime.setText(model.start);
    endTime.setText(model.end);
    title.setText(model.title);
  }

  public void shareOption(){
    if(correctDate && orderedTime && !(title.getText().toString().equals(""))) {
      // Body of the content
      String s = "Look what I have been up to: " + title.getText().toString() + " on " + datePick.getText().toString() + ", "
              + startTime.getText().toString() + " to " + endTime.getText().toString();

      Intent sharingIntent = new Intent(Intent.ACTION_SEND);
      sharingIntent.setType("text/plain");      // type of the content to be shared
      String shareSubject = "Share";      // subject of the content. you can share anything
      sharingIntent.putExtra(Intent.EXTRA_TEXT, s);     // passing body of the content
      sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);     // passing subject of the content
      startActivity(Intent.createChooser(sharingIntent, "Share using"));

      Toast.makeText(getContext(), R.string.sharing, Toast.LENGTH_SHORT).show();
    }else{
      Toast.makeText(getContext(), "Enter all details", Toast.LENGTH_SHORT).show();
    }
  }
  public void deleteOption(){
    NavDirections action = EntryDetailsFragmentDirections.actionCancelInput();
    navController.navigate(action);

  }
  private void saveIt(){
    Journal journal = new Journal(model.title, model.date, model.start, model.end);
    if(dec == 1){
      // CREATE
      model.insertEntry(journal);
      Log.d("CREATE",journal.toString());
    }else{
      // UPDATE
      journal.setUid(UUID.fromString(j.uid));
      Log.d("UPDATE",journal.toString());
      model.updateEntry(journal);
    }
    navController.popBackStack();
  }

  private boolean validDate(String d) {
    boolean res = true;

    Calendar temp = Calendar.getInstance();
    String date = DateFormat.getDateInstance(DateFormat.FULL).format(temp.getTime());

    try {
      String[] dParts = d.split("\\s+");
      String[] dateParts = date.split("\\s+");

      if (dParts.length >= 4 && dateParts.length >= 4) {
        int dDate = tryParseInt(dParts[1]);
        int currDate = tryParseInt(dateParts[1]);

        String currMonthStr = dateParts[2];
        String dMonthStr = dParts[2];

        Integer currMonth = monthMap.get(currMonthStr);
        Integer dMonth = monthMap.get(dMonthStr);

        int dYear = tryParseInt(dParts[3]);
        int currYear = tryParseInt(dateParts[3]);

        if (currMonth != null && dMonth != null) {
          if(dYear > currYear){
            res =true;
          }
          else if (dYear == currYear) {
            if (dMonth > currMonth) {
              res = true;
            } else if (dMonth == currMonth) {
              res = dDate >= currDate;
            }
          }
        }
      }
    } catch (NumberFormatException e) {
      Log.e("ENTRY_DETAILS", "Error parsing date components", e);
    } catch (NullPointerException e) {
      Log.e("ENTRY_DETAILS", "Error getting month from monthMap", e);
    }

    return res;
  }

  private int tryParseInt(String value) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return 0; // or another default value
    }
  }

  private boolean isValidTimeRange(){
    if(start && end){
      // check if start is before end
      int sH,sM,eH,eM;
      try {
        sM = Integer.parseInt(model.start.substring(model.start.indexOf(':') + 1));
        eM = Integer.parseInt(model.end.substring(model.end.indexOf(':') + 1));
        sH = Integer.parseInt(model.start.substring(0, model.start.indexOf(':')));
        eH = Integer.parseInt(model.end.substring(0, model.end.indexOf(':')));

        if (sH < eH) {
          return true;
        } else return sH == eH && sM <= eM;
      }catch(NumberFormatException e) {
        Log.e("ENTRY_DETAILS", "TIME ISSUE");
      }
      return true;
    }
    return false;
  }
}