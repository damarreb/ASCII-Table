package dev.damarreb.asciitable.example;
import dev.damarreb.asciitable.*;

/**
 * <p>
 * This class contains some examples of the library use.
 * </p>
 * @author David Marcos Rebolledo
 * @version 0.1.0
 * @see Table
 * @see Cell
 */
public class Example {
    public static Table voidTable = new Table();
    public static Table rowDelimiterTable = new Table(new Cell[][] {null, null});
    
    public static Table nullCellTable = new Table( new Cell[][] {
        {new Cell("Column 1"),new Cell("Column 2 & 3",2)},
        {new Cell("Cell"),null,new Cell("Other Cell")},
        {new Cell("One apple"),new Cell("Two apples"),null},
        {null,new Cell("This is a comment :)"),null}
    });
    
    public static Table table1 = new Table(new Cell[][] {
        {new Cell("Client", 2), new Cell("Nº 43", 1)},
        {new Cell("Potatoes"), new Cell("30 kg"), new Cell("4.54 €")},
        {new Cell("1L Water Bottle"),new Cell("3 u."),new Cell("5.42 €")}
    });
    
    public static Table table2 = new Table(new Cell[][]{
        { new Cell("Product"), new Cell("Price"), new Cell("Amount",1,1,20)},
        null,
        { new Cell("Apples"), new Cell("1.00"), new Cell("50")},
        { new Cell("Banana"), new Cell("0.50"), new Cell("100")},
        { new Cell("Total products",2), new Cell("150")},
        { new Cell("Comment: New stock have been added this week",3)}
    });

    public static void testAlign(){
        Table table = new Table(table2);
        table.setColumnAlign(1, 0);
        table.setColumnAlign(0, -1);
        table.setColumnAlign(2, 1);
        System.out.println(table.buildTable());
    }

    public static void testAddDeleteRow(){
        Table table = new Table(table2);
        table.addRow(null, -1);
        System.out.println(table.buildTable()+"\n");
        table.addRow(new Cell[] {
            new Cell("This is a new row (between apple and banana)",3)
        }, 3);
        System.out.println(table.buildTable()+"\n");
        table.deleteRow(0);
        System.out.println(table.buildTable()+"\n");
        table.deleteRow(-1);
        System.out.println(table.buildTable()+"\n");
    }
    
    public static void testVoidTable(){
        System.out.println("Before printing void table");
        System.out.println(voidTable.buildTable());
        System.out.println("After printing void table");
    }

    public static void testDelimiterRowTable(){
        System.out.println("Before printing delimiter row table");
        System.out.println(rowDelimiterTable.buildTable());
        System.out.println("After printing delimiter row table");
    }

    public static void testNullCellTable(){
        System.out.println("Before printing null cell table");
        System.out.println(nullCellTable.buildTable());
        System.out.println("After printing null cell table");
    }

}