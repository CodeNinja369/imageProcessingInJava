import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
public class m{
    public static double removalInput(){
        Scanner scnr = new Scanner(System.in);
        System.out.print("Please enter percentage to remove:");
        double m = scnr.nextDouble();
        scnr.close();
        return m;
    }
    public static int movementInput(){
        Scanner scnr = new Scanner(System.in);
        System.out.print("input number of pixels:");
        int inum = scnr.nextInt();
        scnr.nextLine();
        scnr.close();
        return inum;
    }
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

        Map<String, Runnable> commands = new HashMap<>();
        //sorting methods
        commands.put("s1",()->demo.sort1()); //sorts by base 255 single digit colour values
        commands.put("s2", ()->demo.sort2());//sorts on brightness value
        commands.put("s3", ()->demo.sort3());//sorts in checkerboard pattern using relative contrast
        //colour based methods
        commands.put("-r", ()->demo.minusRed(removalInput()));       //removes all red
        commands.put("-g", ()->demo.minusGreen(removalInput()));     //removes all green
        commands.put("-b", ()->demo.minusBlue(removalInput()));      //removes all blue
        commands.put("-gb", ()->demo.minusGreenBlue(removalInput()));//removes all green and blue
        commands.put("-rg", ()->demo.minusRedGreen(removalInput())); //removes all red and green
        commands.put("-rb", ()->demo.minusRedBlue(removalInput()));  //removes all red and blue
        commands.put("-w",()->demo.noWhite1());                      //changes rgb for each pixel to (rgb % minimum rgb value)
        commands.put("-w2",()->demo.noWhite2());                     //minuses minimum rgb value from each rgb value
        commands.put("w",()->demo.white());                          //rgb all set to maximum rgb value
        commands.put("b",()->demo.black());                          //rgb all set to minimum rgb value
        commands.put("m",()->demo.maximum());                        //changes highest rgb values to 255
        commands.put("m2",()->demo.maxornull());                     //changes highest rgb values to 255 and all others to 0
        commands.put("i",()->demo.invert());                         //minuses all rgb values from 255
        commands.put("i2",()->demo.invert2());                       //swaps highest rgb value with lowest rgb value
        commands.put("p",()->demo.pastelise());                      //finds difference between max rgb and 255. adds difference to all rgb values
        commands.put("t",()->demo.tear());                           //finds average rgb of every 3 pixels. sets each pixel to average r,g, or b, all others set to 0
        commands.put("a",()->demo.average1());                       //sets the colour of each column to the average colour of that column
        commands.put("a2",()->demo.average2());                      //sets every pixel to the average colour of the image

        //Obama: sets colour of image to colour pallete of another of equal size
        commands.put("O1",()->demo.obamaAlg(mappa1));              
        commands.put("O2",()->demo.obamaAlg(mappa2));

        //wraparound movement: left, right, up, down
        commands.put("L",()->demo.left(movementInput()));
        commands.put("R",()->demo.right(movementInput()));
        commands.put("U",()->demo.up(movementInput()));
        commands.put("D",()->demo.down(movementInput()));

        //mirror image positional
        commands.put("fx",()->demo.flipx());  //flips the image in the x axis 
        commands.put("fx2",()->demo.flipx2());//flips the two halves of the image in the x axis in opposite directions
        commands.put("fy",()->demo.flipy());  //flips the image in the y axis
        commands.put("fy2",()->demo.flipy2());//flips the two halves of the image in the y axis in opposite directions
        
        while(!inp.equals("e")){
            demo.read();
            System.out.print("input number:");
            inp = scnr.nextLine();
            commands.getOrDefault(inp, () -> System.out.println("Unknown")).run();
            demo.write();
        }
        demo.write();
        if (title.endsWith(", ")) {
            title = title.substring(0, title.length() - 2);
        }
        new File("io.jpg").renameTo(new File(title + ".jpg"));
        scnr.close();
    }
}