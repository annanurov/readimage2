package readimage2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author Bayram
 */
public class sampleDirectory {

    final private File directory;
    final private String directoryName;
//    File directory;

    /**constructor based on an existing directory
     * @param dir*/
    public sampleDirectory(String dir) {
        this.directoryName = dir;
        this.directory = new File(this.directoryName);
    }

    /**get method for Directory name
     * @return String*/
    public String getDirectoryName() {
        return this.directoryName;
    }
    
    /**lists all files in the directory
     * @return File[]*/
    public File[] getFiles() {
        File [] files = this.directory.listFiles();
        return files;
    }//end of getFiles()
    
    /**lists all files ending with specific suffix in the directory
     * @param extension
     * @return File[]*/
    public File[] getFiles(String extension) {
        File [] files = this.directory.listFiles();
        ArrayList<File> newRes = new ArrayList<>();
        for (File file : files){
            if (file.getName().endsWith(extension))
                newRes.add(file);
        }
        File [] array = new File[newRes.size()];
        newRes.toArray(array);
        return array;
    }//end of getFiles(String ext)

    /**save int[][] into a specified file
     * @param matrix
     * @param fileName
     * @param separator*/
    public void saveMatrix(int[][] matrix, String fileName, String separator){
        File outputFile = new File(fileName);
        try{
            PrintWriter pen = new PrintWriter(outputFile);
            for (int [] line: matrix){
                for(int i : line){
                    pen.print(i);
                    pen.print(separator + " ");
                }
                pen.print("\n");
            }
            pen.close();
        }
        catch(Exception ex){ex.printStackTrace();}
    }//end of saveMatrix(int[][] matrix, String fileName, String separator ){
    
    /**save double[][] into a specified file*/
    public void saveMatrix(double[][] matrix, String fileName, String separator ){
        File outputFile = new File(fileName);
        try{
            PrintWriter pen = new PrintWriter(outputFile);
            for (double [] line: matrix){
                for(double i : line){
                    pen.print(i);
                    pen.print(separator + " ");
                }
                pen.print("\n");
            }
            pen.close();
        }
        catch(Exception ex){ex.printStackTrace();}
    }//end of saveMatrix(double[][] matrix, String fileName, String separator ){
    
