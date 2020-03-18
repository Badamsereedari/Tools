package tools;

public enum DataType {
	CONFIG("config"), PROD_TYPE("prod_type"), BAL_TYPE("bal_type"), BAL_TYPES("bal_types"), TXN_CODE("txn_code"),
	INT("int"), STMT_EXCLUSION("stmt_exclusion"), CLS("cls"), PL_OPER("pl_oper"), NMW_OPER_COL("nmw_oper_col"),
	NMW_OPERATIONS("nmw_operation");

	private DataType(String privType) {
		this.privType = privType;
	}

	private final String privType;

	@Override
	public String toString() {
		return this.privType;
	}
}
