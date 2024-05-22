import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Minesweeper extends JFrame implements MouseListener, ActionListener
{
	JPanel boardPanel;
	JToggleButton[][] board;
	int dimR = 9, dimC = 9;
	boolean first = true, gameOn = true, winner = true;
	int numMines = 10;
	int state;
	int counter = 0;

	ImageIcon flag, mine, smile, win, lose, wait;
	ImageIcon one, two, three, four, five, six, seven, eight;
	ImageIcon[] icons;

	JMenuBar menuBar;
	JMenu menu;
	JMenuItem easy, intermediate, hard;
	JToggleButton face;
	JTextField timerText;
	Timer timer;
	int time;




	public Minesweeper()
	{

		flag = new ImageIcon("flag.png");
		flag = new ImageIcon(flag.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		mine = new ImageIcon("mine.png");
		mine = new ImageIcon(mine.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));


		//face Smiles
		smile = new ImageIcon("smile1.png");
		smile = new ImageIcon(smile.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		win = new ImageIcon("win1.png");
		win = new ImageIcon(win.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		lose = new ImageIcon("dead1.png");
		lose = new ImageIcon(lose.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		wait = new ImageIcon("wait1.png");
		wait = new ImageIcon(wait.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		//Array of Icons
		icons = new ImageIcon[8];


		icons[0] = new ImageIcon("1.png");
		icons[0] = new ImageIcon(icons[0].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		icons[1] = new ImageIcon("2.png");
		icons[1] = new ImageIcon(icons[1].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		icons[2] = new ImageIcon("3.png");
		icons[2] = new ImageIcon(icons[2].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		icons[3] = new ImageIcon("4.png");
		icons[3] = new ImageIcon(icons[3].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		icons[4] = new ImageIcon("5.png");
		icons[4] = new ImageIcon(icons[4].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		icons[5] = new ImageIcon("6.png");
		icons[5] = new ImageIcon(icons[5].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		icons[6] = new ImageIcon("7.png");
		icons[6] = new ImageIcon(icons[6].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		icons[7] = new ImageIcon("8.png");
		icons[7] = new ImageIcon(icons[7].getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH));

		timerText = new JTextField();
		timerText.setHorizontalAlignment(SwingConstants.RIGHT);
		timerText.setForeground(Color.CYAN);
		timerText.setBackground(Color.BLACK);

		createBoard(dimR, dimC);
		this.setVisible(true);




		menuBar = new JMenuBar();
		menu = new JMenu("Difficulty");

		easy = new JMenuItem("Easy");
		intermediate = new JMenuItem("Intermediate");
		hard = new JMenuItem("Hard");

		easy.addActionListener(this);
		intermediate.addActionListener(this);
		hard.addActionListener(this);

		menu.add(easy);
		menu.add(intermediate);
		menu.add(hard);

		face = new JToggleButton();
		face.setIcon(smile);
		face.addActionListener(this);

		menuBar.add(menu);
		menuBar.add(face);
		menuBar.add(timerText);

		this.add(menuBar, BorderLayout.NORTH);
		this.setVisible(true);

	}

	public void createBoard(int row, int col)
	{

		timer = new Timer(1000, a1 ->
		{
			time++;
			timerText.setText("Time: "+time+" ");
		});

		timer.start();

		if(boardPanel != null)
			this.remove(boardPanel);

		board = new JToggleButton[row][col];
		boardPanel = new JPanel();
		boardPanel.setLayout(new GridLayout(row, col));


		for(int r = 0; r< board.length; r++)
		{
			for(int c = 0; c<board[r].length; c++)
			{
				board[r][c] = new JToggleButton();
				board[r][c].putClientProperty("row", r);
				board[r][c].putClientProperty("column", c);
				board[r][c].putClientProperty("state", 0);
				board[r][c].addMouseListener(this);
				boardPanel.add(board[r][c]);
			}
		}
		this.add(boardPanel);
		this.setSize(board[0].length*35, board.length*35);
		this.revalidate();
	}

	public void displayIcon(int r, int c, int s)
	{
		board[r][c].setIcon(icons[s-1]);
		board[r][c].setDisabledIcon(icons[s-1]);
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == easy)
		{
			dimR = 9;
			dimC = 9;
			numMines = 10;
		}
		if(e.getSource() == intermediate)
		{
			dimR = 16;
			dimC = 16;
			numMines = 35;
		}
		if(e.getSource() == hard)
		{
			dimR = 16;
			dimC = 35;
			numMines = 90;
		}
		if(e.getSource() == face)
		{
			face.setIcon(smile);
			time = 0;
			createBoard(dimR, dimC);
		}

		createBoard(dimR, dimC);
		first = true;
		gameOn = true;
	}

	public void mouseClicked(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
		face.setIcon(wait);
	}

	public void mouseReleased(MouseEvent e)
	{
		face.setIcon(smile);
		int row = (int)((JToggleButton)e.getComponent()).getClientProperty("row");
		int col = (int)((JToggleButton)e.getComponent()).getClientProperty("column");

		if(e.getButton() == MouseEvent.BUTTON1)
		{
			if(gameOn)
			{
				if(first)
				{
					setBombsAndNums(row, col);
					first = false;
				}

				state = (int)board[row][col].getClientProperty("state");

				if(state == -1 && board[row][col].isEnabled())
				{
					board[row][col].setBackground(Color.RED);
					board[row][col].setContentAreaFilled(false);
					board[row][col].setOpaque(true);
					board[row][col].setIcon(mine);
					board[row][col].setDisabledIcon(mine);
					board[row][col].setEnabled(false);
					showMines();
					gameOn = false;
				}
				else if(board[row][col].isEnabled())
				{
					click(row, col);
				}
			}
		}
		else if(e.getButton() == MouseEvent.BUTTON3)
		{
			if(!board[row][col].isSelected())
			{
				if(board[row][col].getIcon() == null)
				{
					board[row][col].setIcon(flag);
					board[row][col].setDisabledIcon(flag);
					board[row][col].setEnabled(false);
				}
				else
				{
					board[row][col].setIcon(null);
					board[row][col].setEnabled(true);

				}
			}
		}
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{


	}

	public void setBombsAndNums(int selectedRow, int selectedCol)
	{
		int count = numMines;

		while(count > 0)
		{
			int row = (int)(Math.random()*dimR);
			int col = (int)(Math.random()*dimC);
			int state = Integer.parseInt(""+board[row][col].getClientProperty("state"));

			if(state == 0 && (row != selectedRow || col != selectedCol))
			{
				board[row][col].putClientProperty("state", -1);
				count--;
			}
		}
		for(int r=0; r<dimR; r++)
		{
			for(int c=0; c<dimC; c++)
			{
				count = 0;

				state = Integer.parseInt("" + board[r][c].getClientProperty("state"));
				if(state != -1)
				{
					for(int smallR = r-1; smallR<=r+1; smallR++)
					{
						for(int smallC = c-1; smallC<=c+1; smallC++)
						{
							try{
								state = Integer.parseInt(""+board[smallR][smallC].getClientProperty("state"));

								if(state == -1 & (smallR != r || smallC != c))
								{
									count++;
								}
							}
							catch(ArrayIndexOutOfBoundsException e)
							{}
						}
					}
					board[r][c].putClientProperty("state", count);
					board[r][c].setText("");
				}
			}
		}
	}
	public void click(int row, int col)
	{
		if(!board[row][col].isSelected())
		{
			board[row][col].setSelected(true);
		}
		int state = (int)board[row][col].getClientProperty("state");
		if(state != 0)
		{
			board[row][col].getClientProperty("state");
			displayIcon(row, col, state);
		}
		else
		{
			for(int smallR = row-1; smallR <= row+1; smallR++)
			{
				for(int smallC = col-1; smallC<=col+1; smallC++)
				{
					try{
						if(!board[smallR][smallC].isSelected())
						{
							click(smallR, smallC);
						}
					}catch(ArrayIndexOutOfBoundsException e)
					{

					}
				}
			}
		}
		checkWin();
	}
	public void showMines()
	{
		timer.stop();
		face.setIcon(lose);
		for(int r = 0; r<board.length; r++)
		{
			for(int c=0; c<board[0].length; c++)
			{
				int state = Integer.parseInt(""+board[r][c].getClientProperty("state"));

				if(state == -1)
				{

					board[r][c].setIcon(mine);
					board[r][c].setDisabledIcon(mine);
					board[r][c].setEnabled(false);
					board[r][c].setSelected(true);
				}

			}
		}
	}

	public void checkWin()
	{
		winner = true;
		for(int r = 0; r<board.length; r++)
		{
			for(int c=0; c<board[0].length; c++)
			{
				int state = Integer.parseInt(""+board[r][c].getClientProperty("state"));
				if(!board[r][c].isSelected() && state != -1)
				{
					winner = false;
				}
			}
		}
		if(winner)
		{
			timer.stop();
			face.setIcon(win);
		}
	}

	public static void main(String[]args)
	{
		Minesweeper app = new Minesweeper();
	}
}