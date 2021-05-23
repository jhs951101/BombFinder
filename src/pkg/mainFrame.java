package pkg;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;

/**
 * @author Ji-Hoon Shim
 * @date 2016-06-08
 * @brief 지뢰찾기 게임이 진행되는 데에 있어 중심이 되는 UI 클래스
 * 
 */
public class mainFrame extends JFrame{
	
	private mainFrame thisClass = this;
	
	public static String LEVEL = "Novice";
	public static int PROWS = 8;
	public static int MINE = 7;
	public static int FLAG = 0;
	public static int TotalFLAG = 0;
	public static boolean started = false;
	
	JButton[][] buttons;
	private char[][] signs;
	
	rankInOut handleRank;
	private rankInfo[] rank = new rankInfo[5];
	String name;
	
	private gameTimer timer;
	private JPanel gPanel;
	
	private JPanel mainContent;
	private JMenuBar menuBar;
	private JMenu mnExit;
	private JMenuItem mntmExit;
	private JMenu mnLevel;
	private JMenu mnRecord;
	private JMenuItem mntmRanking;
	JButton btnStart;
	private JLabel labelTime;
	JRadioButton ckboxHand;
	private JRadioButton ckboxFlag;
	private ButtonGroup grp1;
	private JRadioButtonMenuItem radioButtonNovice;
	private JRadioButtonMenuItem radioButtonIntermediate;
	private JRadioButtonMenuItem radioButtonExpert;
	private JRadioButtonMenuItem radioButtonCustomize;
	private ButtonGroup grp2;
	
