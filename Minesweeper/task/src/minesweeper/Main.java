package minesweeper;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.print("How many mines do you want on the field? ");
        int numberOfMines = input.nextInt();
        Area area = new Area(9, 9, numberOfMines);
        area.display();

        int x;
        int y;
        do {
            System.out.print("Set/delete mines marks (x and y coordinates): ");
            x = input.nextInt() - 1;
            y = input.nextInt() - 1;
            System.out.println();

            boolean isMarked = area.markOrNonMarkCell(x, y, Command.valueOf(input.nextLine().trim().toUpperCase()));
            area.display();

            if (!isMarked) {
                System.out.println("You stepped on a mine and failed!");
                break;
            }

            if(area.isWon()){
                System.out.println("Congratulations! You found all the mines!");
                break;
            }
        } while (true);
    }
}
