package dev.damarreb.asciitable;
import java.util.ArrayList;

/**
 * <p>
 * This class represents a ASCII table. The table cells are contained
 * in a matrix of <i>Cell</i> objects, called <i>rows</i>.
 * </p>
 * <p>
 * Each row is represented by each <i>Cell</i> array in the matrix.
 * If a row is null, it means that is a delimiter row.
 * </p>
 * <p>
 * If a cell is null, it means that it will act as a cell without content
 * that uses only one column.
 * </p>
 * @author David Marcos Rebolledo
 * @version 0.2.0
 * @see Cell
 */
public class Table {
    public static String CELL_DELI = " | ";
    public static char DELIMITER_CHAR = '-';
    public static char PADDING_CHAR = ' ';
    public static String ROW_HEAD = "| ";
    public static String ROW_TAIL = " |";
    private Cell[][] rows;


    // Constructors
    public Table(){
        this.rows = new Cell[0][];
    }

    public Table(Cell[][] rows) {
        this.rows = rows;
    }

    public Table(Table table){
        this.rows = table.rows;
    }


    // Getters and Setters

    public Cell[][] getRows() {
        return rows;
    }

    public void setRows(Cell[][] rows) {
        this.rows = rows;
    }


    // Methods

    /**
     * This method creates a string that contains the table
     *
     * @return the string of the table
     */
    public String buildTable(){
        if (rows == null || rows.length == 0) return ""; // table doesn't exists
        int[] widths = getWidthsColumns();
        String[][] stringCells = new String[rows.length][];
        /* Create stringCells */
        for (int i = 0; i < rows.length; i++) {
            if (rows[i] == null) continue; // row == null => delimiter row

            stringCells[i] = new String[rows[i].length];
            int actualColumn = 0;
            for (int j = 0; j < rows[i].length; j++) {
                Cell cell = rows[i][j];
                int cellWidth = CELL_DELI.length() * ((cell != null ? cell.getColumns() : 1)-1);
                for (int k = 0; k < (cell != null ? cell.getColumns() : 1); k++)
                    cellWidth += widths[actualColumn+k];
                actualColumn += (cell != null ? cell.getColumns() : 1);
                stringCells[i][j] = (cell != null) ?
                    padString(cell.getContent(), cellWidth, cell.getAlign(),PADDING_CHAR) :
                    padString("", cellWidth, 0,PADDING_CHAR) ;
            }
        }
        /* Join stringCells => stringRows => table */
        String[] stringRows = new String[rows.length];
        for (int i = 0; i < rows.length; i++) {
            if (stringCells[i] == null){ // row == null => delimiter row
                int totalWidth = CELL_DELI.length()*(widths.length - 1);
                for (int width : widths) totalWidth += width;
                totalWidth = Math.max(totalWidth,1);
                stringRows[i] = ROW_HEAD + Character.toString(DELIMITER_CHAR).repeat(totalWidth) + ROW_TAIL;
            } else 
                stringRows[i] = ROW_HEAD + String.join(CELL_DELI, stringCells[i]) + ROW_TAIL;
        }
        return String.join("\n", stringRows);
    }
    
    /**
     * This method sets the align of the cells that belongs to a specific column
     * @param column The index of the column in the table.
     * If the index is smaller than zero or greater than the number of rows,
     * the align is set to the last column.
     * @param align The align type to apply.
     * Smaller than zero means left align.
     * Greater than zero means right align.
     * Equal to zero means center align.
     */
    public void setColumnAlign(int column, int align){
        if (rows == null) return;
        column = (column < 0) || (column > rows.length-1) ?
            rows.length-1 : column;
        for (Cell[] row : rows){
            if (row == null) continue; // row == null => delimiter row
            int actualColumn = 0;
            for (Cell cell : row){
                if (column >= actualColumn && column < actualColumn+cell.getColumns())
                    cell.setAlign(align);
                actualColumn += cell.getColumns();
            }
        }
    }

    /**
     * This method set the minimum width of the cells that belongs to a specific column
     * @param column The index of the column in the table.
     * If the index is smaller than zero or greater than the number of rows,
     * the align is set to the last column. 
     * @param minWidth The minimum width to apply.
     * If it is lesser than zero, it will be replaced by zero.
     */
    public void setColumnMinWidth(int column, int minWidth){
        if (rows == null) return;
        minWidth = minWidth < 0 ? 0 : minWidth; 
        column = (column < 0) || (column > rows.length-1) ?
            rows.length-1 : column;
        for (Cell[] row : rows){
            if (row == null) continue; // row == null => delimiter row
            int actualColumn = 0;
            for (Cell cell : row){
                if (column >= actualColumn && column < actualColumn+cell.getColumns())
                    cell.setMinWidth(minWidth);
                actualColumn += cell.getColumns();
            }
        }
    }

