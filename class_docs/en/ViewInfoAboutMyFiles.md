# Activity: ViewInfoAboutMyFiles

## 1. General Information
*   **Class Name:** `ViewInfoAboutMyFiles`
*   **Type:** Activity
*   **Assigning a class in an application:** Asynchronously fetches all saved records from SQLite and displays them in a RecyclerView.

## 2. Variables (class fields)
| Name | Type | Purpose |
| :--- | :--- | :--- |
| `recyclerView` | `RecyclerView` | UI component displaying list. |
| `fileInfoList` | `List<FileInfo>` | List of file data objects. |
| `executorService` | `ExecutorService` | Background executor for DB reads. |
| `mainHandler` | `Handler` | Posts UI updates to the main thread. |

## 3. Classroom Methods
### Method name: `loadFileInfoFromDatabase`
*   Queries SQLite in a background thread and posts results to the main thread.

### Method name: `showFileInfoDialog`
*   Displays dialog with image/video preview and interactive `MediaController` video playback.

## 4. Lifecycle
*   **`onCreate()`**: Configures Edge-to-Edge, sets up RecyclerView, and triggers async DB load.
*   **`onDestroy()`**: Shuts down `executorService`.
