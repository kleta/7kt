package ru.sevenkt.app.ui.forms;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import ru.sevenkt.db.entities.Params;
import ru.sevenkt.domain.Parameters;

public class ParametersModel {
	private boolean e1;
	private boolean e2;
	private boolean t1;
	private boolean t2;
	private boolean t3;
	private boolean t4;
	private boolean t1Subt2;
	private boolean t3Subt4;
	private boolean v1;
	private boolean v2;
	private boolean v3;
	private boolean v4;
	private boolean v5;
	private boolean v6;
	private boolean v7;
	private boolean v8;
	private boolean v1Subv2;
	private boolean v3Subv4;
	private boolean m1;
	private boolean m2;
	private boolean m3;
	private boolean m4;
	private boolean m1Subm2;
	private boolean m3Subm4;
	private boolean p1;
	private boolean p2;
	private boolean p3;
	private boolean p4;
	private boolean errorCode;
	private boolean workTime;
	private boolean noWorkTime;

	public ParametersModel(List<Params> params) {
		for (Params param : params) {
			Parameters p = param.getId();
			switch (p) {
			case ERROR_CODE:
				errorCode = true;
				break;
			case M1_SUB_M2:
				m1Subm2 = true;
				break;
			case AVG_P1:
				p1 = true;
				break;
			case AVG_P2:
				p2 = true;
				break;
			case AVG_TEMP1:
				t1 = true;
				break;
			case AVG_TEMP2:
				t2 = true;
				break;
			case AVG_TEMP3:
				t3 = true;
				break;
			case AVG_TEMP4:
				t4 = true;
				break;
			case E1:
				e1 = true;
				break;
			case E2:
				e2 = true;
				break;
			case ERROR_TIME:
				noWorkTime = true;
				break;
			case M1:
				m1 = true;
				break;
			case M2:
				m2 = true;
				break;
			case M3:
				m3 = true;
				break;
			case M3_SUB_M4:
				m3Subm4 = true;
				break;
			case M4:
				m4 = true;
				break;
			case T1_SUB_T2:
				t1Subt2 = true;
				break;
			case T3_SUB_T4:
				t3Subt4 = true;
				break;
			case V1:
				v1 = true;
				break;
			case V1_SUB_V2:
				v1Subv2 = true;
				break;
			case V2:
				v2 = true;
				break;
			case V3:
				v3 = true;
				break;
			case V3_SUB_V4:
				v3Subv4 = true;
				break;
			case V4:
				v4 = true;
				break;
			case WORK:
				workTime = true;
				break;
			case V8:
				v8 = true;
				break;
			case V5:
				v5 = true;
				break;
			case V6:
				v6 = true;
				break;
			case V7:
				v7 = true;
				break;
			default:
				break;
			}
		}
	}

	public boolean isE1() {
		return e1;
	}

	public void setE1(boolean e1) {
		this.e1 = e1;
	}

	public boolean isE2() {
		return e2;
	}

	public void setE2(boolean e2) {
		this.e2 = e2;
	}

	public boolean isT1() {
		return t1;
	}

	public void setT1(boolean t1) {
		this.t1 = t1;
	}

	public boolean isT2() {
		return t2;
	}

	public void setT2(boolean t2) {
		this.t2 = t2;
	}

	public boolean isT3() {
		return t3;
	}

	public void setT3(boolean t3) {
		this.t3 = t3;
	}

	public boolean isT4() {
		return t4;
	}

	public void setT4(boolean t4) {
		this.t4 = t4;
	}

	public boolean isT1Subt2() {
		return t1Subt2;
	}

	public void setT1Subt2(boolean t1Subt2) {
		this.t1Subt2 = t1Subt2;
	}

	public boolean isT3Subt4() {
		return t3Subt4;
	}

	public void setT3Subt4(boolean t3Subt4) {
		this.t3Subt4 = t3Subt4;
	}

	public boolean isV1() {
		return v1;
	}

	public void setV1(boolean v1) {
		this.v1 = v1;
	}

	public boolean isV2() {
		return v2;
	}

	public void setV2(boolean v2) {
		this.v2 = v2;
	}

	public boolean isV3() {
		return v3;
	}

	public void setV3(boolean v3) {
		this.v3 = v3;
	}

	public boolean isV4() {
		return v4;
	}

	public void setV4(boolean v4) {
		this.v4 = v4;
	}

	public boolean isV5() {
		return v5;
	}

	public void setV5(boolean v5) {
		this.v5 = v5;
	}

	public boolean isV6() {
		return v6;
	}

	public void setV6(boolean v6) {
		this.v6 = v6;
	}

	public boolean isV7() {
		return v7;
	}

	public void setV7(boolean v7) {
		this.v7 = v7;
	}

	public boolean isV8() {
		return v8;
	}

	public void setV8(boolean v8) {
		this.v8 = v8;
	}

	public boolean isV1Subv2() {
		return v1Subv2;
	}