	public static void main(String[] args){
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFrame frame = new mainFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public mainFrame() {
		
		setTitle("Find Bomb!!");
		setBounds(50, 50, 1070, 900);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent arg0) {
				System.exit(0);
			}
		});
		
		initialize();
	}
	
	private void initialize(){
		
		try {
			String s;
			int i = 0;
			BufferedReader in = new BufferedReader(new FileReader("src/data/ranking.txt"));
		      
			while ((s = in.readLine()) != null) {
				String[] strs = s.split(" ");
				rank[i] = new rankInfo(strs[0], Integer.valueOf(strs[1]), Integer.valueOf(strs[2]));
				i++;
			}
		      
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		handleRank = new rankInOut(rank, this);
		
		mainContent = new JPanel();
		mainContent.setBackground(new Color(204, 255, 255));
		mainContent.setLayout(null);
		
		setContentPane(mainContent);
		
		timer = new gameTimer();
		gPanel = new JPanel();
		
		gPanel.setBackground(new Color(204, 255, 255));
		gPanel.setBounds(17, 103, 1027, 733);
		gPanel.setLayout(new GridLayout(PROWS,PROWS,0,0));
		mainContent.add(gPanel);
		
		menuBar = new JMenuBar();
		menuBar.setBackground(new Color(0, 153, 255));
		menuBar.setBounds(0, 0, 1064, 31);
		mainContent.add(menuBar);
		
		mnLevel = new JMenu("Level");
		mnLevel.setForeground(Color.WHITE);
		menuBar.add(mnLevel);
		
		radioButtonNovice = new JRadioButtonMenuItem("Novice");
		radioButtonNovice.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if(mainFrame.started){
						JOptionPane.showMessageDialog(thisClass, "게임이 이미 진행중입니다.",
								"Notice", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					PROWS = 8;
					MINE = 7;
					LEVEL = "Novice";
				}
			}
		});
		radioButtonNovice.setSelected(true);
		mnLevel.add(radioButtonNovice);
		
		radioButtonIntermediate = new JRadioButtonMenuItem("Intermediate");
		radioButtonIntermediate.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if(mainFrame.started){
						JOptionPane.showMessageDialog(thisClass, "게임이 이미 진행중입니다.",
								"Notice", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					PROWS = 16;
					MINE = 77;
					LEVEL = "Intermediate";
				}
			}
		});
		mnLevel.add(radioButtonIntermediate);
		
		radioButtonExpert = new JRadioButtonMenuItem("Expert");
		radioButtonExpert.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if(mainFrame.started){
						JOptionPane.showMessageDialog(thisClass, "게임이 이미 진행중입니다.",
								"Notice", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					PROWS = 24;
					MINE = 288;
					LEVEL = "Expert";
				}
			}
		});
		mnLevel.add(radioButtonExpert);
		
		radioButtonCustomize = new JRadioButtonMenuItem("Customize");
		radioButtonCustomize.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					if(mainFrame.started){
						JOptionPane.showMessageDialog(thisClass, "게임이 이미 진행중입니다.",
								"Notice", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					String row = JOptionPane.showInputDialog(thisClass,
							"행과 열(n*n) 갯수를 입력하십시오", "");
					String mine = JOptionPane.showInputDialog(thisClass,
							"지뢰의 갯수를 입력하십시오", "");
					
					if(row != null && mine != null){
						if(!(isNumber(row) && isNumber(mine))){
							JOptionPane.showMessageDialog(thisClass, "유효한 값을 입력해 주세요",
									"Notice", JOptionPane.INFORMATION_MESSAGE);
							radioButtonNovice.setSelected(true);
							return;
						}
						
						int Irow = Integer.valueOf(row);
						int Imine = Integer.valueOf(mine);
						
						if(Irow > 24){
							JOptionPane.showMessageDialog(thisClass, "행과 열 갯수는 24 이하이여야 합니다",
									"Notice", JOptionPane.INFORMATION_MESSAGE);
							radioButtonNovice.setSelected(true);
						}
						else if(Irow*Irow <= Imine){
							JOptionPane.showMessageDialog(thisClass, "지뢰의 갯수가 너무 많습니다",
									"Notice", JOptionPane.INFORMATION_MESSAGE);
							radioButtonNovice.setSelected(true);
						}
						else {
							PROWS = Irow;
							MINE = Imine;
							LEVEL = "Customize";
							JOptionPane.showMessageDialog(thisClass, "정상적으로 설정 되었습니다",
									"Notice", JOptionPane.INFORMATION_MESSAGE);
						}
					}
				}
			}
		});
		mnLevel.add(radioButtonCustomize);
		
		mnRecord = new JMenu("Record");
		mnRecord.setForeground(Color.WHITE);
		menuBar.add(mnRecord);
		
		mntmRanking = new JMenuItem("Ranking");
		mntmRanking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				handleRank.showAll();
			}
		});
		mnRecord.add(mntmRanking);
		
		mnExit = new JMenu("Exit");
		mnExit.setForeground(Color.WHITE);
		menuBar.add(mnExit);
		
		mntmExit = new JMenuItem("Exit");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int q = JOptionPane.showConfirmDialog(thisClass, "정말로 나가시겠습니까?", "Question", JOptionPane.YES_NO_OPTION);
				if(q == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
		mnExit.add(mntmExit);
		
		btnStart = new JButton("START");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				name = JOptionPane.showInputDialog(thisClass,
						"이름을 입력하십시오 (공백 없이)", "");
				
				if(name != null){
					if(isBlank(name)){
						JOptionPane.showMessageDialog(thisClass, "공백 없이 입력해 주세요",
								"Notice", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					btnStart.setEnabled(false);
					btnStart.setVisible(false);
				
					timer = new gameTimer(labelTime);
					setGame();
				
					gameTimer.flag = true;
					labelTime.setText("00:00");
					timer.start();
				}
			}
		});
		btnStart.setForeground(new Color(255, 255, 255));
		btnStart.setBackground(new Color(0, 51, 255));
		btnStart.setFont(new Font("돋움", Font.BOLD, 22));
		btnStart.setBounds(17, 47, 125, 41);
		mainContent.add(btnStart);
		
		labelTime = new JLabel("00:00");
		labelTime.setFont(new Font("돋움", Font.PLAIN, 30));
		labelTime.setBounds(948, 47, 96, 41);
		mainContent.add(labelTime);
		
		ckboxHand = new JRadioButton("Hand");
		ckboxHand.setBackground(new Color(204, 255, 255));
		ckboxHand.setForeground(Color.BLUE);
		ckboxHand.setSelected(true);
		ckboxHand.setFont(new Font("돋움", Font.PLAIN, 22));
		ckboxHand.setBounds(442, 63, 85, 29);
		mainContent.add(ckboxHand);
		
		ckboxFlag = new JRadioButton("Flag");
		ckboxFlag.setBackground(new Color(204, 255, 255));
		ckboxFlag.setForeground(Color.BLUE);
		ckboxFlag.setFont(new Font("돋움", Font.PLAIN, 22));
		ckboxFlag.setBounds(536, 63, 85, 29);
		mainContent.add(ckboxFlag);
		
		grp1 = new ButtonGroup();
		grp1.add(ckboxHand);
		grp1.add(ckboxFlag);
		
		grp2 = new ButtonGroup();
		grp2.add(radioButtonNovice);
		grp2.add(radioButtonIntermediate);
		grp2.add(radioButtonExpert);
		grp2.add(radioButtonCustomize);
	}
	
	public void setGame(){
		mainFrame.FLAG = 0;
		mainFrame.TotalFLAG = 0;
		mainFrame.started = true;
		
		gPanel.setVisible(false);
		gPanel.removeAll();
		gPanel.setLayout(new GridLayout(PROWS,PROWS,0,0));
		
		buttons = new JButton[mainFrame.PROWS][];
		for(int i=0; i<mainFrame.PROWS; i++)
			buttons[i] = new JButton[mainFrame.PROWS];
		
		for(int i=0; i<mainFrame.PROWS; i++){
			for(int j=0; j<mainFrame.PROWS; j++){
				buttons[i][j] = new JButton();
				buttons[i][j].setForeground(new Color(0, 0, 0));
				buttons[i][j].setBackground(new Color(0, 230, 255));
				buttons[i][j].setEnabled(true);
				
				if(mainFrame.LEVEL.equals("Novice"))
					buttons[i][j].setFont(new Font("돋움", Font.BOLD, 30));
				else if(mainFrame.LEVEL.equals("Intermediate"))
					buttons[i][j].setFont(new Font("돋움", Font.BOLD, 25));
				else if(mainFrame.LEVEL.equals("Expert") || mainFrame.LEVEL.equals("Customize"))
					buttons[i][j].setFont(new Font("돋움", Font.BOLD, 12));
				
				buttons[i][j].setText("");
				
				gPanel.add(buttons[i][j]);
			}
		}
		
		signs = new char[mainFrame.PROWS+2][];
		for(int i=0; i<mainFrame.PROWS+2; i++)
			signs[i] = new char[mainFrame.PROWS+2];
		
		for(int i=0; i<mainFrame.PROWS+2; i++)
			for(int j=0; j<mainFrame.PROWS+2; j++)
				signs[i][j] = ' ';
		
		for(int n=1; n<=mainFrame.MINE; n++){
				
			int x;
			int y;
			
			do{
				x = (int)(Math.random() * mainFrame.PROWS) + 1;
				y = (int)(Math.random() * mainFrame.PROWS) + 1;
			}while(signs[x][y] == '*');
			
			signs[x][y] = '*';
		}
		
		for(int i=1; i<=mainFrame.PROWS; i++){
			for(int j=1; j<=mainFrame.PROWS; j++){
				if(signs[i][j] != '*'){
					int n = 0;
				
					if(signs[i][j-1] == '*') n++;
					if(signs[i-1][j-1] == '*') n++;
					if(signs[i-1][j] == '*') n++;
					if(signs[i-1][j+1] == '*') n++;
					if(signs[i][j+1] == '*') n++;
					if(signs[i+1][j+1] == '*') n++;
					if(signs[i+1][j] == '*') n++;
					if(signs[i+1][j-1] == '*') n++;
				
					signs[i][j] = (char)(n+48);
				}
			}
		}
		
		for(int i=0; i<mainFrame.PROWS; i++)
			for(int j=0; j<mainFrame.PROWS; j++)
				buttons[i][j].addActionListener(new buttonAction(i, j, gPanel, signs, this, timer, handleRank));
		
		gPanel.setVisible(true);
		
		/*for(int i=1; i<=mainFrame.PROWS; i++){
			for(int j=1; j<=mainFrame.PROWS; j++){
				System.out.print(signs[i][j] + " ");
			}
			System.out.println("");
		}*/
	}
	
	/**
	 * 
	 * @param num: 숫자인지 아닌지 판단받기 위한 값
	 */
	public boolean isNumber(String num){  // string 값이 숫자인지 아닌지 판단하는 함수
		
		for(int i=0; i<num.length(); i++){
			char c = num.charAt(i);
			
			if(c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && 
					c != '6' && c != '7' && c != '8' && c != '9')
				return false;
		}
		
		return true;
	}
	
	/**
	 * 
	 * @param str: 공백이 있는지 없는지 판단받기 위한 값
	 */
	public boolean isBlank(String str){  // string 값에 공백이 있는지 판단하는 함수
		
		for(int i=0; i<str.length(); i++){
			char c = str.charAt(i);
			
			if(c == ' ')
				return true;
		}
		
		return false;
	}
}