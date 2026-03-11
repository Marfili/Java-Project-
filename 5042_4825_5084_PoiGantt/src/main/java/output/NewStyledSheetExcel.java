package output;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import dom.gantt.TaskAbstract;
import util.FileTypes;
import util.ProjectInfo;

public class NewStyledSheetExcel {
	private static final String[] titles = {"", "Level", "Id", "Description", "Cost", "Effort"};
		
	public boolean createStylesSheet(List<TaskAbstract> tasks, ProjectInfo info, FileTypes type, String sheetName, String headerStyleName,
			String topBarStyleName, String topDataStyleName, String nonTopBarStyleName, String nonTopDataStyleName, String normalStyleName) {
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
            
            int sheetIndex = workbook.getSheetIndex(sheetName);
            Sheet sheet;
            if (sheetIndex != -1) {
                workbook.removeSheetAt(sheetIndex);
                sheet = workbook.createSheet(sheetName);
            } else {
            	sheet = workbook.createSheet(sheetName);
            }
            
            Map<String, CellStyle> styles = createStyles(workbook);
                        
            Row headerRow = sheet.createRow(0);
            headerRow.setHeightInPoints(15.75f);
            for (int i = 0; i < titles.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(titles[i]);
                cell.setCellStyle(styles.get(headerStyleName));
            }
            
            int start = tasks.get(0).getTaskStart();
            int end = Integer.MIN_VALUE;
            
            for(TaskAbstract task: tasks) {
            	if(task.getTaskEnd() > end) {
            		end = task.getTaskEnd();
            	}
            }
            
            int count = titles.length;
            for(int i = start; i <= end; i++) {
            	Cell cell = headerRow.createCell(count);
            	cell.setCellValue(i);
            	cell.setCellStyle(styles.get(headerStyleName));
            	count++;
            }
            
            int rownum = 1;
            for (int i = 0; i < tasks.size(); i++, rownum++) {
                Row row = sheet.createRow(rownum);
                
                TaskAbstract task = tasks.get(i);
                
                String dataStyle;
                String barStyle;
                
                if (task.isSimple()) {
                	dataStyle = nonTopDataStyleName;
                	barStyle = nonTopBarStyleName;
                	
                } else {
                	dataStyle = topDataStyleName;
                	barStyle = topBarStyleName;
                }
                                
                Cell cell = row.createCell(0);
            	cell.setCellStyle(styles.get(dataStyle));
            	cell = row.createCell(1);
            	if (!task.isSimple()) {
            		cell.setCellValue("TOP");
                }
            	cell.setCellStyle(styles.get(dataStyle));
            	cell = row.createCell(2);
            	cell.setCellValue(task.getTaskId());
            	cell.setCellStyle(styles.get(dataStyle));
            	cell = row.createCell(3);
            	cell.setCellValue(task.getTaskText());
            	cell.setCellStyle(styles.get(dataStyle));
            	cell = row.createCell(4);
            	cell.setCellValue(String.format(Locale.US, "%.2f", task.getCost()));
            	cell.setCellStyle(styles.get(dataStyle));
            	cell = row.createCell(5);
            	cell.setCellValue(String.format(Locale.US, "%.2f", task.getEffort()));
            	cell.setCellStyle(styles.get(dataStyle));
            	
            	count = 6;
            	for (int j = start; j <= end; j++) {
                	cell = row.createCell(count);
                	if (j >= task.getTaskStart() && j <= task.getTaskEnd()) {
                		cell.setCellStyle(styles.get(barStyle));
                	} else {
                		cell.setCellStyle(styles.get(normalStyleName));
                	}
                	
                	count++;
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
	
	private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<>();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBold(true);
        style = createBorderedStyle(wb);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("DefaultHeaderStyle", style);
        
        style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short)14);
        font.setColor(IndexedColors.BLUE.getIndex());
        font.setBold(true);
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setFont(font);
        style.setWrapText(false);
        styles.put("TopTask_data_style", style);

        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setWrapText(true);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(false);
        styles.put("NonTopTask_data_style", style);

        style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("TopTask_bar_style", style);
        
        style = wb.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styles.put("NonTopTask_bar_style", style);
        
        style = wb.createCellStyle();
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styles.put("Normal", style);
        
        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb) {
        BorderStyle thin = BorderStyle.THIN;
        short black = IndexedColors.BLACK.getIndex();

        CellStyle style = wb.createCellStyle();
        style.setBorderRight(thin);
        style.setRightBorderColor(black);
        style.setBorderBottom(thin);
        style.setBottomBorderColor(black);
        style.setBorderLeft(thin);
        style.setLeftBorderColor(black);
        style.setBorderTop(thin);
        style.setTopBorderColor(black);
        return style;
    }
}
