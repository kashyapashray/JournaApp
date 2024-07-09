# JournalApp

# Programmer's Setup
If local repo exists before this commit run the following commands\
```git rm -cached build.gradle```\
```git rm -cached .idea/misc.xml```\
Commits might be staged after that so use
```git restore --stage ./<filepath?>/filename```

# Description
 1. MainActivity hosts the navgraph. The List Fragment shows the list of all entries in the journal_database using a LiveData observer 
    to make synchronous changes whenever database has a change. Clicking any entry allows for the DetailsFragment to open in Update/Delete
    mode via passed safeargs along with the entry being passed. Journal Entry from recycler is accessed via a callback to acheieve this .
 2. For creating a Journal Entry safearg has default values passed to it and floatingActionButton opens a blank entry page
 3. depending on opCreateUpdateDelete (Update/Delete if 2 otherwise 1) save button responds differently.
 4. Delete will delete entry from table if 2 is passed otherwise it will just pop the current fragment from the navigation stack
 5. EntryDetails uses ViewModel to store UI state 

# Tasks
1. Implemented navigation using nav graph actions, including UI tests for proper navigation. Start code contains a UI test for future development.
2. Modified the database to include new columns; implemented INSERT, QUERY, UPDATE, and DELETE. Unit-tested the database; referred to Android dev docs for testing guidelines.
3. Added DELETE button to EntryDetailsFragment menu bar, prompting a confirmation dialog. Implemented functionality to delete the entry upon user confirmation.
4. Added SHARE button to EntryDetailsFragment menu bar, creating a plain text message with entry details. Prompted user to choose a sharing method (SMS, email, etc.) and returned to the detailed entry view after sending.
5. Added INFO button to EntryList fragment menu bar, leading to a fragment with a brief app description. Expanded nav graph for new elements.
6. Checked app accessibility using Talk Back, providing a descriptive paragraph of the user experience.
7. Generated an Accessibility Scanner report for the app.
8. Wrote Espresso test cases that include accessibility checks, ensuring a comprehensive testing approach.


# Accessibility Scanner Issues
 1. Custom Toolbar had fixed View Height for EntryDetailsFragment.java and EntryListFragment.java
 2. fragment.entry.xml colour contrast for text

# Accessibility Scanner Issues Fixes
 1. Made layout height wrap_content
 2. Added TextColorPrimary, Secondary, Tertiary and Hint to Theme.JournalApp

# Testing
The Room database was tested for Insert, Update and Delete operations.
 RoomDatabase Testing was done after RoomController was created for checking the database operations 
 and Instrumented Tests using UI  
 The Room database was tested for Insert, Update and Delete operations.
 Espresso was used for Accessibility Testing

# Resources
https://stackoverflow.com/questions/34636934/android-data-binding-setsupportactionbar
https://stackoverflow.com/questions/42424045/how-to-fix-this-when-fragment-is-under-the-toolbar
https://stackoverflow.com/questions/31231609/creating-a-button-in-android-toolbar
https://stackoverflow.com/questions/18579590/how-to-send-data-from-dialogfragment-to-a-fragment
https://stackoverflow.com/questions/50754523/how-to-get-a-result-from-fragment-using-navigation-architecture-component
https://stackoverflow.com/questions/56243119/pass-data-back-to-previous-fragment-using-android-navigation
https://stackoverflow.com/questions/31367599/how-to-update-recyclerview-adapter-data
https://stackoverflow.com/questions/49969278/recyclerview-item-click-listener-the-right-way
https://jacquessmuts.github.io/post/modularization_room/
https://medium.com/@joannekao/android-working-with-themes-and-styles-18cde717f4d
https://stackoverflow.com/questions/72456277/passing-arguments-to-a-destination-in-a-test
https://stackoverflow.com/questions/71097286/how-can-i-launch-a-fragment-with-safe-args-in-an-instrumentation-test
