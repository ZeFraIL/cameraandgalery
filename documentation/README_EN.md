# 📱 Android Application Documentation: CameraAndGalery

________________________________________
## 🧾 General Information
**Project Name:**
CameraAndGalery

**Author(s):**
Zeev Fraiman

**Date:**
May 2024

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
•	View file details (photo preview or video playback) in a popup dialog.
•	Open the system gallery.

### Non-functional Requirements
•	**Performance:** Fast loading of the file list from the DB using RecyclerView.
•	**Usability:** Simple interface with four main buttons on the home screen.
•	**Reliability:** Proper permission handling and camera availability checks.
________________________________________
## 🧠 General Architecture
•	**Chosen Approach:**
–	MVC (Model-View-Controller)

•	**Why this approach:**
For a small-scale project, this approach ensures clear separation between data (Model), interface (View), and control logic (Controller/Activity) without unnecessary complexity.

•	**Main System Components:**
- **Model:** DatabaseHelper, FileInfo.
- **View:** XML layouts (activity_main, item_file_info).
- **Controller:** MainActivity, ViewInfoAboutMyFiles.
________________________________________
## 🧩 UML Diagram
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
### 📌 Class: MainActivity
**Role:** Main application screen.
**Responsibility:** Lifecycle management, button click handling, launching camera/video Intents, and requesting permissions.
**Main Methods:**
- `onCreate()` — Initializes UI and launchers.
- `dispatchTakePictureIntent()` — Starts camera for taking a photo.
- `dispatchTakeVideoIntent()` — Starts camera for recording video.
- `saveFileInfoToDatabase()` — Writes data to the database.

### 📌 Class: DatabaseHelper
**Role:** Data layer.
**Why used:** To interact with the local SQLite database, maintain table structure, and execute queries.

### 📌 Class: FileInfo
**Role:** Data model.
**Description:** Contains fields like `name`, `date`, `time`, `uri`, and `type` to represent media file information.

### 📌 Class: ViewInfoAboutMyFiles
**Role:** List view screen.
**Responsibility:** Loads data from the DB, displays it in a RecyclerView via an adapter, and shows a detail dialog.
________________________________________
## 🔄 Application Workflow
1. User clicks "Take Photo".
2. System camera opens.
3. After capture, metadata (file path and timestamp) is saved to SQLite via `DatabaseHelper`.
4. User navigates to "View Info" to see the list of all entries.
5. Clicking a list item opens a dialog with a preview.
________________________________________
## 🎨 UI/UX Analysis
•	**Design Choice:** A grid-like button structure for quick access to core features.
•	**Principles Used:**
–	**Simplicity:** Minimal steps to achieve the goal.
–	**Logic:** Meaningful grouping of actions.
•	**Improvements:** Add dark mode support and a delete feature for entries.
________________________________________
## ⚙️ Threading
•	**Usage:**
- In the current version, DB and UI operations mostly run on the Main Thread, which is acceptable for small datasets.
•	**ANR Prevention:** Using Glide for image loading, which handles background threading efficiently.
________________________________________
## 💾 Data Management
•	**Storage:** Local SQLite database (all_info.db) and app's internal storage (for media).
•	**Choice:** Ensures autonomy and fast data retrieval without internet access.
________________________________________
## 🌐 Networking
•	This version does not include networking features (the app is fully offline).
________________________________________
## 🔐 Security (Basic Level)
•	File access is restricted to the application scope using `FileProvider`.
________________________________________
## 🧪 Testing
•	**Unit Tests:** Validating file name generation logic.
•	**UI Tests:** Verifying camera intent launching (automated via Espresso).
________________________________________
## 🐞 Error Handling
•	`resolveActivity` check before launching Intents to prevent crashes if no camera app is found.
•	Try-catch blocks during temporary file creation.
________________________________________
## ⚡ Performance
•	**Optimizations:** Glide for caching and lazy loading of previews.
•	**Bottlenecks:** Pagination might be required for very large lists (thousands of entries).
________________________________________
## 🚀 Expansion Possibilities
•	Add filtering by date and file type.
•	Sync with cloud storage (e.g., Firebase).
•	In-app photo editing tools.
