import java.awt.image.BufferedImage;
import java.util.Scanner;
public class m{
    public static void main(String[]args){
        int w = 205;
        int h = 246;
        BufferedImage demoIm = null;
        BufferedImage mapIm = null;
        String inp = "";
        Scanner scnr = new Scanner(System.in);
        processor demo = new processor("io.jpg", "io.jpg", w, h, demoIm);
        while(inp!="e"){
            System.out.print("input number:");
            inp = scnr.nextLine();
            if(!(inp.equals(""))){
                demo.read();
                if(inp.equals("s1")){
                    demo.sort1();
                }
                if(inp.equals("s2")){
                    demo.sort2();
                }
                if(inp.equals("s3")){
                    demo.sort3();
                }
                if(inp.equals("-r")){
                    demo.noRed();
                }
                if(inp.equals("-g")){
                    demo.noGreen();
                }
                if(inp.equals("-b")){
                    demo.noBlue();
                }
                if(inp.equals("-gb")){
                    demo.onlyRed();
                }
                if(inp.equals("-rb")){
                    demo.onlyGreen();
                }
                if(inp.equals("-rg")){
                    demo.onlyBlue();
                }
                if(inp.equals("a1")){
                    demo.average1();
                }
                /*if(inp.equals("a2")){
                    demo.average2();
                }*/

                demo.write();
            }
            
        }
        
        
    }
    

}