import java.io.*;
import java.util.Scanner;

class Main extends Harta {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(new File("map.in"));
        Harta graf = new Harta();
        graf.createHarta(input);
    }
}