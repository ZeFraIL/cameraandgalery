# Activity: MainActivity

## 1. General Information
*   **Class Name:** `MainActivity`
*   **Type:** Activity
*   **Assigning a class in an application:** This is the "Control Center." It handles the main user actions: taking photos, recording videos, and navigating to the gallery or the list of files.
*   **How it interacts with other components:**
    *   Starts the System Camera app.
    *   Uses `DatabaseHelper` to save info.
    *   Starts `ViewInfoAboutMyFiles` activity.

## 2. Variables (class fields)
| Name | Type | Purpose | Where is it used |
| :--- | :--- | :--- | :--- |
| `currentPhotoPath` | `String` | Stores the temporary path of a photo being taken. | `createImageFile`, `onActivityResult`. |
| `takePictureLauncher` | `ActivityResultLauncher` | Modern way to handle "returning" from the camera app. | `onCreate`, `dispatchTakePictureIntent`. |
| `dbHelper` | `DatabaseHelper` | Instance to talk to the database. | `onCreate`, `saveFileInfoToDatabase`. |

## 3. Classroom Methods
### Method name: `dispatchTakePictureIntent`
*   **Type:** `private`
*   **Return value:** `void`
*   **What does the method do:**
    1. Prepares an Intent for `ACTION_IMAGE_CAPTURE`.
    2. Creates an empty file on the phone to store the image.
    3. Tells the camera app: "Take a picture and save it into THIS file."
    4. Launches the camera.
*   **When called:** When the "Take Photo" button is clicked.

### Method name: `saveFileInfoToDatabase`
*   **Type:** `private`
*   **Return value:** `void`
*   **What does the method do:** Extracts the filename from the path, gets current date/time, and tells `dbHelper` to save it.

## 4. Lifecycle
*   **`onCreate()`**:
    *   Sets the layout.
    *   **Permissions:** Requests CAMERA and STORAGE permissions from the user. (Crucial for modern Android!)
    *   Sets up button click listeners.
    *   Registers "Launchers" for camera results.

## 5. Interface Interaction (UI)
*   **Buttons:** `btnTakePhoto`, `btnRecordVideo`, `btnViewGallery`, `btnViewInfo`.
*   **Events:** `setOnClickListener` handles user taps.

## 6. Interaction with other components
*   **System Camera:** Interacts via `Intent`.
*   **DatabaseHelper:** For storage.
*   **ViewInfoAboutMyFiles:** Via `Intent`.

## 7. General logic of the class
This class coordinates "Intents" (requests). It doesn't take the photo itself—it asks the system's camera app to do it. When the camera app finishes, `MainActivity` gets a notification, finds out where the file is, and logs that info into the database.

**Use Case:**
1. User clicks "Take Photo".
2. `MainActivity` creates a file and launches the Camera.
3. User snaps a photo and hits "OK".
4. `MainActivity` receives the result and saves the file details to SQLite.

## 8. Simplified explanation
**Explanation in simple words:**
Think of `MainActivity` as a **Manager** at a construction site. The Manager doesn't drive the truck or lay the bricks (doesn't take the photo). Instead, the Manager calls the "Camera Specialist" (the system camera app) and says, "Hey, go do your job and put the result in this box (the file)". When the specialist is done, the Manager takes notes in a logbook (the database) about what happened.
