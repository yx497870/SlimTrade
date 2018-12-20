package com.zrmiller.slimtrade.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.zrmiller.slimtrade.Overlay;

public class BasicMenuWindow extends JPanel{
	private static final long serialVersionUID = 1L;

	//THIS
	private int width;
	
	
	//TITLEBAR
	private int minimumWidth = 100;
	private int titlebarHeight = 20;
	private String title = "";
	public JPanel titlebar;
	private JPanel titlebarPanel;
	private JLabel titlebarLabel;
	private JButton closeButton;
	private Color defaultTitlebarColor = Color.LIGHT_GRAY;
	private Color clear = new Color(1.0f,1.0f,1.0f,0.0f);
	private int offsetX = 0;
	private int offsetY = 0;
	private JPanel visibilityPanel;
	private int snapSize = 10;
	
	//CONTENT PANEL
	public JPanel container;
	private int height;
	
	public BasicMenuWindow(int width, int height){
		this.titlebarHeight = 0;
		this.width = width;
		this.height = height;
		buildMenu();
	}
	
	public BasicMenuWindow(String title, int width, int height){
		this.setBackground(Color.RED);
		this.title = title;
		this.width = width;
		this.height = height;
		buildMenu();
	}
	
	private void buildMenu(){
		this.setLayout(Overlay.flowLeft);
		this.setPreferredSize(new Dimension(width, height+titlebarHeight));
		this.setMinimumSize(new Dimension(minimumWidth, titlebarHeight*2));
		this.setBounds(0, 0, width, height+titlebarHeight);
		this.setBackground(clear);
		buildTitlebar();
		this.add(titlebar);
		container = new JPanel();
		container.setPreferredSize(new Dimension(width, height));
		container.setBackground(Color.RED);
		this.add(container);
		visibilityPanel = this;
	}
	
	private void buildTitlebar(){
		titlebar = new JPanel();
		titlebar.setLayout(Overlay.flowLeft);
		titlebar.setPreferredSize(new Dimension(width, titlebarHeight));
		titlebarPanel = new JPanel();
		titlebarPanel.setLayout(Overlay.flowLeft);
		titlebarPanel.setPreferredSize(new Dimension(width-titlebarHeight, titlebarHeight));
		titlebarLabel = new JLabel(title);
		closeButton = new JButton();
		closeButton.setPreferredSize(new Dimension(titlebarHeight, titlebarHeight));
		titlebarPanel.add(new BasicPanel(5, titlebarHeight, null));
		titlebarPanel.add(titlebarLabel);
		titlebarPanel.setBackground(defaultTitlebarColor);
		titlebar.add(titlebarPanel);
		titlebar.add(closeButton);
		
		closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
		    public void mouseClicked(java.awt.event.MouseEvent e) {
		    	visibilityPanel.setVisible(false);
//		    	Overlay.hideAllTempFrames();
//		    	Overlay.menubar.setVisible(false);
//		    	Overlay.menubarShowButton.setVisible(true);
		    }
		});
		
		titlebarPanel.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent e) {
		    	offsetX = e.getXOnScreen()-getPosX();
		    	offsetY = e.getYOnScreen()-getPosY();
		    }
		});
		
		titlebarPanel.addMouseMotionListener(new java.awt.event.MouseAdapter() {
		    public void mouseDragged(java.awt.event.MouseEvent e) {
		    	moveBox(e.getXOnScreen()-offsetX, e.getYOnScreen()-offsetY);
		    }
		});
	}
	
	private int getPosX(){
		return this.getX();
	}
	
	private int getPosY(){
		return this.getY();
	}
	
	private void moveBox(int posX, int posY){
		this.setLocation(posX, posY);
	}
	
	public void resizeWindow(int width, int height){
		if(width<this.getMinimumSize().getWidth() || height < this.getMinimumSize().getHeight()){
			return;
		}
		int w = width;
		int h = height;
		System.out.println(width + " : " + w);
		System.out.println(width + " : " + w);
		while((double)(w%snapSize) != 0){
			w--;
		}
		while(h%snapSize != 0){
			h--;
		}
		Dimension size = new Dimension(w, h);
		this.setSize(size);
		titlebar.setPreferredSize(new Dimension(w, titlebar.getHeight()));
		titlebarPanel.setPreferredSize(new Dimension(w-titlebar.getHeight(), titlebar.getHeight()));
		closeButton.setPreferredSize(new Dimension(titlebar.getHeight(), titlebar.getHeight()));
		container.setPreferredSize(new Dimension(w, h-titlebar.getHeight()));
		this.setVisible(false);
		this.setVisible(true);
	}
	
	public void setSnapSize(int size){
		this.snapSize = size;
	}
	
}
