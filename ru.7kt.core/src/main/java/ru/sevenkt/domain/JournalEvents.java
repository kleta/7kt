package ru.sevenkt.domain;

public enum JournalEvents {
	TIME(1, "��������� �������"),
	FORMULA(2, "��������� �������"),
	TC(4, "�����������"),
	TYPE_TERMO(8,"��� �������������"),
	L(16,"�"),
	RESET(32,"����� ���������"),
	SET_TP(64,"���������� ��������");
	
	
	private int mask;
	private String name;
	JournalEvents(int mask, String name){
		this.mask=mask;
		this.name=name;
	}
	public int getMask() {
		return mask;
	}
	public String getName() {
		return name;
	}
	
}
