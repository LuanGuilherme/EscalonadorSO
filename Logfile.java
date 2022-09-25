import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logfile {
    private String nameFile;
    public Logfile(int quantum) { 
        try {
            if (quantum < 10)
              nameFile = "log0" + String.valueOf(quantum)+ ".txt";
            else 
              nameFile = "log" + String.valueOf(quantum)+ ".txt";

            File log = new File(nameFile);

            
            if (!log.exists()) {
              log.createNewFile();
              System.out.println("File created: " + log.getName());
            } else {
              log.delete();
              log.createNewFile();
              System.out.println("File already exists.");
            }
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void addLog(String log){
      try {
        FileWriter fileWritter = new FileWriter(nameFile, true);
        BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
        bufferWritter.write(log + "\n");
        bufferWritter.close();
        fileWritter.close();
        System.out.println("Successfully wrote to the file.");
      } catch (IOException e) {
        System.out.println("An error occurred.");
        e.printStackTrace();
      }
    }
}
