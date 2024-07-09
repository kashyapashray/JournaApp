package androidsamples.java.journalapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.content.Context;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class JournalRoomDatabaseTest {
    @Rule
    public
    InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    private RoomController rc;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        RoomController.init(context);
        rc = RoomController.getInstance();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        UUID mUid;
        final Journal[] t1 = new Journal[1];
        CountDownLatch latch = new CountDownLatch(1);
        Journal entry = new Journal("Test", "THU, NOV 10, 2022", "11:00", "12:00");
        mUid = entry.getUid();
        rc.insertEntry(entry);

        AtomicReference<Journal> lst = new AtomicReference<>();
        LiveData<Journal> jd = rc.getEntry(mUid);
        jd.observeForever(lst::set);

        latch.await(7, TimeUnit.SECONDS);
        assertEquals(entry.title, lst.get().title);
        assertEquals(entry.date, lst.get().date);
        assertEquals(entry.start, lst.get().start);
        assertEquals(entry.end, lst.get().end);
    }

    @Test
    public void updateIt() throws Exception {
        UUID mUid;
        final Journal[] t1 = new Journal[1];
        CountDownLatch latch = new CountDownLatch(1);
        Journal entry = new Journal("Test", "THU, NOV 10, 2022", "11:00", "12:00");
        mUid = entry.getUid();
        rc.insertEntry(entry);

        entry = new Journal("999", "THU, NOV 10, 9999", "00:00", "12:00");
        entry.setUid(mUid);

        rc.updateEntry(entry);

        AtomicReference<Journal> lst = new AtomicReference<>();
        LiveData<Journal> jd = rc.getEntry(mUid);
        jd.observeForever(lst::set);

        latch.await(7, TimeUnit.SECONDS);
        assertEquals(entry.title, lst.get().title);
        assertEquals(entry.date, lst.get().date);
        assertEquals(entry.start, lst.get().start);
        assertEquals(entry.end, lst.get().end);
    }

    @Test
    public void deleteInList() throws Exception{
        Journal entry = new Journal("Test", "THU, NOV 10, 2022", "11:00", "12:00");
        rc.insertEntry(entry);
        UUID mUid = entry.getUid();

        rc.deleteEntry(entry);

        LiveData<List<Journal>> allEntriesLiveData = rc.getAllEntries();
        final Object[] data = new Object[1];
        CountDownLatch latch = new CountDownLatch(1);

        allEntriesLiveData.observeForever(value -> {
            data[0] = value;
            latch.countDown();
        });

        // Wait for the observer to receive the data
        latch.await(2, TimeUnit.SECONDS);
        boolean isEntryPresent = false;
        for (Journal journal :  (List<Journal>) data[0]) {
            if (journal.getUid() == mUid) {
                isEntryPresent = true;
                break;
            }
        }

        assertFalse(isEntryPresent);
    }
}
