package dev.damarreb.asciitable;
/**
 * <p>
 * This class represents a cell of an ASCII table.
 * A cell is defined by its content,
 * the number of columns it uses,
 * the minimum width it needs and
 * the text align.
 *</p>
 * @author David Marcos Rebolledo
 * @version 0.1.0
 * @see Table
 */
public class Cell {

    private String content;
    private int columns = 1;
    private int align = -1;
    private int minWidth = 0;
    
    // Constructors
    public Cell(String content) {
        this.content = content;
    }

    public Cell(String content, int columns) {
        this.content = content;
        this.columns = columns>0 ? columns : 1;
    }

    public Cell(String content, int columns, int align) {
        this.content = content;
        this.columns = columns>0 ? columns : 1;
        this.align = align;
    }

    public Cell(String content, int columns, int align, int minWidth) {
        this.content = content;
        this.columns = columns>0 ? columns : 1;
        this.align = align;
        this.minWidth = minWidth;
    }

    // Getters and Setters
    public String getContent() {
        return content;
    }

    public int getColumns() {
        return columns;
    }

    public int getAlign() {
        return align;
    }

    public int getMinWidth() {
        return minWidth;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public void setColumns(int columns) {
        this.columns = columns>0 ? columns : 1;
    }

    public void setAlign(int align) {
        this.align = align;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }    
    
}
