import java.util.ArrayList;
import java.util.List;

/*
Class for representing Vectors in R^(rows). Vectors are indexed starting from 0
 */
public class Vector {
    private int rows;
    private List<Double> entries;

    /*
    Construct a vector given each of the entries individually.
     */
    public Vector(Double... entries) {

        this.rows = 0;
        this.entries = new ArrayList<>();

        for (Double entry : entries) {
            this.entries.add(entry);
            this.rows++;
        }
    }

    /*
    Construct a vector given the entries as a list.
     */
    public Vector(List<Double> entries) {

        this.rows = entries.size();
        this.entries = entries;
    }

    /*
    Constructs the zero vector in R^n
     */
    public Vector (int rows) {
        this.rows = rows;
        this.entries = new ArrayList<>();
        for(int i = 0; i<rows; i++) {
            entries.add(0.0);
        }
    }
    /*
    Get the number of rows.
     */
    public int getRows() {
        return rows;
    }

    /*
    Get the nth entry, indexed from 1.
     */
    public Double getEntryAt(int n) throws IndexOutOfBoundsException {
        if (0 <=n && n < rows )
            return entries.get(n);
        else
            throw new IndexOutOfBoundsException("Invalid row.");
    }

    /*
    Sets the nth entry of the vector to k
     */
    public void setEntryAt(int n, double k) throws IndexOutOfBoundsException {
        if (0 <=n && n < rows )
           entries.set(n, k);
        else
            throw new IndexOutOfBoundsException("Invalid row.");
    }

    /*
    Return the result of the inner product of this with v, if the dimensions are valid.
     */
    public Double innerProduct(Vector v) throws IllegalArgumentException {
        if (v.getRows() !=  this.rows)
            throw new IllegalArgumentException("Cannot multiply a vector in R^"+ this.rows+
                                               "with a vector in R^"+v.getRows());

        double innerP = 0;

        for (int i = 0; i < rows ; i ++ ) {
            innerP += this.entries.get(i)*v.getEntryAt(i);
        }

        return innerP;
    }

    /*
    Returns a new vector which is the result of multiplying this vector by s.
     */
    public Vector scalarProduct(Double s) {
        List<Double> newEntries = new ArrayList<>();

        for (Double entry: this.entries) {
            newEntries.add(s*entry);
        }
        return new Vector(newEntries);
    }

    public Vector add (Vector v) {
        if (v.getRows() !=  this.rows)
            throw new IllegalArgumentException("Cannot add a vector in R^"+ this.rows+
                    "to a vector in R^"+v.getRows());

        List<Double> newEntries = new ArrayList<>();

        for (int i = 0; i < rows ; i ++ ) {
            newEntries.add(this.entries.get(i)+ v.getEntryAt(i));
        }

        return new Vector(newEntries);
    }

    /*
    Returns the norm of the vector, i.e. (<v,v>)^(1/2)
     */
    public Double getNorm() {
        return Math.sqrt(innerProduct(this));
    }

    /*
    Returns a new unit vector in the direction of this vector.
     */
    public Vector normalise() {
        return scalarProduct(1/this.getNorm());
    }

    public void printColumnEntries() {
        for(Double entry : entries) {
            System.out.println(entry + "\t");
        }
    }

    public void printRowEntries() {
        int maxDigits = 24;
        for(Double entry : entries) {
            int spaces = maxDigits - digits(entry) + 2;
            System.out.print(entry + padRight(entry, spaces));
        }
        System.out.println();
    }

    /*
    Pretty print functions.
    */
    private String padRight(Double entry, int spaces) {
        return String.format("%1$-"+ spaces + "s", "");
    }

    private int digits(Double entry) {
        return  entry.toString().length();
    }
}
