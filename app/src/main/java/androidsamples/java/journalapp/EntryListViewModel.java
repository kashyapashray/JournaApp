package androidsamples.java.journalapp;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class EntryListViewModel extends ViewModel {

    private final RoomController roomController;

    public EntryListViewModel(){
        roomController = RoomController.getInstance();
    }

    LiveData<List<Journal>> getAllEntries(){
        return roomController.getAllEntries();
    }
}
