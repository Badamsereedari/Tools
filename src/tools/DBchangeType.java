package tools;

public enum DBchangeType {
	GEN_SYSTEM("gen_system"), ADM_SYSTEM("adm_system"), EOD_SYSTEM("eod_system"), RAM_SYSTEM("ram_system"),
	CONST("const"), DICT("dictionary"), MSG("msg"), SCREEN("screen"), CACHE("cache"), BULG_TYPE("bulg_type"),
	BULG_FIELD("bulg_field"), OPER("oper"), PRIV("priv"), OPER_PRIV("oper_priv"), PROC_OPER("proc_oper"),
	TLLR_OPER("tllr_oper"), TLLR_PRIV("tllr_priv"), TLLR_OPER_PRIV("tllr_oper_priv");

	private DBchangeType(String privType) {
		this.privType = privType;
	}

	private final String privType;

	@Override
	public String toString() {
		return this.privType;
	}
}
