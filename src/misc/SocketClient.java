package misc;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.prefs.Preferences;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/* * Created on Nov 5, 2017
 * By Kenneth Evans, Jr.
 */

public class SocketClient extends JFrame
{
    private static final String NAME = "Java Socket Client";
    private static final String VERSION = "1.0.0";
    private static final String HELP_TITLE = NAME + " " + VERSION;
    private static final String AUTHOR = "Written by Kenneth Evans, Jr.";
    private static final String COPYRIGHT = "Copyright (c) 2012-2017 Kenneth Evans";
    private static final String COMPANY = "kenevans.net";
    private static final int WIDTH = 400;
    private static final int HEIGHT = 600;

    private static final long serialVersionUID = 1L;
    private static final String TITLE = NAME;

    private static final String TIME_FORMAT = "HH:mm:ss.SSS";
    private static final SimpleDateFormat mFormatter = new SimpleDateFormat(
        TIME_FORMAT, Locale.US);
    private static final String INITIAL_DATA = "Msg from " + NAME;

    private JMenuBar menuBar;
    private JTextField portText = new JTextField(40);
    private JTextField ipText = new JTextField(40);
    private JTextField dataText = new JTextField(40);
    private JTextArea textArea;

    private BufferedReader in;
    private PrintWriter out;
    private boolean socketRunning = false;

    private Socket socket;
    private static final int SERVER_PORT = 6000;
    private int serverPort = SERVER_PORT;
    private int serverPortNext = SERVER_PORT;
    private static final String SERVER_IP = "192.168.0.100";
    private String serverIp = SERVER_IP;
    private String serverIpNext = SERVER_IP;

    private static final String P_PREFERENCE_NODE = "net/kenevans/socketClient/preferences";
    private static final String P_DEFAULT_PORT = "ServerPort";
    private static final int D_DEFAULT_PORT = SERVER_PORT;
    private static final String P_DEFAULT_IP = "ServerIP";
    private static final String D_DEFAULT_IP = SERVER_IP;

    public SocketClient() {
        Preferences prefs = getUserPreferences();
        serverPort = serverPortNext = prefs.getInt(P_DEFAULT_PORT,
            D_DEFAULT_PORT);
        serverIp = serverIpNext = prefs.get(P_DEFAULT_IP, D_DEFAULT_IP);
        uiInit();
    }

