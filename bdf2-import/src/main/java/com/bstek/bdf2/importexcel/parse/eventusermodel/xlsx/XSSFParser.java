package com.bstek.bdf2.importexcel.parse.eventusermodel.xlsx;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import com.bstek.bdf2.importexcel.parse.eventusermodel.EventUserModelParser;
import com.bstek.bdf2.importexcel.parse.eventusermodel.IParseExcelRowMapper;

@Service(XSSFParser.BEAN_ID)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class XSSFParser extends EventUserModelParser {

	public static final String BEAN_ID = "bdf2.XSSFParser";

	private SharedStringsTable sst;
	private String lastContents;
	private int sheetIndex = -1;
	private Map<Integer, Object> columns = new HashMap<Integer, Object>();
	private int curRow = 0;
	private int curColumn = 0;
	private StylesTable stylesTable;
	private IParseExcelRowMapper parseExcelRowMapper;
	private String sheetName;

	public void execute(InputStream inputStream, IParseExcelRowMapper parseExcelRowMapper) throws Exception {
		OPCPackage pkg = OPCPackage.open(inputStream);
		XSSFReader xssfReader = new XSSFReader(pkg);
		sst = xssfReader.getSharedStringsTable();
		stylesTable = xssfReader.getStylesTable();

		SAXParserFactory saxFactory = SAXParserFactory.newInstance();
		SAXParser saxParser = saxFactory.newSAXParser();
		XMLReader parser = saxParser.getXMLReader();
		parser.setContentHandler(new XSSFParserHandler());

		this.parseExcelRowMapper = parseExcelRowMapper;

		XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
		while (iter.hasNext()) {
			curRow = 0;
			sheetIndex++;
			InputStream sheet = iter.next();
			sheetName = iter.getSheetName();
			InputSource sheetSource = new InputSource(sheet);
			parser.parse(sheetSource);
			sheet.close();
		}

	}

	public class XSSFParserHandler extends DefaultHandler {

		private short formatIndex;
		private String formatString;
		private XssfDataType nextDataType = XssfDataType.NUMBER;
		private DataFormatter formatter = new DataFormatter();

		public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
			if (name.equals("c")) {
				String r = attributes.getValue("r");
				int firstDigit = -1;
				for (int c = 0; c < r.length(); ++c) {
					if (Character.isDigit(r.charAt(c))) {
						firstDigit = c;
						break;
					}
				}
				curColumn = nameToColumn(r.substring(0, firstDigit));
				columns.put(curColumn, null);

				String cellType = attributes.getValue("t");
				this.nextDataType = XssfDataType.NUMBER;
				this.formatIndex = -1;
				this.formatString = null;
				String cellStyleStr = attributes.getValue("s");
				if ("b".equals(cellType))
					nextDataType = XssfDataType.BOOL;
				else if ("e".equals(cellType))
					nextDataType = XssfDataType.ERROR;
				else if ("inlineStr".equals(cellType))
					nextDataType = XssfDataType.INLINESTR;
				else if ("s".equals(cellType))
					nextDataType = XssfDataType.SSTINDEX;
				else if ("str".equals(cellType))
					nextDataType = XssfDataType.FORMULA;
				else if (cellStyleStr != null) {
					int styleIndex = Integer.parseInt(cellStyleStr);
					XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
					this.formatIndex = style.getDataFormat();
					this.formatString = style.getDataFormatString();
					if (this.formatString == null) {
						this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
					}
				}
			}
			lastContents = "";
		}

		public void endElement(String uri, String localName, String name) throws SAXException {
			if (name.equals("c")) {
				String value = lastContents.trim();

				Object obj = StringUtils.isNotBlank(value) ? value : null;
				switch (nextDataType) {
				case BOOL:
					char first = value.charAt(0);
					obj = first == '0' ? false : true;
					break;

				case ERROR:
					break;

				case FORMULA:
					break;

				case INLINESTR:
					XSSFRichTextString rtsi = new XSSFRichTextString(value);
					obj = rtsi.toString();
					break;

				case SSTINDEX:
					String sstIndex = value.toString();
					try {
						int idx = Integer.parseInt(sstIndex);
						XSSFRichTextString rtss = new XSSFRichTextString(sst.getEntryAt(idx));
						sstIndex = rtss.toString();
					} catch (NumberFormatException ex) {
					}
					obj = sstIndex.toString();
					break;

				case NUMBER:
					if (StringUtils.isNotBlank(value) && this.formatString != null) {
						obj = formatter.formatRawCellContents(Double.parseDouble(value), this.formatIndex, this.formatString);
					}
					break;

				default:
					break;
				}
				if (obj!=null && nextDataType.name().equals(XssfDataType.NUMBER.name())) {
					try {
						Object obj2 = Double.valueOf((String) obj);
					} catch (NumberFormatException e) {
						try {
							obj = DateUtil.getJavaDate(Double.valueOf(value));
						} catch (NumberFormatException nfe) {
						}
					}
				}
				columns.put(curColumn, obj);
			} else if (name.equals("row")) {
				try {
					parseExcelRowMapper.executeRowMapper(sheetIndex, sheetName, curRow, columns);
				} catch (Exception e) {
					e.printStackTrace();
				}
				columns.clear();
				curRow++;
				curColumn = 0;
			}
		}

		private int nameToColumn(String name) {
			int column = -1;
			for (int i = 0; i < name.length(); ++i) {
				int c = name.charAt(i);
				column = (column + 1) * 26 + c - 'A';
			}
			return column;
		}

		public void characters(char[] ch, int start, int length) throws SAXException {
			lastContents += new String(ch, start, length);
		}

	}

	public boolean supportFileExtension(String fileExtension) {
		if (StringUtils.isNotBlank(fileExtension) && fileExtension.equals("xlsx")) {
			return true;
		}
		return false;
	}

	enum XssfDataType {
		BOOL, ERROR, FORMULA, INLINESTR, SSTINDEX, NUMBER,
	}
}
