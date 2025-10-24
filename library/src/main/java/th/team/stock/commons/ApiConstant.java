package th.team.stock.commons;

import java.util.Locale;

public interface ApiConstant {
    

    public static final Locale LOCALE_THAI = Locale.forLanguageTag("th-TH");
    public static final Locale LOCALE_ENG = Locale.ENGLISH;
	public static final String FORMAT_DATE_DEFAULT = "dd/MM/yyyy HH:mm";
    
	public static final String STATUS = "status";
	public static final String MESSAGE = "message";
	public static final String DATA = "data";
	public static final String SUCCESS = "success";
	public static final String MSG_NOT_FOUND = "Data Not Found";
	public static final String MSG_DUPPLICATION_CODE = "ข้อมูลซ้ำ";
	public static final String MSG_EDIT_DATA_NOTFOUND = "ไม่พบข้อมูล";
	public static final String MSG_REPORT = "รายงานผู้มีสิทธ์ใช้งานในระบบ";
	public static final String MSG_DELETE_SUCCESS = "ลบข้อมูลสำเร็จ";
	public static final String EXCEPTION = "exception";
	public static final String ENTRIES = "entries";
	public static final String MEDIATYPE = "mediaType";
	public static final String EXCELTYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	public static final String PDFTYPE = "application/pdf";
	public static final String QUERY = LogType.QUERY.getValue();
	public static final String PARAMS = "params";
	public static final String PARAM_Q = "?q=";

	public static final String DEFAULT_LANG = "th";
	public static final String DEFAULT_DROPDOWN_LIMIT = " offset 0 limit 15 ";
	public static final String TH = "th";
	public static final String EN = "en";
	
	public static final String CLASSPATH = "language/messages";

    public static final String SELECT = "select ";
    public static final String SELECT_COUNT_STAR = "select count(*) ";
	public static final String PIPE = "|";
    public static final String DELIMITER_COMMA = ",";
    public static final String DELIMITER_COLON = ":";
    public static final String PARENTHESES_OPEN = "(";
    public static final String PARENTHESES_CLOSE = ")";
    public static final String CARRIAGE_RETURN_LINE_FEED = "\r\n";
	public static final String WHERE = " where 1 = 1 ";
	public static final String LIMIT = " offset ? limit ? ";
	public static final String TOTAL_RECORDS = "totalRecords";
	public static final String CHECK_NCCRS_VALIDATE_DUPE = "มีรายการคำขอสืบค้นนี้อยู่ระหว่างการพิจารณา";
	
	public enum RunningNumber {
		A("A"),
		B("B"),
		RECEIPT_NO("");
		
		private final String value;

		private RunningNumber(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}
	
	public static final String MODE_SEARCH = "search";
	public static final String MODE_EXPORT_EXCEL_BASE64 = "excelbase64";
	public static final String MODE_EXPORT_CSV = "csv";
	public static final String MODE_EXPORT_EXCEL = "excel";
	public static final String MODE_PDF = "pdf";
	public static final String MODE_TXT = "txt";
	public static final String MODE_DOCX = "docx";
	public static final String MODE_COPY = "copy";
	
	public static final String SLASH = "/";

	public static final String CURRENCY_THB = "THB";
	public static final String TOKEN = "token";

	public enum PathModule {
		ECONSENT,
		ORIGINALNCCRS,
		ORIGINALNCCRSTIF,
		ORIGINALNCRS,
		ORIGINALNCRSTIF
	}

	public static final String E_CONSENT_NCCRS = "e-consent-nccrs";
	public static final String E_CONSENT_NCRS = "e-consent-ncrs";
	public static final String ORIGINAL_NCCRS = "original-NCCRS";
	public static final String CONVERT_NCCRS = "convert-NCCRS";
	public static final String ORIGINAL_NCCRS_TIF = "original-NCCRS-tif";
	public static final String ORIGINAL_NCRS = "original-NCRS";
	public static final String CONVERT_NCRS = "convert-NCRS";
	public static final String ORIGINAL_NCRS_TIF = "original-NCRS-tif";
	public static final String NCCRS_CREDIT_PDF  = "NCCRS_credit_pdf";
	public static final String NCRS_CREDIT_PDF  = "NCRS_credit_pdf";
	public static final String CONSENT_MANAGEMENT  = "consent_management";
	public static final String ORIGINAL_NCCRS_MARKUP = "original-NCCRS-markup";
	public static final String ORIGINAL_NCRS_MARKUP = "original-NCRS-markup";
	public static final String EXPORT_FOR_DV = "DV";
	public static final String EXPORT_CONSENT = "Export-Consent-file";
	public static final String ZIP_CONSENT = "zip-consent";
	public static final String GSB_STORAGE = "gsb";
	public static final String DEPARTMENT = "Department";
	public static final String LOAN_TYPE = "LoanType";
	public static final String LOG_CONSENT = "log-consent";
	public static final String LOG_E_CONSENT = "log-e-consent";

	public static final String PATH_A = "a";
	public static final String PATH_B = "b";

	public static final String Q = "";

	public enum Status {
		PASS("pass"),
		FAIL("fail"),
		MISS("miss");
		
		private final String value;

		private Status(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public static final String TRANSACTION_STATE_AUTHORIZED = "Authorized";
	public static final String TRANSACTION_STATE_PRE_AUTHORIZED = "Pre-Authorized";
	public static final String TRANSACTION_STATE_SETTLED = "Settled";
	public static final String REDIRECT = "redirect:";
	
	public enum Ordering {
		ASC,
		DESC
	}

	public static final String MSG_INVALID_USER = "Invalid User";
	
	public static final String Q_PDFTOTIF = "pdftotif";
	public static final String Q_UPLOADTODV = "uploadtodv";
	public static final String Q_CHECKBATCHCONSENT = "checkbatchconsent";
	public static final String Q_PROCESSCONSENT = "processconsent";
	public static final String Q_APPROVECONSENT = "approveconsent";

	public static final String Q_DOWNLOADFILEBRANCH = "downloadfilebranch";
	public static final String Q_READFILEBRANCHTOTEMP = "readfilebranchtotemp";
	public static final String Q_DUMPTEMPTOBRANCH = "dumptemptobranch";

	public static final String Q_DOWNLOADFILEBLOANTYPE = "downloadfilebloantype";
	public static final String Q_READFILEBLOANTYPETOTEMP = "readfilebloantypetotemp";
	public static final String Q_DUMPTEMPTOBLOANTYPE = "dumptemptobloantype";
	public static final String Q_GENERATEDVFORFTP = "generatedvforftp";
	public static final String Q_GENERATEFILESEARCHREQUESTATTACH = "generatefilesearchrequestattach";

	public enum LogType {
		QUERY("query"),
		SAVE("save"),
		EDIT("edit"),
		DELETE("delete"),
		VERIFY("verify");
		
		private final String value;

		private LogType(String value) {
			this.value = value;
		}

		public String getValue() {
			return this.value;
		}
	}

	public enum SyncTable {

		MAS_BRANCH("mas_branch"),
		MAS_LOAN_TYPE("mas_loan_type");

		private final String value;
		
		private SyncTable(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
	}

}
