# Class: DatabaseHelper

## 1. General Information
*   **Class Name:** `DatabaseHelper`
*   **Type:** Normal Class (extends `SQLiteOpenHelper`)
*   **Assigning a class in an application:** Manages local SQLite database creation and data insertions.

## 2. Classroom Methods
### Method name: `insertFileInfo`
*   Inserts new file record into SQLite table using `ContentValues` safely without closing connection prematurely.
