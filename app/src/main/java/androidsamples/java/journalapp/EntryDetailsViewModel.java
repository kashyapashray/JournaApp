package androidsamples.java.journalapp;

import androidx.lifecycle.ViewModel;

public class EntryDetailsViewModel extends ViewModel {
    String title;
    String date;
    String start;
    String end;

    private final RoomController roomController;

    public EntryDetailsViewModel(){
        title = "";
        date = "Date";
        start = "Start Time";
        end = "End Time";
        roomController = RoomController.getInstance();
    }

    void deleteEntries(Journal j){
        roomController.deleteEntry(j);
    }

    void updateEntry(Journal j){
        roomController.updateEntry(j);
    }

    void insertEntry(Journal j){
        roomController.insertEntry(j);
    }
}
