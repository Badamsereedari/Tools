package tools;

public enum DataType {
	CONFIG("config"), PROD_TYPE("prod_type"), BAL_TYPE("bal_type"), BAL_TYPES("bal_types"), TXN_CODE("txn_code"),
	INT("int"), STMT_EXCLUSION("stmt_exclusion"), CLS("cls"), PL_OPER("pl_oper"), NMW_OPER_COL("nmw_oper_col"),
	NMW_OPERATIONS("nmw_operation"), GL_CFG("glip_gl_cfg"), GL_CONT_CFG("glip_cont_entry_cfg"),
	GL_TXN_CFG("glip_txn_config"), LM_OPER("lm_oper");

	private DataType(String privType) {
		this.privType = privType;
	}

	private final String privType;

	@Override
	public String toString() {
		return this.privType;
	}
}
