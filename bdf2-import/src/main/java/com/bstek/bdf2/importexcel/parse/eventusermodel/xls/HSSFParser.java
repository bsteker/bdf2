package com.bstek.bdf2.importexcel.parse.eventusermodel.xls;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.apache.commons.lang.StringUtils;

import com.bstek.bdf2.importexcel.parse.eventusermodel.EventUserModelParser;
import com.bstek.bdf2.importexcel.parse.eventusermodel.IParseExcelRowMapper;

@Service(HSSFParser.BEAN_ID)
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HSSFParser extends EventUserModelParser implements HSSFListener {

	public static final String BEAN_ID = "bdf2.HSSFParser";

	private int minColumns;
	private POIFSFileSystem fs;
	private int lastRowNumber;
	private int lastColumnNumber;
	private boolean outputFormulaValues = true;
	private SheetRecordCollectingListener workbookBuildingListener;
	private HSSFWorkbook stubWorkbook;
	private SSTRecord sstRecord;
	private FormatTrackingHSSFListener formatListener;
	private int sheetIndex = -1;
	private BoundSheetRecord[] orderedBSRs;
	private ArrayList<BoundSheetRecord> boundSheetRecords = new ArrayList<BoundSheetRecord>();

	private int nextRow;
	private int nextColumn;
	private boolean outputNextStringRecord;

	private int curRow = 0;
	private Map<Integer, Object> columns = new HashMap<Integer, Object>();
	private String sheetName;

	private IParseExcelRowMapper parseExcelRowMapper;

	public void execute(InputStream inputStream, IParseExcelRowMapper parseExcelRowMapper) throws IOException {
		this.fs = new POIFSFileSystem(inputStream);
		this.minColumns = -1;
		this.parseExcelRowMapper = parseExcelRowMapper;
		MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
		formatListener = new FormatTrackingHSSFListener(listener);
		HSSFEventFactory factory = new HSSFEventFactory();
		HSSFRequest request = new HSSFRequest();
		if (outputFormulaValues) {
			request.addListenerForAllRecords(formatListener);
		} else {
			workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
			request.addListenerForAllRecords(workbookBuildingListener);
		}
		factory.processWorkbookEvents(request, fs);
	}

	public void processRecord(Record record) {
		int thisRow = -1;
		int thisColumn = -1;
		String value = null;
		switch (record.getSid()) {
		case BoundSheetRecord.sid:
			boundSheetRecords.add((BoundSheetRecord) record);
			break;
		case BOFRecord.sid:
			BOFRecord br = (BOFRecord) record;
			if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
				if (workbookBuildingListener != null && stubWorkbook == null) {
					stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
				}
				sheetIndex++;
				if (orderedBSRs == null) {
					orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
				}
				sheetName = orderedBSRs[sheetIndex].getSheetname();
			}
			break;

		case SSTRecord.sid:
			sstRecord = (SSTRecord) record;
			break;

		case BlankRecord.sid:
			BlankRecord brec = (BlankRecord) record;
			thisRow = brec.getRow();
			thisColumn = brec.getColumn();

			break;
		case BoolErrRecord.sid:
			BoolErrRecord berec = (BoolErrRecord) record;
			thisRow = berec.getRow();
			thisColumn = berec.getColumn();
			if (berec.isBoolean()) {
				columns.put(thisColumn, berec.getBooleanValue());
			} else if (berec.isError()) {
				columns.put(thisColumn, berec.getErrorValue());
			}
			break;

		case FormulaRecord.sid:
			FormulaRecord frec = (FormulaRecord) record;

			thisRow = frec.getRow();
			thisColumn = frec.getColumn();

			if (outputFormulaValues) {
				if (Double.isNaN(frec.getValue())) {
					// Formula result is a string
					// This is stored in the next record
					outputNextStringRecord = true;
					nextRow = frec.getRow();
					nextColumn = frec.getColumn();
				} else {
					// thisStr = formatListener.formatNumberDateCell(frec);
				}
			} else {
				// thisStr = '"' +
				// HSSFFormulaParser.toFormulaString(stubWorkbook,
				// frec.getParsedExpression()) + '"';
			}

			break;
		case StringRecord.sid:
			if (outputNextStringRecord) {
				// StringRecord srec = (StringRecord) record;
				// thisStr = srec.getString();
				thisRow = nextRow;
				thisColumn = nextColumn;
				outputNextStringRecord = false;
			}
			break;

		case LabelRecord.sid:
			LabelRecord lrec = (LabelRecord) record;
			curRow = thisRow = lrec.getRow();
			thisColumn = lrec.getColumn();
			columns.put(thisColumn, lrec.getValue());
			break;
		case LabelSSTRecord.sid:
			LabelSSTRecord lsrec = (LabelSSTRecord) record;
			curRow = thisRow = lsrec.getRow();
			thisColumn = lsrec.getColumn();
			if (sstRecord == null) {
				columns.put(thisColumn, null);
			} else {
				value = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
				columns.put(thisColumn, value);
			}
			break;
		case NoteRecord.sid:
			NoteRecord nrec = (NoteRecord) record;
			thisRow = nrec.getRow();
			thisColumn = nrec.getColumn();
			break;
		case NumberRecord.sid:
			NumberRecord numrec = (NumberRecord) record;
			curRow = thisRow = numrec.getRow();
			thisColumn = numrec.getColumn();
			value = formatListener.formatNumberDateCell(numrec).trim();
			Object obj = null;
			try {
				Object obj2 = Double.valueOf(value);
				obj = value;
			} catch (NumberFormatException e) {
				obj = HSSFDateUtil.getJavaDate(numrec.getValue());
			}
			columns.put(thisColumn, obj);
			break;
		case RKRecord.sid:
			RKRecord rkrec = (RKRecord) record;
			thisRow = rkrec.getRow();
			thisColumn = rkrec.getColumn();
			break;
		default:
			break;
		}
		if (thisRow != -1 && thisRow != lastRowNumber) {
			lastColumnNumber = -1;
		}

		if (record instanceof MissingCellDummyRecord) {
			MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
			curRow = thisRow = mc.getRow();
			thisColumn = mc.getColumn();
			columns.put(thisColumn, null);
		}
		if (thisRow > -1) {
			lastRowNumber = thisRow;
		}
		if (thisColumn > -1) {
			lastColumnNumber = thisColumn;
		}
		if (record instanceof LastCellOfRowDummyRecord) {
			if (minColumns > 0) {
				if (lastColumnNumber == -1) {
					lastColumnNumber = 0;
				}
			}
			lastColumnNumber = -1;
			try {
				parseExcelRowMapper.executeRowMapper(sheetIndex, sheetName, curRow, columns);
			} catch (Exception e) {
				e.printStackTrace();
			}
			columns.clear();
		}
	}

	public boolean supportFileExtension(String fileExtension) {
		if (StringUtils.isNotEmpty(fileExtension) && fileExtension.equals("xls")) {
			return true;
		}
		return false;
	}
}
