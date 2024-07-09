package androidsamples.java.journalapp;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.room.Room;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RoomController {

    private static final String DATABASE_NAME = "journal";
    private final JournalDAO mJournalEntryDao;
    private static RoomController sInstance;

    private final Executor mExecutor = Executors.newSingleThreadExecutor();

    private RoomController(Context context) {
        JournalDatabase db
                = Room.databaseBuilder(context.getApplicationContext(),
                JournalDatabase.class,
                DATABASE_NAME).build();
        mJournalEntryDao = db.dao();
    }

    public static void init(Context context) {
        if (sInstance == null) sInstance = new RoomController(context);
    }

    public static RoomController getInstance() {
        if (sInstance == null)
            throw new IllegalStateException("Repo must be initialized");
        return sInstance;
    }

    public void insertEntry(Journal data){
        mExecutor.execute(() ->  mJournalEntryDao.insertJournal(data));
    }
    public void deleteEntry(Journal data){
        mExecutor.execute(() ->  mJournalEntryDao.deleteJournal(data));
    }
    public void updateEntry(Journal data){
        mExecutor.execute(() ->  mJournalEntryDao.updateJournal(data));
    }
    public LiveData<Journal> getEntry(UUID uid){
        return mJournalEntryDao.getEntry(uid);
    }
    public LiveData<List<Journal>> getAllEntries(){
        return mJournalEntryDao.getAll();
    }
}

