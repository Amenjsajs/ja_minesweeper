package minesweeper;

import java.util.Random;

public class Area {
    private int row;
    private int col;
    private int numberOfMines;
    private Cell[][] cells;

    private int attempt = 0;

    public Area(int row, int col, int numberOfMines) {
        this.row = row;
        this.col = col;
        this.numberOfMines = numberOfMines;
        this.cells = new Cell[row][col];
    }

    private void init() {
        Random random = new Random();
        int i;
        int j;
        int remains = numberOfMines;
        do {
            i = random.nextInt(row);
            j = random.nextInt(col);

            if (cells[i][j] == null) {
                cells[i][j] = new Cell(i, j, true);

                remains--;
            }
        } while (remains > 0);

        for (i = 0; i < row; i++) {
            for (j = 0; j < col; j++) {
                if (cells[i][j] == null) {
                    cells[i][j] = new Cell(i, j);
                }
            }
        }

        for (Cell[] line : cells) {
            for (Cell cell : line) {
                if (cell.containsMine()) {
                    cell.incrementNumberOfMinesAround();
                }
            }
        }
    }

    public void display() {
        System.out.print(" |");
        int i = 0;
        for (; i < col; i++) {
            System.out.print(i + 1);
        }
        System.out.println("|");

        System.out.printf("-|%s|\n", "-".repeat(col));


        i = 0;
        for (Cell[] line : cells) {
            System.out.print(++i + "|");
            for (Cell cell : line) {
                if (attempt == 0) {
                    System.out.print(Cell.EMPTY);
                } else {
                    System.out.print(cell.getState());
                }
            }
            System.out.print("|\n");
        }
        System.out.printf("-|%s|\n", "-".repeat(col));
    }

    public boolean markOrNonMarkCell(int x, int y, Command command) {
        attempt++;
        if (Command.FREE.equals(command)) {
            if (attempt == 1) {
                cells[x][y] = new Cell(x, y, false);
            }
        }
        if (attempt == 1) {
            init();
        }
        System.out.println(cells[y][x]);
        return cells[y][x].markOrNonMark(command);
    }

    public boolean isWon() {
        int markedCount = 0;
        int freeCount = 0;
        int mineCount = 0;

        for (Cell[] line : cells) {
            for (Cell cell : line) {
                if(cell.getState().equals(Cell.MARKED)){
                    if(cell.containsMine()){
                        mineCount++;
                    } else {
                        markedCount++;
                    }
                }
                if (!cell.isHideContent() && (cell.getState().equals(Cell.FREE) || (cell.getState().matches("[1-8]")))) {
                    freeCount++;
                }
            }
        }

        return (mineCount == numberOfMines && markedCount == 0) || freeCount + numberOfMines == row * col;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getNumberOfMines() {
        return numberOfMines;
    }
}