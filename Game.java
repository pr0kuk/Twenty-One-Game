import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.EmptyStackException;

public class Game extends Canvas implements Runnable, MouseListener {
   static Boolean running = true, win = false, lose = false, startflag = true;
   public static final String NAME = "Twenty-One Java Game";
   public static final int WIDTH  = 2000;
   public static final int HEIGHT = 1000;
   static int[][][] hand = new int[2][9][2];
   static int[][] cards = new int[36][4];
   static int[] num_cards = new int[2];
   static int[] card_sum = new int[2];
   static int balance = 100;
   static int bank = 100;
   static int stake = 0;
   static int ready = 0;
   static int card = 0;

   public void start() {
      running = true;
      new Thread(this).start();
   }

   public void drawhand(Graphics g, int player) {
      for (int i = 0; i < num_cards[player]; i++)
      {
         g.setColor(Color.GRAY);
         g.drawRect(1200+i*75-800*player, 800, 65, 100); 
         g.setColor(Color.BLACK);
         g.drawString("Card: " + hand[player][i][0] + " " + hand[player][i][1], 1220+i*75-800*player, 930);
      }
      String ss = "Your";
      if (player == 0)
         ss ="Bot";
      g.drawString(ss + " sum:" + card_sum[player], 1200-800*player, 970);
   }
   public void drawstakes(Graphics g) {
      g.drawString("Choose Stake", 790, 310);
      g.setColor(Color.RED);
      g.drawRect(770, 330, 50, 50);
      g.setColor(Color.RED);
      g.drawRect(770, 390, 50, 50);
      g.setColor(Color.RED);
      g.drawRect(830, 330, 50, 50);
      g.setColor(Color.RED);
      g.drawRect(830, 390, 50, 50);
   }
   public void  render() {
      BufferStrategy bs = getBufferStrategy(); 
      if (bs == null) {
         createBufferStrategy(2);
         requestFocus();
         return;
      }
      Graphics g = bs.getDrawGraphics();
      g.setFont(new Font("Monospaced", Font.PLAIN, 12));
      g.clearRect(0, 0, WIDTH, HEIGHT);
      setBackground(Color.WHITE);

      g.setColor(Color.GRAY);
      g.drawRect(800, 600, 65, 100); 

      drawhand(g, 0);
      drawhand(g, 1);

      g.setColor(Color.BLACK);
      g.drawString("Balance: " + balance, 10, 20);
      g.drawString("Bank: " + bank, 10, 40);
      g.drawString("Stake: " + stake, 10, 60);

      if (win == true || lose == true || startflag == true) {
         g.setColor(Color.RED);
         g.drawRect(700, 500, 300, 50);
         g.setColor(Color.BLACK);
         String ss;
         if (win == true)
            ss = "You won!";
         else if (lose == true)
            ss = "You lost!";
         else
            ss = "Welcome to Twenty-One Java Game";
         g.drawString(ss, 760, 525);
         drawstakes(g);
      }
      else {
         g.setColor(Color.RED);
         g.drawRect(1800, 700, 150, 50);
         g.setColor(Color.BLACK);
         g.drawString("Get card", 1825, 725);

         g.setColor(Color.RED);
         g.drawRect(1800, 775, 150, 50);
         g.setColor(Color.BLACK);
         g.drawString("End Turn", 1825, 800);
      }
      g.dispose();
      bs.show();
   }

   public Boolean ClickInRect(int X, int Y, int x1, int y1, int x2, int y2) {
      if (X > x1 && X < (x2+x1) && Y > y1 && Y < (y2+y1))
         return true;
      return false;
   }
   public void stake(int sum) {
      balance -= sum;
      stake = sum;
   }
   public void get_card(int player) {
      int a, b;
      do {
         a = (int)(Math.random() * 9) + 2;
         b = (int)(Math.random() * 4);
      } while (cards[a][b] == 1);
      cards[a][b] = 1;
      card_sum[player] += a;
      hand[player][num_cards[player]][0] = a;
      hand[player][num_cards[player]][1] = b;
      num_cards[player]++;
   }

   public void check() {
      if (card_sum[1] > 21)
         lose();
      else if (card_sum[1] == 21)
         win();
      else if (ready == 1)
            play_bot();
   }

   public void play_bot() {
      do {
         get_card(0);
      } while(card_sum[0] < 17);
      if (Math.abs(card_sum[0]-21) <= Math.abs(card_sum[1]-21))
         lose();
      else
         win();
      ready = 0;
   }

   public void update(int X, int Y) {
      if (lose == false && win == false && startflag == false) {
         if (ClickInRect(X,Y, 1800, 700, 100, 50) == true) {
            get_card(1);
            check();
         }
         if (ClickInRect(X,Y, 1800, 775, 100, 50) == true) {
            play_bot();
         }
      }
      else {
         if (ClickInRect(X,Y, 770, 330, 50, 50) == true)
            stake(5);
         else if (ClickInRect(X,Y, 770, 390, 50, 50) == true)
            stake(10);
         else if (ClickInRect(X,Y, 830, 330, 50, 50) == true)
            stake(15);
         else if (ClickInRect(X,Y, 830, 390, 50, 50) == true)
            stake(20);
         else
            return;
         card_sum[0] = 0;
         card_sum[1] = 0;
         num_cards[0] = 0;
         num_cards[1] = 0;
         lose = false;
         win = false;
         startflag = false;
      }
   }

   public void lose() {
      bank += stake;
      stake = 0;
      lose = true;
   }
   public void win() {
      bank -= stake;
      balance += (2 * stake);
      stake = 0;
      win = true;  
   }

   public void run() {
      addMouseListener(this);
      while(running) {
         render();
      }
   }

   public static void main(String[] args) {
      Game game = new Game();
      game.setPreferredSize(new Dimension(WIDTH, HEIGHT));
      JFrame frame = new JFrame(Game.NAME);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      frame.add(game, BorderLayout.CENTER);
      frame.pack();
      frame.setResizable(false);
      frame.setVisible(true);
      game.start();
   }

   public void mouseClicked(MouseEvent e) {update(e.getX(), e.getY());}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mousePressed(MouseEvent e) {}
   public void mouseReleased(MouseEvent e) {}
}