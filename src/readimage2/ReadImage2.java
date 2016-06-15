
package readimage2;

/**
 *
 * @author Bayram
 */
public class ReadImage2 {
    public static void main(String[] args) {
        // TODO code application logic here
        sampleDirectory bukvy = new sampleDirectory("s1a");
//        bukvy.FT2D();

        String[] dirs = new String []{
//            "s1b",
//            "s2a","s2b","s3a","s3b",
//            "s4a","s4b",
            "s5a","s5b"
//                ,
//            "s6a","s6b","s7a","s7b",
//            "s8a","s8b","s9a","s9b"
        };   
        for(String d : dirs){
            bukvy = new sampleDirectory(d);
            bukvy.FT2D();
        }

    }
    
}
