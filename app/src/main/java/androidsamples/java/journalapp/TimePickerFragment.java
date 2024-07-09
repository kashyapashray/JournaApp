package androidsamples.java.journalapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import java.util.Calendar;
import java.util.Objects;

public class TimePickerFragment extends DialogFragment {

  NavController navController;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    navController = NavHostFragment.findNavController(this);
  }

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
    Calendar myCal = Calendar.getInstance();
    TimePickerFragmentArgs args = TimePickerFragmentArgs.fromBundle(getArguments());
    String result = args.getTimeType();
    return new TimePickerDialog(requireContext(), (tp, hm, m)->{
      myCal.set(Calendar.HOUR_OF_DAY,hm);
      myCal.set(Calendar.MINUTE, m);
      String date = hm +":";
      if(m < 10){
        date += "0"+m;
      }else{
        date += m;
      }
      Log.d("TIME_TYPE",result);
      Log.d("TIME_PICKER_FRAG",date);
      Objects.requireNonNull(navController.getPreviousBackStackEntry())
              .getSavedStateHandle().set(result, date);
    }, myCal.get(Calendar.HOUR_OF_DAY), myCal.get(Calendar.MINUTE), true);
  }
}
