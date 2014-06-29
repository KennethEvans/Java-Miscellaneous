package misc;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Hashtable;

import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Synthesizer based on a Mathematica example.
 * 
 * @author Kenneth Evans, Jr.
 */
public class Synthesizer extends JPanel
{
  private static final int DEFAULT_INSTRUMENT = 1;
  private static final long serialVersionUID = 1L;
  private int borderWidth = 3;
  private int keyGap = 1;
  private int whiteKeyWidth = 50;
  private int whiteKeyHeight = 150;
  private int blackKeyWidth = 30;
  private int blackKeyHeight = 90;
  private boolean isMouseDown = false;
  private boolean isShiftDown = false;
  private static int FIRST_PITCH = 60;
  private int instrument = DEFAULT_INSTRUMENT;
  
  private static final String[] keys = {"1C", "1C#", "1D", "1D#", "1E", "1F",
    "1F#", "1G", "1G#", "1A", "2A#", "2B", "2C", "2C#", "2D", "2D#", "2E",
    "2F", "2F#", "2G", "G#", "3A", "3A#", "3B", "3C"};
  private Hashtable<String, Integer> keyMap = null;

  private JPanel keyPanel = null;
  private javax.sound.midi.Synthesizer synth = null;
  private MouseListener pianoKeyListener = null;
  private MidiChannel channel = null;

  /**
   * Synthesizer constructor
   */
  public Synthesizer() {
    this(DEFAULT_INSTRUMENT);
  }

  /**
   * Synthesizer constructor
   * 
   * @param instrument
   */
  public Synthesizer(int instrument) {
    this.instrument = instrument;
    
    // Create the synthesizer
    try {
      synth = MidiSystem.getSynthesizer();
      synth.open();
        channel = synth.getChannels()[0];
        channel.programChange(instrument - 1);
    } catch(MidiUnavailableException ex) {
      excMsg("Unable to get synthesizer:", ex);
    }

    // Create the keyMap
    keyMap = new Hashtable<String, Integer>();
    for(int i = 0; i < keys.length; i++) {
      keyMap.put(keys[i], new Integer(FIRST_PITCH + i));
    }
    
    // Create the mouse listener
    pianoKeyListener = new MouseListener() {
      public void mouseClicked(MouseEvent ev) {
        // TODO
      }

      public void mouseEntered(MouseEvent ev) {
        if(isMouseDown && isShiftDown) {
          channel.allNotesOff();
          Object source = ev.getSource();
          if(!(source instanceof JPanel)) return;
          JPanel key = (JPanel)source;
          int pitch = keyMap.get(key.getName());
          channel.noteOn(pitch, FIRST_PITCH);
          key.setBackground(Color.gray);
          key.getParent().repaint();
        }
      }

      public void mouseExited(MouseEvent ev) {
        if(isMouseDown && isShiftDown) {
          Object source = ev.getSource();
          if(!(source instanceof JPanel)) return;
          JPanel key = (JPanel)source;
          restoreKeyColor(key);
        }
      }

      public void mousePressed(MouseEvent ev) {
        Object source = ev.getSource();
        if(!(source instanceof JPanel)) return;
        JPanel key = (JPanel)source;
        int pitch = keyMap.get(key.getName());
        channel.noteOn(pitch, FIRST_PITCH);
        isMouseDown = true;
        isShiftDown = ev.isShiftDown();
        key.setBackground(Color.gray);
        key.getParent().repaint();
//        getLocation(key);
//        System.out.println("  " + ev.getX() + "," + ev.getY());
      }

      public void mouseReleased(MouseEvent ev) {
        Object source = ev.getSource();
        if(!(source instanceof JPanel)) return;
        JPanel mouseDownKey = (JPanel)source;
        channel.allNotesOff();
        isMouseDown = false;
        Point srcPos = mouseDownKey.getLocation();
        Point pt = ev.getPoint();
        pt.translate(srcPos.x, srcPos.y);
        Component component = keyPanel.getComponentAt(pt);
        if(component instanceof JPanel) {
          JPanel mouseUpKey = (JPanel)component;
          if(mouseUpKey != null && !(mouseUpKey.equals(keyPanel))) {
            restoreKeyColor(mouseUpKey);
          }
          if(!mouseDownKey.equals(mouseUpKey)) {
            restoreKeyColor(mouseDownKey);
          }
        }
      }
    };

    // Initialize the UI
    uiInit();
  }

