import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.EmptyStackException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Arrays;

public class Game extends Canvas implements Runnable, MouseListener {
   static Boolean running = true, win = false, lose = false, startflag = true, banner = false;
   public static final String NAME = "Twenty-One Java Game";
   public static final int WIDTH  = 2000, HEIGHT = 1000;
   public static final int CARDW = 90, CARDH = 140;
   public static final int HANDH = 600, RIGHTHANDV = 1200, DISTANCE = 800;
   public static final int STAKEH = 20, LEFTSTAKEV = 885, STAKESIZE = 50;
   public static final int LEFTBUTTONV = 1800, BUTTONH = 810, BUTTOND = 60;
   static int[][][] hand = new int[2][9][2];
   static int[][] cards = new int[12][4];
   static int[] num_cards = new int[2];
   static int[] card_sum = new int[2];
   static int balance = 100, bank = 100, stake = 0, num_cards_in_deck = 36;
   static Sprite shirt, back, box;
   static Sprite[][] spritescard = new Sprite[12][4];

   public void stake(int sum) {
      balance -= sum;
      stake = sum;
   }
   public void get_card(int player) {
      int a, b;
      do {
         a = (int)(Math.random() * 10) + 2;
         b = (int)(Math.random() * 4);
      } while (cards[a][b] == 1 || a==5);
      cards[a][b] = 1;
      card_sum[player] += a;
      hand[player][num_cards[player]][0] = a;
      hand[player][num_cards[player]][1] = b;
      num_cards[player]++;
      num_cards_in_deck--;
   }

   public void check() {
      if (card_sum[1] > 21)
         lose();
      else if (card_sum[1] == 21)
         win();
   }