    /**apply 2D Fourier Transform on the image files:
     * (1) list image files
     * (2) convert images to double[][]
     * (3) apply FT2D, obtain REAL, IMAG, AMPL arrays
     * (4) normalize REAL, IMAG, AMPL arrays and save to text files
     * (5) extract data as "concentric circles" save to file.
     * (6) prepare extracted data in arff and cluto formats.
     */
    public void FT2D(){
        File [] files = directory.listFiles();
        ImgMod30 im30 = new ImgMod30();
    try{
        
        String REALoutputFileName = directoryName + "\\FT2D-REAL-1.txt";
            File REALoutputFile =  new File(REALoutputFileName);
                PrintWriter REALpen = new PrintWriter(REALoutputFile);
        String IMAGoutputFileName = directoryName + "\\FT2D-IMAG-1.txt";
            File IMAGoutputFile =  new File(IMAGoutputFileName);
                PrintWriter IMAGpen = new PrintWriter(IMAGoutputFile);
        String AMPLoutputFileName = directoryName + "\\FT2D-AMPL-1.txt";
            File AMPLoutputFile =  new File(AMPLoutputFileName);
                PrintWriter AMPLpen = new PrintWriter(AMPLoutputFile);
        int counter = 1;
        for (File file : files){
                BufferedImage image = ImageIO.read(file);
                double [][] inputData  =  makeInputDataforFTT(image, 0);
                double [][] realOut = new double[inputData.length][inputData[0].length];
                double [][] imagOut = new double[inputData.length][inputData[0].length];
                double [][] amplOut = new double[inputData.length][inputData[0].length];
                ///apply FT2D:
                im30.xform2D(inputData, realOut, imagOut, amplOut);

                ///save images:
//                saveAsImage(realOut, "FT2Dimages\\REAL"+file.getName(), image.getType());
//                saveAsImage(imagOut, "FT2Dimages\\IMAG"+file.getName(), image.getType());
//                saveAsImage(amplOut, "FT2Dimages\\AMPL"+file.getName(), image.getType());
                
        ///save data into files:
                ///noralize first:
        int [][] NrealOut = normalizeSpectrum(realOut);
        int [][] NimagOut = normalizeSpectrum(imagOut);
        int [][] NamplOut = normalizeSpectrum(amplOut);
                ///save now:
                for(int i = 0 ; i < amplOut.length; i++){
                    for(int j = 0 ; j < amplOut[0].length; j++){
//                        REALpen.print(realOut[i][j] + " ");
                        REALpen.print(NrealOut[i][j] + " ");
//                        IMAGpen.print(imagOut[i][j] + " ");
                        IMAGpen.print(NimagOut[i][j] + " ");
//                        AMPLpen.print(amplOut[i][j] + " ");
                        AMPLpen.print(NamplOut[i][j] + " ");
                    }
                }//end of for, i
                
                //get the consonant value:
                    String fileName = file.getName();
//                    int a = fileName.indexOf("Syl");
//                    int a = "resizedT".length();
                    int a = fileName.indexOf("-");
                    int b = fileName.indexOf("X");

//                    REALpen.println( fileName.substring(a+3, b) );
                    REALpen.println(fileName.substring(a + 1, b) );
//                    IMAGpen.println( fileName.substring(a+3, b) );
                    IMAGpen.println(fileName.substring(a + 1, b) );
//                    AMPLpen.println( fileName.substring(a+3, b) );
                    AMPLpen.println(fileName.substring(a + 1, b) );
//                REALpen.print("\n");IMAGpen.print("\n");AMPLpen.print("\n");

///end of save as dato to files
        System.out.print (" " + (counter++));
        }//end of for, files
                REALpen.close();
                IMAGpen.close();
                AMPLpen.close();
        }
            catch(Exception ex){ex.printStackTrace();}
    }
    
    /**Prepare image files for FT2D. 
     * @param colorThreshold 
     * Use colortThreshold = 0 for simple extraction. 
     * Set colorThreshold to an int value (1..255) for thresholding.
     */
    
    public static double [][] makeInputDataforFTT(BufferedImage image, int colorThreshold){
        int H = image.getHeight();
        int W = image.getWidth();
        int pixel;
        double[][] res = new double[W][H];
        for (int i = 0; i < W; i++){
            for (int j = 0; j < H; j++){
                pixel= image.getRGB(i, j);
                if (colorThreshold < 1)
                    if(  ( (pixel & 0xFF) + ((pixel >> 8) & 0xFF) + ((pixel >> 16) & 0xFF) )/ 3 < 10)
                        res[i][j] = 1;
                    else 
                        res[i][j] = 0;
                else{
                    if (( (pixel & 0xFF) + ((pixel >> 8) & 0xFF) + ((pixel >> 16) & 0xFF) )<  colorThreshold)
                        res[i][j] = 1;
                    else
                        res[i][j] = 0;
                }
            }//end of for, j
        }//end of for, i
        return res;
    }//end of makeInputDataforFTT(BufferedImage image, int colorTreshold)
    
    /**Normalize returned data*/
    private int[][] normalizeSpectrum(double[][] data){
        int[][] res = new int[data.length][data[0].length];
        double max = data[0][0];    
        double min = data[0][0];
        int newWidth = data.length; 
        int newHeight = data[0].length;
        for(int i = 0; i < newWidth; i ++){
            for(int j = 0 ; j < newHeight; j ++){
                if (max < data[i][j]) max = data[i][j];
                if (min > data[i][j]) min = data[i][j];
            }//end of for, j
        }//end of for, i
        double range = max - min;
//        System.out.println(range);
        if(range <= 0) System.exit(0);
        for(int i = 0; i < newWidth; i ++){
            for(int j = 0 ; j < newHeight; j ++){
                res[i][j] =  (int)(255.0 * (max - data[i][j]) / range);//this will give only blue channel
            }//end of for, j
        }//end of for, i
        
        return res;
    }//end of normalizeSpectrum

    
}
