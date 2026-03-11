package MainControllerTest;

import static org.junit.Assert.*;
import java.util.List;
import org.junit.Test;
import dom.gantt.TaskAbstract;
import service.MainController;
import util.FileTypes;

public class RawWriteToExcelFileTest {
	@Test
	public void T6_V0_HappyDay() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasks = controller.getAllTasks();
		controller.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/output/ShopOutputFromTests.xlsx");
		boolean success = controller.rawWriteToExcelFile(tasks);
		
		assertEquals(true, success);
	}
	
	@Test
	public void T6_V0_WrongPath() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasks = controller.getAllTasks();
		controller.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/wrongFile/ShopOutputFromTest.xlsx");
		boolean success = controller.rawWriteToExcelFile(tasks);
		
		assertEquals(false, success);
	}
	
	@Test(expected = NullPointerException.class)
	public void T6_V0_NoTargetWorkbook() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		List<TaskAbstract> tasks = controller.getAllTasks();
		boolean success = controller.rawWriteToExcelFile(tasks);
		
		assertEquals(true, success);
	}
}
