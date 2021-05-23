package pkg;

/**
 * @author Ji-Hoon Shim
 * @date 2016-06-08
 * @brief ��ŷ ���� �ϳ��� �ǹ��ϴ� Ŭ����
 * 
 */
public class rankInfo{
	private String name;
	private int minute;
	private int second;
	
	public rankInfo(){
		name = "";
		minute = 0;
		second = 0;
	}
	
	public rankInfo(String n, int m, int s){
		name = n;
		minute = m;
		second = s;
	}

	public String getName() {
		return name;
	}

	public int getSecond() {
		return second;
	}

	public int getMinute() {
		return minute;
	}
}