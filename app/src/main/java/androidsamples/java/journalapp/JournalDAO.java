package androidsamples.java.journalapp;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
import java.util.UUID;

@Dao
public interface JournalDAO {
    @Query("SELECT * FROM journal")
    LiveData<List<Journal>> getAll();

    @Query("SELECT * from journal WHERE id=(:id)")
    LiveData<Journal> getEntry(UUID id);

    @Insert
    void insertJournal(Journal journals);

    @Delete
    void deleteJournal(Journal journal);

    @Update
    void updateJournal(Journal journal);
}
