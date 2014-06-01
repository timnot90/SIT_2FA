package webserver;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.SwingWorker;

import java.awt.BorderLayout;
import java.util.List;

public class ProgressBarExample {

	CustomBar progressBar;
	JCheckBox progressType;
	JCheckBox switchType;
	final JButton goButton;

	// Note: Typically the main method will be in a
	// separate class. As this is a simple one class
	// example it's all in the one class.
	public static void main(String[] args) {

		// Use the event dispatch thread for Swing components
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				new ProgressBarExample();
			}
		});

	}
	
	public class CustomBar extends JProgressBar {
		
		public CustomBar(int a, int b) {
			super(a,b);
		}
		
		@Override
		protected void paintComponent(Graphics g) {
		  Graphics2D g2d = (Graphics2D) g;
		  g2d.scale(-1, 1); //Flips over y-axis
		  g2d.translate(-getWidth(), 0); //Moves back to old position.
		  super.paintComponent(g2d);
		}
	}

	public ProgressBarExample() {
		JFrame guiFrame = new JFrame();

		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Creating a Table Example");
		guiFrame.setSize(700, 200);

		// This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);

		goButton = new JButton("Go");
		goButton.setActionCommand("Go");
		goButton.addActionListener(new ActionListener() {

			// When the button is clicked the SwingWorker class is executed and
			// the button is disabled
			@Override
			public void actionPerformed(ActionEvent event) {

				//progressBar.setStringPainted(false);
				Sleeper task = new Sleeper();
				task.execute();
				goButton.setEnabled(false);
			}
		});

		// create a panel to hold the checkboxes
		JPanel chkPanel = new JPanel();

		// Create a checkbox to pick between a determined or indeterminate
		// progressbar
		progressType = new JCheckBox("Determined Progress Bar", true);
		progressType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {

				switchType.setEnabled(!progressType.isSelected());
			}
		});

		// Create a checkbox to switch progress bar modes
		switchType = new JCheckBox("Switch to Determined");
		switchType.setEnabled(false);

		chkPanel.add(progressType);
		chkPanel.add(switchType);

		// create progress bar
		progressBar = new CustomBar(0, 100);
		progressBar.setValue(0);

		guiFrame.add(goButton, BorderLayout.WEST);
		guiFrame.add(progressBar, BorderLayout.CENTER);
		guiFrame.add(chkPanel, BorderLayout.SOUTH);

		guiFrame.setVisible(true);
	}

	// SwingWorker class is used to simulate a task being performed
	class Sleeper extends SwingWorker {

		@Override
		public Void doInBackground() throws InterruptedException {

			try {
				int progress = 0;
				while (progress < 100) {

					// pause thread and then update the progress
					Thread.sleep(100);
					progress++;

					// Call the process method to update the GUI
					publish(progress);

				}
			} catch (InterruptedException e) {
			}
			return null;
		}

		@Override
		protected void process(List chunks) {
			for (Object chunk : chunks) {
				Integer intChunk = (Integer) chunk;
				progressBar.setValue(intChunk);

				// if the switchtype checkbox is selected then
				// change the progressbar to a determined type
				// once the progress has reached 50
				if (intChunk > 49) {
					if (switchType.isEnabled() && switchType.isSelected()) {
						progressBar.setStringPainted(true);

					}

				}
			}
		}

		// when the 'task' has finished re-enable the go button
		@Override
		public void done() {
			goButton.setEnabled(true);
		}
	}

}
