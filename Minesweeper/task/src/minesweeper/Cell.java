package minesweeper;

import java.util.ArrayList;
import java.util.List;

public class Cell {
    private static final String REGEX = "[1-8]";
    public static final String EMPTY = ".";
    public static final String MINE = "X";
    public static final String MARKED = "*";
    public static final String FREE = "/";
    private int x;
    private int y;
    private String state = EMPTY;
    private String stateTmp = EMPTY;
    private boolean mine;
    private boolean hideContent = true;

    private static final List<Cell> cells = new ArrayList<>();
    private final List<Cell> linkedCells = new ArrayList<>();

    public Cell(int x, int y, boolean mine) {
        this(x, y);

        if (mine) {
            this.state = MINE;
            this.stateTmp = MINE;
        } else {
            this.state = EMPTY;
        }
        this.mine = mine;
    }

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        cells.add(this);

        for (Cell cell : cells) {
            if ((cell.y == y && (x - 1 == cell.x || x + 1 == cell.x)) ||
                    (cell.x == x && (y - 1 == cell.y || y + 1 == cell.y)) ||
                    (x - 1 == cell.x && (y - 1 == cell.y || y + 1 == cell.y)) ||
                    (x + 1 == cell.x && (y - 1 == cell.y || y + 1 == cell.y))) {
                linkedCells.add(cell);
                cell.linkedCells.add(this);
            }
        }
    }

    public boolean markOrNonMark(Command command) {
        if (Command.MINE.equals(command)) {
            if (state.equals(MARKED)) {
                state = stateTmp;
                hideContent = true;
            } else if (mine || state.equals(EMPTY) || (state.matches(REGEX) && hideContent)) {
                state = MARKED;
                hideContent = false;
            }
        } else {
            if (state.matches(REGEX)) {
                hideContent = false;
            } else if (mine) {
                for (Cell cell : cells) {
                    if (cell.mine) {
                        cell.hideContent = false;
                        return false;
                    }
                }
            } else if (state.equals(EMPTY)) {
                hideContent = false;
                state = stateTmp;

                for (Cell cell : linkedCells) {
                    if (cell.state.matches(REGEX)) {
                        cell.hideContent = false;
                    } else if (cell.state.equals(EMPTY)) {
                        cell.hideContent = false;
                        cell.state = cell.stateTmp;
                        recursiveMark(cell);
                    }
                }
            }
        }
        return true;
    }

    private void recursiveMark(Cell cell) {
        for (Cell item : cell.linkedCells) {
            if (!item.mine) {
                item.hideContent = false;
                if (item.state.equals(MARKED) && item.stateTmp.matches(REGEX)) {
                    item.state = item.stateTmp;
                }
                if (item.state.equals(EMPTY) || (item.stateTmp.equals(EMPTY) && item.state.equals(MARKED))) {
                    item.state = FREE;
                    recursiveMark(item);
                }
            }
        }
    }

    public boolean isHideContent() {
        return hideContent;
    }

    public void setHideContent(boolean hideContent) {
        this.hideContent = hideContent;
    }

    public String getState() {
        return hideContent ? EMPTY : state;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean containsMine() {
        return mine;
    }

    public List<Cell> getLinkedCells() {
        return linkedCells;
    }

    public void incrementNumberOfMinesAround() {
        if (mine) {
            for (Cell cell : linkedCells) {
                if (cell.state.equals(EMPTY)) {
                    cell.state = "1";
                    cell.stateTmp = "1";
                } else if (cell.state.matches(REGEX)) {
                    cell.state = String.valueOf(Integer.parseInt(cell.state) + 1);
                    cell.stateTmp = cell.state;
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Cell{" +
                "x=" + (x + 1) +
                ", y=" + (y + 1) +
                ", state='" + state + '\'' +
                ", stateTmp='" + stateTmp + '\'' +
                ", mine=" + mine +
                ", hide=" + hideContent +
                '}';
    }
}
