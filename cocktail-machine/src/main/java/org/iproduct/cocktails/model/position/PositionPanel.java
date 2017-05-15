package org.iproduct.cocktails.model.position;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class PositionPanel extends JComponent implements Subscriber<Position> {
	private static final long serialVersionUID = 1L;
	private Position currentPosition;
	private JTextField tfX;
	private JTextField tfY;
	private Subscription subscription;
	private JTextField tfH;

	public PositionPanel() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JLabel lblX = new JLabel("X:");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_lblX = new GridBagConstraints();
		gbc_lblX.insets = new Insets(0, 0, 5, 5);
		gbc_lblX.anchor = GridBagConstraints.EAST;
		gbc_lblX.gridx = 0;
		gbc_lblX.gridy = 1;
		add(lblX, gbc_lblX);
		
		tfX = new JTextField();
		tfX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_tfX = new GridBagConstraints();
		gbc_tfX.anchor = GridBagConstraints.EAST;
		gbc_tfX.insets = new Insets(0, 5, 5, 5);
		gbc_tfX.gridx = 1;
		gbc_tfX.gridy = 1;
		add(tfX, gbc_tfX);
		tfX.setColumns(5);
		
		JLabel lblY = new JLabel("Y:");
		lblY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_lblY = new GridBagConstraints();
		gbc_lblY.anchor = GridBagConstraints.EAST;
		gbc_lblY.insets = new Insets(0, 5, 5, 5);
		gbc_lblY.gridx = 2;
		gbc_lblY.gridy = 1;
		add(lblY, gbc_lblY);
		
		tfY = new JTextField();
		tfY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_tfY = new GridBagConstraints();
		gbc_tfY.anchor = GridBagConstraints.EAST;
		gbc_tfY.insets = new Insets(0, 0, 5, 5);
		gbc_tfY.gridx = 3;
		gbc_tfY.gridy = 1;
		add(tfY, gbc_tfY);
		tfY.setColumns(5);
		
		JLabel lblH = new JLabel("H:");
		lblH.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_lblH = new GridBagConstraints();
		gbc_lblH.anchor = GridBagConstraints.EAST;
		gbc_lblH.insets = new Insets(0, 5, 5, 5);
		gbc_lblH.gridx = 4;
		gbc_lblH.gridy = 1;
		add(lblH, gbc_lblH);
		
		tfH = new JTextField();
		tfH.setFont(new Font("Tahoma", Font.PLAIN, 15));
		GridBagConstraints gbc_tfH = new GridBagConstraints();
		gbc_tfH.anchor = GridBagConstraints.EAST;
		gbc_tfH.insets = new Insets(0, 0, 5, 0);
		gbc_tfH.gridx = 5;
		gbc_tfH.gridy = 1;
		add(tfH, gbc_tfH);
		tfH.setColumns(4);
		
		currentPosition = new Position(0, 0, 0);
		onNext(currentPosition); 
	}

	public PositionPanel(Position initialPosition) {
		this();
		currentPosition = initialPosition;
		onNext(currentPosition); 
	}

	@Override
	public void onComplete() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onError(Throwable t) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNext(Position position) {
		SwingUtilities.invokeLater( () -> {
			tfX.setText(String.format("%6.2f", position.getX()));
			tfY.setText(String.format("%6.2f", position.getY()));
			tfH.setText(String.format("%6.2f", position.getHeading()));
		});
	}

	@Override
	public void onSubscribe(Subscription s) {
		this.subscription = s;	
		s.request(Long.MAX_VALUE);
	}

}
