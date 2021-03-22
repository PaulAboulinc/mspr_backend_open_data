package com.pafpsdnc.opendata;

import com.pafpsdnc.opendata.controller.OpenDataController;
import com.pafpsdnc.opendata.exception.OpenDataNotFound;
import com.pafpsdnc.opendata.model.OpenData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class OpenDataApplicationTests {
	@Autowired
	private OpenDataController controller;

	@Test
	void testModelOpenData() {
		OpenData openData = new OpenData();
		openData.setUniqId(1);
		openData.setName("name");
		openData.setValue("value");
		assertThat(openData.getUniqId()).isEqualTo(1);
		assertThat(openData.getName()).isEqualTo("name");
		assertThat(openData.getValue()).isEqualTo("value");
	}

	@Test
	void testModelOpenDataController() throws OpenDataNotFound {
		assertThat(controller).isNotNull();

		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("uniqId", 1);
		requestBody.put("name", "name");
		requestBody.put("value", "value");

		OpenData openData = controller.createOpenData(requestBody);
		assertThat(openData).isNotNull();

		requestBody.put("name", "name updated");
		openData = controller.updateOpenData(openData.getId(), requestBody);
		assertThat(openData.getName()).isEqualTo("name updated");

		String message = "Les données " + openData.getId() + " ont bien été supprimées";
		assertThat(controller.deleteOpenData(openData.getId())).isEqualTo(message);
	}

	@Test
	void testException() {
		OpenDataNotFound openDataNotFound = new OpenDataNotFound();
		assertThat(openDataNotFound.getMessage()).isEqualTo("Les données n'ont pas été trouvées");

		OpenDataNotFound openDataNotFound2 = new OpenDataNotFound("test");
		assertThat(openDataNotFound2.getMessage()).isEqualTo("test");
	}
}