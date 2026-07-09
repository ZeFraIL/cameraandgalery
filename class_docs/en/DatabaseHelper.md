# Class: DatabaseHelper

## 1. General Information
*   **Class Name:** `DatabaseHelper`
*   **Type:** Normal Class (extends `SQLiteOpenHelper`)
*   **Assigning a class in an application:** This class manages the local database (SQLite). It is responsible for creating the database file, creating tables, updating the structure (if needed), and inserting data.
*   **How it interacts with other components:**
    *   `MainActivity` uses it to save new file info.
    *   `ViewInfoAboutMyFiles` uses it to read the list of files.

## 2. Variables (class fields)
| Name | Type | Purpose | Where is it used |
| :--- | :--- | :--- | :--- |
| `DATABASE_NAME` | `String` | The filename of the database on disk (`all_info.db`). | Constructor. |
| `DATABASE_VERSION` | `int` | Version number (helps with updates). | Constructor. |
| `TABLE_NAME` | `String` | Name of the table storing file info. | `onCreate`, `onUpgrade`, `insertFileInfo`. |
| `COLUMN_...` | `String` | Names of the columns (name, date, time, uri, type). | Throughout the class to avoid typos. |
| `SQL_CREATE_ENTRIES` | `String` | The raw SQL command to create the table. | `onCreate`. |

## 3. Classroom Methods
### Method name: `onCreate`
*   **Type:** `public`
*   **Return value:** `void`
*   **Parameters:**
    | Name | Type | Description |
    | :--- | :--- | :--- |
    | `db` | `SQLiteDatabase` | The database object to work with. |
*   **What does the method do:** Executes the SQL command to create the table.
*   **When called:** Automatically by Android the very first time the app tries to access the database.

### Method name: `insertFileInfo`
*   **Type:** `public`
*   **Return value:** `void`
*   **Parameters:** `name`, `date`, `time`, `uri`, `type` (all `String`).
*   **What does the method do:**
    1. Opens the database for writing.
    2. Packages the values into a `ContentValues` object.
    3. Tells the database to "insert" this package into the table.
    4. Closes the connection.
*   **When called:** Manually from `MainActivity` after a photo/video is taken.

## 4. Lifecycle
*   Not applicable (Not an Activity). However, it follows the Android Database Lifecycle managed by the system.

## 5. Interface Interaction (UI)
*   No direct UI interaction. It works "under the hood."

## 6. Interaction with other components
*   **SQLite Database:** It is the bridge between Java and the actual file on the phone's storage.

## 7. General logic of the class
This class centralizes all "bookkeeping." Instead of writing complex SQL code everywhere in the app, we just call simple methods like `insertFileInfo`.

**Use Case:**
1. App says: "Hey, I have a new photo named 'pic.jpg'".
2. `DatabaseHelper` says: "Got it! I'll write that into the 'info_about_files' table in the 'all_info.db' file."

## 8. Simplified explanation
**Explanation in simple words:**
Think of `DatabaseHelper` as a **Librarian**. When you want to register a new book (file) in the library, you don't go and write in the big ledger yourself. You give the info to the Librarian, and they know exactly which book (database), which page (table), and which column to write it in. They also make sure the library building (the database file) is built correctly in the first place.
