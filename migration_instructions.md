Of course, your English is perfectly clear, and this is a very important problem to solve. You are correct, this is a database migration issue. I can guide you through the process of fixing it so you can keep your old data.

When you change a database table (like adding the `occupation` column), the old database file on your device no longer matches the new code. You need to give Room instructions on how to upgrade the old database to the new version. This is called a **Migration**.

Here are the steps to fix this.

### Step 1: Increase the Database Version

First, you must tell Room that there is a new version of the database.

1.  **Open this file:** `composeApp/src/commonMain/kotlin/com/waiyan/myittar_oo_emr/local/database/EmrDatabase.kt`
2.  **Change the version number from `1` to `2`** in the `@Database` annotation.

**Change this:**
```kotlin
@Database(
    entities = [Patient::class, MedicalInfo::class, FollowUp::class, Visit::class], version = 1
)
```

**To this:**
```kotlin
@Database(
    entities = [Patient::class, MedicalInfo::class, FollowUp::class, Visit::class], version = 2
)
```

### Step 2: Create the Migration Instructions

Now, we need to create the instructions that tell Room *how* to go from version 1 to version 2. We will add the `occupation` column using SQL.

1.  **Open this file:** `composeApp/src/androidMain/kotlin/com/waiyan/myittar_oo_emr/database/EmrDatabase.kt`
2.  **Add the following code** to that file, just above the `getDatabaseBuilder` function.

```kotlin
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

// This object tells Room how to migrate from version 1 to 2
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // SQL command to add the 'occupation' column to the 'Patient' table
        // We set a default value of '' (an empty string) for all existing rows.
        db.execSQL("ALTER TABLE Patient ADD COLUMN occupation TEXT NOT NULL DEFAULT ''")
    }
}

fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<EmrDatabase> {
    // ... (rest of your function)
}
```

### Step 3: Add the Migration to the Database Builder

Finally, you need to tell the database builder to use your new migration instructions when it creates the database.

1.  **Stay in the same file:** `composeApp/src/androidMain/kotlin/com/waiyan/myittar_oo_emr/database/EmrDatabase.kt`
2.  **Modify the `Room.databaseBuilder` call** inside the `getDatabaseBuilder` function to include `.addMigrations()`.

**Change this:**
```kotlin
fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<EmrDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("emr_database.db")
    return Room.databaseBuilder<EmrDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    ).setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
}
```

**To this:**
```kotlin
fun getDatabaseBuilder(context: Context): RoomDatabase.Builder<EmrDatabase> {
    val appContext = context.applicationContext
    val dbFile = appContext.getDatabasePath("emr_database.db")
    return Room.databaseBuilder<EmrDatabase>(
        context = appContext,
        name = dbFile.absolutePath
    )
    .addMigrations(MIGRATION_1_2) // <-- ADD THIS LINE
    .setJournalMode(RoomDatabase.JournalMode.TRUNCATE)
}
```

### Summary

That's it! After making these three changes, the next time you run your app, Room will see the new version number, find `MIGRATION_1_2`, and execute the SQL command. This will update your existing database table by adding the `occupation` column to it without deleting any of your patient data. All your old patients will have an empty string `""` as their occupation, which you can then edit in the app.