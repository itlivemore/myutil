package pers.itlivemore.bean;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @Title GenerateBeanFromExcel.java
 * @Package: pers.itlivemore.bean
 * @Description: 从excel中生成bean的字段
 *
 * @Author: itlivemore
 * @Date: 2018年2月24日 下午2:41:07
 *
 *        Copyright @ 2018 Corpration Name
 * 
 */
public class GenerateBeanFromExcel {
	private Workbook workbook;
	private Sheet sheet;

	private List<String> fieldNameList = new ArrayList<>(); // 存放java类中字段名
	private List<String> fieldTypeList = new ArrayList<>(); // 存放java类中字段类型
	private List<String> fieldDescList = new ArrayList<>(); // 存放java类中字段注释

	/**
	 * 
	 * @Title: readFile
	 * @Description: 读取excel文件
	 * @param @param
	 *            filePath 设定文件
	 * @return void 返回类型
	 * @throws @Author
	 *             itlivemore
	 * @Date 2018年2月24日 下午2:53:08
	 */
	public void readFile(String filePath) {
		try {
			workbook = WorkbookFactory.create(GenerateBeanFromExcel.class.getResourceAsStream(filePath));
			sheet = workbook.getSheetAt(0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: readFieldInfo
	 * @Description: 读取字段信息
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws @Author
	 *             laigc
	 * @Date 2018年2月24日 下午2:53:42
	 */
	private void readFieldInfo() {
		// 这里从1开始，因为第一行是标题
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			Cell fieldNameCell = row.getCell(0);
			String fieldName = fieldNameCell.getStringCellValue();
			if (fieldName == null || fieldName.trim().equals("")) {
				throw new RuntimeException("第" + (i + 1) + "行字段名称存在空");
			}
			fieldNameList.add(fieldName);
			// 第二列是字段类型
			Cell fieldTypeCell = row.getCell(1);
			String fieldType = fieldTypeCell.getStringCellValue();
			if (fieldType == null || fieldType.trim().equals("")) {
				throw new RuntimeException("第" + (i + 1) + "行字段类型存在空");
			}
			fieldTypeList.add(fieldType);
			// 第三列是字段描述
			Cell fieldDescCell = row.getCell(2);
			String fieldDesc = fieldDescCell.getStringCellValue();
			if (fieldDesc == null) {
				fieldDesc = "";
			}
			fieldDescList.add(fieldDesc);
		}
	}

	/**
	 * 拼接bean的java代码
	 * 
	 * @Title: generateBeanCode
	 * @Description: 拼接bean的java代码
	 * @param @return
	 *            bean的java代码
	 * @return String bean的java代码
	 * @throws @Author
	 *             laigc
	 * @Date 2018年2月24日 下午3:06:49
	 */
	private String generateBeanCode() {
		StringBuilder sb = new StringBuilder();
		int size = fieldNameList.size();
		for (int i = 0; i < size; i++) {
			sb.append("private ");
			sb.append(fieldTypeList.get(i) + " ");
			sb.append(fieldNameList.get(i));
			sb.append(";// ");
			sb.append(fieldDescList.get(i));
			sb.append("\n");
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		String filePath = "/bean/beanExcel.xlsx";
		GenerateBeanFromExcel bean = new GenerateBeanFromExcel();
		bean.readFile(filePath);
		bean.readFieldInfo();
		String beanCode = bean.generateBeanCode();
		System.out.println(beanCode);
	}

}
