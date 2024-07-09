package androidsamples.java.journalapp;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavBackStackEntry;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class EntryListFragment extends Fragment {

  NavBackStackEntry  navBackStackEntry;
  NavController navController;
  EntryAdapter itemEntryAdapter;
  EntryListViewModel model;
  CommonViewModel vm;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @RequiresApi(api = Build.VERSION_CODES.N)
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_entry_list, container, false);
    FloatingActionButton floatingActionButton = view.findViewById(R.id.btn_add_entry);
    Toolbar toolbar = view.findViewById(R.id.list_toolbar);

    model = new ViewModelProvider(this).get(EntryListViewModel.class);

    // Receive C(R)UD signals and notify adapter as well as use model.dao to make changes
    model.getAllEntries().observe(getViewLifecycleOwner(),(List<Journal> entries) -> itemEntryAdapter.insertData(entries));

    MenuProvider menuProvider = new MenuProvider() {
      @Override
      public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.list_menu, menu);
      }

      @Override
      public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
        int id = menuItem.getItemId();
        if(id == R.id.info){
          navToInfo();
        }
        return false;
      }
    };
    toolbar.addMenuProvider(menuProvider);

    ArrayList<Journal> list = new ArrayList<>();
    // Assign list to ItemAdapter
    itemEntryAdapter = new EntryAdapter(list);

    // Set the LayoutManager that this RecyclerView will use.
    RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    // adapter instance is set to the recyclerview to inflate the items.
    recyclerView.setAdapter(itemEntryAdapter);
    itemEntryAdapter.setListener(this::openForUpdate);

    floatingActionButton.setOnClickListener(this::navToEntry);

    return view;
  }

  private void navToEntry(View v){
    EntryListFragmentDirections.AddEntryAction action = EntryListFragmentDirections.addEntryAction();
    action.setOpCreateUpdateDelete(1);
    navController.navigate(action);
  }
  private void navToInfo(){
    NavDirections action = EntryListFragmentDirections.toInfoAction();
    navController.navigate(action);
  }

  void openForUpdate(int pos, Journal j){
    EntryListFragmentDirections.AddEntryAction action = EntryListFragmentDirections.addEntryAction();
    action.setJEntry(true);
    vm.journalData = JournalData.fromJournal(j);
    action.setOptionalIndex(pos);
    action.setOpCreateUpdateDelete(2);
    Log.d("RECYCLER ITEM MOD",vm.journalData.toString());
    navController.navigate(action);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    navController = Navigation.findNavController(view);
    navBackStackEntry = Navigation.findNavController(view).getBackStackEntry(R.id.nav_graph);
    vm = new ViewModelProvider(navBackStackEntry).get(CommonViewModel.class);
  }

}