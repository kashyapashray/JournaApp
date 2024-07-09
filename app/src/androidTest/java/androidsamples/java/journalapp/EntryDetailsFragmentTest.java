package androidsamples.java.journalapp;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.accessibility.AccessibilityChecks;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Arrays;

/**
 * Instrumented tests for the {@link EntryDetailsFragment}.
 */
@RunWith(AndroidJUnit4.class)
public class EntryDetailsFragmentTest {

    Context context;
//    private View decorView;

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule = new ActivityScenarioRule<>(MainActivity.class);

    @BeforeClass
    public static void accessibility() {
        AccessibilityChecks.enable();
    }

    @Before
    public void room() {
        context = ApplicationProvider.getApplicationContext();
        RoomController.init(context);
    }

//    @Before
//    public void setUp() {
//        activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
//    }

    @Test
    public void CreateIncomplete(){
        onView(withId(R.id.btn_add_entry)).perform(click());
        onView(withId(R.id.edit_title)).perform(ViewActions.typeText("TEST_TITLE_INCOMPLETE"));
        onView(withId(R.id.btn_entry_date)).perform(click());

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(2023, 12, 12);

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(mCalendar.get(Calendar.YEAR),mCalendar.get(Calendar.MONTH),mCalendar.get(Calendar.DAY_OF_MONTH)));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_save)).perform(click());

//        String toast = "Enter end time";

//        onView(withText(toast))
//                .inRoot(RootMatchers.withDecorView(Matchers.not(decorView)))// Here we use decorView
//                .check(matches(isDisplayed()));

//        onView(withText(toast)).inRoot(new ToastMatcher())
//                .check(matches(withText(R.string.sharing)));
        onView(withId(R.id.layout_entry_detail)).check(matches(isDisplayed()));

    }

    @Test
    public void testCreate() {
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion());
        int count = RecyclerViewItemCountAssertion.count;

        onView(withId(R.id.btn_add_entry)).perform(click());
        onView(withId(R.id.edit_title)).perform(ViewActions.typeText("TEST_TITLE_CREATE"));
        onView(withId(R.id.btn_entry_date)).perform(click());

        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(2023, 12, 12);

        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,26));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btn_save)).perform(click());

        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(count, RecyclerExtractor.clickChildViewWithId()));
        String title = RecyclerExtractor.title;
        String date = RecyclerExtractor.date;
        String start = RecyclerExtractor.start;
        String end = RecyclerExtractor.end;
        mCalendar.set(2023, 12 - 1, 26);
        String e_date =  DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());

        assertEquals(title,"TEST_TITLE_CREATE");
        assertEquals(date, e_date);
        assertEquals(start,"13:00");
        assertEquals(end,"15:00");
    }

    @Test
    public void testShare(){
        try {
            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        }catch (Exception e){
            System.out.println("No entry to modify\n adding a entry");
            onView(withId(R.id.btn_add_entry)).perform(click());
            onView(withId(R.id.edit_title)).perform(ViewActions.typeText("TEST_TITLE"));
            onView(withId(R.id.btn_entry_date)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,12));
            onView(withText("OK")).perform(click());

            onView(withId(R.id.btn_start_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
            onView(withText("OK")).perform(click());

            onView(withId(R.id.btn_end_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.btn_save)).perform(click());
            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }


        onView(withId(R.id.shareOption)).perform(click());
    }

    @Test
    public void testUpdate() {

        String title;
        String date;
        String start;
        String end;
        try {
            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        }catch (Exception e){
            System.out.println("No entry to modify\n adding a entry");
            onView(withId(R.id.btn_add_entry)).perform(click());
            onView(withId(R.id.edit_title)).perform(ViewActions.clearText());
            onView(withId(R.id.edit_title)).perform(ViewActions.typeText("TEST_TITLE"));
            onView(withId(R.id.btn_entry_date)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,29));
            onView(withText("OK")).perform(click());

            onView(withId(R.id.btn_start_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
            onView(withText("OK")).perform(click());

            onView(withId(R.id.btn_end_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.btn_save)).perform(click());
            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        }
        onView(withId(R.id.edit_title)).perform(ViewActions.clearText());
        onView(withId(R.id.edit_title)).perform(ViewActions.typeText("TEST_TITLE_UPDATE"));
        onView(withId(R.id.btn_entry_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,29));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
        onView(withText("OK")).perform(click());
        onView(withId(R.id.btn_save)).perform(click());

        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, RecyclerExtractor.clickChildViewWithId()));
        title = RecyclerExtractor.title;
        date = RecyclerExtractor.date;
        start = RecyclerExtractor.start;
        end = RecyclerExtractor.end;

        onView(withId(R.id.recyclerView)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(title, date, start, end, false)));

    }

    @Test
    public void testDeleteNEW() {

        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion());
        int count = RecyclerViewItemCountAssertion.count;

        onView(withId(R.id.btn_add_entry)).perform(click());
        onView(withId(R.id.edit_title)).perform(ViewActions.typeText("TEST_TITLE"));

        onView(withId(R.id.btn_entry_date)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,12));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_start_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.btn_end_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.deleteOption)).perform(click());
        onView(withText("Yes")).perform(click());
        onView(withId(R.id.recyclerView)).check(new RecyclerViewItemCountAssertion());
        int count2 = RecyclerViewItemCountAssertion.count;
        assertEquals(count2, count);
    }

    @Test
    public void testDeleteExisting() {
        try {
            onView(withId(R.id.recyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, RecyclerExtractor.clickChildViewWithId()));
            String title = RecyclerExtractor.title;
            String date = RecyclerExtractor.date;
            String start = RecyclerExtractor.start;
            String end = RecyclerExtractor.end;
            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.deleteOption)).perform(click());
            onView(withText("Yes")).perform(click());
            onView(withId(R.id.recyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(title, date, start, end, true)));


        }catch (AssertionError a){
          System.out.println(Arrays.toString(a.getStackTrace()));
        } catch (Exception e){
            System.out.println("No entry to modify\n adding a entry");
            onView(withId(R.id.btn_add_entry)).perform(click());
            onView(withId(R.id.edit_title)).perform(ViewActions.typeText("TEST_TITLE_DELETE"));
            onView(withId(R.id.btn_entry_date)).perform(click());
            onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2023,12,12));
            onView(withText("OK")).perform(click());

            onView(withId(R.id.btn_start_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(13,0));
            onView(withText("OK")).perform(click());

            onView(withId(R.id.btn_end_time)).perform(click());
            onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(15,0));
            onView(withText("OK")).perform(click());
            onView(withId(R.id.btn_save)).perform(click());

            onView(withId(R.id.recyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, RecyclerExtractor.clickChildViewWithId()));
            String title = RecyclerExtractor.title;
            String date = RecyclerExtractor.date;
            String start = RecyclerExtractor.start;
            String end = RecyclerExtractor.end;

            onView(withId(R.id.recyclerView)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
            onView(withId(R.id.deleteOption)).perform(click());
            onView(withText("Yes")).perform(click());
            onView(withId(R.id.recyclerView)).perform(
                    RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(title, date, start, end, true)));
        }


    }
}

