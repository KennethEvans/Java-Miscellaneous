package misc;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class PlafTest
{  
   public static void main(String[] args)
   {  
      PlafFrame frame = new PlafFrame();
      frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   A frame with a button panel for changing look and feel
*/
class PlafFrame extends JFrame
{
  private static final long serialVersionUID = 1L;
  public PlafFrame()
   {
      setTitle("PlafTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // add panel to frame

      PlafPanel panel = new PlafPanel();
      add(panel);
   }

   public static final int DEFAULT_WIDTH = 300;
   public static final int DEFAULT_HEIGHT = 200;  
}

/**
   A panel with buttons to change the pluggable look and feel
*/
class PlafPanel extends JPanel
{  
  private static final long serialVersionUID = 1L;

  public PlafPanel()
   {  
      UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
      for (UIManager.LookAndFeelInfo info : infos)
         makeButton(info.getName(), info.getClassName());
   }

   /**
      Makes a button to change the pluggable look and feel.
      @param name the button name
      @param plafName the name of the look and feel class
    */
   void makeButton(String name, final String plafName)
   {  
      // add button to panel

      JButton button = new JButton(name);
      add(button);
      
      // set button action

      button.addActionListener(new 
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {  
               // button action: switch to the new look and feel
               try
               {  
                  UIManager.setLookAndFeel(plafName);
                  SwingUtilities.updateComponentTreeUI(PlafPanel.this);
               }
               catch(Exception e) { e.printStackTrace(); }
            }
         });
   }
}
