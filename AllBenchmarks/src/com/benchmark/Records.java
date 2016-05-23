package com.benchmark;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;


public class Records {
    private double [] records;
    private double [] records1e3;
    private double [] records1e4;
    private double [] records1e5;

    private boolean isRecord1e3;
    private boolean isRecord1e4;
    private boolean isRecord1e5;

    private List<Integer> FEs;
    private int nSucess;
    //private int totalFEsSucess;

    private int actualRun;

    private int n_column_excel;
    private int n_column_excel_10;
    private int n_column_excel_30;
    private int n_column_excel_50;

    private HSSFWorkbook workbook;

    private int n_runs;

    public Records(){
        n_runs = 25;
        workbook = new HSSFWorkbook();

        createWorbook("10");
        createWorbook("30");
        createWorbook("50");

    }

    public Records(int n_runs){
        this.n_runs = n_runs;
        workbook = new HSSFWorkbook();

        createWorbook("10");
        createWorbook("30");
        createWorbook("50");

    }


    public void startRecord(){
        records = new double[n_runs];
        records1e3 = new double[n_runs];
        records1e4 = new double[n_runs];
        records1e5 = new double[n_runs];
        FEs = new ArrayList<>();


        for(int j=0; j<n_runs; j++){
            records[j] = 0;
            records1e3[j] = 0;
            records1e4[j] = 0;
            records1e5[j] = 0;
        }
        nSucess = 0;
        actualRun = 0;
        //totalFEsSucess = 0;
        isRecord1e3 = false;
        isRecord1e4 = false;
        isRecord1e5 = false;
    }


    private static double[] copyArray(double [] aOld){
        double [] aNew = new double [aOld.length];
        for(int i=0; i<aNew.length; i++){
            aNew[i] = aOld[i];
        }
        return aNew;
    }

    private double getMean(List<Integer> rec){
        double sum = 0;
        for(int i=0; i<rec.size(); i++){
            sum+=rec.get(i);
        }
        return sum/rec.size();
    }

    private double getMean(double [] rec){
        double sum = 0;
        for(int i=0; i<n_runs; i++){
            sum+=rec[i];
        }
        return sum/n_runs;
    }

    public double std(double[] rec){
        double mean = getMean(rec);
        BigDecimal sum = BigDecimal.ZERO;
        for(int i=0; i<n_runs; i++){
            sum = sum.add(BigDecimal.valueOf((rec[i] - mean) * (rec[i] - mean)));
        }

      return Math.sqrt(sum.divide(BigDecimal.valueOf(n_runs)).doubleValue());
    }

    private void sort(double [] rec){
        for(int i=0; i<n_runs; i++){
            for(int j=i+1; j<n_runs; j++){
                if(rec[i] > rec[j]){
                    double tmp = rec[i];
                    rec[i] = rec[j];
                    rec[j] = tmp;
                }
            }
        }
    }


    public void newRecord(double value, int iter){
        if(iter >= 1000 && iter <10000 && !isRecord1e3){
            records1e3[actualRun] = value;
            isRecord1e3 = true;
        }else if(iter >= 10000 && iter <100000 && !isRecord1e4){
            records1e4[actualRun] = value;
            isRecord1e4 = true;
        }else if(iter >= 100000 && !isRecord1e5){
            records1e5[actualRun] = value;
            isRecord1e5 = true;
        }
    }

    public void endRun(double value, int current_fes, int max_fes){
        records[actualRun] = value;
        //FEs[actualRun] = current_fes;
        actualRun++;

        isRecord1e3 = false;
        isRecord1e4 = false;
        isRecord1e5 = false;

        if(max_fes > current_fes){
            FEs.add(current_fes);
            nSucess++;
            if(nSucess > n_runs){
                System.err.println("Number of success can't be greater than number of runs.");
                System.exit(0);
            }
        }

    }

