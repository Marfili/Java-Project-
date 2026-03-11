package MainControllerTest;

import static org.junit.Assert.*;
import org.junit.Test;
import service.MainController;
import util.FileTypes;
import util.ProjectInfo;

public class PrepareTargetGroupTest {
	@Test
	public void T5_V0_HappyDay() {
		MainController controller = new MainController();
		controller.load("src/test/resources/input/Shop.xlsx", FileTypes.XLSX);
		ProjectInfo prjInfo = controller.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/output/ShopOutputFromTests.xlsx");
		
		assertEquals("Gantt Diagrams", prjInfo.getProjectName());
		assertEquals("src/test/resources/input/Shop.xlsx", prjInfo.getSourceFileName());
		assertEquals("src/test/resources/output/ShopOutputFromTests.xlsx", prjInfo.getTargetFileName());
		assertEquals(8, prjInfo.getTotalNumTasks());
		assertEquals(3, prjInfo.getTotalTopTasks());
	}
	
	@Test(expected = NullPointerException.class)
	public void T5_V0_NoFile() {
		MainController controller = new MainController();
		controller.prepareTargetWorkbook(FileTypes.XLSX, "src/test/resources/output/ShopOutputFromTests.xlsx");
	}
}
