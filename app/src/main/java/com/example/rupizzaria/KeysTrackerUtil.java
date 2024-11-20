package pizzeria;

import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Utility class for tracking key inputs for easier keyboard shortcut add-ons.
 *
 * @author Frank Garcia
 */
public class KeysTrackerUtil {
    private SortedSet<KeyCode> keysPressed = new TreeSet<>();
    private final SortedSet<KeyCode> closeWindowShortcutWindows = new TreeSet<>(Set.of(KeyCode.W, KeyCode.CONTROL));
    private final SortedSet<KeyCode> closeWindowShortcutMac = new TreeSet<>(Set.of(KeyCode.W, KeyCode.COMMAND));


    /**
     * Listener that enables the user to close the window when they press the Ctrl+W keys on their keyboard.
     */
    protected void closeWindow(KeyEvent keyEvent) {
        Stage stage = (Stage) ((Node) keyEvent.getSource()).getScene().getWindow();
        // System.out.println("Ctrl and W have been pressed");
        stage.close();
    }

    /**
     * Listener that tracks keys pressed by the user.
     * @param keyEvent The Key event for the key being pressed that will be added to the keysPressed TreeSet.
     */
    protected void keysPressedTracker(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        keysPressed.add(keyCode);
        // System.out.println("Added key " + keyCode);
        // System.out.println("Remaining keys: " + keysPressed);
        System.out.println(closeWindowShortcutWindows);
        if (keysPressed.equals(closeWindowShortcutWindows) || keysPressed.equals(closeWindowShortcutMac))
            closeWindow(keyEvent);
    }

    /**
     * Listener that tracks keys released by the user.
     * @param keyEvent The Key event for the key being pressed that will be removed from the keysPressed TreeSet.
     */
    protected void keysReleasedTracker(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        keysPressed.remove(keyCode);
        // System.out.println("Removed key " + keyCode);
        // System.out.println("Remaining keys: " + keysPressed);
    }
}