    /**
     * Initializes the user interface.
     */
    void uiInit() {
        JLabel label;
        GridBagConstraints gbcDefault = new GridBagConstraints();
        gbcDefault.insets = new Insets(2, 2, 2, 2);
        gbcDefault.anchor = GridBagConstraints.WEST;
        gbcDefault.fill = GridBagConstraints.NONE;
        GridBagConstraints gbc = null;

        this.setLayout(new GridBagLayout());

        // Create the entry boxes
        JPanel entries = new JPanel(new GridBagLayout());
        entries.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        this.add(entries, gbc);

        // Create the IP panel
        JPanel ipPanel = new JPanel();
        ipPanel.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        entries.add(ipPanel, gbc);

        label = new JLabel("IP Address:");
        label.setToolTipText(
            "The server IP address. Hit Enter to save it for next start.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 0;
        ipPanel.add(label, gbc);

        ipText = new JTextField(40);
        ipText.setToolTipText(label.getToolTipText());
        ipText.setText(serverIp);
        ipText.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the TextField.
             */
            public void actionPerformed(ActionEvent e) {
                String text = ipText.getText();
                // TODO check if valid
                serverIpNext = text;
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        ipPanel.add(ipText, gbc);

        // Create the port panel
        JPanel portPanel = new JPanel();
        portPanel.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        entries.add(portPanel, gbc);

        label = new JLabel("Server Port:");
        label.setToolTipText(
            "The server port. Hit Enter to save it for next start.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 0;
        portPanel.add(label, gbc);

        portText = new JTextField(40);
        portText.setToolTipText(label.getToolTipText());
        portText.setText(Integer.toString(serverPort));
        portText.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the TextField.
             */
            public void actionPerformed(ActionEvent e) {
                int intVal;
                try {
                    intVal = Integer.parseInt(portText.getText());
                } catch(NumberFormatException ex) {
                    Utils.excMsg("Invalid port", ex);
                    return;
                }
                serverPortNext = intVal;
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        portPanel.add(portText, gbc);

        // Create the data panel
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new GridBagLayout());
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        entries.add(dataPanel, gbc);

        label = new JLabel("Send:");
        label.setToolTipText(
            "Data to send to server or ? for help. Hit Enter to send it.");
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 0;
        dataPanel.add(label, gbc);

        dataText = new JTextField(40);
        dataText.setText(INITIAL_DATA);
        dataText.setToolTipText(label.getToolTipText());
        dataText.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the TextField by sending
             * the contents of the text field to the server and displaying the
             * response from the server in the text area.
             */
            public void actionPerformed(ActionEvent e) {
                if(socket == null) {
                    showMsg("Socket is null");
                    return;
                }
                if(!socket.isConnected()) {
                    showMsg("Socket is not connected");
                    return;
                }
                if(out == null) {
                    showMsg("Socket output writer is null");
                    return;
                }
                if(in == null) {
                    showMsg("Socket input reader is null");
                    return;
                }
                try {
                    out.println(dataText.getText());
                } catch(Exception ex) {
                    showMsg("IO error writing to socket", ex);
                }
                dataText.selectAll();
            }
        });
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 100;
        dataPanel.add(dataText, gbc);

        // Create the text area used for output.
        textArea = new JTextArea(25, 30);
        textArea.setToolTipText("Output window.  Latest at the top.");
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        gbc = (GridBagConstraints)gbcDefault.clone();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 100;
        gbc.weighty = 100;
        this.add(scrollPane, gbc);
    }

    /**
     * Initializes the menus.
     */
    private void initMenus() {
        // Menu
        menuBar = new JMenuBar();

        // File
        JMenu menu = new JMenu();
        menu.setText("File");
        menuBar.add(menu);

        JMenuItem menuItem;
        JSeparator separator;

        menuItem = new JMenuItem();
        menuItem.setText("Start");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                start();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText("Stop");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                stop();
            }
        });
        menu.add(menuItem);

        separator = new JSeparator();
        menu.add(separator);

        menuItem = new JMenuItem();
        menuItem.setText("About");
        menuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                JOptionPane
                    .showMessageDialog(
                        null, new AboutBoxEvansPanel(HELP_TITLE, AUTHOR,
                            COMPANY, COPYRIGHT),
                        "About", JOptionPane.PLAIN_MESSAGE);
            }
        });
        menu.add(menuItem);

        separator = new JSeparator();
        menu.add(separator);

        // File Exit
        menuItem = new JMenuItem();
        menuItem.setText("Exit");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                quit();
            }
        });
        menu.add(menuItem);
    }

    /**
     * Returns the user preference store.
     * 
     * @return
     */
    public static Preferences getUserPreferences() {
        return Preferences.userRoot().node(P_PREFERENCE_NODE);
    }

    public void run() {
        try {
            // Create and set up the window.
            // JFrame.setDefaultLookAndFeelDecorated(true);
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // SwingUtilities.updateComponentTreeUI(this);
            this.setTitle(TITLE);
            // this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            this.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent event) {
                    quit();
                }
            });
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
            String msg = "Error running " + NAME;
            Utils.excMsg(msg, t);
            showMsg("Error closing socket", t);
        }
    }

    private void showMsg(String text) {
        Date currentTime = new Date();
        String timeString = mFormatter.format(currentTime);
        if(textArea != null) {
            textArea.insert(timeString + " " + text + "\n", 0);
        }
    }

    private void showMsg(String text, Throwable t) {
        String msg = text + " " + t.getMessage();
        showMsg(msg);
    }

    /**
     * Starts the socket.
     */
    private void start() {
        stop();
        serverIp = serverIpNext;
        serverPort = serverPortNext;
        new Thread(new ClientThread()).start();
        showMsg("Started");
    }

    /**
     * Closes the socket.
     */
    private void stop() {
        socketRunning = false;
        if(socket != null) {
            try {
                socket.close();
                showMsg("Socket closed");
                socket = null;
            } catch(Exception ex) {
                showMsg("Error closing socket", ex);
            }
            showMsg("Socket stopped");
        }
    }

    /**
     * Quits the application
     */
    private void quit() {
        // Save the current values
        Preferences prefs = getUserPreferences();
        prefs.put(P_DEFAULT_IP, serverIp);
        prefs.putInt(P_DEFAULT_PORT, serverPort);

        if(socket != null) {
            try {
                socket.close();
            } catch(Exception ex) {
                // Do nothing
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        final SocketClient app = new SocketClient();
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

    private class ClientThread implements Runnable
    {
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVER_PORT);
                in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                socketRunning = true;
                showMsg("Socket: " + socket.getInetAddress().getHostAddress()
                    + ":" + socket.getPort());
            } catch(UnknownHostException ex) {
                String msg = "Unknown host error starting socket";
                Utils.excMsg(msg, ex);
                showMsg("Error closing socket", ex);
            } catch(IOException ex) {
                String msg = "IO error starting socket";
                Utils.excMsg(msg, ex);
                showMsg("Error closing socket", ex);
            }
            String inputLine;
            while(socketRunning) {
                try {
                    while((inputLine = in.readLine()) != null) {
                        showMsg("Server: " + inputLine);
                    }
                } catch(IOException ex) {
                    showMsg("Error reading socket", ex);
                    break;
                }
                // Clean up
                if(in != null) {
                    try {
                        in.close();
                    } catch(Exception ex) {
                        // Do nothing
                    }
                }
                if(out != null) {
                    try {
                        out.close();
                    } catch(Exception ex) {
                        // Do nothing
                    }
                }
                if(socket != null) {
                    try {
                        socket.close();
                    } catch(Exception ex) {
                        // Do nothing
                    }
                }
            }
        }
    }
}
