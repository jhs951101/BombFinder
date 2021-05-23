package pkg;

import javax.swing.JLabel;

/**
 * @author Ji-Hoon Shim
 * @date 2016-06-08
 * @brief 위쪽에 시간을 띄우고 시간 정보를 갖고있는 클래스
 * 
 */
public class gameTimer extends Thread{
	
	static boolean flag = true;
	
	private int second;
	private int minute;
	
	private JLabel time;
	
	public gameTimer(){}
	
	public gameTimer(JLabel t){
		time = t;
		
		second = 0;
		minute = 0;
	}
	
	@Override
	public void run(){
		
		while(flag){
				
			try{
				sleep(1000);
			
				if(second == 59){
					second = 0;
					minute++;
				}
				else{
					second++;
				}
			
				if(minute <= 9 && second >= 10)
					time.setText(String.format("0%d:%d", minute, second));
				else if(minute >= 10 && second <= 9)
					time.setText(String.format("%d:0%d", minute, second));
				else if(minute >= 10 && second >= 10)
					time.setText(String.format("%d:%d", minute, second));
				else if(minute <= 9 && second <= 9)
					time.setText(String.format("0%d:0%d", minute, second));
			
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		
	}

	public int getSecond() {
		return second;
	}

	public int getMinute() {
		return minute;
	}
}