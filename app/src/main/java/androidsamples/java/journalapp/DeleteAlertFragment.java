package androidsamples.java.journalapp;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.datepicker.MaterialStyledDatePickerDialog;

import java.util.Objects;
import java.util.UUID;

public class DeleteAlertFragment extends DialogFragment {

    Calendar mCalendar;
    NavController navController;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        navController = NavHostFragment.findNavController(this);
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(requireContext())
                .setTitle("Delete Entry?")
                .setCancelable(false)
                .setMessage("Do you want to delete your input ?")
                .setPositiveButton("Yes", (dialog, which) -> Objects.requireNonNull(navController.getPreviousBackStackEntry())
                        .getSavedStateHandle().set(getString(R.string.cancel),true))
                .setNegativeButton("No", (dialog, which) -> {
                    // If user click no then dialog box is canceled.
                    Objects.requireNonNull(navController.getPreviousBackStackEntry())
                            .getSavedStateHandle().set(getString(R.string.cancel),false);
                    dialog.cancel();
                })
                .create();
    }

}
