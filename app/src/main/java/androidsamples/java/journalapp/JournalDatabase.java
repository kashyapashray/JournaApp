package androidsamples.java.journalapp;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Journal.class}, version = 1, exportSchema = false)
@TypeConverters(JournalTypeConverter.class)
public abstract class JournalDatabase extends RoomDatabase {
    public abstract JournalDAO dao();
}
