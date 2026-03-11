package load;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dom.gantt.TaskAbstract;
import dom.gantt.Task;
import util.FileTypes;

public class LoadExcel implements ILoad {
	private FileTypes type;
	private List<TaskAbstract> topTasks;
	
	public LoadExcel(FileTypes type) {
		this.type = type;
		topTasks = new ArrayList<>();
	}
	
	public List<TaskAbstract> importFile(String path) {
		try {
            FileInputStream file = new FileInputStream(new File(path)); 
            Workbook workbook;
            
            if (type == FileTypes.XLSX) {
            	workbook = new XSSFWorkbook(file);
            } else {
            	workbook = new HSSFWorkbook(file);
            }
  
            Sheet sheet = workbook.getSheetAt(0); 
            Iterator<Row> rowIterator = sheet.iterator();
            List<String[]> rows = new ArrayList<>();
            
            while (rowIterator.hasNext()) { 
                Row row = rowIterator.next();
                List<String> cellValues = new ArrayList<>();
  
                Iterator<Cell> cellIterator = row.cellIterator(); 
  
                while (cellIterator.hasNext()) { 
                    Cell cell = cellIterator.next(); 
                    switch (cell.getCellType()) {
	                    case STRING:
	                        cellValues.add(cell.getStringCellValue());
	                        break;
	                    case NUMERIC:
	                        if (DateUtil.isCellDateFormatted(cell)) {
	                            cellValues.add(cell.getDateCellValue().toString());
	                        } else {
	                            cellValues.add(String.valueOf(cell.getNumericCellValue()));
	                        }
	                        break;
	                    case BOOLEAN:
	                        cellValues.add(String.valueOf(cell.getBooleanCellValue()));
	                        break;
	                    case FORMULA:
	                        cellValues.add(cell.getCellFormula());
	                        break;
	                    case BLANK:
	                        cellValues.add("");
	                        break;
	                    default:
	                        cellValues.add("");
	                        break;
                    }
                }
                rows.add(cellValues.toArray(new String[0]));
            }
            file.close();
            workbook.close();
            
            for (String[] line: rows){
            	if (line.length == 3) {
            		int min = Integer.MAX_VALUE;
            		int max = Integer.MIN_VALUE;
            		double sumCost = 0;
            		double sumEffort = 0;
            		List<TaskAbstract> subtasks = new ArrayList<>();
            		
            		for (String[] task: rows){
            			if(task[2].equals(line[0])) {
            				int taskStart = (int) Double.parseDouble(task[3]);
            				if(taskStart < min) {
            					min = taskStart;
            				}
            				
            				int taskEnd = (int) Double.parseDouble(task[4]);
            				if(taskEnd > max) {
            					max = taskEnd;
            				}
            				
            				sumCost+= Double.parseDouble(task[5]);
            				sumEffort+= Double.parseDouble(task[6]);
            				
            				int taskId = (int) Double.parseDouble(task[0]);
                			int mamaId = (int) Double.parseDouble(task[2]);
                			int start = (int) Double.parseDouble(task[3]);
                			int end = (int) Double.parseDouble(task[4]);
                			double cost = Double.parseDouble(task[5]);
                			double effort = Double.parseDouble(task[6]);
                			subtasks.add(new Task(taskId, task[1], mamaId, start, end, cost, effort, true));
            			}
            		}
            		
            		int taskId = (int) Double.parseDouble(line[0]);
        			int mamaId = (int) Double.parseDouble(line[2]);
            		topTasks.add(new Task(taskId, line[1], mamaId, min, max, sumCost, sumEffort, false, subtasks));
            		
            	} else if (line[2].equals("0")) {
            		int taskId = (int) Double.parseDouble(line[0]);
        			int mamaId = (int) Double.parseDouble(line[2]);
        			int start = (int) Double.parseDouble(line[3]);
        			int end = (int) Double.parseDouble(line[4]);
        			double cost = Double.parseDouble(line[5]);
        			double effort = Double.parseDouble(line[6]);
            		topTasks.add(new Task(taskId, line[1], mamaId, start, end, cost, effort, false));
            	}
            }
            return topTasks;
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
		return null;
	}
}
