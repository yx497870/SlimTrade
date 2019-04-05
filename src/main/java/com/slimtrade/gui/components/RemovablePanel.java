package main.java.com.slimtrade.gui.components;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JPanel;

public class RemovablePanel extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private boolean newPanel = true;
	private boolean toBeDeleted = false;
	
	private JButton removeButton = new JButton();
	
	public RemovablePanel(){
		
		this.add(removeButton);
		Random rand = new Random();
		int r = 50+rand.nextInt(150);
		int g = 50+rand.nextInt(150);
		int b = 50+rand.nextInt(150);
		this.setBackground(new Color(r, g, b));
		
		removeButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				markForDeletion();
			}
		});
	}
	
	public void markForDeletion(){
		this.toBeDeleted = true;
		this.setVisible(false);
	}
	
	public void setToBeDeleted(boolean state){
		toBeDeleted = state;
	}
	
	public boolean isToBeDeleted(){
		return toBeDeleted;
	}
	
	public void setNewPanel(boolean state){
		newPanel = state;
	}
	
	public boolean isNewPanel(){
		return newPanel;
	}
	
}
