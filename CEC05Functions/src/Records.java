import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Records {
    public double [] records;
    public double [] records1e3;
    public double [] records1e4;
    public double [] records1e5;

    public int actualRun;
    public int nFun;

    public Records(){
        records = new double[25];
        records1e3 = new double[25];
        records1e4 = new double[25];
        records1e5 = new double[25];



            for(int j=0; j<25; j++){
                records[j] = 0;
                records1e3[j] = 0;
                records1e4[j] = 0;
                records1e5[j] = 0;
            }

        actualRun = 0;
    }

    public double getMean(double [] rec){
        double sum = 0;
        for(int i=0; i<25; i++){
            sum+=rec[i];
        }
        return sum/25;
    }

    public double std(double [] rec){ //TODO HACER ESTO FUNCIONAR
        double mean = getMean(rec);
        double sum = 0;
        for(int i=0; i<25; i++){
            sum += (rec[i] - mean) * (rec[i] - mean);
        }

      return Math.sqrt(sum/25);
    }

    public void sort(double [] rec){
        for(int i=0; i<25; i++){
            for(int j=i+1; j<25; j++){
                if(rec[i] > rec[j]){
                    double tmp = rec[i];
                    rec[i] = rec[j];
                    rec[j] = tmp;
                }
            }
        }
    }


    public void newRecord(double value, int iter){
        if(iter >= 1000){
            records1e3[actualRun] = value;
        }else if(iter >= 10000){
            records1e4[actualRun] = value;
        }else if(iter >= 100000){
            records1e5[actualRun] = value;
        }
    }

    public void newRecord(double value){
        records[actualRun] = value;
        actualRun++;
    }

    public void write(int dim, int fun, String file_name, boolean excel){
        File file = new File( file_name + ".txt");
        StringBuilder stringBuilder = new StringBuilder();
        String results = "";

        if(!excel) {
            stringBuilder.append("Function: " + fun + "\n");
            stringBuilder.append("DIM: " + dim + "\n");


            sort(records);
            results = "Records:\n";
            results += "1: " + records[0] + " 7: " + records[6] + " 13: " + records[12] + " 19: " + records[18] + " 25: " + records[24] + "\n" + "Mean: " + getMean(records) + " STD: " + std(records) + "\n";
            stringBuilder.append(results);

            sort(records1e3);
            results = "Records 1e3:\n";
            results += "1: " + records1e3[0] + " 7: " + records1e3[6] + " 13: " + records1e3[12] + " 19: " + records1e3[18] + " 25: " + records1e3[24] + "\n" + "Mean: " + getMean(records1e3) + " STD: " + std(records1e3) + "\n";
            stringBuilder.append(results);

            sort(records1e4);
            results = "Records 1e4:\n";
            results += "1: " + records1e4[0] + " 7: " + records1e4[6] + " 13: " + records1e4[12] + " 19: " + records1e4[18] + " 25: " + records1e4[24] + "\n" + "Mean: " + getMean(records1e4) + " STD: " + std(records1e4) + "\n";
            stringBuilder.append(results);

            sort(records1e5);
            results = "Records 1e5:\n";
            results += "1: " + records1e5[0] + " 7: " + records1e5[6] + " 13: " + records1e5[12] + " 19: " + records1e5[18] + " 25: " + records1e5[24] + "\n" + "Mean: " + getMean(records1e5) + " STD: " + std(records1e5) + "\n";
            stringBuilder.append(results);

            stringBuilder.append("\n");
        }else{
            sort(records);
            results = records[0] + " " + records[6] + " " + records[12] + " " + records[18] + " " + records[24] + " " + " " + getMean(records) + " " + std(records) + "\n";
            stringBuilder.append(results);

            sort(records1e3);
            results = records1e3[0] + " " + records1e3[6] + " " + records1e3[12] + " " + records1e3[18] + " " + records1e3[24] +  " " + getMean(records1e3) + " " + std(records1e3) + "\n";
            stringBuilder.append(results);

            sort(records1e4);
            results = records1e4[0] + " " + records1e4[6] + " " + records1e4[12] + " " + records1e4[18] + " " + records1e4[24] +  " " + getMean(records1e4) + " " + std(records1e4) + "\n";
            stringBuilder.append(results);

            sort(records1e5);
            results = records1e5[0] + " " + records1e5[6] + " " + records1e5[12] + " " + records1e5[18] + " " + records1e5[24] + " " + getMean(records1e5) + " " + std(records1e5) + "\n";
            stringBuilder.append(results);

            stringBuilder.append("\n");
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream fop = new FileOutputStream(file, true)) {

            byte[] contentInBytes = stringBuilder.toString().getBytes();
            fop.write(contentInBytes);
            fop.flush();
            fop.close();

            System.out.println("Done" + "FUN: " + fun + " DIM: " + dim);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}

