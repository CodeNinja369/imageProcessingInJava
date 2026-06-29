import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Field;
import javax.imageio.ImageIO;

public class processor {
    private String fileName;
    private String fileOutName;
    private int xSize;
    private int ySize;
    private BufferedImage image;
    private pixel[] pArray;
    private int[] fArray;
    public processor(String fname, String fOut, int x,int y, BufferedImage e){
        this.fileName = fname;
        this.fileOutName = fOut;
        this.xSize = x;
        this.ySize = y;
        this.image = e;
        read();
        this.fArray = getFarray();
        this.pArray = getPArray();
    }
    //read and write to image files
    public void read() {
        try {
            image = ImageIO.read(new File(this.fileName));
        } catch (Exception e) {
            System.err.println("read: " + e.getMessage());
        }
    }

    public void write() {
        try {
            File output = new File(this.fileOutName);
            ImageIO.write(this.image, "jpg", output);
        } catch (Exception e) {
            System.err.println("write: " + e.getMessage());
        }
    }
    //create array of pixel objects in an image
    private pixel[] getPArray(){
        int d = 0;
        pixel[] out = new pixel[this.xSize*this.ySize];
        for(int i = 0; i<this.fArray.length; i++){
            out[i] = new pixel(this.fArray[i]);
        }
        
        return out;
    }
    //created flattened array
    private int[] getFarray(){
        int d = 0;
        int[] out = new int[this.xSize*this.ySize];
        for(int x =0; x<this.xSize; x++){
            for(int y = 0; y<this.ySize; y++){
                out[d]= this.image.getRGB(x, y);
                d++;
            }
        }
        return out;
    }
    //quicksort
    private int partition(Field f, int low, int high) {
        int i = low - 1;
        try {
            double pivot = (double) f.get(pArray[high]);

            for (int j = low; j <= high - 1; j++) {
                if ((double) f.get(pArray[j]) < pivot) {
                    i++;
                    swap(i, j);
                }
            }
            swap(i + 1, high);

        } catch (IllegalAccessException e) {
            System.err.println("partition: cannot access field — " + e.getMessage());
        }
        return i + 1;
    }

    private void swap(int i, int j) {
        pixel temp = this.pArray[i];
        pArray[i] = pArray[j];
        pArray[j] = temp;
    }

    public void quickSort(String att, int low, int high) {
        try {
            Field field = pixel.class.getField(att);
            quickSortInternal(field, low, high);
        } catch (NoSuchFieldException e) {
            System.err.println("quickSort: no field named '" + att + "' on pixel");
        }
    }

    private void quickSortInternal(Field field, int low, int high) {
        if (low < high) {
            int pi = partition(field, low, high);
            quickSortInternal(field, low, pi - 1);
            quickSortInternal(field, pi + 1, high);
        }
    }
    //update image file
    private void update(){
        int e = 0;
        for (int x = 0; x < this.xSize; x++) {
            for (int y = 0; y < this.ySize; y++) {
                this.image.setRGB(x, y, this.pArray[e].pixelC);
                e++;
            }
        }
    }
    //sorts base 255 colour values using quicksort
    public void sort1() {
        quickSort("pixelC", 0, pArray.length - 1);
        update();
    }
    //sorting by brightness calculated for differences in human vision
    public void sort2(){
        quickSort("pixelB", 0, this.pArray.length-1);
        update();
    }


//sorted by red value
    public void sort2_2(){
        quickSort("red", 0, this.pArray.length-1);
        update();
    }
    //sorted by green
    public void sort2_3(){
        quickSort("green", 0, this.pArray.length-1);
        update();
    }
    //sorted by blue
    public void sort2_4(){
        quickSort("blue", 0, this.pArray.length-1);
        update();
    }

    //sorts into checkerboard-like pattern of alternating pixels with maximised contrast
    public void sort3(){
        for(int index = 0; index < pArray.length - 1; index++){
            pixel c1 = pArray[index];
            pixel pstore = c1;
            int ppos = 0; 
            double contstore = 0;
            for(int i = index+1; i < pArray.length; i++){
                double contrast = 0;
                if(c1.pixelB > pArray[i].pixelB){
                    contrast = (c1.pixelB + 0.05) / (pArray[i].pixelB + 0.05);
                }
                if(pArray[i].pixelB > c1.pixelB){
                    contrast = (pArray[i].pixelB + 0.05) / (c1.pixelB + 0.05);
                }
                if(contstore < contrast){
                    contstore = contrast;
                    pstore = pArray[i];
                    ppos = i;
                }
            }
            pixel np = pArray[index+1];
            pArray[index+1] = pstore;
            pArray[ppos] = np;
        }
        int e = 0;
        update();
    }
    //creates brightness map of image
    public int[] createMap() {
        int[] map = new int[pArray.length];
    
        for (int i = 0; i < pArray.length; i++) {
            int rank = 0;
            for (int j = 0; j < pArray.length-1; j++) {
                if (pArray[j].pixelB <= pArray[i].pixelB) {
                    rank++;
                }
            }
            map[i] = rank;
        }
    
        return map;
    }
    //recolours image using brightness map, and image of equal size sorted by brightness
    public void obamaAlg(processor pp){
        int[] m = createMap();
        for(int i =0; i<pArray.length-1; i++){
            this.pArray[i] = pp.pArray[m[i]];
        }
        update();
    }

    
}
