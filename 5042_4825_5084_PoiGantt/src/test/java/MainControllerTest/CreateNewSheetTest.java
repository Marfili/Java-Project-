package MainControllerTest;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import dom.gantt.TaskAbstract;
import service.MainController;
import util.FileTypes;

public class CreateNewSheetTest {
	@Test
	public void T8_V0_HappyDay() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasks = controller.getAllTasks();
		controller.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/output/ShopOutputFromTests.xlsx");
		boolean success = controller.createNewSheet("ALL_Styled", tasks, 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 
		
		assertEquals(true, success);
	}
	
	@Test
	public void T8_V0_WrongPath() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasks = controller.getAllTasks();
		controller.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/wrongFile/ShopOutputFromTests.xlsx");
		boolean success = controller.createNewSheet("ALL_Styled", tasks, 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 
		
		assertEquals(false, success);
	}
	
	@Test(expected = NullPointerException.class)
	public void T8_V0_NoTargetWorkbook() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasks = controller.getAllTasks();
		boolean success = controller.createNewSheet("ALL_Styled", tasks, 
				"DefaultHeaderStyle", "TopTask_bar_style", "TopTask_data_style", "NonTopTask_bar_style", "NonTopTask_data_style", "Normal"); 
		
		assertEquals(true, success);
	}
}