    public void exportExcel(String file_name){
        String filename = file_name + ".xls";
        File file = new File(filename);
        if (file.exists()) {
            file.delete();
        }

        try {
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Your excel file has been generated!");
    }


  /*  public void write(int dim, int fun, String file_name, boolean excel){
        if(!excel) {
            File file = new File(file_name + ".txt");
            StringBuilder stringBuilder = new StringBuilder();
            String results = "";


            stringBuilder.append("Function: " + fun + "\n");
            stringBuilder.append("DIM: " + dim + "\n");


            sort(records);
            results = "com.benchmark.Records:\n";
            results += "1: " + records[0] + " 7: " + records[6] + " 13: " + records[12] + " 19: " + records[18] + " 25: " + records[24] + "\n" + "Mean: " + getMean(records) + " STD: " + std(records) + "\n";
            stringBuilder.append(results);

            sort(records1e3);
            results = "com.benchmark.Records 1e3:\n";
            results += "1: " + records1e3[0] + " 7: " + records1e3[6] + " 13: " + records1e3[12] + " 19: " + records1e3[18] + " 25: " + records1e3[24] + "\n" + "Mean: " + getMean(records1e3) + " STD: " + std(records1e3) + "\n";
            stringBuilder.append(results);

            sort(records1e4);
            results = "com.benchmark.Records 1e4:\n";
            results += "1: " + records1e4[0] + " 7: " + records1e4[6] + " 13: " + records1e4[12] + " 19: " + records1e4[18] + " 25: " + records1e4[24] + "\n" + "Mean: " + getMean(records1e4) + " STD: " + std(records1e4) + "\n";
            stringBuilder.append(results);

            sort(records1e5);
            results = "com.benchmark.Records 1e5:\n";
            results += "1: " + records1e5[0] + " 7: " + records1e5[6] + " 13: " + records1e5[12] + " 19: " + records1e5[18] + " 25: " + records1e5[24] + "\n" + "Mean: " + getMean(records1e5) + " STD: " + std(records1e5) + "\n";
            stringBuilder.append(results);

            double nSuc = nSucess;
            results = "\nSucess rate: " + (nSuc / 25.0) + " Sucess performance: " + (getMean(FEs) * 25) / nSucess + "\n";

            stringBuilder.append(results);

            stringBuilder.append("\n");


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
        }else {

        }

    }*/

    public void endRecord(int fun, int dim) {
        try {
            int n_row = 0;

            switch (dim){
                case 10:
                    n_column_excel = n_column_excel_10;
                    break;
                case 30:
                    n_column_excel = n_column_excel_30;
                    break;
                case 50:
                    n_column_excel = n_column_excel_50 ;
                    break;
            }


            HSSFSheet sheet = workbook.getSheet(String.valueOf(dim));
            sheet.setColumnWidth(n_column_excel, (int)Math.round((3.0 * 1000)/0.78) );



            HSSFRow rowhead = sheet.getRow((short)n_row);
            rowhead.createCell(n_column_excel).setCellValue(fun);
            n_row++;


            for(int FES=0; FES<4; FES++) {
                double [] current_record = null;
                switch (FES){
                    case 0:
                        current_record = copyArray(records1e3);
                        break;
                    case 1:
                        current_record = copyArray(records1e4);
                        break;
                    case 2:
                        current_record = copyArray(records1e5);
                        break;
                    case 3:
                        current_record = copyArray(records);
                        break;
                }
                sort(current_record);
                for (int i = 0; i < n_runs; i++) {
                    HSSFRow row = sheet.getRow((short) n_row);
                    n_row++;
                    row.createCell(n_column_excel).setCellValue(current_record[i]);
                }
                HSSFRow rowStd = sheet.getRow((short) n_row);
                rowStd.createCell(n_column_excel).setCellValue(std(current_record));
                n_row++;

                HSSFRow rowMean = sheet.getRow((short) n_row);
                n_row++;
                rowMean.createCell(n_column_excel).setCellValue(getMean(current_record));

                HSSFRow rowBest = sheet.getRow((short) n_row);
                rowBest.createCell(n_column_excel).setCellValue(current_record[0]);
                n_row++;

                HSSFRow rowMedian = sheet.getRow((short) n_row);
                n_row++;
                rowMedian.createCell(n_column_excel).setCellValue(current_record[Math.round(current_record.length/2)]);

                HSSFRow rowWorst = sheet.getRow((short) n_row);
                n_row++;
                rowWorst.createCell(n_column_excel).setCellValue(current_record[current_record.length-1]);

            }

            HSSFRow rowNsuccess = sheet.getRow((short) n_row);
            rowNsuccess.createCell(n_column_excel).setCellValue(nSucess/n_runs);
            n_row++;

            HSSFRow rowSucessPerf = sheet.getRow((short) n_row);
            n_row++;
            rowSucessPerf.createCell(n_column_excel).setCellValue((getMean(FEs) * n_runs) / nSucess);

            n_column_excel++;

            switch (dim){
                case 10:
                    n_column_excel_10 = n_column_excel;
                    break;
                case 30:
                    n_column_excel_30 = n_column_excel;
                    break;
                case 50:
                    n_column_excel_50 = n_column_excel;
                    break;
            }

        } catch ( Exception ex ) {
            System.out.println(ex);
        }
    }



    private void createWorbook(String dim){
        try {

            int n_row = 0;


            HSSFSheet sheet = workbook.createSheet(dim);

            //1000 = 0.78cm
            sheet.setColumnWidth(0, (int)Math.round((3.5 * 1000)/0.78) );
            sheet.setColumnWidth(1, (int)Math.round((3.5 * 1000)/0.78) );



            HSSFRow rowhead = sheet.createRow((short)n_row);
            rowhead.createCell(0).setCellValue("FES");
            rowhead.createCell(1).setCellValue("Problem");
            n_row++;

            for(int FES=0; FES<4; FES++) {
                String tag = null;
                switch (FES){
                    case 0:
                        tag = "1E3";
                        break;
                    case 1:
                        tag = "1E4";
                        break;
                    case 2:
                        tag = "1E5";
                        break;
                    case 3:
                        tag = "Finish";
                        break;
                }


                CellStyle cellStyle = workbook.createCellStyle();
                cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
                rowhead.getCell(0).setCellStyle(cellStyle);

                HSSFRow row = sheet.createRow((short) n_row);
                row.createCell(0).setCellValue(tag);
                row.createCell(1).setCellValue("Run: " + 1);
                n_row++;


                sheet.addMergedRegion(new CellRangeAddress(n_row - 1, n_row + n_runs +3, 0, 0));

                for (int i = 1; i < n_runs; i++) {
                    row = sheet.createRow((short) n_row);
                    n_row++;
                    row.createCell(1).setCellValue("Run: " + (i + 1));
                }
                HSSFRow rowStd = sheet.createRow((short) n_row);
                rowStd.createCell(1).setCellValue("Std:");
                n_row++;

                HSSFRow rowMean = sheet.createRow((short) n_row);
                n_row++;
                rowMean.createCell(1).setCellValue("Mean:");

                HSSFRow rowBest = sheet.createRow((short) n_row);
                rowBest.createCell(1).setCellValue("Best:");
                n_row++;

                HSSFRow rowMedian = sheet.createRow((short) n_row);
                n_row++;
                rowMedian.createCell(1).setCellValue("Median:");

                HSSFRow rowWorst = sheet.createRow((short) n_row);
                n_row++;
                rowWorst.createCell(1).setCellValue("Worst:");

            }
            HSSFRow rowNsuccess = sheet.createRow((short) n_row);
            rowNsuccess.createCell(1).setCellValue("SuccessRate:");
            n_row++;

            HSSFRow rowSucessPerf = sheet.createRow((short) n_row);
            n_row++;
            rowSucessPerf.createCell(1).setCellValue("SucessPerfomance:");


            n_column_excel = 2;
            n_column_excel_10 = 2;
            n_column_excel_30 = 2;
            n_column_excel_50 = 2;

        } catch ( Exception ex ) {
            System.out.println(ex);
        }
    }

}

