package pkg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * @author Ji-Hoon Shim
 * @date 2016-06-08
 * @brief 지뢰찾기 게임에서 사용될 JButton들한테 액션을 지정해주기 위한 클래스
 * 
 */
public class buttonAction implements ActionListener
{
	private int x;
	private int y;
	private JPanel panel;
	private char[][] signs;
	private mainFrame frame;
	private gameTimer timer;
	private rankInOut handleRank;
	
	public buttonAction(){}
	
	public buttonAction(int x, int y, JPanel p, char[][] s, mainFrame f, gameTimer t, rankInOut h){
		this.x = x;
		this.y = y;
		panel = p;
		signs = s;
		frame = f;
		timer = t;
		handleRank = h;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(mainFrame.started){
			takeAction(x, y);
			
			for(int i=0; i<mainFrame.PROWS; i++){
				for(int j=0; j<mainFrame.PROWS; j++){
					if(signs[i+1][j+1] == 'z'){
						frame.buttons[i][j].setText("0");
						signs[i+1][j+1] = '0';
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * @param x: JButton 2차원 배열에서의 x값
	 * @param y: JButton 2차원 배열에서의 y값
	 */
	public void takeAction(int x, int y){
		
		if(frame.ckboxHand.isSelected()){
			
		if(signs[x+1][y+1] == '*'){
			
			for(int i=0; i<mainFrame.PROWS; i++){
				for(int j=0; j<mainFrame.PROWS; j++){
					if(signs[i+1][j+1] == '*')
						frame.buttons[i][j].setText(String.valueOf(signs[i+1][j+1]));
					
					frame.buttons[i][j].setEnabled(false);
				}
			}
					
			mainFrame.started = false;
			frame.btnStart.setEnabled(true);
			frame.btnStart.setVisible(true);
			gameTimer.flag = false;
			
			JOptionPane.showMessageDialog(frame, "지뢰가 폭발했습니다... ㅠㅠ", "Notice", 
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if(signs[x+1][y+1] >= '1'){
			if(frame.buttons[x][y].getText().equals("#"))
				mainFrame.TotalFLAG--;
				
			frame.buttons[x][y].setText(String.valueOf(signs[x+1][y+1]));
			frame.buttons[x][y].setEnabled(false);
		}
		else{
			if(frame.buttons[x][y].getText().equals("#"))
				mainFrame.TotalFLAG--;
			
			frame.buttons[x][y].setText(String.valueOf(signs[x+1][y+1]));
			frame.buttons[x][y].setEnabled(false);
			signs[x+1][y+1] = 'z';
			
			int a=0, b=0;
			for(int n=1; n<=8; n++){
				
				if(n == 1){
					a = x;
					b = y-1;
				}
				else if(n == 2){
					a = x-1;
					b = y-1;
				}
				else if(n == 3){
					a = x-1;
					b = y;
				}
				else if(n == 4){
					a = x-1;
					b = y+1;
				}
				else if(n == 5){
					a = x;
					b = y+1;
				}
				else if(n == 6){
					a = x+1;
					b = y+1;
				}
				else if(n == 7){
					a = x+1;
					b = y;
				}
				else if(n == 8){
					a = x+1;
					b = y-1;
				}
				
				if(signs[a+1][b+1] >= '1'){
					if(frame.buttons[a][b].getText().equals("#"))
						mainFrame.TotalFLAG--;
					
					frame.buttons[a][b].setText(String.valueOf(signs[a+1][b+1]));
					frame.buttons[a][b].setEnabled(false);
				}
				else if(signs[a+1][b+1] == '0'){
					takeAction(a,b);
				}
			}
		}
		}
		else{
			if(!frame.buttons[x][y].getText().equals("#")){
				if(signs[x+1][y+1] == '*') mainFrame.FLAG++;
				mainFrame.TotalFLAG++;
				
				frame.buttons[x][y].setText("#");
			}
			else{
				if(signs[x+1][y+1] == '*') mainFrame.FLAG--;
				mainFrame.TotalFLAG--;
				frame.buttons[x][y].setText("");
			}
		}
		
		decideEnd();
	}
	
	public void decideEnd(){
		if(mainFrame.FLAG == mainFrame.MINE && mainFrame.TotalFLAG == mainFrame.FLAG){
			mainFrame.started = false;
			frame.btnStart.setEnabled(true);
			frame.btnStart.setVisible(true);
			gameTimer.flag = false;
			
			rankInfo info = new rankInfo(frame.name, timer.getMinute(), timer.getSecond());
			handleRank.InsertWrite(info);
			
			JOptionPane.showMessageDialog(frame, "수고하셨습니다!! ^0^", "Notice", 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
	
}