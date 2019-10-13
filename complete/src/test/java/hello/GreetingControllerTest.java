package hello;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Test;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;


import static org.assertj.core.api.Assertions.assertThat;

public class GreetingControllerTest {

	@Test
	public void shouldGetPoiWorkbookStream() throws Exception {
		ResponseEntity<InputStreamResource> actual = new GreetingController().poi();

		Workbook workbook = WorkbookFactory.create(actual.getBody().getInputStream());
		assertThat(workbook.getNumberOfSheets()).isEqualTo(1);
	}
}