   public void play_bot() {
      do {
         get_card(0);
      } while(card_sum[0] < 17);
      if (card_sum[0] <= 21 && card_sum[0] >= card_sum[1])
         lose();
      else
         win();
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

   public void loadsprites()
   {
      back = new Sprite("textures\\table.png").width(WIDTH).height(HEIGHT);
      box = new Sprite("textures\\d00237box.jpg").width(CARDW).height(CARDH);
      shirt = new Sprite("textures\\shirt.jpg").width(CARDW).height(CARDH);
      String ss, s1 = "", s2= "";
      for (int i = 2; i <= 11; i++)
      {
         if (i == 5) i++;
         switch(i) {
            case (2):
               s1 = "U"; break;
            case(3):
               s1 = "O"; break;
            case(4):
               s1 = "K"; break;
            case(11):
               s1 = "A"; break;
            default:
               s1 = ""+i;
         }
         for (int j = 0; j < 4; j++)
         {
            switch(j) {
               case(0):
                  s2 = "c"; break;
               case(1):
                  s2 = "d"; break;
               case(2):
                  s2 = "h"; break;
               case(3):
                  s2 = "s"; break;
            }
            ss = s2 + s1;
            spritescard[i][j] = new Sprite("textures\\d00237d03" + ss + ".jpg").width(CARDW).height(CARDH);
         }
      }
   }

   public void drawhand(Graphics g, int player) {
      for (int i = 0; i < num_cards[player]; i++)
      {
         spritescard[hand[player][i][0]][hand[player][i][1]].center_draw(g, RIGHTHANDV+(int)(i*(CARDW)*1.15)-DISTANCE*player, HANDH);
         g.setColor(Color.WHITE);
         setTextCenter(g, "Card: " + hand[player][i][0], RIGHTHANDV+(int)(i*(CARDW)*1.15)-DISTANCE*player, HANDH+(int)(0.8*CARDH));
      }
      g.setColor(Color.WHITE);
      String ss = "Your";
      if (player == 0)
         ss ="Bot";
      g.drawString(ss + " sum: " + card_sum[player], 1525-1110*player, HANDH+150);
   }
   public void drawstakes(Graphics g) {
      g.setColor(Color.WHITE);
      setTextCenter(g,"Choose Stake or", 1000, STAKEH+65);
      for (int i  = 0; i < 4; i++)
      {
         g.setColor(Color.RED);
         g.fillRect(LEFTSTAKEV + (int)(i*1.2*STAKESIZE), STAKEH, STAKESIZE, STAKESIZE);
         g.setColor(Color.WHITE);
         setTextCenter(g,""+(i+1)*5, LEFTSTAKEV+25 + (int)(i*1.2*STAKESIZE), STAKEH+25);
      }
      g.setColor(Color.RED);
      g.fillRect(LEFTSTAKEV, STAKEH+75, STAKESIZE+(int)(3*1.2*STAKESIZE), STAKESIZE);
      g.setColor(Color.WHITE);
      setTextCenter(g,"Pass", 1000, STAKEH+100);

      g.setColor(Color.RED);
      g.fillRect(LEFTBUTTONV, BUTTONH+2*BUTTOND, 150, 50);
      g.setColor(Color.WHITE);
      setTextCenter(g, "Shuffle", LEFTBUTTONV + 75, BUTTONH + 25 + BUTTOND*2);
   }
   public void setTextCenter(Graphics g, String s, int x, int y)
   {
      int sw = (int)g.getFontMetrics().getStringBounds(s, g).getWidth();
      int sh = (int)g.getFontMetrics().getStringBounds(s, g).getHeight();
      int xc = x - sw / 2;
      int yc = y - sh / 2;
      g.drawString(s, xc, y);
   }

   public void paint() {
      BufferStrategy bs = getBufferStrategy(); 
      if (bs == null) {
         createBufferStrategy(2);
         requestFocus();
         return;
      }
      Graphics g = bs.getDrawGraphics();
      g.clearRect(0, 0, WIDTH, HEIGHT);

      g.setFont(new Font("Monospaced", Font.PLAIN, 15));
      back.draw(g,0,0);
      
      box.center_draw(g,950,400);
      shirt.center_draw(g,1050,400);
      g.setColor(Color.WHITE);
      setTextCenter(g, "Number of cards in deck: " + num_cards_in_deck, 1000, 525);

      drawhand(g, 0);
      drawhand(g, 1);

      g.setColor(Color.WHITE);
      g.drawString("Balance: " + balance, 10, 20);
      g.drawString("Bank: " + bank, 10, 40);
      g.drawString("Stake: " + stake, 10, 60);

      if (win == true || lose == true || startflag == true) {
         g.setColor(Color.RED);
         g.fillRect(850, 250, 300, 50);
         g.setColor(Color.WHITE);
         String ss;
         if (win == true)
            ss = "You won!";
         else if (lose == true)
            ss = "You lost!";
         else
            ss = "Welcome to Twenty-One Java Game";
         setTextCenter(g,ss, 1000, 275);
      }
      if (banner)
         drawstakes(g);
      else
         drawbuttons(g);
      g.dispose();
      bs.show();
   }

   public void drawbuttons(Graphics g) {
         g.setColor(Color.RED);
         g.fillRect(LEFTBUTTONV, BUTTONH, 150, 50);
         g.setColor(Color.WHITE);
         setTextCenter(g, "Get card", LEFTBUTTONV + 75, BUTTONH + 25);

         g.setColor(Color.RED);
         g.fillRect(LEFTBUTTONV, BUTTONH+BUTTOND, 150, 50);
         g.setColor(Color.WHITE);
         setTextCenter(g, "End Turn", LEFTBUTTONV + 75, BUTTONH + 25 + BUTTOND);

         // g.setColor(Color.RED);
         // g.fillRect(LEFTBUTTONV, BUTTONH+2*BUTTOND, 150, 50);
         // g.setColor(Color.WHITE);
         // setTextCenter(g, "Shuffle", LEFTBUTTONV + 75, BUTTONH + 25 + BUTTOND*2);
   }

   public void shuffle() {
      // num_cards_in_deck = 36 - num_cards[1];
      // for (int i = 0; i < 12; i++)
      //    for (int j = 0; j < 4; j++)
      //       cards[i][j] = 0;
      // for (int i = 0; i <num_cards[1]; i++)
      //    cards[hand[1][i][0]][hand[1][i][1]] = 1;

      num_cards_in_deck = 36;
      for (int i = 0; i < 12; i++)
         for (int j = 0; j < 4; j++)
            cards[i][j] = 0;
      pass();
   }

   public void pass() {            
      card_sum[0] = card_sum[1] = num_cards[0] = num_cards[1] = 0;
      get_card(1);
   }

   public void upd(int X, int Y) {
      if (lose == false && win == false && startflag == false && banner == false) {
         if (ClickInRect(X,Y, LEFTBUTTONV, BUTTONH, 150, 50) == true) {
            get_card(1);
            check();
         }
         if (ClickInRect(X,Y, LEFTBUTTONV, BUTTONH+BUTTOND, 150, 50) == true)
            play_bot();
         // if (ClickInRect(X,Y, LEFTBUTTONV, BUTTONH+2*BUTTOND, 150, 50) == true)
         //    shuffle();
      }
      else if (banner == false) {
         if (ClickInRect(X, Y, 850, 250, 300, 50) == true) {
            card_sum[0] = card_sum[1] = num_cards[0] = num_cards[1] = 0;
            get_card(1);
            lose = win = startflag = false;
            banner = true;
         }
      }
      else {
         if (ClickInRect(X,Y, LEFTBUTTONV, BUTTONH+2*BUTTOND, 150, 50) == true) {
            shuffle();
            return;
         }
         else if (ClickInRect(X, Y, LEFTSTAKEV, STAKEH, STAKESIZE, STAKESIZE) == true)
            stake(5);
         else if (ClickInRect(X, Y, LEFTSTAKEV + (int)(1.2*1*STAKESIZE), STAKEH, STAKESIZE, STAKESIZE) == true)
            stake(10);
         else if (ClickInRect(X, Y, LEFTSTAKEV + (int)(1.2*2*STAKESIZE), STAKEH, STAKESIZE, STAKESIZE) == true)
            stake(15);
         else if (ClickInRect(X, Y, LEFTSTAKEV + (int)(1.2*3*STAKESIZE), STAKEH, STAKESIZE, STAKESIZE) == true)
            stake(20);
         else if (ClickInRect(X, Y, LEFTSTAKEV, STAKEH+75, STAKESIZE+(int)(3*1.2*STAKESIZE), STAKESIZE) == true){
            pass();
            return;
         } else
            return;
         banner = false;
      }
   }


   public Game() {
      setPreferredSize(new Dimension(WIDTH, HEIGHT));
      start();
   }

   public void start() {
      running = true;
      new Thread(this).start();
   }

   public void run() {
      loadsprites();
      long delta = 0, lastTime = 0;
      addMouseListener(this);
      while(running){
         delta = System.currentTimeMillis() - lastTime;
         if (delta >= 16){
               lastTime = System.currentTimeMillis();	
               paint();
            }
      }
   }

   public static void main(String[] args) {
      Game game = new Game();
      JFrame frame = new JFrame(Game.NAME);
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setLayout(new BorderLayout());
      frame.add(game, BorderLayout.CENTER);
      frame.pack();
      frame.setResizable(false);
      frame.setVisible(true);
   }

   public Boolean ClickInRect(int X, int Y, int x1, int y1, int x2, int y2) {
      if (X > x1 && X < (x2+x1) && Y > y1 && Y < (y2+y1))
         return true;
      return false;
   }
   public void mouseClicked(MouseEvent e) {}
   public void mouseEntered(MouseEvent e) {}
   public void mouseExited(MouseEvent e) {}
   public void mousePressed(MouseEvent e) {upd(e.getX(), e.getY());}
   public void mouseReleased(MouseEvent e) {}
}