  /**
   * Initializes the user interface.
   */
  void uiInit() {
    this.setLayout(null);

    keyPanel = new JPanel();
    this.add(keyPanel);
    keyPanel.setBackground(Color.black);
    keyPanel.setLayout(null);
    keyPanel.setBounds(20, 50, 2 * borderWidth + 15 * (whiteKeyWidth + keyGap)
      - keyGap, whiteKeyHeight + 2 * borderWidth);

    JLabel label = new JLabel("Click to play. Hold Shift key down to drag from");
    label.setBounds(20, 5, 300, 15);
    this.add(label);
    label = new JLabel("key to key (shift must be down before clicking).");
    label.setBounds(20, 20, 300, 15);
    this.add(label);
    label = new JLabel("Volume:");
    label.setBounds(590, 15, 60, 15);
    this.add(label);

    JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 255, 127);
    volumeSlider.setBounds(650, 15, 120, 24);
    this.add(volumeSlider);
    volumeSlider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent ev) {
        Object source = ev.getSource();
        if(!(source instanceof JSlider)) return;
        JSlider slider = (JSlider)source;
        // Controller 7 is the volume controller
        channel.controlChange(7, slider.getValue());
      }
    });

    JComboBox<String> instMenu = new JComboBox<String>();
    this.add(instMenu);
    Instrument[] instruments = synth.getAvailableInstruments();
    for(int i = 0; i < instruments.length; i++) {
      int instNumber = i + 1;
      instMenu.addItem(instNumber + " " + instruments[i].getName());
    }
    instMenu.setBounds(340, 15, 160, 24);
    instMenu.setSelectedIndex(instrument - 1); // Convert to 0-based index
    instMenu.addItemListener(new ItemListener() {

      public void itemStateChanged(ItemEvent ev) {
        Object source = ev.getSource();
        if(!(source instanceof JComboBox<?>)) return;
        // Avoid unchecked cast warning in the smallest possible scope. To check
        // here you would have to use List<?> since you cannot perform
        // instanceof check against parameterized type List<GpxModel>.
        // That generic type information is erased at runtime. Using List<?>
        // doesn't eliminate the warning. Use tempList in the try block as the
        // @SuppressWarnings("unchecked") has to be where the value is declared
        // _and_ set.
        @SuppressWarnings("unchecked")
        JComboBox<String> combo = (JComboBox<String>)source;
        int index = combo.getSelectedIndex();
        if(index < 0) return;
        channel.programChange(index);
      }
    });

    // Add the keys
    for(int i = 0; i < keys.length; i++) {
      createKey(keys[i]);
    }

    // JavaShow(frm);
    // keyMap =
    // Map(
    // createKey(keyPanel, pianoKeyListener, #)&,
    // Transpose({{"C", "C#", "D", "D#", "E", "F", "F#", "G",
    // "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "F",
    // "F#", "G", "G#", "A", "A#", "B", "C"}, Range(60, 84)})
    // );
    // keyPanel@repaint();
    // frm@setModal();
  }

  /**
   * Creates the individual keys. White keys are added at the end. Thus, the
   * last one can be found at the end. Black keys are added at the beginning so
   * they will be on top in the Z order and get mouse events instead of the
   * white keys they overlap.
   * 
   * @param keyName is the name of the key to be created.
   */
  private void createKey(String keyName) {
    JPanel key = new JPanel();
    key.addMouseListener(pianoKeyListener);
    Component lastWhiteKey = null;
    int leftEdge = 0;
    if(!keyName.contains("#")) {
      // White key
      if(keyPanel.getComponentCount() == 0) {
        // First key
        leftEdge = borderWidth;
      } else {
        lastWhiteKey = keyPanel.getComponent(keyPanel.getComponentCount() - 1);
        leftEdge = lastWhiteKey.getX() + keyGap + whiteKeyWidth;
      }
      keyPanel.add(key);
      key.setBounds(leftEdge, borderWidth, whiteKeyWidth, whiteKeyHeight);
      key.setBackground(Color.white);
    } else {
      // Black key
      lastWhiteKey = keyPanel.getComponent(keyPanel.getComponentCount() - 1);
      leftEdge = lastWhiteKey.getX() + whiteKeyWidth - blackKeyWidth
        / 2;
      keyPanel.add(key,0);
      key.setBounds(leftEdge, borderWidth, blackKeyWidth, blackKeyHeight);
      key.setBackground(Color.black);
      key.setForeground(Color.white);
    }
    key.setName(keyName);
  }

  /**
   * Restores the default color (black or white) of the key.
   * 
   * @param key
   */
  private void restoreKeyColor(JPanel key) {
    String keyName = key.getName();
    if(keyName.contains("#")) {
      key.setBackground(Color.black);
    } else {
      key.setBackground(Color.white);
    }
    key.getParent().repaint();
  }

  /**
   * Exception message dialog.  Displays message plus the exception and
   * exception message.
   * @param msg
   * @param ex
   */
  public static void excMsg(String msg, Exception ex) {
    msg += "\n" + "Exception: " + ex + "\n" + ex.getMessage();
    // Show it in a message box
    JOptionPane
      .showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
    System.out.println(msg);
  }
  
  public void getLocation(JPanel key) {
    System.out.println(key.getName());
    System.out.println("  " +  key.getBounds());
    System.out.println("  " +  key.getLocation());
  }
  
  /**
   * Prints information about the key JPanel locations. Used for debugging.
   * 
   */
  public void printLocations() {
    int count = keyPanel.getComponentCount();
    JPanel key;
    for(int i = 0; i < count; i++) {
      key = (JPanel)keyPanel.getComponent(i);  
      getLocation(key);
    }
  }

  /**
   * Method to put the panel in a JFrame and run  the JFrame.
   * 
   */
  public void run() {
    try {
      // Create and set up the window.
      JFrame.setDefaultLookAndFeelDecorated(true);
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
      JFrame frame = new JFrame("Midi Synthesizer");
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//      frame.setLocationRelativeTo(null);

      // Add this panel to the frame
      Container contentPane = frame.getContentPane();
      contentPane.setLayout(new BorderLayout());
      contentPane.add(this, BorderLayout.CENTER);

      // Display the window
      frame.setBounds(20, 20, 825, 275);
      frame.setVisible(true);
//      printLocations();
    } catch(Throwable t) {
      t.printStackTrace();
    }
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    // Make the job run in the AWT thread
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        Synthesizer app = new Synthesizer();
        app.run();
      }
    });
  }

}
