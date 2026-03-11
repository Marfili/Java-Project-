package output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class IntermediateStateExcel {
	public boolean writeToExcel(List<TaskAbstract> tasks, ProjectInfo info, FileTypes type) {
        try {
        	File file = new File(info.getTargetFileName());
        	Workbook workbook = null;
            if (file.exists()) {
                FileInputStream excelFile = new FileInputStream(file);
                if (type == FileTypes.XLSX) {
                    workbook = new XSSFWorkbook(excelFile);
                } else {
                    workbook = new HSSFWorkbook(excelFile);
                }
                excelFile.close();
            } else {
            	if (type == FileTypes.XLSX) {
                    workbook = new XSSFWorkbook();
                } else {
                    workbook = new HSSFWorkbook();
                }
            }
            
            String timestamp = new SimpleDateFormat("dd-MM-yyyy HH_mm_ss").format(new Date());
            Sheet sheet = workbook.createSheet(timestamp);
            
            int r = 0;
            int c = 1;
            Row row = sheet.createRow(r++);
            row.createCell(c++).setCellValue("Level");
            row.createCell(c++).setCellValue("Id");
            row.createCell(c++).setCellValue("Description");
            row.createCell(c++).setCellValue("Cost");
            row.createCell(c++).setCellValue("Effort");
            
            int start = tasks.get(0).getTaskStart();
            int end = Integer.MIN_VALUE;
            
            for(TaskAbstract task: tasks) {
            	if(task.getTaskEnd() > end) {
            		end = task.getTaskEnd();
            	}
            }

            for(int i = start; i <= end; i++) {
            	row.createCell(c++).setCellValue(String.valueOf(i));
            }
            
            for(TaskAbstract task: tasks) {
            	row = sheet.createRow(r++);
            	c = 1;
            	if(task.isSimple()==false) {
            		row.createCell(c++).setCellValue("TOP");
            	}else {
            		c++;
            	}
            	
            	row.createCell(c++).setCellValue(String.valueOf(task.getTaskId()));
            	row.createCell(c++).setCellValue(task.getTaskText());
            	row.createCell(c++).setCellValue(String.format(Locale.US, "%.2f", task.getCost()));
            	row.createCell(c++).setCellValue(String.format(Locale.US, "%.2f", task.getEffort()));
            	
            	for(int i = start; i <= end; i++) {
            		if(task.getTaskStart()<= i && task.getTaskEnd() >= i) {
            			row.createCell(c++).setCellValue("x");
            		}else {
            			c++;
            		}
                }
            }

            FileOutputStream fos = new FileOutputStream(info.getTargetFileName());
            workbook.write(fos);
            workbook.close();
            fos.close();
            return true;

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
	}
}