	public void setV1Subv2(boolean v1Subv2) {
		this.v1Subv2 = v1Subv2;
	}

	public boolean isV3Subv4() {
		return v3Subv4;
	}

	public void setV3Subv4(boolean v3Subv4) {
		this.v3Subv4 = v3Subv4;
	}

	public boolean isM1() {
		return m1;
	}

	public void setM1(boolean m1) {
		this.m1 = m1;
	}

	public boolean isM2() {
		return m2;
	}

	public void setM2(boolean m2) {
		this.m2 = m2;
	}

	public boolean isM3() {
		return m3;
	}

	public void setM3(boolean m3) {
		this.m3 = m3;
	}

	public boolean isM4() {
		return m4;
	}

	public void setM4(boolean m4) {
		this.m4 = m4;
	}

	public boolean isM1Subm2() {
		return m1Subm2;
	}

	public void setM1Subm2(boolean m1Subm2) {
		this.m1Subm2 = m1Subm2;
	}

	public boolean isM3Subm4() {
		return m3Subm4;
	}

	public void setM3Subm4(boolean m3Subm4) {
		this.m3Subm4 = m3Subm4;
	}

	public boolean isP1() {
		return p1;
	}

	public void setP1(boolean p1) {
		this.p1 = p1;
	}

	public boolean isP2() {
		return p2;
	}

	public void setP2(boolean p2) {
		this.p2 = p2;
	}

	public boolean isP3() {
		return p3;
	}

	public void setP3(boolean p3) {
		this.p3 = p3;
	}

	public boolean isP4() {
		return p4;
	}

	public void setP4(boolean p4) {
		this.p4 = p4;
	}

	public boolean isErrorCode() {
		return errorCode;
	}

	public void setErrorCode(boolean errorCode) {
		this.errorCode = errorCode;
	}

	public boolean isWorkTime() {
		return workTime;
	}

	public void setWorkTime(boolean workTime) {
		this.workTime = workTime;
	}

	public boolean isNoWorkTime() {
		return noWorkTime;
	}

	public void setNoWorkTime(boolean noWorkTime) {
		this.noWorkTime = noWorkTime;
	}

	public List<Params> getParams() throws IllegalArgumentException, IllegalAccessException {
		List<Params> params = new ArrayList<>();
		Field[] filds = getClass().getDeclaredFields();
		for (Field field : filds) {
			String fieldName = field.getName();
			Object object = field.get(this);
			if (object instanceof Boolean) {
				Boolean val = (Boolean) object;
				if (val)
					switch (fieldName) {
					case "errorCode":

						params.add(new Params(Parameters.ERROR_CODE));
						break;
					case "m1Subm2":

						params.add(new Params(Parameters.M1_SUB_M2));
						break;
					case "p1":

						params.add(new Params(Parameters.AVG_P1));
						break;
					case "p2":

						params.add(new Params(Parameters.AVG_P2));
						break;
					case "t1":

						params.add(new Params(Parameters.AVG_TEMP1));
						break;
					case "t2":

						params.add(new Params(Parameters.AVG_TEMP2));
						break;
					case "t3":

						params.add(new Params(Parameters.AVG_TEMP3));
						break;
					case "t4":

						params.add(new Params(Parameters.AVG_TEMP4));
						break;
					case "e1":

						params.add(new Params(Parameters.E1));
						break;
					case "e2":

						params.add(new Params(Parameters.E2));
						break;
					case "noWorkTime":

						params.add(new Params(Parameters.ERROR_TIME));
						break;
					case "m1":

						params.add(new Params(Parameters.M1));
						break;
					case "m2":

						params.add(new Params(Parameters.M2));
						break;
					case "m3":

						params.add(new Params(Parameters.M3));
						break;
					case "m3Subm4":

						params.add(new Params(Parameters.M3_SUB_M4));
						break;
					case "m4":

						params.add(new Params(Parameters.M4));
						break;
					case "t1Subt2":

						params.add(new Params(Parameters.T1_SUB_T2));
						break;
					case "t3Subt4":

						params.add(new Params(Parameters.T3_SUB_T4));
						break;
					case "v1":

						params.add(new Params(Parameters.V1));
						break;
					case "v1Subv2":

						params.add(new Params(Parameters.V1_SUB_V2));
						break;
					case "v2":

						params.add(new Params(Parameters.V2));
						break;
					case "v3":

						params.add(new Params(Parameters.V3));
						break;
					case "v3Subv4":

						params.add(new Params(Parameters.V3_SUB_V4));
						break;
					case "v4":

						params.add(new Params(Parameters.V4));
						break;
					case "workTime":

						params.add(new Params(Parameters.WORK));
						break;
					case "v8":

						params.add(new Params(Parameters.V8));
						break;
					case "v5":

						params.add(new Params(Parameters.V5));
						break;
					case "v6":

						params.add(new Params(Parameters.V6));
						break;
					case "v7":

						params.add(new Params(Parameters.V7));
						break;
					default:
						break;
					}
			}
		}
		return params;

	}
}
