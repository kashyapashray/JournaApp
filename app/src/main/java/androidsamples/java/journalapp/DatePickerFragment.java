package androidsamples.java.journalapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Objects;

public class DatePickerFragment extends DialogFragment {

  Calendar mCalendar;
  NavController navController;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navController = NavHostFragment.findNavController(this);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @NonNull
  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    mCalendar = Calendar.getInstance();
    int year = mCalendar.get(Calendar.YEAR);
    int month = mCalendar.get(Calendar.MONTH);
    int dayOfMonth = mCalendar.get(Calendar.DAY_OF_MONTH);

    return new DatePickerDialog(requireContext(), (view, year1, month1, dayOfMonth1) -> {
      mCalendar = Calendar.getInstance();
      mCalendar.set(year1, month1, dayOfMonth1);
      String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
      Log.d("DATE_PICKER_FRAG",selectedDate);
      Objects.requireNonNull(navController.getPreviousBackStackEntry())
              .getSavedStateHandle().set(getString(R.string.DATE_KEY), selectedDate);
    }, year, month, dayOfMonth);
  }

}