    /**
     * This method adds a row to the table at a specific position
     * @param row The cell array to add 
     * @param index The position where the row will be added.
     * If the index is smaller than zero or greater than the number of rows,
     * the row will be added as the last row
     */
    public void addRow(Cell[] row,int index){
        if (rows == null) rows = new Cell[0][];
        index = (index < 0) || (index > rows.length) ? rows.length : index;
        Cell[][] newRows = new Cell[rows.length + 1][];
        for (int i = 0; i < index; i++)
            newRows[i] = rows[i];
        newRows[index] = row;
        for (int i = index+1; i < newRows.length; i++)
            newRows[i] = rows[i-1];
        rows = newRows;
    }

    /**
     * This method deletes a row to the table at a specific position
     * @param row The cell array to add 
     * @param index The position where the row will be added.
     * If the index is smaller than zero, the last row will be removed
     */
    public void deleteRow(int index){
        if (rows == null || rows.length == 0) return;
        index = index < 0 ? rows.length-1 : index;
        Cell[][] newRows = new Cell[rows.length - 1][];
        for (int i = 0; i < index; i++)
            newRows[i] = rows[i];
        for (int i = index; i < newRows.length; i++)
            newRows[i] = rows[i+1];
        rows = newRows;
    }

    /**
     * This method gets an array of the widths of each column of the table
     * @return an int array with the size of the number of columns in the table
     */
    private int[] getWidthsColumns(){
        if (getColumns() == 0) return new int[0];
        int[] widths = new int[getColumns()];
        int maxCellColumns = getMaxCellColumns();
        /* Process cells in ascendant order of columns that are used */
        for (int cellColumns = 1; cellColumns <= maxCellColumns; cellColumns++){
            for (Cell[] row : rows) {
                if (row == null) continue; // row == null => delimiter row
                int actualColumn = 0;
                for (Cell cell : row) {
                    if ((cell != null ? cell.getColumns() : 1) != cellColumns) continue;
                    int length = (cell != null) ? Math.max(cell.getContent().length(), cell.getMinWidth())
                        - CELL_DELI.length()*(cellColumns-1) :
                        0;

                    int actualWidth = 0;
                    for (int column = actualColumn; column < actualColumn + cellColumns; column++)
                        actualWidth += widths[column];
                    if (actualWidth < length){
                        ArrayList<Integer> increaseColumns = new ArrayList<Integer>();
                        int lengthPerColumn = Math.ceilDiv(length, cellColumns);
                        int lengthPerIncreasedColumn = 0;
                        for (int column = actualColumn; column < actualColumn + cellColumns; column++)
                            if (widths[column] < lengthPerColumn) increaseColumns.add(column);
                            else lengthPerIncreasedColumn += widths[column];
                        
                        lengthPerIncreasedColumn = Math.ceilDiv(
                            length - lengthPerIncreasedColumn,increaseColumns.size());    

                        for (int column : increaseColumns)
                            widths[column] = lengthPerIncreasedColumn;
                    }
                    actualColumn += cellColumns;
                }
            }
        }



        return widths;
    }

    /**
     * This method gets the number of columns in the table
     * @return an int with the number of columns in the table
     */
    private int getColumns(){
        if (rows == null || rows.length == 0) return 0; // void table
        int columns = 0;
        for (Cell[] row : rows) {
            if (row == null) continue; // row == null => delimiter row
            int actualColumns = 0;
            for (Cell cell : row) actualColumns += cell != null ? cell.getColumns() : 1;
            if (actualColumns > columns) columns = actualColumns;
        }
        return columns;
    }

    /**
     * This method gets the number of columns in the table
     * @return an int with the number of columns in the table
     */
    private int getMaxCellColumns(){
        int max = 0;
        for (Cell[] row : rows){
            if (row == null) continue; // row == null => delimiter row
            for (Cell cell : row) max = Math.max(max, (cell != null) ? cell.getColumns() : 1);
        }
        return max;

    }


    public static String padString(String x, int length, int align, char padCh){
        int spaces = length - x.length();
        if (spaces <= 0) return x;
        if (align < 0){ // left align
            return x + Character.toString(padCh).repeat(spaces);
        }
        else if (align > 0){ // right align
            return Character.toString(padCh).repeat(spaces) + x;
        }
        else { // center align
            return Character.toString(padCh).repeat(Math.ceilDiv(spaces,2)) + x + Character.toString(padCh).repeat(Math.floorDiv(spaces,2));
        }
    }
}
