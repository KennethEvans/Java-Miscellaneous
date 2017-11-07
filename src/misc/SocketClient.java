package misc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
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
    private JTextField dataField = new JTextField(40);
    private JTextArea textArea;

    private BufferedReader in;
    private PrintWriter out;

    private Socket socket;
    private static final int SERVERPORT = 6000;
    private static final String SERVER_IP = "192.168.0.100";

    public SocketClient() {
        uiInit();
    }

    /**
     * Initializes the user interface.
     */
    void uiInit() {
        this.setLayout(new BorderLayout());

        // Create the ata field
        dataField = new JTextField(40);
        dataField.setText(INITIAL_DATA);
        dataField.addActionListener(new ActionListener() {
            /**
             * Responds to pressing the enter key in the textfield by sending
             * the contents of the text field to the server and displaying the
             * response from the server in the text area.
             */
            public void actionPerformed(ActionEvent e) {
                String response;
                if(socket == null) {
                    showMsg("Socket is null" + "\n");
                    return;
                }
                if(!socket.isConnected()) {
                    showMsg("Socket is not connected" + "\n");
                    return;
                }
                if(out == null) {
                    showMsg("Socket output writer is null" + "\n");
                    return;
                }
                if(in == null) {
                    showMsg("Socket input reader is null" + "\n");
                    return;
                }
                try {
                    out.println(dataField.getText());
                    response = in.readLine();
                    // if(response == null || response.equals("")) {
                    // System.exit(0);
                    // }
                } catch(IOException ex) {
                    response = "IO Error: " + ex;
                } catch(Exception ex) {
                    response = "Error: " + ex;
                }
                showMsg(response + "\n");
                dataField.selectAll();
            }
        });
        this.add(dataField, BorderLayout.NORTH);

        // Create the text area used for output. Request
        // enough space for 5 rows and 30 columns.
        textArea = new JTextArea(25, 30);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);
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
                // Start the socket
                new Thread(new ClientThread()).start();
            }
        });
        menu.add(menuItem);

        menuItem = new JMenuItem();
        menuItem.setText("Stop");
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(socket != null && !socket.isClosed()) {
                    try {
                        socket.close();
                    } catch(Exception ex) {
                        showMsg("Error closing socket", ex);
                        return;
                    }
                }
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
            String msg = "Error running " + NAME;
            Utils.excMsg(msg, t);
            showMsg("Error closing socket", t);
        }
    }

    private void showMsg(String text) {
        Date currentTime = new Date();
        String timeString = mFormatter.format(currentTime);
        if(textArea != null) {
            textArea.append(timeString + " " + text);
        }
    }

    private void showMsg(String text, Throwable t) {
        String msg = text + " " + t.getMessage() + "\n";
        showMsg(msg);
    }

    /**
     * Quits the application
     */
    private void quit() {
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
                socket = new Socket(serverAddr, SERVERPORT);
                in = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                showMsg("Socket: " + socket.getInetAddress().getHostAddress()
                    + ":" + socket.getPort() + "\n");
            } catch(UnknownHostException ex) {
                String msg = "Unknown host error starting socket";
                Utils.excMsg(msg, ex);
                showMsg("Error closing socket", ex);
            } catch(IOException ex) {
                String msg = "IO error starting socket";
                Utils.excMsg(msg, ex);
                showMsg("Error closing socket", ex);
            }
            while (!Thread.currentThread().isInterrupted()) {
            
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
        }
    }
}
