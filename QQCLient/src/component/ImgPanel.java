package component;

import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class ImgPanel extends JPanel {
	private ImageIcon img = null;
	
	public ImgPanel() {
		// TODO Auto-generated constructor stub
	}
	public ImgPanel(String imgPath) {
		this.img = new ImageIcon(imgPath);
	}

	@Override
	protected void paintComponent(Graphics g) {
		g.drawImage(this.img.getImage(), 0, 0, this);
	}
	
}
