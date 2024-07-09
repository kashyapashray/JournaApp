package androidsamples.java.journalapp;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.UUID;

@Entity
public class Journal {

    public static Journal fromJournalData(JournalData journalData){
        Journal journal = new Journal(journalData.title,journalData.date,journalData.start,journalData.end);
        journal.setUid(UUID.fromString(journalData.uid));
        return journal;
    }

    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    private UUID uid;

    @ColumnInfo(name = "title")
    public String title;

    @ColumnInfo(name = "duration")
    public String date;

    public Journal(@NonNull String title, String date, String start, String end) {
        uid = UUID.randomUUID();
        this.title = title;
        this.date = date;
        this.start = start;
        this.end = end;
    }

    @ColumnInfo(name = "start")
    public String start;

    @ColumnInfo(name = "end")
    public String end;

    @NonNull
    public UUID getUid() {
        return uid;
    }

    public void setUid(@NonNull UUID uid) {
        this.uid = uid;
    }

    @NonNull
    @Override
    public String toString(){
        return "UID: "+this.uid+" title: "+this.title+" date: "+this.date+" start: "+this.start+" end: "+this.end;
    }
}

class JournalData{
    public String uid;
    public String title;
    public String date;
    public String start;
    public String end;
    public JournalData(String uid, String title, String date, String start, String end){
        this.uid = uid;
        this.title = title;
        this.date = date;
        this.start = start;
        this.end = end;
    }
    public static JournalData fromJournal(Journal j){
        return new JournalData((j.getUid()).toString(), j.title,j.date, j.start, j.end);
    }

    @NonNull
    @Override
    public String toString(){
        return "UID: "+this.uid+" title: "+this.title+" date: "+this.date+" start: "+this.start+" end: "+this.end;
    }
}
