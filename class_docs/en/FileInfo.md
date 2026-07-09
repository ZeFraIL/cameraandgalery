# Class: FileInfo

## 1. General Information
*   **Class Name:** `FileInfo`
*   **Type:** Normal Class (Model/POJO)
*   **Assigning a class in an application:** This class acts as a "container" or "data model." Its sole purpose is to hold information about a specific media file (photo or video) in a structured way.
*   **How it interacts with other components:**
    *   It is used by `DatabaseHelper` to represent a row from the database.
    *   It is used by `ViewInfoAboutMyFiles` and its Adapter to display data in the list.

## 2. Variables (class fields)
| Name | Type | Purpose | Where is it used |
| :--- | :--- | :--- | :--- |
| `name` | `String` | Stores the filename. | Constructor and `getName()`. |
| `date` | `String` | Stores the date the file was created. | Constructor and `getDate()`. |
| `time` | `String` | Stores the time the file was created. | Constructor and `getTime()`. |
| `uri` | `String` | Stores the path (address) of the file. | Constructor and `getUri()`. |
| `type` | `String` | Stores whether it is an "image" or "video". | Constructor and `getType()`. |

## 3. Classroom Methods
### Method name: `FileInfo` (Constructor)
*   **Type:** `public`
*   **Return value:** None (Constructor)
*   **Parameters:**
    | Name | Type | Description |
    | :--- | :--- | :--- |
    | `name` | `String` | The name of the file. |
    | `date` | `String` | The date string. |
    | `time` | `String` | The time string. |
    | `uri` | `String` | The file location string. |
    | `type` | `String` | The file type (image/video). |
*   **What does the method do:** It initializes a new instance of the class by assigning the provided values to the class fields.
*   **When called:** When you want to create a new object representing a file, usually after reading from a database.
*   **What is important to understand:** This method is called once per object creation.

### Method name: `getName`, `getDate`, `getTime`, `getUri`, `getType` (Getters)
*   **Type:** `public`
*   **Return value:** `String` (the requested field value)
*   **Parameters:** None
*   **What does the method do:** Simply returns the value of the private field to the caller.
*   **When called:** When another class needs to read the data stored in this object.
*   **What is important to understand:** These are standard "getter" methods used to maintain encapsulation (keeping variables private).

## 4. Lifecycle
*   Not applicable (This is a normal Java class, not an Activity).

## 5. Interface Interaction (UI)
*   Does not interact with the UI directly. It only provides the data that the UI will eventually show.

## 6. Interaction with other components
*   **Database:** Used to map database rows into Java objects.
*   **UI Components:** Used by `RecyclerView` to know what text or image to display.

## 7. General logic of the class
This class is a simple "Data Transfer Object". It doesn't perform complex calculations. It just sits there and holds data so that we can pass a single `FileInfo` object around instead of 5 separate strings.

**Use Case:**
1. Database returns a row.
2. We call `new FileInfo(...)` with the row's data.
3. We add this object to a list.
4. The list is passed to an Adapter to show on screen.

## 8. Simplified explanation
**Explanation in simple words:**
Think of `FileInfo` like a **index card** in a library. On this card, we write down the book's title, the date it was added, its location on the shelf, and whether it's a hardback or a paperback. The card itself doesn't "do" anything, but it's much easier to carry one card around than to carry the whole book or try to remember all the details separately.
