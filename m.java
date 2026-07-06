import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;
public class m{
    public static void main(String[]args){
        int w = 205;
        int h = 246;
        BufferedImage demoIm = null;
        BufferedImage mapIm = null;
        String inp = "";
        Path sourcePath = Paths.get("demo1.jpg");
        Path destinationPath = Paths.get("io.jpg");

        try {
            Files.copy(sourcePath, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File duplicated successfully!");
        } catch (IOException e) {
            System.err.println("Error occurred while duplicating the file: " + e.getMessage());
        }
        String title = "d1..";
        Scanner scnr = new Scanner(System.in);
        processor demo = new processor("io.jpg", "io.jpg", w, h, demoIm);
        processor mappa1 = new processor("demo3.jpg", "io.jpg", w, h, mapIm);
        processor mappa2 = new processor("demo4.jpg", "io.jpg", w, h, mapIm);
        int count = 0;
        while(!inp.equals("e")){
            
            System.out.print("input number:");
            inp = scnr.nextLine();
            
            if(!(inp.equals(""))){
                demo.read();
                if(inp.equals("s1")){
                    demo.sort1();
                    title+="s1";
                }
                if(inp.equals("s2")){
                    demo.sort2();
                    title+="s2";
                }
                if(inp.equals("s3")){
                    demo.sort3();
                    title+="s3";
                }
                if(inp.equals("-r")){
                    demo.noRed();
                    title+="-r";
                }
                if(inp.equals("-g")){
                    demo.noGreen();
                    title+="-g";
                }
                if(inp.equals("-b")){
                    demo.noBlue();
                    title+="-b";
                }
                if(inp.equals("-gb")){
                    demo.onlyRed();
                    title+="-gb";
                }
                if(inp.equals("-rb")){
                    demo.onlyGreen();
                    title+="-rb";
                }
                if(inp.equals("-rg")){
                    demo.onlyBlue();
                    title+="-rg";
                }
                if(inp.equals("w")){
                    demo.white();
                    title+="w";
                }
                if(inp.equals("-w1")){
                    demo.noWhite1();
                    title+="-w1";
                }
                if(inp.equals("-w2")){
                    demo.noWhite2();
                    title+="-w2";
                }
                if(inp.equals("b")){
                    demo.black();
                    title+="b";
                }
                if(inp.equals("a1")){
                    demo.average1();
                    title+="a1";
                }

                /*if(inp.equals("a2")){
                    demo.average2();
                }*/
                if(inp.equals("O1")){
                    mappa1.sort2();
                    demo.obamaAlg(mappa1);
                    title+="O1";
                }
                if(inp.equals("O2")){
                    mappa2.sort2();
                    demo.obamaAlg(mappa2);
                    title+="O2";
                }
                if(inp.equals("l")){
                    demo.left();
                    title+="l";
                }
                demo.write();
               if(!inp.equals("e")){
                title+=", ";
            } 
            }
            
        }
        demo.write();
        new File("io.jpg").renameTo(new File(title + ".jpg"));
        
        
    }
    

}