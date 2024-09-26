import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class Clock {
  private JFrame frame;
  private JLabel label;
  private ClockDisplay clock;
  private boolean clockRunning = false;
  private TimerThread timerThread;

  public Clock() {
    makeFrame();
    clock = new ClockDisplay();
  }

  private void start() {
    if(!clockRunning) {
      clockRunning = true;
      timerThread = new TimerThread();
      timerThread.start();
    }
  }

  private void stop() {
    if(clockRunning) {
      clockRunning = false;
      timerThread.interrupt();
    }
  }

  private void step() {
    clock.timeTick();
    label.setText(clock.getTime());
  }

  private void showAbout() {
    JOptionPane.showMessageDialog(frame, "Clock Version 1.0\n" + "Brought to you by Java", "About Clock", JOptionPane.INFORMATION_MESSAGE);
  }

  private void quit() {
    System.exit(0);
  }

  private void makeFrame() {
    frame = new JFrame("Clock");
    JPanel contentPane = (JPanel)frame.getContentPane();
    contentPane.setBorder(BorderFactory.createEmptyBorder(1, 60, 1, 60));
    makeMenuBar(frame);
    contentPane.setLayout(new BorderLayout(12, 12));

    label = new JLabel("00:00", SwingConstants.CENTER);
    Font displayFont = label.getFont().deriveFont(96.0f);
    label.setFont(displayFont);
    contentPane.add(label, BorderLayout.CENTER);

    JPanel toolBar = new JPanel();
    toolBar.setLayout(new GridLayout(1, 0));

    JButton startButton = new JButton("Start");
    startButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        start();
      }
    });
    toolBar.add(startButton);

    JButton stopButton = new JButton("Stop");
    stopButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        stop();
      }
    });
    toolBar.add(stopButton);

    JButton stepButton = new JButton("Step");
    stepButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        step();
      }
    });
    toolBar.add(stepButton);

    JPanel flow = new JPanel();
    flow.add(toolBar);

    contentPane.add(flow, BorderLayout.SOUTH);

    frame.pack();
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation(d.width/2 - frame.getWidth()/2, d.height/2 - frame.getHeight()/2);
    frame.setVisible(true);
  }

  private void makeMenuBar(JFrame frame) {
    final int SHORTCUT_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

    JMenuBar menubar = new JMenuBar();
    frame.setJMenuBar(menubar);

    JMenu menu;
    JMenuItem item;

    menu = new JMenu("File");
    menubar.add(menu);

    item = new JMenuItem("About Clock...");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        showAbout();
      }
    });
    menu.add(item);

    menu.addSeparator();

    item = new JMenuItem("Quit");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        quit();
      }
    });
    menu.add(item);
  }

  class TimerThread extends Thread {
    public void run() {
      while(clockRunning) {
        clock.timeTick();
        label.setText(clock.getTime());
        try {
          Thread.sleep(1000);
        } catch(InterruptedException e) {
        }
      }
    }
  }
}
