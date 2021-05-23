package pkg;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * @author Ji-Hoon Shim
 * @date 2016-06-08
 * @brief 랭킹 정보를 기록하고 화면에 띄우기 위한 클래스
 * 
 */
public class rankInOut{
	
	private mainFrame frame;
	rankInfo[] ranks = new rankInfo[5];
	
	public rankInOut(){
		for(int i=0; i<ranks.length; i++)
			ranks[i] = new rankInfo();
		
		frame = null;
	}
	
	public rankInOut(rankInfo[] r, mainFrame f){
		ranks = r;
		frame = f;
	}
	
	/**
	 * 
	 * @param thing: 투입할 랭킹 정보
	 */
	public void InsertWrite(rankInfo thing){
		
		int sec1, sec2;
		sec2 = thing.getMinute() * 60 + thing.getSecond();
		
		for(int i=0; i<ranks.length; i++){
			sec1 = ranks[i].getMinute() * 60 + ranks[i].getSecond();
			
			if(sec1 > sec2){
				for(int j=ranks.length-2; j>=i; j--)
					ranks[j+1] = ranks[j];
				
				ranks[i] = thing;
				break;
			}
		}
		

		try{
	        BufferedWriter out = new BufferedWriter(new FileWriter("src/data/ranking.txt"));
	 
	        for(int i=0; i<ranks.length; i++){
	            out.write(ranks[i].getName() + " " + ranks[i].getMinute() + " " + ranks[i].getSecond());
	            out.newLine();
	        }
	             
	        out.close();
		}catch (IOException e){
			e.printStackTrace();
		}

	}
	
	public void showAll(){
		String str = "";
		
		for(int i=0; i<ranks.length; i++)
			str += (i+1 + "등: " + ranks[i].getName()+ " " + ranks[i].getMinute() + ":" + ranks[i].getSecond() + "\n");
		
		JOptionPane.showMessageDialog(frame, str, "Ranking - Top 5", JOptionPane.INFORMATION_MESSAGE);
	}
}