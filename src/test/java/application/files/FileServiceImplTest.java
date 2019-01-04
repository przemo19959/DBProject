package application.files;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FileServiceImplTest {
	private FileService mockService=new FileServiceImpl();
	private String mockFileName="cos";
	
	@BeforeEach
	void init() {
		
	}
	
	@Test
	@DisplayName("file is created right")
	void test1() throws IOException {
		mockService.createTxtFile(mockFileName);
		assertThat(Paths.get(FileService.pathToMainFolder+"/"+mockFileName+".txt").toFile().exists()).isTrue();
	}
	
	@Test
	@DisplayName("appending text to file works 1")
	void test2() throws IOException {
		String mockText="cosTam";
		mockService.writeTxtFile(mockFileName, mockText);
		List<String> tmp=Files.readAllLines(Paths.get(FileService.pathToMainFolder+"/"+mockFileName+".txt"));
		assertThat(tmp.size()).isEqualTo(1);
		assertThat(tmp.get(0)).isEqualTo(mockText);
	}
	
	@Test
	@DisplayName("clearing text file works")
	void test3() throws IOException {
		mockService.clearTxtFile(mockFileName);
		List<String> tmp=Files.readAllLines(Paths.get(FileService.pathToMainFolder+"/"+mockFileName+".txt"));
		assertThat(tmp.size()).isEqualTo(0);
	}
	
	@Test
	@DisplayName("appending text to file works 2")
	void test4() throws IOException {
		String mockText="cosTamjesjadsf\nasdcasd\n4324fdasd";
		mockService.writeTxtFile(mockFileName, mockText);
		List<String> tmp=Files.readAllLines(Paths.get(FileService.pathToMainFolder+"/"+mockFileName+".txt"));
		assertThat(tmp.size()).isEqualTo(3);
		assertThat(tmp.get(0)).isEqualTo("cosTamjesjadsf");
		assertThat(tmp.get(1)).isEqualTo("asdcasd");
		assertThat(tmp.get(2)).isEqualTo("4324fdasd");
	}
	
	@Test
	@DisplayName("deleting given line works")
	void test5() throws IOException {
		mockService.deleteRecordFromTxtFile(mockFileName, 1);
		List<String> tmp=Files.readAllLines(Paths.get(FileService.pathToMainFolder+"/"+mockFileName+".txt"));
		assertThat(tmp.size()).isEqualTo(2);
		assertThat(tmp.get(0)).isEqualTo("cosTamjesjadsf");
		assertThat(tmp.get(1)).isEqualTo("4324fdasd");
	}
	
	
	@Test
	@DisplayName("file is deleted right")
	void testX() throws IOException {
		mockService.deleteTxtFile(mockFileName);
		assertThat(Paths.get(FileService.pathToMainFolder+"/"+mockFileName+".txt").toFile().exists()).isFalse();
	}
}
