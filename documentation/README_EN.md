# 📱 Android Application Documentation: CameraAndGalery

________________________________________
## 🧾 General Information
**Project Name:**
CameraAndGalery

**Author(s):**
Zeev Fraiman

**Date:**
May 2024 (Updated: February 2025)

**Language:**
Java

**Development Environment:**
Android Studio

**Android Version (minSdk / targetSdk):**
28 / 34
________________________________________
## 🎯 Project Goal
•	**Task the app solves:** Allows users to take photos and record videos, automatically saving file metadata (name, date, time, path) into a local database for later review.
•	**Why it is important:** Simplifies the organization of media files and provides quick access to information about created content in one place.
•	**Target Audience:** Users who need to keep track of their created media content.
________________________________________
## 📌 Application Requirements
### Functional Requirements
•	Capture photos using the system camera.
•	Record videos using the system camera.
•	Save file information (name, date, time, URI) to SQLite.
•	View a list of all saved records.
•	View file details (photo preview or video playback with controls) in a popup dialog.
•	Open the system gallery.

### Non-functional Requirements
•	**Performance:** Fast background loading of the file list from DB using RecyclerView and ExecutorService.
•	**Usability:** Simple interface with four main buttons on the home screen, adapted for system Edge-to-Edge insets.
•	**Reliability:** Proper permission handling (`ActivityResultContracts.RequestPermission`) and secure file sharing via `FileProvider`.
________________________________________
## 🧠 General Architecture
•	**Chosen Approach:**
–	MVC (Model-View-Controller)

•	**Why this approach:**
For a small-scale project, this approach ensures clear separation between data (Model), interface (View), and control logic (Controller/Activity) without unnecessary complexity.

•	**Main System Components:**
- **Model:** DatabaseHelper, FileInfo.
- **View:** XML layouts (activity_main, activity_view_info_about_my_files, item_file_info).
- **Controller:** Start, MainActivity, ViewInfoAboutMyFiles.
________________________________________
## 🧩 UML Diagram
`[Start]` –> `[MainActivity]`
`[MainActivity]` –> `[DatabaseHelper]`
`[ViewInfoAboutMyFiles]` –> `[DatabaseHelper]`
`[ViewInfoAboutMyFiles]` –> `[FileInfo]`
`[DatabaseHelper]` –> `[SQLite Database]`

**Packages:**
The project uses a flat package structure: `zeev.fraiman.cameraandgalery`.
- Helps in quick navigation for a small number of classes.
- For scaling, it is recommended to split into `data`, `ui`, and `model` packages.
________________________________________
## 🧩 Detailed Class Description
### 📌 Class: Start
**Role:** Splash Screen.
**Responsibility:** Displays splash screen for 2 seconds, safely cancels timer in `onDestroy()`, launches `MainActivity`, and calls `finish()`.

### 📌 Class: MainActivity
**Role:** Main application screen.
**Responsibility:** Lifecycle management, button click handling, launching camera/video Intents, runtime CAMERA permission handling, and asynchronous DB saving.
**Main Methods:**
- `onCreate()` — Initializes UI, launchers, and Edge-to-Edge insets.
- `checkCameraPermissionAndExecute()` — Verifies and requests camera permissions.
- `dispatchTakePictureIntent()` — Starts camera for taking a photo.
- `dispatchTakeVideoIntent()` — Starts camera for recording video.
- `openGallery()` — Safely opens system gallery with valid MIME intent.
- `saveFileInfoToDatabase()` — Saves file info asynchronously off the UI thread.

### 📌 Class: DatabaseHelper
**Role:** Data layer.
**Why used:** To interact with the local SQLite database, maintain table structure, and execute queries.

### 📌 Class: FileInfo
**Role:** Data model.
**Description:** Contains fields like `name`, `date`, `time`, `uri`, and `type` to represent media file information.

### 📌 Class: ViewInfoAboutMyFiles
**Role:** List view screen.
**Responsibility:** Asynchronously loads data from the DB, displays items in a RecyclerView with video/image thumbnails via Glide, and shows interactive detail dialogs.
________________________________________
## 🔄 Application Workflow
1. App launches `Start` with a 2-second timer.
2. `MainActivity` opens. User taps "Make a photo" or "Make a video".
3. CAMERA permission is verified/requested. System camera app launches.
4. After capture, metadata (file path and timestamp) is written to SQLite in a background thread.
5. User navigates to "View info", where entries are fetched asynchronously.
6. RecyclerView displays image and video thumbnails using Glide.
7. Clicking an item opens a dialog with full details and video playback controls (`MediaController`).
________________________________________
## 🎨 UI/UX Analysis
•	**Design Choice:** A grid-like button structure for quick access to core features with Edge-to-Edge support.
•	**Principles Used:**
–	**Simplicity:** Minimal steps to achieve the goal.
–	**Logic:** Meaningful grouping of actions.
•	**Improvements:** Add dark mode support and a delete feature for entries.
________________________________________
## ⚙️ Threading
•	**Usage:**
- Yes. SQLite DB reading and writing execute asynchronously in background threads using `ExecutorService`.
•	**ANR Prevention:**
- Database I/O is offloaded from the UI Thread.
- Glide is used for asynchronous image/video thumbnail decoding and caching.
________________________________________
## 💾 Data Management
•	**Storage:** Local SQLite database (all_info.db) and app's private external storage `getExternalFilesDir()` (for media).
•	**Choice:** Ensures autonomy, compliance with Android storage policies, and fast offline data retrieval.
________________________________________
## 🌐 Networking
•	This version does not include networking features (the app is fully offline).
________________________________________
## 🔐 Security (Basic Level)
•	File access is restricted to application scope and shared safely using `FileProvider` (`<external-files-path>`).
________________________________________
## 🧪 Testing
•	**Unit Tests:** Validating file name generation logic.
•	**UI Tests:** Verifying camera intent launching (automated via Espresso).
________________________________________
## 🐞 Error Handling
•	Intent handling checks and try-catch blocks during temporary file creation.
•	Permission denials are handled gracefully with Toast user notifications.
________________________________________
## ⚡ Performance
•	**Optimizations:** Background threads (`ExecutorService`) for SQLite and Glide caching for video/image previews.
•	**Bottlenecks:** Pagination might be required for very large lists (thousands of entries).
________________________________________
## 🚀 Expansion Possibilities
•	Add filtering by date and file type.
•	Sync with cloud storage (e.g., Firebase).
•	In-app photo editing tools.
