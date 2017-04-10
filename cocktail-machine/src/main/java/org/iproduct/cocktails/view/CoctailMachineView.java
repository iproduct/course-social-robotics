package org.iproduct.cocktails.view;

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
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import org.iproduct.cocktails.controller.CocktailMachineController;
import javax.swing.BoxLayout;
import java.awt.Font;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 * MVC View for {@link wumpus.controller.LabyrinthController
 * LabyrinthController} built using
 * <a href="https://eclipse.org/windowbuilder/">WindowBuilder</a> plugin.
 * 
 * @author Trayan Iliev
 *
 */
public class CoctailMachineView extends JFrame {

	// public static final int WIDTH = 10;
	// public static final int HEIGHT = 10;

	private CocktailMachineController controller;

	// Swing components
	JPanel mainPanel = new JPanel(); // main game panel
	JPanel buttonPanel = new JPanel(); // bottom button panel
	JSlider[] sliders = new JSlider[3];

	public CoctailMachineView(String title, CocktailMachineController controller) throws HeadlessException {
		super(title);
		this.controller = controller;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);
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

		for (int i = 0; i < sliders.length; i++) {
			sliders[i] = new JSlider();
			sliders[i].setPaintTicks(true);
			sliders[i].setValue(10);
			sliders[i].setMinorTickSpacing(5);
			sliders[i].setMajorTickSpacing(10);
			sliders[i].setMaximum(50);
			sliders[i].setOrientation(SwingConstants.VERTICAL);
		}
		JLabel lblIngrediant = new JLabel("Ingredient 1");
		lblIngrediant.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblIngredient = new JLabel("Ingredient 2");
		lblIngredient.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JLabel lblIngredient_1 = new JLabel("Ingredient 3");
		lblIngredient_1.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JProgressBar progressBar = new JProgressBar();
		progressBar.setValue(10);
		progressBar.setOrientation(SwingConstants.VERTICAL);

		JProgressBar progressBar_1 = new JProgressBar();
		progressBar_1.setValue(10);
		progressBar_1.setOrientation(SwingConstants.VERTICAL);

		JProgressBar progressBar_2 = new JProgressBar();
		progressBar_2.setValue(10);
		progressBar_2.setOrientation(SwingConstants.VERTICAL);
		GroupLayout gl_mainPanel = new GroupLayout(mainPanel);
		gl_mainPanel.setHorizontalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_mainPanel
				.createSequentialGroup().addGap(74)
				.addComponent(sliders[0], GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE).addGap(85)
				.addComponent(sliders[1], GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE).addGap(90)
				.addComponent(sliders[2], GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
				.addPreferredGap(ComponentPlacement.RELATED, 112, Short.MAX_VALUE)
				.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE).addGap(53)
				.addComponent(progressBar_1, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE).addGap(37)
				.addComponent(progressBar_2, GroupLayout.PREFERRED_SIZE, 53, GroupLayout.PREFERRED_SIZE).addGap(26))
				.addGroup(gl_mainPanel.createSequentialGroup().addGap(48).addComponent(lblIngrediant).addGap(26)
						.addComponent(lblIngredient, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
						.addGap(29)
						.addComponent(lblIngredient_1, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(362, Short.MAX_VALUE)));
		gl_mainPanel.setVerticalGroup(gl_mainPanel.createParallelGroup(Alignment.LEADING).addGroup(gl_mainPanel
				.createSequentialGroup().addGap(27)
				.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
						.addComponent(progressBar, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
						.addComponent(progressBar_2, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
						.addComponent(progressBar_1, GroupLayout.PREFERRED_SIZE, 254, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_mainPanel
								.createSequentialGroup()
								.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING, false)
										.addComponent(lblIngredient_1, GroupLayout.PREFERRED_SIZE, 25,
												GroupLayout.PREFERRED_SIZE)
										.addGroup(gl_mainPanel.createParallelGroup(Alignment.BASELINE)
												.addComponent(lblIngredient, GroupLayout.PREFERRED_SIZE, 25,
														GroupLayout.PREFERRED_SIZE)
												.addComponent(lblIngrediant)))
								.addGap(18)
								.addGroup(gl_mainPanel.createParallelGroup(Alignment.TRAILING)
										.addComponent(sliders[2], GroupLayout.PREFERRED_SIZE, 266,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(sliders[1], GroupLayout.PREFERRED_SIZE, 266,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(sliders[0], GroupLayout.PREFERRED_SIZE, 266,
												GroupLayout.PREFERRED_SIZE))))
				.addContainerGap(52, Short.MAX_VALUE)));
		mainPanel.setLayout(gl_mainPanel);

		// Bottom button panel
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		JPanel pathSearchPanel = new JPanel();
		pathSearchPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
		pathSearchPanel.setLayout(new GridLayout(0, 1, 0, 0));

		JButton btnStop = new JButton("Stop It!");
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.stop();
			}
		});

		JButton btnMakeCocktail = new JButton("Make It!");
		btnMakeCocktail.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnMakeCocktail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				controller.makeCocktail();
			}
		});
		pathSearchPanel.add(btnMakeCocktail);
		pathSearchPanel.add(btnStop);

		JButton btnExit = new JButton("Exit Cocktails App");
		btnExit.setFont(new Font("Tahoma", Font.PLAIN, 20));
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ev) {
				controller.exit();
			}
		});
		pathSearchPanel.add(btnExit);
		GroupLayout gl_buttonPanel = new GroupLayout(buttonPanel);
		gl_buttonPanel.setHorizontalGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup().addContainerGap().addComponent(pathSearchPanel,
						GroupLayout.DEFAULT_SIZE, 782, Short.MAX_VALUE)));
		gl_buttonPanel.setVerticalGroup(gl_buttonPanel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_buttonPanel.createSequentialGroup().addContainerGap()
						.addComponent(pathSearchPanel, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		buttonPanel.setLayout(gl_buttonPanel);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		// show the window
		GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
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

	public static void main(String[] args) {
		CocktailMachineController controller = new CocktailMachineController();
		JFrame cocktailsView = new CoctailMachineView("Make Your Own Cocktail", controller);
	}
}
