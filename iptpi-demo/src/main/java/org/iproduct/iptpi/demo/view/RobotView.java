package org.iproduct.iptpi.demo.view;

import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.border.TitledBorder;

import org.iproduct.iptpi.demo.controller.RobotController;
/**
 * MVC View for {@link wumpus.controller.LabyrinthController LabyrinthController} 
 * built using <a href="https://eclipse.org/windowbuilder/">WindowBuilder</a>
 * plugin.
 * 
 * @author Trayan Iliev
 *
 */
public class RobotView extends JFrame{
	
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;

	private RobotController controller;
	
	public static final String[] SEARCH_ALGORITHMS = {
		"Depth First Search (DFS)", 
		"Breadth First Search (BFS)"
	};
	
	//Swing components
	JPanel mainPanel = new JPanel(); //main game panel
	JPanel buttonPanel = new JPanel(); //bottom button panel
	
	
	public RobotView(String title, RobotController controller, 
			List<JComponent> presentationViews) throws HeadlessException {
		super(title);
		this.controller = controller;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(320,329);
		setLocationRelativeTo(null);
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
			System.err.println("Nimbus look and feel not available. Using default.");
		}
		
		// Main game panel
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		mainPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		// Bottom button panel
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		for(JComponent service : presentationViews) {
			mainPanel.add(service);
		}
		
		// with lambda
		JButton btnUp = new JButton("Up");
		btnUp.addActionListener(
			(ActionEvent e) -> {
				controller.moveUp();
				mainPanel.repaint();
			}
		);
		
		JButton btnDown = new JButton("Down");
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.moveDown();
				mainPanel.repaint();
			}
		});
		
		JButton btnLeft = new JButton("Left");
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.moveLeft();
				mainPanel.repaint();
			}
		});
		
		JButton btnRight = new JButton("Right");
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.moveRight();
				mainPanel.repaint();
			}
		});
		
		JPanel pathSearchPanel = new JPanel();
		pathSearchPanel.setBorder(new TitledBorder(null, " Path Search", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnLeft, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnRight)
							.addPreferredGap(ComponentPlacement.UNRELATED))
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addGap(40)
							.addComponent(btnDown))
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addGap(39)
							.addComponent(btnUp, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)))
					.addGap(10)
					.addComponent(pathSearchPanel, GroupLayout.PREFERRED_SIZE, 157, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_buttonPanel.setVerticalGroup(
			gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
						.addComponent(pathSearchPanel, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE)
						.addGroup(gl_buttonPanel.createSequentialGroup()
							.addComponent(btnUp, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
								.addComponent(btnRight, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
								.addComponent(btnLeft, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDown, GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)))
					.addContainerGap())
		);
		pathSearchPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		JButton btnStartSearch = new JButton("FollowLine");
		btnStartSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.followLine();
			}
		});
		
		JButton btnSayHello = new JButton("Say Hello");
		btnSayHello.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.sayHello();
			}
		});
		pathSearchPanel.add(btnSayHello);
		pathSearchPanel.add(btnStartSearch);
		
		JButton btnNextStep = new JButton("Stop");
		btnNextStep.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.stop();
				mainPanel.repaint();
			}
		});
		pathSearchPanel.add(btnNextStep);
		
		buttonPanel.setLayout(gl_buttonPanel);
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenuItem mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.exit();
			}
		});
		menuBar.add(Box.createHorizontalGlue());
		menuBar.add(mntmExit);
		
		// with lambda
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		  .addKeyEventDispatcher(ev -> {
			  	if(ev.getID() == KEY_PRESSED) {
					switch (ev.getKeyCode()) {
						case VK_UP: controller.moveUp(); break;
						case VK_DOWN: controller.moveDown(); break;
						case VK_LEFT: controller.moveLeft(); break;
						case VK_RIGHT: controller.moveRight(); break;
					}
					mainPanel.repaint();
			  	}
				return false;
			});
		
		// show the window
		GraphicsDevice d = GraphicsEnvironment
		    .getLocalGraphicsEnvironment().getDefaultScreenDevice();
		if (d.isFullScreenSupported()) {
		    setUndecorated(true);
		    setResizable(false);
		    addFocusListener(new FocusListener() {

		        @Override
		        public void focusGained(FocusEvent arg0) {
		            setAlwaysOnTop(true);
		        }

		        @Override
		        public void focusLost(FocusEvent arg0) {
		            setAlwaysOnTop(false);
		        }
		    });
		    d.setFullScreenWindow(this);
		} else {
		    setVisible(true);
		}
	}
}
