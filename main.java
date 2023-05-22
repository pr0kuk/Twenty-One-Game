import java.util.Scanner;
public class main {
    static int balance = 100;
    static int bank = 100;
    static int stake = 0;
    static int card_sum = 0;
    static int card_sum_bot = 0;
    static int ready = 0;


    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        while(true) {
            System.out.print("Your stake: ");
            stake(in.nextInt());
            while(ready == 0 && stake != 0){
                get_card(1);
                System.out.println("Your sum: " + card_sum);
                check();
                if (stake == 0)
                    break;
                System.out.print("Wanna continue? ");
                ready = in.nextInt();
                check();
            }
            System.out.println("itog " + balance + " " + bank);
        }
    }
    public static void check() {
        if (card_sum > 21)
            lose();
        else if (card_sum == 21)
            win();
        else if (ready == 1)
            play_bot();
    }
    public static void play_bot() {
        do {
            get_card(0);
        } while(card_sum_bot < 17);
        System.out.println("Sums: " + card_sum + " " + card_sum_bot);
        if (Math.abs(card_sum_bot-21) <= Math.abs(card_sum-21))
            lose();
        else
            win();
        ready = 0;
    }
    public static void lose() {
        bank += stake;
        stake = 0;
        card_sum = 0;
        card_sum_bot = 0;
        System.out.println("You lost! Your balance: " + balance + ". " + "Bank: " + bank);
        
    }
    public static void win() {
        bank -= stake;
        balance += (2 * stake);
        stake = 0;
        card_sum = 0;
        card_sum_bot = 0;
        System.out.println("You won! Your balance: " + balance + ". " + "Bank: " + bank);

    }
    public static void stake(int sum) {
        balance -= sum;
        stake = sum;
    }
    static int[][] cards = new int[36][4];
    public static void get_card(int player) {
        int a ,b;
        do {
            a = (int)(Math.random() * 9) + 2;
            b = (int)(Math.random() * 4);
        } while (cards[a][b] == 1);
        System.out.println("card " + a + ' ' + b);
        cards[a][b] = 1;
        if (player == 1)
            card_sum += a;
        else
            card_sum_bot += a;
    }
}