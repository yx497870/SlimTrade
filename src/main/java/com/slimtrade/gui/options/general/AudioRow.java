package main.java.com.slimtrade.gui.options.general;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import main.java.com.slimtrade.core.Main;
import main.java.com.slimtrade.core.audio.AudioManager;
import main.java.com.slimtrade.core.audio.Sound;
import main.java.com.slimtrade.core.utility.TradeUtility;
import main.java.com.slimtrade.gui.buttons.IconButton;

public class AudioRow extends JPanel {

	private static final long serialVersionUID = 1L;
	private final int WIDTH = 200;
	private final int HEIGHT = 25;
	private final int BUTTON_SIZE = HEIGHT-5;
	private final int LABEL_WIDTH = 120;
	
	JSlider slider = new JSlider();
	JComboBox<Sound> soundCombo = new JComboBox<Sound>();
	
	public AudioRow(String title){
		this.setLayout(new GridBagLayout());
		GridBagConstraints gc = new GridBagConstraints();
		
		IconButton sampleButton = new IconButton("/resources/icons/play1.png", BUTTON_SIZE);
		
		JPanel labelPanel = new JPanel(new GridBagLayout());
		labelPanel.setPreferredSize(new Dimension(LABEL_WIDTH, HEIGHT));
		JLabel label = new JLabel(title);
		labelPanel.add(label);
		
//		slider;
		Dimension sliderSize = slider.getPreferredSize();
		sliderSize.height = HEIGHT;
		slider.setMinimum(0);
		slider.setMaximum(100);
//		slider.setPreferredSize(sliderSize);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setSnapToTicks(true);
		slider.setFocusable(false);
		slider.setPaintTicks(true);
		
//		JComboBox<String> soundCombo = new JComboBox<String>();
		
		soundCombo.setFocusable(false);
		//TODO :sound types
		for(Sound s : Sound.values()){
			soundCombo.addItem(s);
		}
		slider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent arg0) {
//				System.out.println(slider.getValue());
//				
//				System.out.println(f-70);
//				System.out.println(getVolume());
//				float vol = TradeUtility.getAudioVolume(slider.getValue());
//				System.out.println(TradeUtility.getAudioPercent(vol));
			}
		});
		this.add(sampleButton);
		this.add(labelPanel);
		this.add(slider);
		this.add(soundCombo);
		
		sampleButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				Sound sound = (Sound) soundCombo.getSelectedItem();
				float volume = TradeUtility.getAudioVolume(slider.getValue());
				AudioManager.playRaw(sound, volume);
			}
		});
	}
	
	public void setValue(Sound sound, int volume){
		soundCombo.setSelectedItem(sound);
//		System.out.println(volume);
		slider.setValue(volume);
	}
	
	public int getVolume(){
//		int range=40;
//		float f = (float) ((AudioManager.RANGE/100.0)*(float)(slider.getValue()));
//		return f-AudioManager.RANGE+AudioManager.MAX_VOLUME;
		return slider.getValue();
	}
	
	public Sound getSound(){
		Sound s = (Sound) soundCombo.getSelectedItem();
		return s;
	}
	
}