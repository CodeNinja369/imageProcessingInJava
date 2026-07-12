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
        this.pArray = getParray();
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
   private pixel[] getParray(){
        pixel[] out = new pixel[this.xSize*this.ySize];
        for(int i = 0; i<this.fArray.length; i++){
            // fArray index i == x*ySize + y, so:
            int px = i / this.ySize;
            int py = i % this.ySize;
            out[i] = new pixel(this.fArray[i], px, py);
        }
        return out;
    }

    public void updatePP(){
        pixel[] newArr = new pixel[xSize*ySize];
        for(int i = 0; i<xSize*ySize; i++){
            int idx = this.pArray[i].px * ySize + this.pArray[i].py;
            if(newArr[idx] == null){
                newArr[idx] = this.pArray[i];
            }
        }
        for(int i = 0; i<newArr.length; i++){
            if(newArr[i] == null){
                newArr[i] = this.pArray[i]; // no pixel mapped here — keep the original
            }
        }
        this.pArray = newArr;
        update();
    }
    //created flattened array
    private int [] getFarray(){
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
            double pivot = Double.parseDouble(f.get(pArray[high]).toString());
 
            for (int j = low; j <= high - 1; j++) {
                if (Double.parseDouble(f.get(pArray[j]).toString()) < pivot) {
                    i++;
                    swap(i, j, this.pArray);
                }
            }
            swap(i + 1, high, this.pArray);
 
        } catch (IllegalAccessException e) {
            System.err.println("partition: cannot access field — " + e.getMessage());
        }
        return i + 1;
    }
 
    private void swap(int i, int j, Object[] arr) {
        Object temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    private void swapint(int i, int j, int[] arr) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
    public int[][] createMap() {
        
        int[][] map = new int[this.xSize][this.ySize];
 
        for (int i = 0; i < xSize; i++) {
            int rank = 0;
            for (int j = 0; j < ySize - 1; j++) {
                int idx = i * ySize + j;
                if (pArray[idx].pixelB <= pArray[idx + 1].pixelB) {
                    rank++;
                    map[i][j] = rank;
                }
            }
            
        }
    
        return map;
    }
    //recolours image using brightness map, and image of equal size sorted by brightness
    public void obamaAlg(processor pp) {
        int[][] m = createMap();
        
 
        for (int i = 0; i < xSize-1 ; i++) {
            for(int j = 0; j<ySize-1; j++){
                int idx = i * ySize + j;
                this.pArray[idx] = pp.pArray[idx];
            }
        }
        update();
    }
    //remove Red, Green or Blue, or any combination of two
    public void onlyRed(){
        for(int i =0; i<pArray.length; i++){
            pArray[i].green=0;
            pArray[i].blue=0;
            pArray[i].updateC();
        }
        update();
    }
    public void onlyBlue(){
        for(int i =0; i<pArray.length; i++){
            pArray[i].red=0;
            pArray[i].green=0;
            pArray[i].updateC();
        }
        update();
    }
    public void onlyGreen(){
        for(int i =0; i<pArray.length; i++){
            pArray[i].red=0;
            pArray[i].blue=0;
            pArray[i].updateC();
        }
        update();
    }
    public void noRed(){
        for(int i =0; i<pArray.length; i++){
            pArray[i].red=0;
            pArray[i].updateC();
        }
        update();
    }
    public void noGreen(){
        for(int i =0; i<pArray.length; i++){
            pArray[i].green=0;
            pArray[i].updateC();
        }
        update();
    }
    public void noBlue(){
        for(int i =0; i<pArray.length; i++){
            pArray[i].blue=0;
            pArray[i].updateC();
        }
        update();
    }
    public void noWhite1(){
        for(int i = 0; i < pArray.length; i++){
            int min = Math.min(pArray[i].red, Math.min(pArray[i].green, pArray[i].blue));
            if(min>0){
                pArray[i].red   = pArray[i].red%min;
                pArray[i].green = pArray[i].green%min;
                pArray[i].blue  = pArray[i].blue%min;
                
            }
            pArray[i].updateC();
            
        }
        update();
    }
    public void noWhite2(){
        for(int i = 0; i < pArray.length; i++){
            int min = Math.min(pArray[i].red, Math.min(pArray[i].green, pArray[i].blue));
            if(min>0){
                pArray[i].red   = pArray[i].red-min;
                pArray[i].green = pArray[i].green-min;
                pArray[i].blue  = pArray[i].blue-min;
                
            }
            pArray[i].updateC();
            
        }
        update();
    }
    public void white(){
        for(int i = 0; i < pArray.length; i++){
            int max = Math.max(pArray[i].red, Math.max(pArray[i].green, pArray[i].blue));
            pArray[i].red   = max;
            pArray[i].green = max;
            pArray[i].blue  = max;
            pArray[i].updateC();
            
        }
        update();
    }
    public void black(){
        for(int i = 0; i < pArray.length; i++){
            int max = Math.min(pArray[i].red, Math.min(pArray[i].green, pArray[i].blue));
            pArray[i].red   = max;
            pArray[i].green = max;
            pArray[i].blue  = max;
            pArray[i].updateC();
            
        }
        update();
    }
    public void maximum(){
        for(int i = 0; i < pArray.length; i++){
            int max = Math.max(pArray[i].red, Math.max(pArray[i].green, pArray[i].blue));
            if(pArray[i].red==max)
                pArray[i].red   = 255;
            if(pArray[i].green==max)
                pArray[i].green = 255;

            if(pArray[i].blue  ==max)
                pArray[i].blue = 255;
            pArray[i].updateC();
            
        }
        update();
    }
    public void maxornull(){
        for(int i = 0; i < pArray.length; i++){
            int max = Math.max(pArray[i].red, Math.max(pArray[i].green, pArray[i].blue));
            if(pArray[i].red==max)
                pArray[i].red   = 255;
            else pArray[i].red   = 0;
            if(pArray[i].green==max)
                pArray[i].green = 255;
            else pArray[i].green = 0;
            if(pArray[i].blue  ==max)
                pArray[i].blue = 255;
            else pArray[i].blue = 0;
            pArray[i].updateC();
            
        }
        update();
    }

    //changes the values for rgb to be in ascending order
    public void invert(){
        for(pixel p: this.pArray){
            if(p.pixelA[0]>p.pixelA[1]){
                swapint(0, 1, p.pixelA);
            }
            if(p.pixelA[1]>p.pixelA[2]){
                swapint(1, 2, p.pixelA);
            }
            if(p.pixelA[0]>p.pixelA[2]){
                swapint(0, 2, p.pixelA);
            }
            p.updateCA();
        }
        update();
    }
    public void invert2(){
        for(pixel p: this.pArray){
            p.red = 255-p.red;
            p.green = 255-p.green;
            p.blue=255-p.blue;
            p.updateC();
        }
        update();
    }
    //changes a pixel's colour value to an average value based on position
    public void average1(){
        int[] rowAv = new int[3];
        int[][] rAvs = new int[this.ySize][3];
        int e =0;
        for(int i =0; i<this.ySize; i++){
            for(int j = 0; j<this.xSize; j++){
                rowAv[0]+=pArray[e].red;
                rowAv[1]+=pArray[e].green;
                rowAv[2]+=pArray[e].blue;
                e++;
            }
            rAvs[i][0] = rowAv[0]/this.xSize;
            rAvs[i][1] = rowAv[1]/this.xSize;
            rAvs[i][2] = rowAv[2]/this.xSize;
            rowAv = new int[3];
        }
        e=0;
        for(int i =0; i<this.ySize; i++){
            for(int j = 0; j<this.xSize; j++){
                pArray[e].red = rAvs[i][0];
                pArray[e].green = rAvs[i][1];
                pArray[e].blue = rAvs[i][2];
                pArray[e].updateC();
                e++;
            }
        }
        update();
    }
    //moves images in a direction a number of pixels (with wraparound)
    public void down(int num){
        for(int i = 0; i<xSize*ySize; i++){
            pArray[i].py = (pArray[i].py + num) % ySize;
        }
        updatePP();
        update();
    }
    public void up(int num){
        for(int i = 0; i<xSize*ySize; i++){
            pArray[i].py = ((pArray[i].py - num)% ySize + ySize) % ySize;
        }
        updatePP();
        update();
    }
    public void left(int num){
        for(int i = 0; i<xSize*ySize; i++){
            pArray[i].px = ((pArray[i].px - num) % xSize + xSize) % xSize;
        }
        updatePP();
        update();
    }
    
    public void right(int num){
        for(int i = 0; i<xSize*ySize; i++){
            pArray[i].px = ((pArray[i].px + num)) % xSize;
        }
        updatePP();
        update();
    }

    //supposed to move pixels in a circle: broken
    public void circle(int r, int[] c){
        for(int i = 0; i<xSize*ySize; i++){
            double theta = Math.atan2(this.pArray[i].py - c[1], this.pArray[i].px - c[0]);
            int newPx = (int) Math.round(c[0] + r * Math.cos(theta));
            int newPy = (int) Math.round(c[1] + r * Math.sin(theta));
            if(newPx >= 0 && newPx < xSize && newPy >= 0 && newPy < ySize){
                this.pArray[i].px = newPx;
                this.pArray[i].py = newPy;
            }
        }
        updatePP();

    }

    public void flipx(){
        for(int i = 0; i<xSize*ySize; i++){
            this.pArray[i].px = (xSize - this.pArray[i].px)%xSize;
        }
        updatePP();
    }
    public void flipx2(){
        for(int i = 0; i<xSize*ySize; i++){
            if(xSize/2 - this.pArray[i].px>=0){
                this.pArray[i].px = (xSize/2 - this.pArray[i].px)%xSize;
            }
            else{
                this.pArray[i].px = (xSize/2 + (xSize-(this.pArray[i].px%xSize)));
            }
        }
        updatePP();
    }
    public void flipy(){
        for(int i = 0; i<xSize*ySize; i++){
            this.pArray[i].py = ((ySize - this.pArray[i].py)%ySize);
        }
        updatePP();
    }
    public void flipy2(){
        for(int i = 0; i<xSize*ySize; i++){
            if(ySize/2 - this.pArray[i].py>=0){
                this.pArray[i].py = (ySize/2 - this.pArray[i].py)%ySize;
            }
            else{
                this.pArray[i].py = (ySize/2 + (ySize-(this.pArray[i].py%ySize)));
            }
        }
        updatePP();
    }
}