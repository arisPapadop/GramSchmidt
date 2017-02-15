import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.ArrayList;
import java.util.List;


public class GramSchmidt {

    /*
    Static method implementing the classical Gram-Schmidt algorithm on the list vectors.
    Pre-condition: the vectors in the argument are linearly independent.
    Post-condition: the vectors returned are orthonormal.
     */
    public static List<Vector> classicalGramSchmidt(List<Vector> vectors){

        //Holds the final orthonormal vectors, i.e. the qi's.
        List<Vector> orthonormals = new ArrayList<>();

        // v1 = a1 and q1 = v1/||v1||
        orthonormals.add(vectors.get(0).normalise());
        // for i = 2 to n, but as Java's native lists are zero-indexed the implementation becomes easier
        // by using different indices rather than shifting the entire list.
        for (int i = 1; i < vectors.size(); i++) {
            // Calculate the sum of contributions in all of the qj directions in ai.
            // The contribution is initialised as the zero vector in R^m
            Vector ai = vectors.get(i);
            Vector contribution = new Vector(vectors.get(0).getRows());
            for (int j = 0; j <= i-1; j++) {
                Vector qj = orthonormals.get(j);
                Vector proj  = qj.scalarProduct(ai.innerProduct(qj));
                contribution = contribution.add(proj);
            }
            // vi = ai - sumOfContributions
            Vector vi = ai.add(contribution.scalarProduct(-1.0));
            // qi = vi/||vi||
            orthonormals.add(vi.normalise());
        }
        return orthonormals;
    }

    /*
    Static method implementing the modified Gram-Schmidt algorithm on the list vectors.
    Pre and post conditions are the same as for the Classical Gram-Schmidt.
     */
    public static List<Vector> modifiedGramSchmidt(List<Vector> vectors){

        //Holds the final orthonormal vectors, i.e. the qi's.
        List<Vector> orthonormals = new ArrayList<>();
        //Holds each iteration of the vi's.
        List<Vector> temporaries  = new ArrayList<>();

        //for i = 1 to n
        for(int i = 0; i < vectors.size(); i++) {
            //vi = ai
            temporaries.add(vectors.get(i));
        }
        //q1 = v1/||v1||
        Vector q1 = temporaries.get(0).normalise();
        orthonormals.add(q1);
        //for i = 2 to n
        for(int i = 1; i < vectors.size(); i++) {
            //for j = i to n
            for (int j = i; j < vectors.size(); j++) {
                //temp is vj(i-1)
                Vector temp = temporaries.get(j);
                //prod is <vj(i-1),q_(i-1)>
                double prod = temp.innerProduct(orthonormals.get(i-1));
                //vj(i) = vj(i-1) - <vj(i-1),q_(i-1)>q_(i-1)
                temporaries.set(j, temp.add(orthonormals.get(i-1).scalarProduct(-1*prod)));
            }
            //qi = vi(i)/||vi(i)||
            orthonormals.add(temporaries.get(i).normalise());
        }
        return orthonormals;
    }


    /*
    Main method which applies the algorithm to each of the sets of vectors given in the spec
    and then prints the result of the identity minus the matrix Q given by the vectors produced
    in each algorithm multiplied by its transpose.
     */
    public static void main(String[] args) {
        double[] ks = {Math.pow(10.0,-1.0), Math.pow(10.0,-5.0), Math.pow(10.0,-10.0)};
        List<Vector> vectors = new ArrayList<>();
        Matrix id = new Matrix(4);

        for(int i = 0; i < ks.length; i++) {

            double k = ks[i];

            Vector a1 = new Vector(k, 0.0, 0.0, 0.0, 1.0);
            Vector a2 = new Vector(0.0, k, 0.0, 0.0, 1.0);
            Vector a3 = new Vector(0.0, 0.0, k, 0.0, 1.0);
            Vector a4 = new Vector(0.0, 0.0, 0.0, k, 1.0);

            vectors.clear();

            vectors.add(a1);
            vectors.add(a2);
            vectors.add(a3);
            vectors.add(a4);

            List<Vector> orthsCGS = classicalGramSchmidt(vectors);
            List<Vector> orthsMGS = modifiedGramSchmidt(vectors);

            System.out.println("Classical Gram-Schmidt for k = " + k);
            Matrix q1  = new Matrix(orthsCGS);
            Matrix q1_T = q1.transpose();
            id.addMatrix(q1_T.matrixMultiplyRight(q1).scalarProduct(-1.0)).printMatrix();

            System.out.println();

            System.out.println("ModifiedGram-Schmidt for k = " + k);
            Matrix q2  = new Matrix(orthsMGS);
            Matrix q2_T = q2.transpose();
            id.addMatrix(q2_T.matrixMultiplyRight(q2).scalarProduct(-1.0)).printMatrix();

            System.out.println();
            System.out.println();
        }
    }

}
