package misc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/*
 * Created on Jan 2, 2019
 * By Kenneth Evans, Jr.
 */

public class SimpleGUIApp extends JFrame
{
    private static final long serialVersionUID = 1L;
    private static final int WIDTH = 500;
    private static final int HEIGHT = 600;
    private static final String TITLE = "PhotoSwipe Gallery";

    private JMenuBar menuBar;

    /**
     * SimpleGUIApp constructor.
     */
    public SimpleGUIApp() {
        // loadUserPreferences();
        uiInit();
    }

    /**
     * Initializes the user interface.
     */
    void uiInit() {
        this.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        this.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     * Initializes the menus.
     */
    private void initMenus() {
        JMenuItem menuItem;

        // Menu
        menuBar = new JMenuBar();

        // File
        JMenu menu = new JMenu();
        menu.setText("File");
        menuBar.add(menu);

        // // File Open
        // JMenuItem menuItem = new JMenuItem();
        // menuItem.setText("Open...");
        // menuItem.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent ae) {
        // open();
        // }
        // });
        // menu.add(menuItem);
        //
        // JSeparator separator = new JSeparator();
        // menu.add(separator);

        // File Exit
        menuItem = new JMenuItem();
        menuItem.setText("Exit");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                quit();
            }
        });
        menu.add(menuItem);

        // // Help
        // menu = new JMenu();
        // menu.setText("Help");
        // menuBar.add(menu);

        // menuItem = new JMenuItem();
        // menuItem.setText("Contents");
        // menuItem.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent ae) {
        // try {
        // java.awt.Desktop.getDesktop()
        // .browse(java.net.URI.create(HELP_URL));
        // } catch(IOException ex) {
        // Utils.excMsg("Cannot open help contents", ex);
        // }
        // }
        // });
        // menu.add(menuItem);

        // menuItem = new JMenuItem();
        // menuItem.setText("About");
        // menuItem.addActionListener(new ActionListener() {
        // public void actionPerformed(ActionEvent ae) {
        // JOptionPane.showMessageDialog(null,
        // new AboutBoxPanel(HELP_TITLE, AUTHOR, COMPANY, COPYRIGHT),
        // "About", JOptionPane.PLAIN_MESSAGE);
        // }
        // });
        // menu.add(menuItem);
    }

    /**
     * Quits the application
     */
    private void quit() {
        System.exit(0);
    }

    /**
     * Puts the panel in a JFrame and runs the JFrame.
     */
    public void run() {
        try {
            // Create and set up the window.
            // JFrame.setDefaultLookAndFeelDecorated(true);
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // SwingUtilities.updateComponentTreeUI(this);
            this.setTitle(TITLE);
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // frame.setLocationRelativeTo(null);

            // // Set the icon
            // ImageUtils.setIconImageFromResource(this,
            // "/resources/ICC Profile Viewer.256x256.png");

            // Has to be done here. The menus are not part of the JPanel.
            initMenus();
            this.setJMenuBar(menuBar);

            // Display the window
            this.setBounds(20, 20, WIDTH, HEIGHT);
            this.setVisible(true);
        } catch(Throwable t) {
            Utils.excMsg("Error running ICCProfileViewer", t);
        }
    }

    /**
     * @param args
     */

    /**
     * @param args
     */
    public static void main(String[] args) {
        final SimpleGUIApp app = new SimpleGUIApp();

        try {
            // Set window decorations
            JFrame.setDefaultLookAndFeelDecorated(true);

            // Set the native look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(Throwable t) {
            Utils.excMsg("Error setting Look & Feel", t);
        }

        // Make the job run in the AWT thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                if(app != null) {
                    app.run();
                }
            }
        });
    }

}
