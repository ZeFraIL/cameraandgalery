# Activity: MainActivity

## 1. General Information
*   **Class Name:** `MainActivity`
*   **Type:** Activity
*   **Assigning a class in an application:** Control Center. Handles photo capture, video recording, opening gallery, and navigating to saved files list.
*   **How it interacts with other components:** Launches camera intents, uses `DatabaseHelper` in background threads for saving, opens `ViewInfoAboutMyFiles`.

## 2. Variables (class fields)
| Name | Type | Purpose |
| :--- | :--- | :--- |
| `currentPhotoPath` | `String` | Temporary path of captured photo. |
| `currentVideoPath` | `String` | Temporary path of captured video. |
| `takePictureLauncher` | `ActivityResultLauncher` | Photo capture launcher. |
| `takeVideoLauncher` | `ActivityResultLauncher` | Video recording launcher. |
| `requestCameraPermissionLauncher` | `ActivityResultLauncher` | Camera permission launcher. |
| `dbHelper` | `DatabaseHelper` | SQLite database helper instance. |
| `executorService` | `ExecutorService` | Single thread executor for async DB saves. |

## 3. Classroom Methods
### Method name: `checkCameraPermissionAndExecute`
*   Verifies `Manifest.permission.CAMERA`. Launches capture if granted or requests permission if needed.

### Method name: `saveFileInfoToDatabase`
*   Asynchronously saves file details to SQLite in a background thread.

## 4. Lifecycle
*   **`onCreate()`**: Configures Edge-to-Edge insets, registers launchers, and sets click listeners.
*   **`onDestroy()`**: Shuts down `executorService`.