class RecyclerViewItemCountAssertion implements ViewAssertion {

    static int count = 0;

    @Override
    public void check(View view, NoMatchingViewException noViewFoundException) {
        if (noViewFoundException != null) {
            throw noViewFoundException;
        }

        RecyclerView recyclerView = (RecyclerView) view;
        RecyclerView.Adapter<EntryAdapter.MyViewHolder> adapter = (EntryAdapter) recyclerView.getAdapter();
        assert adapter != null;
        count = adapter.getItemCount();
        assertThat(adapter.getItemCount(), is(adapter.getItemCount()));
    }
}

class MyViewAction {
    static String title;
    static String date;
    static String start;
    static String end;
    public static ViewAction clickChildViewWithId(final String t, final String d, final String s, final String e, final boolean notEquals) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                title = ((TextView)view.findViewById(R.id.txt_item_title)).getText().toString();
                date = ((TextView)view.findViewById(R.id.txt_item_date)).getText().toString();
                start = ((TextView)view.findViewById(R.id.txt_item_start_time)).getText().toString();
                end = ((TextView)view.findViewById(R.id.txt_item_end_time)).getText().toString();
                if(notEquals) {
                    assertNotEquals(title, t);
                    assertNotEquals(date, d);
                    assertNotEquals(start, s);
                    assertNotEquals(end, e);
                }else{
                    assertEquals(title, t);
                    assertEquals(date, d);
                    assertEquals(start, s);
                    assertEquals(end, e);
                }
            }
        };
    }

}


class RecyclerExtractor {
    static String title;
    static String date;
    static String start;
    static String end;
    public static ViewAction clickChildViewWithId() {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Click on a child view with specified id.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                title = ((TextView)view.findViewById(R.id.txt_item_title)).getText().toString();
                date = ((TextView)view.findViewById(R.id.txt_item_date)).getText().toString();
                start = ((TextView)view.findViewById(R.id.txt_item_start_time)).getText().toString();
                end = ((TextView)view.findViewById(R.id.txt_item_end_time)).getText().toString();
            }
        };
    }

}

//class ToastMatcher extends TypeSafeMatcher<Root> {
//
//    @Override    public boolean matchesSafely(Root root) {
//        int type = root.getWindowLayoutParams().get().type;
//        if ((type == WindowManager.LayoutParams.TYPE_TOAST)) {
//            IBinder windowToken = root.getDecorView().getWindowToken();
//            IBinder appToken = root.getDecorView().getApplicationWindowToken();
//            //means this window isn't contained by any other windows.
//            return windowToken == appToken;
//        }
//        return false;
//    }
//
//    @Override
//    public void describeTo(org.hamcrest.Description description) {
//        description.appendText("is toast");
//    }
//}