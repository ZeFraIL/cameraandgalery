# Activity: Start

## 1. General Information
*   **Class Name:** `Start`
*   **Type:** Activity
*   **Assigning a class in an application:** This is the "Splash Screen" or the entry point of the app. It shows a temporary screen (usually with a logo) before moving the user to the main part of the app.
*   **How it interacts with other components:**
    *   It starts the `MainActivity` after a delay.

## 2. Variables (class fields)
This class does not have global variables. All logic is contained within the `onCreate` method.

## 3. Classroom Methods
### Method name: `onCreate`
*   **Type:** `protected`
*   **Return value:** `void`
*   **Parameters:**
    | Name | Type | Description |
    | :--- | :--- | :--- |
    | `savedInstanceState` | `Bundle` | Container for data if the activity is recreated. |
*   **What does the method do:**
    1. Sets up the screen layout.
    2. Enables "Edge-to-Edge" (making the app content go under the status bar).
    3. Starts a `CountDownTimer` set for 2000 milliseconds (2 seconds).
*   **When called:** When the app first opens.

### Method name: `onFinish` (inside CountDownTimer)
*   **Type:** `public`
*   **Return value:** `void`
*   **Parameters:** None
*   **What does the method do:**
    1. Creates an `Intent` (a request to start another component) for `MainActivity`.
    2. Calls `startActivity(go)` to switch screens.
*   **When called:** Automatically after the timer reaches zero.

## 4. Lifecycle
*   **`onCreate()`**: Called when the Activity is created. This is where we set the layout and start the timer.
*   **`onStart()` / `onResume()`**: Standard system calls, not explicitly overridden but they happen after `onCreate`.

## 5. Interface Interaction (UI)
*   **Layout:** Uses `R.layout.activity_start`.
*   **Edge-to-Edge:** Adjusts padding so that system bars (like the clock) don't cover the app content.

## 6. Interaction with other components
*   **MainActivity:** Starts `MainActivity` via an `Intent`.

## 7. General logic of the class
The app starts here. The user sees a screen for 2 seconds. Behind the scenes, a timer is counting down. Once the timer is up, the app automatically switches to the main screen.

**Use Case:**
1. User taps the app icon.
2. `Start` activity opens.
3. User sees the splash screen.
4. 2 seconds pass.
5. `MainActivity` opens.

## 8. Simplified explanation
**Explanation in simple words:**
Think of the `Start` class as a **waiting room** or a **movie intro**. When you enter a theater, you don't start the movie immediately; first, you see a short intro or a logo. After a few seconds, the main show starts. This class is that 2-second intro that says "Hello" before the real work begins.
