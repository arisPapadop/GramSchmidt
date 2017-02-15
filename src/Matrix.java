import java.util.ArrayList;
import java.util.List;

/*
 Class representing a matrix in R^(rows)x(columns). Both rows and columns are indexed from 0.
 */
public class Matrix {

    private int rows;
    private int columns;
    private List<Vector> entries;

    /*
    Both constructors construct a matrix given a list of column vectors, checking that the dimension of each column
    is the same as that of the others. The main difference is in the format of the arguments.
     */
    public Matrix(Vector... columns) throws IllegalArgumentException {

        if(columns.length > 0)
            this.rows = columns[0].getRows();
        else
            throw new IllegalArgumentException("No columns given to matrix.");

        this.entries = new ArrayList<>();
        for(Vector column : columns) {
            if(column.getRows() != this.rows)
                throw new IllegalArgumentException("Columns of matrix have varying length.");

            entries.add(column);
            this.columns++;
        }
    }

    public Matrix(List<Vector> columns) throws IllegalArgumentException {

        if(columns.size() > 0)
            this.rows = columns.get(1).getRows();
        else
            throw new IllegalArgumentException("No columns given to matrix.");

        this.entries = new ArrayList<>();
        for(Vector column : columns) {
            if(column.getRows() != this.rows)
                throw new IllegalArgumentException("Columns of matrix have varying length.");

            entries.add(column);
            this.columns++;
        }
    }

    /*
    Constructs the identity in R^(nxn)
     */
    public Matrix(int n) throws IllegalArgumentException {
        this.rows    = n;
        this.columns = n;
        this.entries = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            Vector v = new Vector(n);
            v.setEntryAt(i, 1.0);
            entries.add(v);
        }
    }

    /*
    Returns the number of rows of the matrix.
     */
    public int getRows() {
        return rows;
    }

    /*
    Returns the number of columns of the matrix.
     */
    public int getColumns() {
        return columns;
    }

    /*
    Returns a copy of the nth row of the matrix, if the index is valid.
     */
    public Vector getRowAt(int n) {
        List<Double> row = new ArrayList<>();

        if(!(0 <= n && n < rows))
            throw new IndexOutOfBoundsException();

        for (Vector column: entries) {
            row.add(column.getEntryAt(n));
        }

        return new Vector(row);
    }

    public Vector getColumnAt(int n) {

        if(0 <= n && n < columns) {
            return this.entries.get(n);
        }
        throw new ArrayIndexOutOfBoundsException();
    }

    /*
    Returns the row, column entry of the matrix.
     */
    public Double getEntryAt(int row, int column) throws IndexOutOfBoundsException {

        if(0 <= column && column < columns)
            return entries.get(column).getEntryAt(row);
        else
            throw new IndexOutOfBoundsException("Invalid column.");
    }

    /*
    Returns a new matrix which is the result of multiplying this matrix by the matrix m, on the right, i.e.
                               matrixMultiplyRight(Matrix m) = this*m
     if the dimensions are correct.
     */
    public Matrix matrixMultiplyRight(Matrix m) throws IllegalArgumentException {
        if(this.columns != m.getRows())
            throw new IllegalArgumentException("Invalid dimensions");

        List<Vector> entries = new ArrayList<>();
        for(int j = 0; j < m.getColumns(); j++) {
            List<Double> column = new ArrayList<>();
            for(int i = 0; i < rows; i++) {
                double entry = this.getRowAt(i).innerProduct(m.getColumnAt(j));
                column.add(entry);
            }
            entries.add(new Vector(column));
        }
        return new Matrix(entries);
    }

    public Matrix addMatrix(Matrix m) throws IllegalArgumentException {
        if(this.columns != m.getColumns() || this.rows != m.getRows())
            throw new IllegalArgumentException("Invalid dimensions");
        List<Vector> newColumns = new ArrayList<>();

        for(int i = 0; i < this.columns; i++) {
            Vector c = this.getColumnAt(i).add(m.getColumnAt(i));
            newColumns.add(c);
        }
        return new Matrix(newColumns);
    }

    public Matrix scalarProduct(Double s) throws IllegalArgumentException {
        List<Vector> newColumns = new ArrayList<>();

        for(int i = 0; i < this.columns; i++) {
            Vector c = this.getColumnAt(i).scalarProduct(s);
            newColumns.add(c);
        }
        return new Matrix(newColumns);
    }

    /*
    Returns a new (columns)x(rows) matrix which is the transpose of this matrix.
     */
    public Matrix transpose() {

        List<Vector> newColumns = new ArrayList<>();
        for(int i = 0; i < rows; i++) {
            newColumns.add(this.getRowAt(i));
        }

        return new Matrix(newColumns);
    }


    public void printMatrix() {
        for(int i = 0; i < rows; i++) {
            this.getRowAt(i).printRowEntries();
        }
    }

}
