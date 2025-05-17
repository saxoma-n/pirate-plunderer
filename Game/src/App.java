import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;

import javax.swing.JButton;

public class App 
{

	public static void main(String []args)
	{
		JFrame frame = new JFrame();
		final int FRAME_WIDTH = 1000;
		final int FRAME_HEIGHT = 600;
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final BallComponent b = new BallComponent();
		final JLabel display = new JLabel();
		frame.add(b,BorderLayout.CENTER);
		frame.add(display, BorderLayout.NORTH);
		frame.setVisible(true); 
		b.startAnimation();
		while (true) display.setText("Score: " + b.getScore());
	}
}
