# Activity: Start

## 1. General Information
*   **Class Name:** `Start`
*   **Type:** Activity
*   **Assigning a class in an application:** Splash Screen entry point.
*   **How it interacts with other components:** Launches `MainActivity` after 2 seconds and finishes itself.

## 2. Variables (class fields)
| Name | Type | Purpose |
| :--- | :--- | :--- |
| `cdt` | `CountDownTimer` | Timer instance for 2-second splash screen. |

## 3. Classroom Methods
### Method name: `onCreate`
*   Sets layout, Edge-to-Edge insets, and starts `CountDownTimer`.

### Method name: `onDestroy`
*   Cancels `cdt` timer to prevent memory leaks.
