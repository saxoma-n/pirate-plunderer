import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JButton;

public class Test 
{

	public static void main(String []args)
	{
		JFrame frame = new JFrame();
		final int FRAME_WIDTH = 1000;
		final int FRAME_HEIGHT = 600;
		frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		final BallComponent b = new BallComponent();
		frame.add(b,BorderLayout.CENTER);
		frame.setVisible(true); 
		b.startAnimation();
	}
}
